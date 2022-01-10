package org.hyperion.cache.util;

import org.hyperion.cache.CacheFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;

/**
 * A utility class for GZIP.
 *
 * @author Graham Edgecombe
 */
public final class ZipUtils {

    private ZipUtils() {

    }

    /**
     * Unzips a cache file.
     *
     * @param file The cache file.
     * @return The unzipped byte buffer.
     * @throws IOException if an I/O error occurs.
     */
    public static ByteBuffer unzip(final CacheFile file) throws IOException {
        final byte[] data = new byte[file.getBuffer().remaining()];
        file.getBuffer().get(data);
        final InputStream is = new GZIPInputStream(new ByteArrayInputStream(data));
        byte[] out;
        try {
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            try (os) {
                while (true) {
                    final byte[] buf = new byte[1024];
                    final int read = is.read(buf, 0, buf.length);
                    if (read == -1) {
                        break;
                    }
                    os.write(buf, 0, read);
                }
            }
            out = os.toByteArray();
        } finally {
            is.close();
        }
        final ByteBuffer newBuf = ByteBuffer.allocate(out.length);
        newBuf.put(out);
        newBuf.flip();
        return newBuf;
    }

}
