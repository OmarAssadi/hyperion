package com.grahamedgecombe.rs2.task.impl;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.task.Task;

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
