package com.grahamedgecombe.rs2.event;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.model.Player;

/**
 * An <code>Event</code> used for handling game actions.
 * 
 * @author blakeman8192
 */
public abstract class ActionEvent extends Event {

	/**
	 * The <code>GameEngine</code> context of the ActionEvent.
	 */
	private GameEngine context;

	/**
	 * The <code>Player</code> associated with this ActionEvent.
	 */
	private Player player;

	/**
	 * Creates a new ActionEvent.
	 * 
	 * @param context
	 *            The context.
	 * @param player
	 *            The player.
	 * @param delay
	 *            The initial delay.
	 */
	public ActionEvent(GameEngine context, Player player, long delay) {
		super(delay);
		this.context = context;
		this.player = player;
	}

	/**
	 * Gets the context.
	 * 
	 * @return
	 */
	public GameEngine getContext() {
		return context;
	}

	/**
	 * Gets the player.
	 * 
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

}
