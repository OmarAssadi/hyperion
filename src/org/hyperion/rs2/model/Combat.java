package org.hyperion.rs2.model;

import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Damage.Hit;
import org.hyperion.rs2.model.Damage.HitType;
import org.hyperion.rs2.model.EntityCooldowns.CooldownFlags;

/**
 * Handles the combat system.
 * @author Brett
 *
 */
public class Combat {
	
	
	/**
	 * Attack types.
	 */
	public static enum AttackType {
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

	/**
	 * Get the attackers' weapon speed.
	 * @param player The player for whose weapon we are getting the speed value.
	 * @return A <code>double</code>-type value of the weapon speed.
	 */
	public static int getAttackSpeed(Entity entity) {
		// TODO
		// double attackSpeed = player.getEquipment().getWeaponSpeed();
		return 2200;
	}
	
	/**
	 * Checks if an entity can attack another. Shamelessly stolen from another of Graham's projects.
	 * @param source The source entity.
	 * @param victim The target entity.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public static boolean canAttack(Entity source, Entity victim) {
		// PvP combat, PvE not supported (yet)
		if(victim.isDead() || source.isDead())
			return false;
		if((source instanceof Player) && (victim instanceof Player)) {
			// attackable zones, etc
		}
		return true;
	}
	
	/**
	 * Inflicts damage on the recipient.
	 * @param recipient The entity taking the damage.
	 * @param damage The damage to be done.
	 */
	public static void inflictDamage(Entity recipient, Entity aggressor, Hit damage) {
		if((recipient instanceof Player) && (aggressor != null)) {
			Player p = (Player) recipient;
			p.inflictDamage(damage, aggressor);
			p.playAnimation(Animation.create(434, 2));
		}
	}
	
	/**
	 * Calculates the damage a single hit will do.
	 * @param source The attacking entity.
	 * @param victim The defending entity.
	 * @return An <code>int</code> representing the damage done.
	 */
	public static Hit calculateHit(Entity source, Entity victim, AttackType attack) {
		int verdict = 1;
		HitType hit = HitType.NORMAL_DAMAGE;
		Hit thisAttack = new Hit(verdict, hit);
		return thisAttack;
	}
	
	/**
	 * Carries out a single attack.
	 * @param source The entity source of the attack.
	 * @param victim The entity victim of the attack.
	 * @param attackType The type of attack.
	 */
	public static void doAttack(Entity source, Entity victim, AttackType attackType) {
		if(!canAttack(source, victim))
			return;
		source.setInteractingEntity(victim);
		source.playAnimation(Animation.create(422, 1));
		inflictDamage(victim, source, calculateHit(source, victim, attackType));
	}
}
