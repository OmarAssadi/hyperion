package com.grahamedgecombe.rs2.event;

import com.grahamedgecombe.rs2.model.World;
import com.grahamedgecombe.rs2.task.CleanupTask;

/**
 * An event which runs periodically and performs tasks such as garbage
 * collection.
 * @author Graham
 *
 */
public class CleanupEvent extends Event {

	/**
	 * The delay in milliseconds between consecutive cleanups.
	 */
	public static final int CLEANUP_CYCLE_TIME = 300000;
	
	/**
	 * Creates the cleanup event to run every 5 minutes.
	 */
	public CleanupEvent() {
		super(CLEANUP_CYCLE_TIME);
	}

	@Override
	public void execute() {
		World.getWorld().submit(new CleanupTask());
	}

}
