package com.grahamedgecombe.rs2.event;

public abstract class Event {
	
	private int delay;
	private boolean running = true;
	
	public Event(int delay) {
		this.delay = delay;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public void setDelay(int delay) {
		if(delay < 0) {
			throw new IllegalArgumentException("Delay must be positive.");
		}
		this.delay = delay;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void stop() {
		running = false;
	}
	
	public abstract void execute();

}
