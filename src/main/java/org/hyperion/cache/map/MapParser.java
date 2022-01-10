package org.hyperion.cache.map;

import org.hyperion.cache.Cache;

/**
 * A class which parses map files in the game cache.
 *
 * @author Graham Edgecombe
 */
public class MapParser {

    /**
     * The cache.
     */
    private final Cache cache;

    /**
     * The area id.
     */
    private final int area;

    /**
     * The map listener.
     */
    private final MapListener listener;

    /**
     * Creates the map parser.
     *
     * @param cache    The cache.
     * @param area     The area id.
     * @param listener The listener.
     */
    public MapParser(final Cache cache, final int area, final MapListener listener) {
        this.cache = cache;
        this.area = area;
        this.listener = listener;
    }

    /**
     * Parses the map file.
     */
    public void parse() {

    }

}
