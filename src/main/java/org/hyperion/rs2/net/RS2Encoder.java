package org.hyperion.rs2.net;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.hyperion.rs2.model.Player;

/**
 * Game protocol encoding class.
 *
 * @author Graham Edgecombe
 */
public class RS2Encoder implements ProtocolEncoder {

    @Override
    public void encode(final IoSession session, final Object in, final ProtocolEncoderOutput out) throws Exception {
        final Packet p = (Packet) in;

        /*
         * Check what type the packet is.
         */
        if (p.isRaw()) {
            /*
             * If the packet is raw, send its payload.
             */
            out.write(p.getPayload());
        } else {
            /*
             * If not, get the out ISAAC cipher.
             */
            final ISAACCipher outCipher = ((Player) session.getAttribute("player")).getOutCipher();

            /*
             * Get the packet attributes.
             */
            int opcode = p.getOpcode();
            final Packet.Type type = p.getType();
            final int length = p.getLength();

            /*
             * Encrypt the packet opcode.
             */
            opcode += outCipher.getNextValue();

            /*
             * Compute the required size for the buffer.
             */
            int finalLength = length + 1;
            switch (type) {
                case VARIABLE -> finalLength += 1;
                case VARIABLE_SHORT -> finalLength += 2;
            }

            /*
             * Create the buffer and write the opcode (and length if the
             * packet is variable-length).
             */
            final IoBuffer buffer = IoBuffer.allocate(finalLength);
            buffer.put((byte) opcode);
            switch (type) {
                case VARIABLE -> buffer.put((byte) length);
                case VARIABLE_SHORT -> buffer.putShort((short) length);
            }

            /*
             * Write the payload itself.
             */
            buffer.put(p.getPayload());

            /*
             * Flip and dispatch the packet.
             */
            out.write(buffer.flip());
        }
    }

    @Override
    public void dispose(final IoSession session) throws Exception {

    }

}
