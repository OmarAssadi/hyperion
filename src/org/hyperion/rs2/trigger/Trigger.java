package org.hyperion.rs2.trigger;

import org.hyperion.rs2.model.Player;

/**
 * A <code>Trigger</code> handles the logic necessary to fire a new
 * <code>Action</code> for a specified <code>Player</code>.
 * @author Graham Edgecombe
 *
 */
public interface Trigger {
	
	/**
	 * Fires the appropriate <code>Action</code>.
	 * @param player The player who triggered the trigger.
	 * @param arguments The arguments passed to this trigger.
	 */
	public void fire(Player player, Object... arguments);

}
