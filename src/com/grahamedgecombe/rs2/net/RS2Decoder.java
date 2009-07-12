package com.grahamedgecombe.rs2.net;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.grahamedgecombe.rs2.Constants;
import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.net.Packet.Type;

/**
 * Game protocol decoding class.
 * @author Graham
 *
 */
public class RS2Decoder extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		synchronized(session) {
			ISAACCipher inCipher = ((Player) session.getAttribute("player")).getInCipher();
			
			int opcode = (Integer) session.getAttribute("opcode", -1);
			int size = (Integer) session.getAttribute("size", -1);
			if(opcode == -1) {
				if(in.remaining() >= 1) {
					opcode = in.get() & 0xFF;
					opcode = (opcode - inCipher.getNextKey()) & 0xFF;
					size = Constants.PACKET_SIZES[opcode];
					session.setAttribute("opcode", opcode);
					session.setAttribute("size", size);
				} else {
					return false;
				}
			}
			if(size == -1) {
				if(in.remaining() >= 1) {
					size = in.get() & 0xFF;
					session.setAttribute("size", size);
				} else {
					return false;
				}
			}
			if(in.remaining() >= size) {
				byte[] data = new byte[size];
				in.get(data);
				IoBuffer payload = IoBuffer.allocate(data.length);
				payload.put(data);
				payload.flip();
				out.write(new Packet(opcode, Type.FIXED, payload));
				session.setAttribute("opcode", -1);
				session.setAttribute("size", -1);
				return true;
			}
			return false;
		}
	}

}
