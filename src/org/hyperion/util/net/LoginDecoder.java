package org.hyperion.util.net;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * Login protocol decoding class.
 * @author Graham
 *
 */
public class LoginDecoder extends CumulativeProtocolDecoder {
	
	/**
	 * The current opcode.
	 */
	private int opcode = -1;
	
	/**
	 * The current length.
	 */
	private int length = -1;

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		if(opcode == -1) {
			if(in.remaining() >= 1) {
				opcode = in.get() & 0xFF;
			} else {
				return false;
			}
		}
		if(length == -1) {
			if(in.remaining() >= 2) {
				length = in.getUnsignedShort();
			} else {
				return false;
			}
		}
		if(in.remaining() >= length) {
			byte[] payload = new byte[length];
			in.get(payload);
			IoBuffer buf = IoBuffer.allocate(length);
			buf.put(payload);
			buf.flip();
			out.write(new LoginPacket(opcode, buf));
			return true;
		} else {
			return false;
		}
	}

}
