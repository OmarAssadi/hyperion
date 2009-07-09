package com.grahamedgecombe.rs2.task;

import com.grahamedgecombe.rs2.GameEngine;

public class DeathTask implements Task {

	@Override
	public void execute(GameEngine context) {
		if(context.isRunning()) {
			context.stop();
		}
	}

}
