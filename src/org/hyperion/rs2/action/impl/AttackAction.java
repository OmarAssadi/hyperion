package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Combat;
import org.hyperion.rs2.model.Combat.AttackType;

/**
 * Handles an action for an attacking player.
 * @author Brett
 *
 */
public class AttackAction extends Action {
	
	/**
	 * The victim of this attack action.
	 */
	private Entity victim;
	
	/**
	 * The type of attack.
	 */
	private AttackType type = AttackType.MELEE;
	
	/**
	 * Constructor method for this action.
	 * @param player The attacker.
	 * @param victim The attacked.
	 * @param type The type of attack.
	 */
	public AttackAction(Player player, Entity victim) {
		super(player, 0);
		this.victim = victim;
	}

	@Override
	public QueuePolicy getQueuePolicy() {
		return QueuePolicy.NEVER;
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.FOLLOW;
	}

	
	@Override
	public void execute() {
		final Player player = getPlayer();
		if(Combat.canAttack(player, victim)) {
			if(this.getDelay() == 0) {
				this.setDelay(Combat.getAttackSpeed(player));
				// init();
				if(this.isRunning()) {
					Combat.initiateCombat(victim, player);
					Combat.doAttack(player, victim, type);
				}
			} else {
				Combat.initiateCombat(victim, player);
				Combat.doAttack(player, victim, type);
			}
		} else {
			this.stop();
		}
	}

}
