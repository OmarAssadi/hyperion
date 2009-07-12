package com.grahamedgecombe.rs2.task;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.World;

/**
 * A task that is executed when a player has logged in.
 * @author Graham
 *
 */
public class SessionLoginTask implements Task {

	/**
	 * The player.
	 */
	private Player player;
	
	/**
	 * Creates the session login task.
	 * @param player The player that logged in.
	 */
	public SessionLoginTask(Player player) {
		this.player = player;
	}

	@Override
	public void execute(GameEngine context) {
		World.getWorld().register(player);
	}

}
