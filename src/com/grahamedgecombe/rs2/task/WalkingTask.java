package com.grahamedgecombe.rs2.task;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.model.Player;

public class WalkingTask implements Task {
	
	private Player player;
	
	public WalkingTask(Player player) {
		this.player = player;
	}

	public void execute(GameEngine context) {
		player.getWalkingQueue().processNextPlayerMovement();
	}

}
