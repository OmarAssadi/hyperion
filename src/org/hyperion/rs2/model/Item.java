package org.hyperion.rs2.model;

/**
 * Represents a single item.
 * @author Graham
 *
 */
public class Item {
	
	/**
	 * The id.
	 */
	private int id;
	
	/**
	 * The number of items.
	 */
	private int count;
	
	/**
	 * Creates a single item.
	 * @param id The id.
	 */
	public Item(int id) {
		this(id, 1);
	}
	
	/**
	 * Creates a stacked item.
	 * @param id The id.
	 * @param count The number of items.
	 * @throws IllegalArgumentException if count is negative.
	 */
	public Item(int id, int count) {
		if(count < 0) {
			throw new IllegalArgumentException("Count cannot be negative.");
		}
		this.id = id;
		this.count = count;
	}
	
	/**
	 * Gets the item id.
	 * @return The item id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the count.
	 * @return The count.
	 */
	public int getCount() {
		return count;
	}

}