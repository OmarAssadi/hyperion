package org.hyperion.rs2.model;

import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Damage;
import org.hyperion.rs2.model.Damage.Hit;
import org.hyperion.rs2.model.Damage.HitType;

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
	public static long getAttackSpeed(Entity entity) {
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
		if((source instanceof Player) && (victim instanceof Player)) {
			Player pSource = (Player) source;
			Player pVictim = (Player) victim;
			// TODO implement attackable zones - using new trigger system?
			if(pVictim.isDead() || pSource.isDead())
				return false;
		}
		return true;
	}
	
	/**
	 * Inflicts damage on the recipient.
	 * @param recipient The entity taking the damage.
	 * @param damage The damage to be done.
	 */
	public static void inflictDamage(Entity recipient, Hit damage) {
		if(recipient instanceof Player) {
			Player p = (Player) recipient;
			p.inflictDamage(damage);
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
	 * Put the victim in combat and trigger their autoretaliation.
	 * @param victim The victim entity.
	 * @param source The source entity.
	 */
	public static void initiateCombat(Entity victim, Entity source) {
		if(!victim.isInCombat()) {
			victim.setInCombat(true);
			victim.setAggressorState(false);
			if(victim.isAutoRetaliating()) {
				//victim.setInteractingEntity(source);
				victim.face(source.getLocation());
			}
		}
		if(!source.isInCombat()) {
			source.setInCombat(true);
			//source.setInteractingEntity(victim);
			source.face(victim.getLocation());
			source.setAggressorState(true);
		}
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
		source.face(victim.getLocation());
		source.playAnimation(Animation.create(422, 1));
		inflictDamage(victim, calculateHit(source, victim, attackType));
	}
}
