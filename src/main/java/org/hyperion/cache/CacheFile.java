package org.hyperion.cache;

import java.nio.ByteBuffer;

/**
 * Represents a single cache file.
 *
 * @author Graham Edgecombe
 */
public class CacheFile {

    /**
     * The cache id.
     */
    private final int cache;

    /**
     * The file id.
     */
    private final int file;

    /**
     * The file data.
     */
    private final ByteBuffer data;

    /**
     * Creates a cache file.
     *
     * @param cache The cache id.
     * @param file  The file id.
     * @param data  The file data.
     */
    public CacheFile(final int cache, final int file, final ByteBuffer data) {
        this.cache = cache;
        this.file = file;
        this.data = data;
    }

    /**
     * Gets the cache id.
     *
     * @return The cache id.
     */
    public int getCache() {
        return cache;
    }

    /**
     * Gets the file id.
     *
     * @return The file id.
     */
    public int getFile() {
        return file;
    }

    /**
     * Gets the buffer.
     *
     * @return The buffer.
     */
    public ByteBuffer getBuffer() {
        return data;
    }

    /**
     * Gets the buffer as a byte array.
     *
     * @return The byte array.
     */
    public byte[] getBytes() {
        final byte[] bytes = new byte[data.limit()];
        data.position(0);
        data.get(bytes);
        return bytes;
    }

}
