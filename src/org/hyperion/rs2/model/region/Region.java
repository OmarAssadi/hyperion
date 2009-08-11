package org.hyperion.rs2.model.region;

import java.util.LinkedList;
import java.util.List;

import org.hyperion.rs2.model.GameObject;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;

/**
 * Represents a single region.
 * @author Graham
 *
 */
public class Region {

	/**
	 * The region coordinates.
	 */
	private RegionCoordinates coordinate;
	
	/**
	 * A list of players in this region.
	 */
	private List<Player> players = new LinkedList<Player>();
	
	/**
	 * A list of NPCs in this region.
	 */
	private List<NPC> npcs = new LinkedList<NPC>();
	
	/**
	 * A list of objects in this region.
	 */
	private List<GameObject> objects = new LinkedList<GameObject>();
	
	/**
	 * Creates a region.
	 * @param coordinate The coordinate.
	 */
	public Region(RegionCoordinates coordinate) {
		this.coordinate = coordinate;
	}
	
	/**
	 * Gets the region coordinates.
	 * @return The region coordinates.
	 */
	public RegionCoordinates getCoordinates() {
		return coordinate;
	}

	/**
	 * Gets the list of players.
	 * @return The list of players.
	 */
	public List<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Gets the list of NPCs.
	 * @return The list of NPCs.
	 */
	public List<NPC> getNpcs() {
		return npcs;
	}
	
	/**
	 * Gets the list of objects.
	 * @return The list of objects.
	 */
	public List<GameObject> getGameObjects() {
		return objects;
	}

}
