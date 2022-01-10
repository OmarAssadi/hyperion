package org.hyperion.rs2.model;

import org.hyperion.rs2.event.impl.CooldownEvent;

import java.util.BitSet;

/**
 * Represents an entity's individual cooldowns.
 *
 * @author Graham Edgecombe
 * @author Adaptations by Brett Russell
 */
public class EntityCooldowns {

    /**
     * The bitset (flag data).
     */
    private final BitSet cooldowns = new BitSet();

    /**
     * Checks if an update required.
     *
     * @return <code>true</code> if 1 or more flags are set,
     * <code>false</code> if not.
     */
    public boolean areCooldownsPending() {
        return !cooldowns.isEmpty();
    }

    /**
     * Flags (sets to true) a flag.
     *
     * @param flag The flag to flag.
     */
    public void flag(final CooldownFlags cooldown, final int duration, final Entity entity) {
        cooldowns.set(cooldown.ordinal(), true);
        World.getWorld().submit(new CooldownEvent(entity, cooldown, duration));
    }

    /**
     * Sets a cooldown.
     *
     * @param flag  The flag.
     * @param value The value.
     */
    public void set(final CooldownFlags cooldown, final boolean value) {
        cooldowns.set(cooldown.ordinal(), value);
    }

    /**
     * Gets the value of a flag.
     *
     * @param cooldownFlags The flag to get the value of.
     * @return The flag value.
     */
    public boolean get(final CooldownFlags cooldownFlags) {
        return cooldowns.get(cooldownFlags.ordinal());
    }

    /**
     * Resest all update flags.
     */
    public void reset() {
        cooldowns.clear();
    }

    /**
     * Represents a single type of update flag.
     *
     * @author Graham Edgecombe
     */
    public enum CooldownFlags {
        /**
         * Represents a melee swing cooldown.
         */
        MELEE_SWING,

        /**
         * Represents a ranged attack cooldown.
         */
        RANGED_SHOT,

        /**
         * Represents a spellcasting cooldown.
         */
        SPELL_CAST,

        /**
         * Represents a special attack bar segmentcooldown.
         */
        SPECIAL_ATTACK,


    }

}
