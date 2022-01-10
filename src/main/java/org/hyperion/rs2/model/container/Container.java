package org.hyperion.rs2.model.container;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Item;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A container holds a group of items.
 *
 * @author Graham Edgecombe
 */
public class Container {

    /**
     * The capacity of this container.
     */
    private final int capacity;
    /**
     * A list of listeners.
     */
    private final List<ContainerListener> listeners = new LinkedList<>();
    /**
     * The container type.
     */
    private final Type type;
    /**
     * The items in this container.
     */
    private Item[] items;
    /**
     * Firing events flag.
     */
    private boolean firingEvents = true;

    /**
     * Creates the container with the specified capacity.
     *
     * @param type     The type of this container.
     * @param capacity The capacity of this container.
     */
    public Container(final Type type, final int capacity) {
        this.type = type;
        this.capacity = capacity;
        this.items = new Item[capacity];
    }

    /**
     * Transfers an item from one container to another.
     *
     * @param from     The container to transfer from.
     * @param to       The container to transfer to.
     * @param fromSlot The slot in the original container.
     * @param id       The item id.
     * @return A flag indicating if the transfer was successful.
     */
    public static boolean transfer(final Container from, final Container to, final int fromSlot, final int id) {
        final Item fromItem = from.get(fromSlot);
        if (fromItem == null || fromItem.getId() != id) {
            return false;
        }
        if (to.add(fromItem)) {
            from.set(fromSlot, null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks the firing events flag.
     *
     * @return <code>true</code> if events are fired, <code>false</code> if
     * not.
     */
    public boolean isFiringEvents() {
        return firingEvents;
    }

    /**
     * Sets the firing events flag.
     *
     * @param firingEvents The flag.
     */
    public void setFiringEvents(final boolean firingEvents) {
        this.firingEvents = firingEvents;
    }

    /**
     * Gets the listeners of this container.
     *
     * @return The listeners of this container.
     */
    public Collection<ContainerListener> getListeners() {
        return Collections.unmodifiableCollection(listeners);
    }

    /**
     * Adds a listener.
     *
     * @param listener The listener to add.
     */
    public void addListener(final ContainerListener listener) {
        listeners.add(listener);
        listener.itemsChanged(this);
    }

    /**
     * Removes a listener.
     *
     * @param listener The listener to remove.
     */
    public void removeListener(final ContainerListener listener) {
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
        final Item[] old = items;
        items = new Item[capacity];
        int newIndex = 0;
        for (int i = 0; i < items.length; i++) {
            if (old[i] != null) {
                items[newIndex] = old[i];
                newIndex++;
            }
        }
        if (firingEvents) {
            fireItemsChanged();
        }
    }

    /**
     * Fires an items changed event.
     */
    public void fireItemsChanged() {
        for (final ContainerListener listener : listeners) {
            listener.itemsChanged(this);
        }
    }

    /**
     * Attempts to add an item into the next free slot.
     *
     * @param item The item.
     * @return <code>true</code> if the item was added,
     * <code>false</code> if not.
     */
    public boolean add(final Item item) {
        if (item.getDefinition().isStackable() || type.equals(Type.ALWAYS_STACK)) {
            for (int i = 0; i < items.length; i++) {
                if (items[i] != null && items[i].getId() == item.getId()) {
                    final int totalCount = item.getCount() + items[i].getCount();
                    if (totalCount >= Constants.MAX_ITEMS || totalCount < 1) {
                        return false;
                    }
                    set(i, new Item(items[i].getId(), items[i].getCount() + item.getCount()));
                    return true;
                }
            }
            final int slot = freeSlot();
            if (slot == -1) {
                return false;
            } else {
                set(slot, item);
                return true;
            }
        } else {
            final int slots = freeSlots();
            if (slots >= item.getCount()) {
                final boolean b = firingEvents;
                firingEvents = false;
                try {
                    for (int i = 0; i < item.getCount(); i++) {
                        set(freeSlot(), new Item(item.getId()));
                    }
                    if (b) {
                        fireItemsChanged();
                    }
                    return true;
                } finally {
                    firingEvents = b;
                }
            } else {
                return false;
            }
        }
    }

    /**
     * Gets an item by id.
     *
     * @param id The id.
     * @return The item, or <code>null</code> if it could not be found.
     */
    public Item getById(final int id) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                continue;
            }
            if (items[i].getId() == id) {
                return items[i];
            }
        }
        return null;
    }

    /**
     * Gets the capacity of this container.
     *
     * @return The capacity of this container.
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Clears this container.
     */
    public void clear() {
        items = new Item[items.length];
        if (firingEvents) {
            fireItemsChanged();
        }
    }

    /**
     * Returns an array representing this container.
     *
     * @return The array.
     */
    public Item[] toArray() {
        return items;
    }

    /**
     * Checks if a slot is used.
     *
     * @param slot The slot.
     * @return <code>true</code> if an item is present, <code>false</code> otherwise.
     */
    public boolean isSlotUsed(final int slot) {
        return items[slot] != null;
    }

    /**
     * Checks if a slot is free.
     *
     * @param slot The slot.
     * @return <code>true</code> if an item is not present, <code>false</code> otherwise.
     */
    public boolean isSlotFree(final int slot) {
        return items[slot] == null;
    }

    /**
     * Removes an item.
     *
     * @param item The item to remove.
     * @return The number of items removed.
     */
    public int remove(final Item item) {
        return remove(-1, item);
    }

