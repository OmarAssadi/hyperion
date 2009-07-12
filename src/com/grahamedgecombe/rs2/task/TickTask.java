package com.grahamedgecombe.rs2.task;

import java.util.Queue;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.model.ChatMessage;
import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.UpdateFlags.UpdateFlag;

public class TickTask implements Task {
	
	private Player player;
	
	public TickTask(Player player) {
		this.player = player;
	}

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
