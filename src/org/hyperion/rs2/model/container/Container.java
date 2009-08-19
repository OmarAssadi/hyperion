package org.hyperion.rs2.model.container;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hyperion.rs2.model.Item;

/**
 * A container holds a group of items.
 * @author Graham Edgecombe
 *
 */
public class Container {

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
		listener.itemsChanged(this);
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
	public boolean add(Item item) {
		if(item.getDefinition().isStackable()) {
			for(int i = 0; i < items.length; i++) {
				if(items[i] != null && items[i].getId() == item.getId()) {
					set(i, new Item(items[i].getId(), items[i].getCount() + item.getCount()));
					return true;
				}
			}
			int slot = freeSlot();
			if(slot == -1) {
				return false;
			} else {
				set(slot, item);
				return true;
			}
		} else {
			int slots = freeSlots();
			if(slots >= item.getCount()) {
				for(int i = 0; i < item.getCount(); i++) {
					set(freeSlot(), new Item(item.getId()));
				}
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * Gets the number of free slots.
	 * @return The number of free slots.
	 */
	public int freeSlots() {
		return capacity - size();
	}

	/**
	 * Gets an item.
	 * @param index The position in the container.
	 * @return The item.
	 */
	public Item get(int index) {
		return items[index];
	}
	
	/**
	 * Gets an item by id.
	 * @param id The id.
	 * @return The item, or <code>null</code> if it could not be found.
	 */
	public Item getById(int id) {
		for(int i = 0; i < items.length; i++) {
			if(items[i].getId() == id) {
				return items[i];
			}
		}
		return null;
	}
	
	/**
	 * Gets a slot by id.
	 * @param id The id.
	 * @return The slot, or <code>-1</code> if it could not be found.
	 */
	public int getSlotById(int id) {
		for(int i = 0; i < items.length; i++) {
			if(items[i] == null) {
				continue;
			}
			if(items[i].getId() == id) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Sets an item.
	 * @param index The position in the container.
	 * @param item The item.
	 */
	public void set(int index, Item item) {
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

	/**
	 * Returns an array representing this container.
	 * @return The array.
	 */
	public Item[] toArray() {
		return items;
	}
	
	/**
	 * Checks if a slot is used.
	 * @param slot The slot.
	 * @return <code>true</code> if an item is present, <code>false</code> otherwise.
	 */
	public boolean isSlotUsed(int slot) {
		return items[slot] != null;
	}
	
	/**
	 * Checks if a slot is free.
	 * @param slot The slot.
	 * @return <code>true</code> if an item is not present, <code>false</code> otherwise.
	 */
	public boolean isSlotFree(int slot) {
		return items[slot] == null;
	}
	
	/**
	 * Removes an item.
	 * @param item The item to remove.
	 * @return The number of items removed.
	 */
	public int remove(Item item) {
		return remove(-1, item);
	}
	
	/**
	 * Removes an item.
	 * @param preferredSlot The preferred slot.
	 * @param item The item to remove.
	 * @return The number of items removed.
	 */
	public int remove(int preferredSlot, Item item) {
		int removed = 0;
		if(item.getDefinition().isStackable()) {
			int slot = getSlotById(item.getId());
			Item stack = get(slot);
			if(stack.getCount() > item.getCount()) {
				removed = item.getCount();
				set(slot, new Item(stack.getId(), stack.getCount() - item.getCount()));
			} else {
				removed = stack.getCount();
				set(slot, null);
			}
		} else {
			for(int i = 0; i < item.getCount(); i++) {
				int slot = getSlotById(item.getId());
				if(i == 0 && preferredSlot != -1) {
					Item inSlot = get(preferredSlot);
					if(inSlot.getId() == item.getId()) {
						slot = preferredSlot;
					}
				}
				if(slot != -1) {
					removed++;
					set(slot, null);
				} else {
					break;
				}
			}
		}
		return removed;
	}

	/**
	 * Transfers an item from one container to another.
	 * @param from The container to transfer from.
	 * @param to The container to transfer to.
	 * @param fromSlot The slot in the original container.
	 * @param id The item id.
	 * @return A flag indicating if the transfer was successful.
	 */
	public static boolean transfer(Container from, Container to, int fromSlot, int id) {
		Item fromItem = from.get(fromSlot);
		if(fromItem == null || fromItem.getId() != id) {
			return false;
		}
		if(to.add(fromItem)) {
			from.set(fromSlot, null);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Swaps two items.
	 * @param fromSlot From slot.
	 * @param toSlot To slot.
	 */
	public void swap(int fromSlot, int toSlot) {
		Item temp = get(fromSlot);
		set(fromSlot, get(toSlot));
		set(toSlot, temp);
	}

	/**
	 * Gets the total amount of an item, including the items in stacks.
	 * @param id The id.
	 * @return The amount.
	 */
	public int getCount(int id) {
		int total = 0;
		for(int i = 0; i < items.length; i++) {
			if(items[i] != null) {
				total += items[i].getCount();
			}
		}
		return total;
	}

}
