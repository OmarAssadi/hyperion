package com.grahamedgecombe.rs2.model;

import java.util.LinkedList;
import java.util.List;

public abstract class Entity {
	
	public static final Location DEFAULT_LOCATION = Location.create(3200, 3200, 0);
	
	private int index;
	private Location location = DEFAULT_LOCATION;
	private Location teleportTarget = null;
	private final UpdateFlags updateFlags = new UpdateFlags();
	private final List<Player> localPlayers = new LinkedList<Player>();
	private boolean teleporting = true;
	private final Sprites sprites = new Sprites();
	
	public boolean hasTeleportTarget() {
		return teleportTarget != null;
	}
	
	public Location getTeleportTarget() {
		return teleportTarget;
	}
	
	public void setTeleportTarget(Location teleportTarget) {
		this.teleportTarget = teleportTarget;
	}
	
	public void resetTeleportTarget() {
		this.teleportTarget = null;
	}
	
	public Sprites getSprites() {
		return sprites;
	}
	
	public boolean isTeleporting() {
		return teleporting;
	}
	
	public void setTeleporting(boolean teleporting) {
		this.teleporting = teleporting;
	}
	
	public List<Player> getLocalPlayers() {
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
