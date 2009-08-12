package org.hyperion.rs2.model;

/**
 * Represents a single animation request.
 * @author Graham Edgecombe
 *
 */
public class Animation {
	
	/**
	 * Creates an animation with no delay.
	 * @param id The id.
	 * @return The new animation object.
	 */
	public static Animation create(int id) {
		return create(id, 0);
	}
	
	/**
	 * Creates an animation.
	 * @param id The id.
	 * @param delay The delay.
	 * @return The new animation object.
	 */
	public static Animation create(int id, int delay) {
		return new Animation(id, delay);
	}
	
	/**
	 * The id.
	 */
	private int id;
	
	/**
	 * The delay.
	 */
	private int delay;
	
	/**
	 * Creates an animation.
	 * @param id The id.
	 * @param delay The delay.
	 */
	private Animation(int id, int delay) {
		this.id = id;
		this.delay = delay;
	}
	
	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the delay.
	 * @return The delay.
	 */
	public int getDelay() {
		return delay;
	}

}
