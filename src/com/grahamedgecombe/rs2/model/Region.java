package com.grahamedgecombe.rs2.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a region in the in game map.
 * @author Graham
 *
 */
public class Region {
	
	/**
	 * The x region coordinate.
	 */
	private int x;
	
	/**
	 * The y region coordinate.
	 */
	private int y;
	
	/**
	 * A list of entities within the region.
	 */
	private List<Entity> entities = new LinkedList<Entity>();
	
	/**
	 * Creates the region.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public Region(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Checks if the region is empty. If so, it should be considered for
	 * removal and subsequent garbage collection.
	 * @return <code>true</code> if there are no entities in the region,
	 * <code>false</code> if not.
	 */
	public synchronized boolean isEmpty() {
		return entities.isEmpty();
	}
	
	/**
	 * Gets the region x coordinate.
	 * @return The region x coordinate.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the region y coordinate.
	 * @return The region y coordinate.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Gets the collection of entities within this region.
	 * @return The collection of entities within this region.
	 */
	public synchronized Collection<Entity> getEntities() {
		return Collections.unmodifiableCollection(entities);
	}

	/**
	 * Adds an entity to the region.
	 * @param entity The entity to add.
	 */
	public synchronized void add(Entity entity) {
		entities.add(entity);
	}
	
	/**
	 * Removes an entity from the region.
	 * @param entity The entity to remove.
	 */
	public synchronized void remove(Entity entity) {
		entities.remove(entity);
	}

}
