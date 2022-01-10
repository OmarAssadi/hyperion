package org.hyperion.rs2.pf;

import org.hyperion.rs2.model.Location;

/**
 * An interface which represents a path finding algorithm.
 *
 * @author Graham Edgecombe
 */
public interface PathFinder {

    /**
     * Finds a path between two points.
     *
     * @param location The central point of the tile map.
     * @param radius   The radius of the tile map.
     * @param map      The map the points are on.
     * @param srcX     Source point, x coordinate.
     * @param srcY     Source point, y coordinate.
     * @param dstX     Destination point, x coordinate.
     * @param dstY     Destination point, y coordinate.
     * @return A path between two points if such a path exists, or
     * <code>null</code> if no path exists.
     */
    Path findPath(Location location, int radius, TileMap map, int srcX, int srcY, int dstX, int dstY);

}
