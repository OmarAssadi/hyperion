package org.hyperion.rs2.event;

import java.util.concurrent.TimeUnit;

import org.hyperion.rs2.GameEngine;


/**
 * A class that manages <code>Event</code>s for a specific
 * <code>GameEngine</code>.
 * @author Graham
 *
 */
public class EventManager {
	
	/**
	 * The <code>GameEngine</code> to manager events for.
	 */
	private GameEngine engine;

	/**
	 * Creates an <code>EventManager</code> for the specified
	 * <code>GameEngine</code>.
	 * @param engine The game engine the manager is managing events for.
	 */
	public EventManager(GameEngine engine) {
		this.engine = engine;
	}
	
	/**
	 * Submits a new event to the <code>GameEngine</code>.
	 * @param event The event to submit.
	 */
	public void submit(final Event event) {
		engine.scheduleLogic(new Runnable() {
			@Override
			public void run() {
				if(event.isRunning()) {
					submit(event);
					event.execute();
				}
			}
		}, event.getDelay(), TimeUnit.MILLISECONDS);
	}

}
