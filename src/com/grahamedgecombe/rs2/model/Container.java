package com.grahamedgecombe.rs2.model;

/**
 * A container holds a group of items.
 * @author Graham
 *
 * @param <E> The type of Item this container holds.
 */
public class Container<E extends Item> {

	/**
	 * The capacity of this container.
	 */
	private int capacity;
	
	/**
	 * The items in this container.
	 */
	private Item[] items;
	
	/**
	 * Creates the container with the specified capacity.
	 * @param capacity The capacity of this container.
	 */
	public Container(int capacity) {
		this.capacity = capacity;
		this.items = new Item[capacity];
	}
	
	/**
	 * Gets an item.
	 * @param index The position in the container.
	 * @return The item.
	 */
	@SuppressWarnings("unchecked")
	public E get(int index) {
		return (E) items[index];
	}
	
	/**
	 * Sets an item.
	 * @param index The position in the container.
	 * @param item The item.
	 */
	public void set(int index, E item) {
		items[index] = item;
	}
	
	/**
	 * Gets the capacity of this container.
	 * @return The capacity of this container.
	 */
	public int getCapacity() {
		return capacity;
	}

}
