package com.grahamedgecombe.rs2.model;

import java.util.LinkedList;
import java.util.List;

public abstract class Entity {
	
	public static final Location DEFAULT_LOCATION = Location.create(3200, 3200, 0);
	
	private int index;
	private Location location = DEFAULT_LOCATION;
	private UpdateFlags updateFlags = new UpdateFlags();
	private List<Player> localPlayers = new LinkedList<Player>();
	
	public List<Player> getLocalEntities() {
		return localPlayers;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public UpdateFlags getUpdateFlags() {
		return updateFlags;
	}

}
