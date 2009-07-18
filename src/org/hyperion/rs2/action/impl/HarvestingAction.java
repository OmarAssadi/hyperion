package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.model.Player;

/**
 * <p>A harvesting action is a resource-gathering action, which includes, but
 * is not limited to, woodcutting and mining.</p>
 * 
 * <p>This class implements code related to all harvesting-type skills, such as
 * dealing with the action itself, looping, expiring the object (i.e. changing
 * rocks to the gray rock and trees to the stump), checking requirements and
 * giving out the harvested resources.</p>
 * 
 * <p>The individual woodcutting and mining classes implement things specific
 * to these individual skills such as random events.</p>
 * @author Graham
 *
 */
public abstract class HarvestingAction extends Action {
	
	/**
	 * Creates the harvesting action for the specified player.
	 * @param player The player to create the action for.
	 */
	public HarvestingAction(Player player) {
		super(player, 0);
	}
	
	@Override
	public QueuePolicy getQueuePolicy() {
		return QueuePolicy.NEVER;
	}
	
	/**
	 * Gets the harvest delay.
	 * @return The delay between consecutive harvests.
	 */
	public abstract long getHarvestDelay();

	@Override
	public void execute() {
		if(this.getDelay() == 0) {
			this.setDelay(getHarvestDelay());
		} else {
			
		}
	}

}
