package com.grahamedgecombe.rs2.event;

public class UpdateEvent extends Event {

	public static final int CYCLE_TIME = 600;
	
	public UpdateEvent() {
		super(CYCLE_TIME);
	}

	@Override
	public void execute() {
		
	}

}