    /**
     * Removes an item.
     *
     * @param preferredSlot The preferred slot.
     * @param item          The item to remove.
     * @return The number of items removed.
     */
    public int remove(final int preferredSlot, final Item item) {
        int removed = 0;
        if (item.getDefinition().isStackable() || type.equals(Type.ALWAYS_STACK)) {
            final int slot = getSlotById(item.getId());
            final Item stack = get(slot);
            if (stack.getCount() > item.getCount()) {
                removed = item.getCount();
                set(slot, new Item(stack.getId(), stack.getCount() - item.getCount()));
            } else {
                removed = stack.getCount();
                set(slot, null);
            }
        } else {
            for (int i = 0; i < item.getCount(); i++) {
                int slot = getSlotById(item.getId());
                if (i == 0 && preferredSlot != -1) {
                    final Item inSlot = get(preferredSlot);
                    if (inSlot.getId() == item.getId()) {
                        slot = preferredSlot;
                    }
                }
                if (slot == -1) {
                    break;
                } else {
                    removed++;
                    set(slot, null);
                }
            }
        }
        return removed;
    }

    /**
     * Gets a slot by id.
     *
     * @param id The id.
     * @return The slot, or <code>-1</code> if it could not be found.
     */
    public int getSlotById(final int id) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                continue;
            }
            if (items[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets an item.
     *
     * @param index The position in the container.
     * @return The item.
     */
    public Item get(final int index) {
        return items[index];
    }

    /**
     * Sets an item.
     *
     * @param index The position in the container.
     * @param item  The item.
     */
    public void set(final int index, final Item item) {
        items[index] = item;
        if (firingEvents) {
            fireItemChanged(index);
        }
    }

    /**
     * Fires an item changed event.
     *
     * @param slot The slot that changed.
     */
    public void fireItemChanged(final int slot) {
        for (final ContainerListener listener : listeners) {
            listener.itemChanged(this, slot);
        }
    }

    /**
     * Swaps two items.
     *
     * @param fromSlot From slot.
     * @param toSlot   To slot.
     */
    public void swap(final int fromSlot, final int toSlot) {
        final Item temp = get(fromSlot);
        final boolean b = firingEvents;
        firingEvents = false;
        try {
            set(fromSlot, get(toSlot));
            set(toSlot, temp);
            if (b) {
                fireItemsChanged(new int[]{fromSlot, toSlot});
            }
        } finally {
            firingEvents = b;
        }
    }

    /**
     * Fires an items changed event.
     *
     * @param slots The slots that changed.
     */
    public void fireItemsChanged(final int[] slots) {
        for (final ContainerListener listener : listeners) {
            listener.itemsChanged(this, slots);
        }
    }

    /**
     * Gets the total amount of an item, including the items in stacks.
     *
     * @param id The id.
     * @return The amount.
     */
    public int getCount(final int id) {
        int total = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                if (items[i].getId() == id) {
                    total += items[i].getCount();
                }
            }
        }
        return total;
    }

    /**
     * Inserts an item.
     *
     * @param fromSlot The old slot.
     * @param toSlot   The new slot.
     */
    public void insert(final int fromSlot, final int toSlot) {
        // we reset the item in the from slot
        final Item from = items[fromSlot];
        if (from == null) {
            return;
        }
        items[fromSlot] = null;
        // find which direction to shift in
        if (fromSlot > toSlot) {
            final int shiftFrom = toSlot;
            int shiftTo = fromSlot;
            for (int i = (toSlot + 1); i < fromSlot; i++) {
                if (items[i] == null) {
                    shiftTo = i;
                    break;
                }
            }
            final Item[] slice = new Item[shiftTo - shiftFrom];
            System.arraycopy(items, shiftFrom, slice, 0, slice.length);
            System.arraycopy(slice, 0, items, shiftFrom + 1, slice.length);
        } else {
            int sliceStart = fromSlot + 1;
            final int sliceEnd = toSlot;
            for (int i = (sliceEnd - 1); i >= sliceStart; i--) {
                if (items[i] == null) {
                    sliceStart = i;
                    break;
                }
            }
            final Item[] slice = new Item[sliceEnd - sliceStart + 1];
            System.arraycopy(items, sliceStart, slice, 0, slice.length);
            System.arraycopy(slice, 0, items, sliceStart - 1, slice.length);
        }
        // now fill in the target slot
        items[toSlot] = from;
        if (firingEvents) {
            fireItemsChanged();
        }
    }

    /**
     * Checks if the container contains the specified item.
     *
     * @param id The item id.
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean contains(final int id) {
        return getSlotById(id) != -1;
    }

    /**
     * Checks if there is room in the inventory for an item.
     *
     * @param item The item.
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean hasRoomFor(final Item item) {
        if (item.getDefinition().isStackable() || type.equals(Type.ALWAYS_STACK)) {
            for (int i = 0; i < items.length; i++) {
                if (items[i] != null && items[i].getId() == item.getId()) {
                    final int totalCount = item.getCount() + items[i].getCount();
                    return totalCount < Constants.MAX_ITEMS && totalCount >= 1;
                }
            }
            final int slot = freeSlot();
            return slot != -1;
        } else {
            final int slots = freeSlots();
            return slots >= item.getCount();
        }

    }

    /**
     * Gets the next free slot.
     *
     * @return The slot, or <code>-1</code> if there are no available slots.
     */
    public int freeSlot() {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the number of free slots.
     *
     * @return The number of free slots.
     */
    public int freeSlots() {
        return capacity - size();
    }

    /**
     * Gets the size of this container.
     *
     * @return The size of this container.
     */
    public int size() {
        int size = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                size++;
            }
        }
        return size;
    }

    /**
     * The type of container.
     *
     * @author Graham Edgecombe
     */
    public enum Type {

        /**
         * A standard container such as inventory.
         */
        STANDARD,

        /**
         * A container which always stacks, e.g. the bank, regardless of the
         * item.
         */
        ALWAYS_STACK,

    }

}
