package com.grahamedgecombe.rs2.task;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.World;

public class SessionLoginTask implements Task {

	private Player player;
	
	public SessionLoginTask(Player player) {
		this.player = player;
	}

	@Override
	public void execute(GameEngine context) {
		World.getWorld().register(player);
	}

}
