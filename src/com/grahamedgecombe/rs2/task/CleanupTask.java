package com.grahamedgecombe.rs2.task;

import com.grahamedgecombe.rs2.GameEngine;

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
