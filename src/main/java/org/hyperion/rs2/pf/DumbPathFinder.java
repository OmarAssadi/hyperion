package org.hyperion.rs2.pf;

import org.hyperion.rs2.model.Location;

/**
 * An implementation of a <code>PathFinder</code> which is 'dumb' and only
 * looks at surrounding tiles for a path, suitable for an NPC.
 *
 * @author Graham Edgecombe
 */
public class DumbPathFinder implements PathFinder {

    @Override
    public Path findPath(final Location location, final int radius, final TileMap map, final int srcX, final int srcY, final int dstX, final int dstY) {
        int stepX = 0, stepY = 0;
        if (srcX > dstX && map.getTile(dstX, srcY).isWesternTraversalPermitted()) {
            stepX = -1;
        } else if (srcX < dstX && map.getTile(dstX, srcY).isEasternTraversalPermitted()) {
            stepX = 1;
        }
        if (srcX > dstY && map.getTile(srcX, dstY).isSouthernTraversalPermitted()) {
            stepY = -1;
        } else if (srcX > dstY && map.getTile(srcX, dstY).isNorthernTraversalPermitted()) {
            stepY = 1;
        }
        if (stepX != 0 || stepY != 0) {
            final Path p = new Path();
            p.addPoint(new Point(srcX, srcY));
            p.addPoint(new Point(srcX + stepX, srcY + stepY));
            return p;
        }
        return null;
    }

}
