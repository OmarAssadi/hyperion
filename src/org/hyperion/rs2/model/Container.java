package org.hyperion.rs2.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
	 * A list of listeners.
	 */
	private List<ContainerListener> listeners = new LinkedList<ContainerListener>();
	
	/**
	 * Creates the container with the specified capacity.
	 * @param capacity The capacity of this container.
	 */
	public Container(int capacity) {
		this.capacity = capacity;
		this.items = new Item[capacity];
	}
	
	/**
	 * Gets the listeners of this container.
	 * @return The listeners of this container.
	 */
	public Collection<ContainerListener> getListeners() {
		return Collections.unmodifiableCollection(listeners);
	}
	
	/**
	 * Adds a listener.
	 * @param listener The listener to add.
	 */
	public void addListener(ContainerListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a listener.
	 * @param listener The listener to remove.
	 */
	public void removeListener(ContainerListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Removes all listeners.
	 */
	public void removeAllListeners() {
		listeners.clear();
	}
	
	/**
	 * Shifts all items to the top left of the container leaving no gaps.
	 */
	public void shift() {
		Item[] old = items;
		items = new Item[capacity];
		int newIndex = 0;
		for(int i = 0; i < items.length; i++) {
			if(old[i] != null) {
				items[newIndex] = old[i];
				newIndex++;
			}
		}
		for(ContainerListener listener : listeners) {
			listener.itemsChanged(this);
		}
	}
	
	/**
	 * Gets the next free slot.
	 * @return The slot, or <code>-1</code> if there are no available slots.
	 */
	public int freeSlot() {
		for(int i = 0; i < items.length; i++) {
			if(items[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Attempts to add an item into the next free slot.
	 * @param item The item.
	 * @return <code>true</code> if the item was added,
	 * <code>false</code> if not.
	 */
	public boolean add(E item) {
		int slot = freeSlot();
		if(slot == -1) {
			return false;
		} else {
			set(slot, item);
			return true;
		}
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
		for(ContainerListener listener : listeners) {
			listener.itemChanged(this, index);
		}
	}
	
	/**
	 * Gets the capacity of this container.
	 * @return The capacity of this container.
	 */
	public int capacity() {
		return capacity;
	}
	
	/**
	 * Gets the size of this container.
	 * @return The size of this container.
	 */
	public int size() {
		int size = 0;
		for(int i = 0; i < items.length; i++) {
			if(items[i] != null) {
				size++;
			}
		}
		return size;
	}
	
	/**
	 * Clears this container.
	 */
	public void clear() {
		items = new Item[items.length];
		for(ContainerListener listener : listeners) {
			listener.itemsChanged(this);
		}
	}

}
