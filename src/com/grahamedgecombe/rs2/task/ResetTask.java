package com.grahamedgecombe.rs2.task;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.model.Player;

/**
 * A task which resets a player after an update cycle.
 * @author Graham
 *
 */
public class ResetTask implements Task {
	
	/**
	 * The player to reset.
	 */
	private Player player;
	
	/**
	 * Creates a reset task.
	 * @param player The player to reset.
	 */
	public ResetTask(Player player) {
		this.player = player;
	}

	@Override
	public void execute(GameEngine context) {
		player.getUpdateFlags().reset();
		player.setTeleporting(false);
		player.setMapRegionChanging(false);
		player.resetTeleportTarget();
		player.resetCachedUpdateBlock();
	}

}
