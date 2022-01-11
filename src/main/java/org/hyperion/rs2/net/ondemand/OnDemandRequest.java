package org.hyperion.rs2.net.ondemand;

import org.apache.mina.core.session.IoSession;
import org.hyperion.cache.Cache;
import org.hyperion.rs2.net.PacketBuilder;

import java.io.IOException;

/**
 * <p>Represents a single 'ondemand' request. Ondemand requests are created
 * when the client requests a file from the cache using the update
 * protocol.<?p>
 *
 * @author Graham Edgecombe
 */
public class OnDemandRequest {

    /**
     * The session.
     */
    private final IoSession session;

    /**
     * The cache.
     */
    private final int cacheId;

    /**
     * The file.
     */
    private final int fileId;

    /**
     * The priority.
     */
    private final int priority;

    /**
     * Creates the request.
     *
     * @param session  The session.
     * @param cacheId  The cache.
     * @param fileId   The file.
     * @param priority The priority.
     */
    public OnDemandRequest(final IoSession session, final int cacheId, final int fileId, final int priority) {
        this.session = session;
        this.cacheId = cacheId;
        this.fileId = fileId;
        this.priority = priority;
    }

    /**
     * Services the request.
     */
    public void service(final Cache cache) {
        try {
            final byte[] data = cache.getFile(cacheId + 1, fileId).getBytes();
            final int totalSize = data.length;
            int roundedSize = totalSize;
            while (roundedSize % 500 != 0) {
                roundedSize++;
            }
            final int blocks = roundedSize / 500;
            int sentBytes = 0;
            for (int i = 0; i < blocks; i++) {
                int blockSize = totalSize - sentBytes;
                final PacketBuilder bldr = new PacketBuilder();
                bldr.put((byte) cacheId);
                bldr.put((byte) (fileId >> 8));
                bldr.put((byte) fileId);
                bldr.put((byte) (totalSize >> 8));
                bldr.put((byte) totalSize);
                bldr.put((byte) i);
                if (blockSize > 500) {
                    blockSize = 500;
                }
                bldr.put(data, sentBytes, blockSize);
                sentBytes += blockSize;
                session.write(bldr.toPacket());
            }
        } catch (final IOException ex) {
            session.close(false);
        }
    }

    /**
     * Gets the priority.
     *
     * @return The priority.
     */
    public int getPriority() {
        return priority;
    }

}
