package org.hyperion.rs2.model;

/**
 * Represents a single animation request.
 *
 * @author Graham Edgecombe
 */
public class Animation {

    /**
     * Different animation constants.
     */
    public static final Animation YES_EMOTE = create(855);
    public static final Animation NO_EMOTE = create(856);
    public static final Animation THINKING = create(857);
    public static final Animation BOW = create(858);
    public static final Animation ANGRY = create(859);
    public static final Animation CRY = create(860);
    public static final Animation LAUGH = create(861);
    public static final Animation CHEER = create(862);
    public static final Animation WAVE = create(863);
    public static final Animation BECKON = create(864);
    public static final Animation CLAP = create(865);
    public static final Animation DANCE = create(866);
    public static final Animation PANIC = create(2105);
    public static final Animation JIG = create(2106);
    public static final Animation SPIN = create(2107);
    public static final Animation HEADBANG = create(2108);
    public static final Animation JOYJUMP = create(2109);
    public static final Animation RASPBERRY = create(2110);
    public static final Animation YAWN = create(2111);
    public static final Animation SALUTE = create(2112);
    public static final Animation SHRUG = create(2113);
    public static final Animation BLOW_KISS = create(1368);
    public static final Animation GLASS_WALL = create(1128);
    public static final Animation LEAN = create(1129);
    public static final Animation CLIMB_ROPE = create(1130);
    public static final Animation GLASS_BOX = create(1131);
    public static final Animation GOBLIN_BOW = create(2127);
    public static final Animation GOBLIN_DANCE = create(2128);
    /**
     * The id.
     */
    private final int id;
    /**
     * The delay.
     */
    private final int delay;

    /**
     * Creates an animation.
     *
     * @param id    The id.
     * @param delay The delay.
     */
    private Animation(final int id, final int delay) {
        this.id = id;
        this.delay = delay;
    }

    /**
     * Creates an animation with no delay.
     *
     * @param id The id.
     * @return The new animation object.
     */
    public static Animation create(final int id) {
        return create(id, 0);
    }

    /**
     * Creates an animation.
     *
     * @param id    The id.
     * @param delay The delay.
     * @return The new animation object.
     */
    public static Animation create(final int id, final int delay) {
        return new Animation(id, delay);
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
