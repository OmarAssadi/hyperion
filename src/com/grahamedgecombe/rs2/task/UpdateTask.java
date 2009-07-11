package com.grahamedgecombe.rs2.task;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.model.Player;

public class UpdateTask implements Task {
	
	private Player player;
	
	public UpdateTask(Player player) {
		this.player = player;
	}

	@Override
	public void execute(GameEngine context) {
		player.getUpdateFlags().reset();
	}

}
