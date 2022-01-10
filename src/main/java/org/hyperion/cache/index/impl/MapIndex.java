package org.hyperion.cache.index.impl;

import org.hyperion.cache.index.Index;

/**
 * A map index maps a single area to a map file and landscape file.
 *
 * @author Graham Edgecombe
 */
public class MapIndex extends Index {

    /**
     * The map file.
     */
    private final int mapFile;

    /**
     * The landscape file.
     */
    private final int landscapeFile;

    /**
     * Members only flag.
     */
    private final boolean members;

    /**
     * Creates the index.
     *
     * @param identifier    The identifier.
     * @param mapFile       The map file.
     * @param landscapeFile The landscape file.
     * @param members       The members only flag.
     */
    public MapIndex(final int identifier, final int mapFile, final int landscapeFile, final boolean members) {
        super(identifier);
        this.mapFile = mapFile;
        this.landscapeFile = landscapeFile;
        this.members = members;
    }

    /**
     * Gets the map file.
     *
     * @return The map file.
     */
    public int getMapFile() {
        return mapFile;
    }

    /**
     * Gets the landscape file.
     *
     * @return The landscape file.
     */
    public int getLandscapeFile() {
        return landscapeFile;
    }

    /**
     * Checks if this area is members-only.
     *
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean isMembers() {
        return members;
    }

}
