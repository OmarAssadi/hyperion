package com.grahamedgecombe.rs2.trigger;

import com.grahamedgecombe.rs2.model.Player;

/**
 * A <code>Trigger</code> handles the logic necessary to fire a new
 * <code>Action</code> for a specified <code>Player</code>.
 * @author Graham
 *
 */
public interface Trigger {
	
	/**
	 * Fires the appropriate <code>Action</code>.
	 * @param player The player who triggered the trigger.
	 */
	public void fire(Player player);

}
