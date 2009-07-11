package com.grahamedgecombe.rs2.task;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.model.Player;

public class LocalListTask implements Task {
	
	private Player player;
	
	public LocalListTask(Player player) {
		this.player = player;
	}

	@Override
	public void execute(GameEngine context) {
		
	}

}
