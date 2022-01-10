package org.hyperion.rs2.model;

/**
 * Represents a single location in the game world.
 *
 * @author Graham Edgecombe
 */
public class Location {

    /**
     * The x coordinate.
     */
    private final int x;

    /**
     * The y coordinate.
     */
    private final int y;

    /**
     * The z coordinate.
     */
    private final int z;

    /**
     * Creates a location.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     */
    private Location(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Gets the absolute x coordinate.
     *
     * @return The absolute x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the absolute y coordinate.
     *
     * @return The absolute y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the z coordinate, or height.
     *
     * @return The z coordinate.
     */
    public int getZ() {
        return z;
    }

    /**
     * Gets the local x coordinate relative to this region.
     *
     * @return The local x coordinate relative to this region.
     */
    public int getLocalX() {
        return getLocalX(this);
    }

    /**
     * Gets the local x coordinate relative to a specific region.
     *
     * @param l The region the coordinate will be relative to.
     * @return The local x coordinate.
     */
    public int getLocalX(final Location l) {
        return x - 8 * l.getRegionX();
    }

    /**
     * Gets the region x coordinate.
     *
     * @return The region x coordinate.
     */
    public int getRegionX() {
        return (x >> 3) - 6;
    }

    /**
     * Gets the local y coordinate relative to this region.
     *
     * @return The local y coordinate relative to this region.
     */
    public int getLocalY() {
        return getLocalY(this);
    }

    /**
     * Gets the local y coordinate relative to a specific region.
     *
     * @param l The region the coordinate will be relative to.
     * @return The local y coordinate.
     */
    public int getLocalY(final Location l) {
        return y - 8 * l.getRegionY();
    }

    /**
     * Gets the region y coordinate.
     *
     * @return The region y coordinate.
     */
    public int getRegionY() {
        return (y >> 3) - 6;
    }

    /**
     * Checks if this location is within range of another.
     *
     * @param other The other location.
     * @return <code>true</code> if the location is in range,
     * <code>false</code> if not.
     */
    public boolean isWithinDistance(final Location other) {
        if (z != other.z) {
            return false;
        }
        final int deltaX = other.x - x;
        final int deltaY = other.y - y;
        return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
    }

    /**
     * Checks if this location is within interaction range of another.
     *
     * @param other The other location.
     * @return <code>true</code> if the location is in range,
     * <code>false</code> if not.
     */
    public boolean isWithinInteractionDistance(final Location other) {
        if (z != other.z) {
            return false;
        }
        final int deltaX = other.x - x;
        final int deltaY = other.y - y;
        return deltaX <= 2 && deltaX >= -3 && deltaY <= 2 && deltaY >= -3;
    }

    @Override
    public int hashCode() {
        return z << 30 | x << 15 | y;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Location loc)) {
            return false;
        }
        return loc.x == x && loc.y == y && loc.z == z;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "," + z + "]";
    }

    /**
     * Creates a new location based on this location.
     *
     * @param diffX X difference.
     * @param diffY Y difference.
     * @param diffZ Z difference.
     * @return The new location.
     */
    public Location transform(final int diffX, final int diffY, final int diffZ) {
        return Location.create(x + diffX, y + diffY, z + diffZ);
    }

    /**
     * Creates a location.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     * @return The location.
     */
    public static Location create(final int x, final int y, final int z) {
        return new Location(x, y, z);
    }

}
