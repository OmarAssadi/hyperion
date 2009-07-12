package com.grahamedgecombe.rs2.net;

import java.security.SecureRandom;
import java.util.logging.Logger;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.grahamedgecombe.rs2.model.PlayerDetails;
import com.grahamedgecombe.rs2.model.World;
import com.grahamedgecombe.rs2.util.IoBufferUtils;
import com.grahamedgecombe.rs2.util.NameUtils;

public class RS2LoginDecoder extends CumulativeProtocolDecoder {

	private static final Logger logger = Logger.getLogger(RS2LoginDecoder.class.getName());
	
	public static final int STATE_OPCODE = 0;
	public static final int STATE_LOGIN = 1;
	public static final int STATE_PRECRYPTED = 2;
	public static final int STATE_CRYPTED = 3;
	
	public static final int OPCODE_GAME = 14;
	
	private static final SecureRandom RANDOM = new SecureRandom();
	
	private static final byte[] INITIAL_RESPONSE = new byte[] {
		0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0
	};
	
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		synchronized(session) {
			int state = (Integer) session.getAttribute("state", STATE_OPCODE);
			switch(state) {
			case STATE_OPCODE:
				if(in.remaining() >= 1) {
					int opcode = in.get() & 0xFF;
					switch(opcode) {
					case OPCODE_GAME:
						session.setAttribute("state", STATE_LOGIN);
						return true;
					default:
						logger.info("Invalid opcode : " + opcode);
						session.close(false);
						break;
					}
				} else {
					in.rewind();
					return false;
				}
				break;
			case STATE_LOGIN:
				if(in.remaining() >= 1) {
					@SuppressWarnings("unused")
					int nameHash = in.get() & 0xFF;
					long serverKey = RANDOM.nextLong();
					session.write(new PacketBuilder().put(INITIAL_RESPONSE).put((byte) 0).putLong(serverKey).toPacket());
					session.setAttribute("state", STATE_PRECRYPTED);
					session.setAttribute("serverKey", serverKey);
					return true;
				}
				break;
			case STATE_PRECRYPTED:
				if(in.remaining() >= 2) {
					int loginOpcode = in.get() & 0xFF;
					if(loginOpcode != 16 && loginOpcode != 18) {
						logger.info("Invalid login opcode : " + loginOpcode);
						session.close(false);
						in.rewind();
						return false;
					}
					int loginSize = in.get() & 0xFF;
					int loginEncryptSize = loginSize - (36 + 1 + 1 + 2);
					if(loginEncryptSize <= 0) {
						logger.info("Encrypted packet size zero or negative : " + loginEncryptSize);
						session.close(false);
						in.rewind();
						return false;
					}
					session.setAttribute("state", STATE_CRYPTED);
					session.setAttribute("size", loginSize);
					session.setAttribute("encryptSize", loginEncryptSize);
					return true;
				}
				break;
			case STATE_CRYPTED:
				int size = (Integer) session.getAttribute("size");
				int encryptSize = (Integer) session.getAttribute("encryptSize");
				if(in.remaining() >= size) {
					int magicId = in.get() & 0xFF;
					if(magicId != 255) {
						logger.info("Incorrect magic id : " + magicId);
						session.close(false);
						in.rewind();
						return false;
					}
					int version = in.getShort() & 0xFFFF;
					if(version != 317) {
						logger.info("Incorrect version : " + version);
						session.close(false);
						in.rewind();
						return false;
					}
					@SuppressWarnings("unused")
					boolean lowMemoryVersion = (in.get() & 0xFF) == 1;
					for(int i = 0; i < 9; i++) {
						in.getInt();
					}
					encryptSize--;
					int reportedSize = in.get() & 0xFF;
					if(reportedSize != encryptSize) {
						logger.info("Packet size mismatch (expected : " + encryptSize + ", reported : " + reportedSize + ")");
						session.close(false);
						in.rewind();
						return false;
					}
					int blockOpcode = in.get() & 0xFF;
					if(blockOpcode != 10) {
						logger.info("Invalid login block opcode : " + blockOpcode);
						session.close(false);
						in.rewind();
						return false;
					}
	
					long clientKey = in.getLong();
					long serverKey = (Long) session.getAttribute("serverKey");
					long reportedServerKey = in.getLong();
					if(reportedServerKey != serverKey) {
						logger.info("Server key mismatch (expected : " + serverKey + ", reported : " + reportedServerKey + ")");
						session.close(false);
						in.rewind();
						return false;
					}
					int uid = in.getInt();
					String name = NameUtils.formatName(IoBufferUtils.getRS2String(in));
					String pass = IoBufferUtils.getRS2String(in);
					logger.info("Login request : username=" + name + " password=" + pass);
					
					int[] sessionKey = new int[4];
					sessionKey[0] = (int) (clientKey >> 32);
					sessionKey[1] = (int) clientKey;
					sessionKey[2] = (int) (serverKey >> 32);
					sessionKey[3] = (int) serverKey;
					
					session.removeAttribute("state");
					session.removeAttribute("serverKey");
					session.removeAttribute("size");
					session.removeAttribute("encryptSize");
					
					ISAACCipher inCipher = new ISAACCipher(sessionKey);
					for(int i = 0; i < 4; i++) {
						sessionKey[i] += 50;
					}
					ISAACCipher outCipher = new ISAACCipher(sessionKey);
					
					session.getFilterChain().remove("protocol");
					session.getFilterChain().addFirst("protocol", new ProtocolCodecFilter(RS2CodecFactory.GAME));
					
					PlayerDetails pd = new PlayerDetails(session, name, pass, uid, inCipher, outCipher);
					World.getWorld().load(pd);
				}
				break;
			}
			in.rewind();
			return false;
		}
	}

}
