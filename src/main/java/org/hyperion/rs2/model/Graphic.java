package org.hyperion.rs2.model;

/**
 * Represents a single graphic request.
 *
 * @author Graham Edgecombe
 */
public class Graphic {

    /**
     * The id.
     */
    private final int id;
    /**
     * The delay.
     */
    private final int delay;

    /**
     * Creates a graphic.
     *
     * @param id    The id.
     * @param delay The delay.
     */
    private Graphic(final int id, final int delay) {
        this.id = id;
        this.delay = delay;
    }

    /**
     * Creates an graphic with no delay.
     *
     * @param id The id.
     * @return The new graphic object.
     */
    public static Graphic create(final int id) {
        return create(id, 0);
    }

    /**
     * Creates a graphic.
     *
     * @param id    The id.
     * @param delay The delay.
     * @return The new graphic object.
     */
    public static Graphic create(final int id, final int delay) {
        return new Graphic(id, delay);
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the delay.
     *
     * @return The delay.
     */
    public int getDelay() {
        return delay;
    }

}
