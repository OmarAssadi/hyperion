package org.hyperion.rs2.model;

import org.hyperion.rs2.model.Damage.Hit;
import org.hyperion.rs2.model.Damage.HitType;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Handles the combat system.
 *
 * @author Brett
 */
public final class Combat {

    /**
     * Private constructor to prevent instantiation.
     */
    private Combat() {

    }

    /**
     * Get the attackers' weapon speed.
     *
     * @param player The player for whose weapon we are getting the speed value.
     * @return A <code>double</code>-type value of the weapon speed.
     */
    public static int getAttackSpeed(final Entity entity) {
        // TODO
        // double attackSpeed = player.getEquipment().getWeaponSpeed();
        return 2200;
    }

    public static void initiateCombat(final Entity source, final Entity victim) {

    }

    /**
     * Carries out a single attack.
     *
     * @param source     The entity source of the attack.
     * @param victim     The entity victim of the attack.
     * @param attackType The type of attack.
     */
    public static void doAttack(final Entity source, final Entity victim, final AttackType attackType) {
        if (!canAttack(source, victim)) {
            return;
        }
        source.setInteractingEntity(victim);
        source.playAnimation(Animation.create(422, 1));
        inflictDamage(victim, source, calculatePlayerHit(source, victim, attackType));
    }

    /**
     * Checks if an entity can attack another. Shamelessly stolen from another of Graham's projects.
     *
     * @param source The source entity.
     * @param victim The target entity.
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public static boolean canAttack(final Entity source, final Entity victim) {
        // PvP combat, PvE not supported (yet)
        if (victim.isDead() || source.isDead()) {
            return false;
        }
        if ((source instanceof Player) && (victim instanceof Player)) {
            // attackable zones, etc
        }
        return true;
    }

    /**
     * Inflicts damage on the recipient.
     *
     * @param recipient The entity taking the damage.
     * @param damage    The damage to be done.
     */
    public static void inflictDamage(final Entity recipient, final Entity aggressor, final Hit damage) {
        if ((recipient instanceof Player p) && (aggressor != null)) {
            p.inflictDamage(damage, aggressor);
            p.playAnimation(Animation.create(434, 2));
        }
    }

    /**
     * Calculates the damage a single hit by a player will do.
     *
     * @param source The attacking entity.
     * @param victim The defending entity.
     * @return An <code>int</code> representing the damage done.
     */
    public static Hit calculatePlayerHit(final Entity source, final Entity victim, final AttackType attack) {
        int verdict = 0;
        HitType hit = HitType.NORMAL_DAMAGE;
        if (victim instanceof Player v) {
            // calculations here
            verdict = 3;
            if (verdict >= v.getSkills().getLevel(Skills.HITPOINTS)) {
                verdict = v.getSkills().getLevel(Skills.HITPOINTS);
            }
        }
        if (verdict == 0) {
            hit = HitType.NO_DAMAGE;
        }
        return new Hit(verdict, hit);
    }

    /**
     * Attack types.
     */
    public enum AttackType {
        /**
         * Melee-based attacks.
         */
        MELEE,

        /**
         * Projectile-based attacks.
         */
        RANGED,

        /**
         * Magic-based attacks.
         */
        MAGIC,
    }

    public static class CombatSession {
        private final int damage = 0;
        private final long timestamp = 0;

        public CombatSession() {

        }

        public int getDamage() {
            return this.damage;
        }
    }

    /**
     * Represents an instance of combat, where Entity is an assailant and Integer is the sum of their damage done. This is mapped to every victim in combat,
     * and used to determine drops.
     *
     * @author Brett Russell
     */
    public static class CollectiveCombatSession {
        private final long stamp;
        private final Entity victim;
        private Map<Entity, CombatSession> damageMap;
        private final Set<Entity> names = damageMap.keySet();
        private boolean isActive;

        public CollectiveCombatSession(final Entity victim) {
            final java.util.Date date = new java.util.Date();
            this.stamp = date.getTime();
            this.isActive = true;
            this.victim = victim;
        }

        /**
         * Gets the timestamp for this object (when the session began).
         *
         * @return The timestamp.
         */
        public long getStamp() {
            return stamp;
        }

        /**
         * Gets the entity with the highest damage count this session.
         *
         * @return The entity with the highest damage count.
         */
        public Entity getTopDamage() {
            Entity top = null;
            int damageDone = 0;
            int currentHighest = 0;

            final Iterator<Entity> itr = names.iterator();

            while (itr.hasNext()) {
                final Entity currentEntity = itr.next();
                damageDone = damageMap.get(currentEntity).getDamage();
                if (damageDone > currentHighest) {
                    currentHighest = damageDone;
                    top = currentEntity;
                }
            }
            return top;
        }

        /**
         * Returns the Map of this session's participants. If you would want it, that is...
         *
         * @return A Map of the participants and their damage done.
         */
        public Map<Entity, CombatSession> getDamageCharts() {
            return damageMap;
        }

        /**
         * Adds a participant to this session.
         *
         * @param participant The participant to add.
         */
        public void addParticipant(final Entity participant) {
            // TODO CombatSession
            damageMap.put(participant, null);
        }

        /**
         * Remove a participant.
         *
         * @param participant The participant to remove.
         */
        public void removeParticipant(final Entity participant) {
            damageMap.remove(participant);
        }

        /**
         * Sets this sessions active state.
         *
         * @param state A <code>boolean</code> value representing the state.
         */
        public void setState(final boolean b) {
            this.isActive = b;
        }

        /**
         * Determine the active state of this session.
         *
         * @return The active state as a <code>boolean</code> value.
         */
        public boolean getIsActive() {
            return this.isActive;
        }
    }
}
