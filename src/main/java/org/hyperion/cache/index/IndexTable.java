package org.hyperion.cache.index;

import org.hyperion.cache.Archive;
import org.hyperion.cache.Cache;
import org.hyperion.cache.index.impl.MapIndex;
import org.hyperion.cache.index.impl.StandardIndex;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * The <code>IndexTable</code> class manages all the <code>Index</code>es in
 * the <code>Cache</code>.
 *
 * @author Graham Edgecombe
 */
public class IndexTable {

    /**
     * The map indices.
     */
    private MapIndex[] mapIndices;

    /**
     * The object def indices.
     */
    private StandardIndex[] objectDefinitionIndices;

    /**
     * Creates the index table.
     *
     * @param cache The cache.
     * @throws IOException if an I/O error occurs.
     */
    public IndexTable(final Cache cache) throws IOException {
        final Archive configArchive = new Archive(cache.getFile(0, 2));
        initObjectDefIndices(configArchive);

        final Archive versionListArchive = new Archive(cache.getFile(0, 5));
        initMapIndices(versionListArchive);
    }

    /**
     * Initialises the object definition indices.
     *
     * @param configArchive The config archive.
     * @throws IOException if an I/O error occurs.
     */
    private void initObjectDefIndices(final Archive configArchive) throws IOException {
        final ByteBuffer buf = configArchive.getFileAsByteBuffer("loc.idx");
        final int objectCount = buf.getShort() & 0xFFFF;
        objectDefinitionIndices = new StandardIndex[objectCount];
        int offset = 2;
        for (int id = 0; id < objectCount; id++) {
            objectDefinitionIndices[id] = new StandardIndex(id, offset);
            offset += buf.getShort() & 0xFFFF;
        }
    }

    /**
     * Initialises the map indices.
     *
     * @param versionListArchive The version list archive.
     * @throws IOException if an I/O error occurs.
     */
    private void initMapIndices(final Archive versionListArchive) throws IOException {
        final ByteBuffer buf = versionListArchive.getFileAsByteBuffer("map_index");
        final int indices = buf.remaining() / 7;
        mapIndices = new MapIndex[indices];
        for (int i = 0; i < indices; i++) {
            final int area = buf.getShort() & 0xFFFF;
            final int mapFile = buf.getShort() & 0xFFFF;
            final int landscapeFile = buf.getShort() & 0xFFFF;
            final boolean members = (buf.get() & 0xFF) == 1;
            final MapIndex index = new MapIndex(area, mapFile, landscapeFile, members);
            mapIndices[i] = index;
        }
    }

    /**
     * Gets all of the object definition indices.
     *
     * @return The object definition indices array.
     */
    public StandardIndex[] getObjectDefinitionIndices() {
        return objectDefinitionIndices;
    }

    /**
     * Gets all of the map indices.
     *
     * @return The map indices array.
     */
    public MapIndex[] getMapIndices() {
        return mapIndices;
    }

    /**
     * Gets a single object definition index.
     *
     * @param object The object id.
     * @return The index.
     */
    public StandardIndex getObjectDefinitionIndex(final int object) {
        return objectDefinitionIndices[object];
    }

    /**
     * Gets a single map index.
     *
     * @param area The area id.
     * @return The map index, or <code>null</code> if the area does not exist.
     */
    public MapIndex getMapIndex(final int area) {
        for (final MapIndex index : mapIndices) {
            if (index.getIdentifier() == area) {
                return index;
            }
        }
        return null;
    }

}
