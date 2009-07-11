package com.grahamedgecombe.rs2.event;

import java.util.concurrent.TimeUnit;

import com.grahamedgecombe.rs2.GameEngine;

public class EventManager {
	
	private GameEngine engine;

	public EventManager(GameEngine engine) {
		this.engine = engine;
	}
	
	public void submit(final Event event) {
		engine.getLogicService().schedule(new Runnable() {
			@Override
			public void run() {
				event.execute();
				if(event.isRunning()) {
					submit(event);
				}
			}
		}, event.getDelay(), TimeUnit.MILLISECONDS);
	}

}
