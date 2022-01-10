package org.hyperion.cache;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages an archive file in the cache.
 *
 * @author Graham Edgecombe
 */
public class Archive {

    /**
     * Data buffer.
     */
    private final ByteBuffer data;
    /**
     * File map.
     */
    private final Map<Integer, ArchiveFile> namedFiles = new HashMap<>();
    /**
     * Compressed flag.
     */
    private boolean compressed = false;

    /**
     * Creates the archive.
     *
     * @param cf The cache file.
     * @throws IOException if an I/O error occurs.
     */
    public Archive(final CacheFile cf) throws IOException {
        ByteBuffer bb = cf.getBuffer();
        bb.position(0);
        final int uncompressed = ((bb.get() & 0xFF) << 16) | ((bb.get() & 0xFF) << 8) | (bb.get() & 0xFF);
        final int compressed = ((bb.get() & 0xFF) << 16) | ((bb.get() & 0xFF) << 8) | (bb.get() & 0xFF);
        if (uncompressed != compressed) {
            final byte[] data = new byte[compressed];
            bb.get(data);
            final byte[] decompressed = decompress(data);
            bb = ByteBuffer.allocate(decompressed.length);
            bb.put(decompressed);
            bb.flip();
            this.compressed = true;
        }
        final int dataSize = bb.getShort() & 0xFFFF;
        int off = bb.position() + dataSize * 10;
        for (int i = 0; i < dataSize; i++) {
            final int nameHash = bb.getInt();
            final int uncompressedSize = ((bb.get() & 0xFF) << 16) | ((bb.get() & 0xFF) << 8) | (bb.get() & 0xFF);
            final int compressedSize = ((bb.get() & 0xFF) << 16) | ((bb.get() & 0xFF) << 8) | (bb.get() & 0xFF);
            final ArchiveFile nf = new ArchiveFile(nameHash, uncompressedSize, compressedSize, off);
            namedFiles.put(nf.getHash(), nf);
            off += nf.getCompressedSize();
        }
        data = bb;
    }

    /**
     * Decompresses a byte array using BZIP2.
     *
     * @param data The compressed bytes.
     * @return The uncompressed bytes.
     * @throws IOException if an I/O error occurs.
     */
    private byte[] decompress(final byte[] data) throws IOException {
        final byte[] out;
        final byte[] newData = new byte[data.length + 4];
        System.arraycopy(data, 0, newData, 4, data.length);
        newData[0] = 'B';
        newData[1] = 'Z';
        newData[2] = 'h';
        newData[3] = '1';
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            try (final InputStream is = new BZip2CompressorInputStream(new ByteArrayInputStream(newData))) {
                while (true) {
                    final byte[] buf = new byte[512];
                    final int read = is.read(buf, 0, buf.length);
                    if (read == -1) {
                        break;
                    }
                    os.write(buf, 0, read);
                }
            }
            os.flush();
            out = os.toByteArray();
        }
        return out;
    }

    /**
     * Gets a file as a bytebuffer.
     *
     * @param name The file name.
     * @return The bytebuffer.
     * @throws IOException if an I/O error occurs.
     */
    public ByteBuffer getFileAsByteBuffer(final String name) throws IOException {
        final byte[] data = getFile(name);
        if (data == null) {
            return null;
        } else {
            final ByteBuffer buf = ByteBuffer.allocate(data.length);
            buf.put(data);
            buf.flip();
            return buf;
        }
    }

    /**
     * Gets a file by its name.
     *
     * @param name The file name.
     * @return The file contents.
     * @throws IOException if an I/O error occurs.
     */
    public byte[] getFile(final String name) throws IOException {
        final int hash = hash(name);
        final ArchiveFile nf = namedFiles.get(hash);
        if (nf == null) {
            return null;
        } else {
            final byte[] buf = new byte[nf.getCompressedSize()];
            data.position(nf.getOffset());
            data.get(buf);
            if (compressed) {
                return buf;
            } else {
                return decompress(buf);
            }
        }
    }

    /**
     * Hashes a file name.
     *
     * @param name The file name.
     * @return The hash.
     */
    public static int hash(String name) {
        int hash = 0;
        name = name.toUpperCase();
        for (int j = 0; j < name.length(); j++) {
            hash = (hash * 61 + name.charAt(j)) - 32;
        }
        return hash;
    }

}
