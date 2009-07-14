package com.grahamedgecombe.rs2.task.impl;

import java.util.Queue;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.model.ChatMessage;
import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.UpdateFlags.UpdateFlag;
import com.grahamedgecombe.rs2.task.Task;

/**
 * A task which is executed before an <code>UpdateTask</code>. It is similar to
 * the call to <code>process()</code> but you should use <code>Event</code>s
 * instead of putting timers in this class.
 * @author Graham
 *
 */
public class TickTask implements Task {
	
	/**
	 * The player.
	 */
	private Player player;
	
	/**
	 * Creates a tick task for a player.
	 * @param player The player to create the tick task for.
	 */
	public TickTask(Player player) {
		this.player = player;
	}

	@Override
	public void execute(GameEngine context) {
		Queue<ChatMessage> messages = player.getChatMessageQueue();
		if(messages.size() > 0) {
			player.getUpdateFlags().flag(UpdateFlag.CHAT);
			ChatMessage message = player.getChatMessageQueue().poll();
			player.setCurrentChatMessage(message);
		} else {
			player.setCurrentChatMessage(null);
		}
		player.getWalkingQueue().processNextPlayerMovement();
	}

}
