package org.hyperion.rs2.model;

import java.util.Map;

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
	
	public static class CombatSession {
		private double stamp;
		private Map<Entity, Integer> participants;
		
		public CombatSession(Entity participant, double stamp) {
			this.stamp = stamp;
			participants.put(participant, 0);
		}
		
		public double getStamp() {
			return stamp;
		}
		
		public Map<Entity, Integer> getParticipants() {
			return participants;
		}
		
		public void addParticipant(Entity participant) {
			participants.put(participant, 0);
		}
		
		public void updateParticipantDamage(Entity participant, int damage) {
			participants.put(participant, participants.get(participant)+damage);
		}
		
		public void removeParticipant(Entity participant) {
			participants.remove(participant);
		}
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
	 * Calculates the damage a single hit by a player will do.
	 * @param source The attacking entity.
	 * @param victim The defending entity.
	 * @return An <code>int</code> representing the damage done.
	 */
	public static Hit calculatePlayerHit(Entity source, Entity victim, AttackType attack) {
		int verdict = 0;
		HitType hit = HitType.NORMAL_DAMAGE;
		if(victim instanceof Player) {
			Player v = (Player) victim;
			// calculations here
			verdict = 3;
			if(verdict >= v.getSkills().getLevel(Skills.HITPOINTS)) {
				verdict = v.getSkills().getLevel(Skills.HITPOINTS);
			}
		}
		if(verdict == 0) {
			hit = HitType.NO_DAMAGE;
		}
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
		inflictDamage(victim, source, calculatePlayerHit(source, victim, attackType));
	}
}
