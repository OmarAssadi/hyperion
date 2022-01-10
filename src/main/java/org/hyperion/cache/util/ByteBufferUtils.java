package org.hyperion.cache.util;

import java.nio.ByteBuffer;

/**
 * A utility class for byte buffers.
 *
 * @author Graham Edgecombe
 */
public final class ByteBufferUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private ByteBufferUtils() {

    }

    /**
     * Gets a smart.
     *
     * @param buf The buffer.
     * @return The smart.
     */
    public static int getSmart(final ByteBuffer buf) {
        final int peek = buf.get(buf.position()) & 0xFF;
        if (peek < 128) {
            return buf.get() & 0xFF;
        } else {
            return (buf.getShort() & 0xFFFF) - 32768;
        }
    }

    /**
     * Gets an RS2 string from the buffer.
     *
     * @param buf The buffer.
     * @return The RS2 string.
     */
    public static String getString(final ByteBuffer buf) {
        final StringBuilder bldr = new StringBuilder();
        char c;
        while ((c = (char) buf.get()) != 10) {
            bldr.append(c);
        }
        return bldr.toString();
    }

}
