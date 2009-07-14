package com.grahamedgecombe.rs2.task.impl;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.task.Task;

/**
 * Performs garbage collection and finalization.
 * @author Graham
 *
 */
public class CleanupTask implements Task {

	@Override
	public void execute(GameEngine context) {
		context.submitWork(new Runnable() {
			public void run() {
				System.gc();
				System.runFinalization();
			}
		});
	}

}
