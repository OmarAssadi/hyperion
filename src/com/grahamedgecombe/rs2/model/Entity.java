package com.grahamedgecombe.rs2.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a character in the game world, i.e. a <code>Player</code> or
 * an <code>NPC</code>.
 * @author Graham
 *
 */
public abstract class Entity {
	
	/**
	 * The default, i.e. spawn, location.
	 */
	public static final Location DEFAULT_LOCATION = Location.create(3200, 3200, 0);
	
	/**
	 * The index in the <code>EntityList</code>.
	 */
	private int index;
	
	/**
	 * The current location.
	 */
	private Location location = DEFAULT_LOCATION;
	
	/**
	 * The teleportation target.
	 */
	private Location teleportTarget = null;
	
	/**
	 * The update flags.
	 */
	private final UpdateFlags updateFlags = new UpdateFlags();
	
	/**
	 * The list of local players.
	 */
	private final List<Player> localPlayers = new LinkedList<Player>();
	
	/**
	 * The teleporting flag.
	 */
	private boolean teleporting = true;
	
	/**
	 * The sprites i.e. walk directions.
	 */
	private final Sprites sprites = new Sprites();
	
	/**
	 * Checks if this entity has a target to teleport to.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean hasTeleportTarget() {
		return teleportTarget != null;
	}
	
	/**
	 * Gets the teleport target.
	 * @return The teleport target.
	 */
	public Location getTeleportTarget() {
		return teleportTarget;
	}
	
	/**
	 * Sets the teleport target.
	 * @param teleportTarget The target location.
	 */
	public void setTeleportTarget(Location teleportTarget) {
		this.teleportTarget = teleportTarget;
	}
	
	/**
	 * Resets the teleport target.
	 */
	public void resetTeleportTarget() {
		this.teleportTarget = null;
	}
	
	/**
	 * Gets the sprites.
	 * @return The sprites.
	 */
	public Sprites getSprites() {
		return sprites;
	}
	
	/**
	 * Checks if this player is teleporting.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isTeleporting() {
		return teleporting;
	}
	
	/**
	 * Sets the teleporting flag.
	 * @param teleporting The teleporting flag.
	 */
	public void setTeleporting(boolean teleporting) {
		this.teleporting = teleporting;
	}
	
	/**
	 * Gets the list of local players.
	 * @return The list of local players.
	 */
	public List<Player> getLocalPlayers() {
		return localPlayers;
	}
	
	/**
	 * Sets the entity's index.
	 * @param index The index.
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	/**
	 * Gets the entity's index.
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Sets the current location.
	 * @param location The current location.
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	/**
	 * Gets the current location.
	 * @return The current location.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Gets the update flags.
	 * @return The update flags.
	 */
	public UpdateFlags getUpdateFlags() {
		return updateFlags;
	}

}
