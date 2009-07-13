package com.grahamedgecombe.rs2.task;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.model.World;

/**
 * Performs garbage collection and finalization.
 * @author Graham
 *
 */
public class CleanupTask implements Task {

	@Override
	public void execute(GameEngine context) {
		World.getWorld().getRegionManager().purgeOldRegions();
		context.submitWork(new Runnable() {
			public void run() {
				System.gc();
				System.runFinalization();
			}
		});
	}

}
