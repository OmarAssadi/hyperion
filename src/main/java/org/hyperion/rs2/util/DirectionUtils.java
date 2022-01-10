package org.hyperion.rs2.util;

/**
 * A utility class for direction-related methods.
 *
 * @author Graham Edgecombe
 */
public final class DirectionUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private DirectionUtils() {

    }

    /**
     * Finds a direction.
     *
     * @param dx X difference.
     * @param dy Y difference.
     * @return The direction.
     */
    public static int direction(final int dx, final int dy) {
        if (dx < 0) {
            if (dy < 0) {
                return 5;
            } else if (dy > 0) {
                return 0;
            } else {
                return 3;
            }
        } else if (dx > 0) {
            if (dy < 0) {
                return 7;
            } else if (dy > 0) {
                return 2;
            } else {
                return 4;
            }
        } else if (dy < 0) {
            return 6;
        } else if (dy > 0) {
            return 1;
        } else {
            return -1;
        }
    }

}
