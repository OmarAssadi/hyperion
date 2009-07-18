package com.grahamedgecombe.rs2.action;

import com.grahamedgecombe.rs2.event.Event;
import com.grahamedgecombe.rs2.model.Player;

/**
 * An <code>Event</code> used for handling game actions.
 * 
 * @author blakeman8192
 * @author Graham
 */
public abstract class Action extends Event {
	
	/**
	 * A queue policy determines when the clients should queue up actions.
	 * @author Graham
	 *
	 */
	public enum QueuePolicy {
		ALWAYS,
		NEVER,
	}

	/**
	 * The <code>Player</code> associated with this ActionEvent.
	 */
	private Player player;

	/**
	 * Creates a new ActionEvent.
	 * 
	 * @param player
	 *            The player.
	 * @param delay
	 *            The initial delay.
	 */
	public Action(Player player, long delay) {
		super(delay);
		this.player = player;
	}

	/**
	 * Gets the player.
	 * 
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}
	
	public abstract QueuePolicy getQueuePolicy();
	
	@Override
	public void stop() {
		super.stop();
		player.getActionQueue().processNextAction();
	}

}
