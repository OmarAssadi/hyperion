package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.model.Player;

/**
 * An action for cutting down trees.
 * @author Graham Edgecombe
 *
 */
public class WoodcuttingAction extends HarvestingAction {
	
	/**
	 * The delay.
	 */
	private static final int DELAY = 3500;

	/**
	 * Creates the <code>WoodcuttingAction</code>.
	 * @param player The player performing the action.
	 */
	public WoodcuttingAction(Player player) {
		super(player);
	}

	@Override
	public long getHarvestDelay() {
		return DELAY;
	}

}
