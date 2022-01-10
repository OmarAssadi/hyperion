package org.hyperion.util.login;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * Login protocol encoding class.
 *
 * @author Graham Edgecombe
 */
public class LoginEncoder implements ProtocolEncoder {

    @Override
    public void encode(final IoSession session, final Object in, final ProtocolEncoderOutput out) throws Exception {
        final LoginPacket packet = (LoginPacket) in;
        final IoBuffer buf = IoBuffer.allocate(1 + 2 + packet.getPayload().remaining());
        buf.put((byte) packet.getOpcode());
        buf.putShort((short) packet.getPayload().remaining());
        buf.put(packet.getPayload());
        buf.flip();
        out.write(buf);
    }

    @Override
    public void dispose(final IoSession session) throws Exception {

    }

}
