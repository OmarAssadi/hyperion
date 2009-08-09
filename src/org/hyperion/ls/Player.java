package org.hyperion.ls;

/**
 * Represents a single player.
 * @author Graham
 *
 */
public class Player {
	
	/**
	 * The player name.
	 */
	private String name;
	
	/**
	 * The player rights.
	 */
	private int rights;
	
	/**
	 * Creates the player.
	 * @param name The name.
	 * @param rights The rights.
	 */
	public Player(String name, int rights) {
		this.name = name;
		this.rights = rights;
	}
	
	/**
	 * Gets the player name.
	 * @return The player name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the player rights.
	 * @return The player rights.
	 */
	public int getRights() {
		return rights;
	}

}
