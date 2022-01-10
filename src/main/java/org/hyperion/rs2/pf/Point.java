package org.hyperion.rs2.pf;

/**
 * Represents a point on a <code>Path</code>.
 *
 * @author Graham Edgecombe
 */
public class Point {

    /**
     * The x coordinate.
     */
    private final int x;

    /**
     * The y coordinate.
     */
    private final int y;

    /**
     * Creates a point.
     *
     * @param x The x coordinate of the point.
     * @param y The y coordinate of the point.
     */
    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (x != other.x) {
            return false;
        }
        return y == other.y;
    }

    @Override
    public String toString() {
        return Point.class.getName() + " [x=" + x + ", y=" + y + "]";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
