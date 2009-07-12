package com.grahamedgecombe.rs2.task;

import com.grahamedgecombe.rs2.GameEngine;

/**
 * A task which stops the game engine.
 * @author Graham
 *
 */
public class DeathTask implements Task {

	@Override
	public void execute(GameEngine context) {
		if(context.isRunning()) {
			context.stop();
		}
	}

}
