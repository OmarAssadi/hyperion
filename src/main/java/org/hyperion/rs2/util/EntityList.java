package org.hyperion.rs2.util;

import org.hyperion.rs2.model.Entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * A class which represents a list of entities.
 *
 * @param <E> The type of entity.
 * @author Graham Edgecombe
 */
public class EntityList<E extends Entity> implements Collection<E>, Iterable<E> {

    /**
     * Internal entities array.
     */
    private final Entity[] entities;

    /**
     * Current size.
     */
    private int size = 0;

    /**
     * Creates an entity list with the specified capacity.
     *
     * @param capacity The capacity.
     */
    public EntityList(final int capacity) {
        entities = new Entity[capacity + 1]; // do not use idx 0
    }

    /**
     * Gets an entity.
     *
     * @param index The index.
     * @return The entity.
     * @throws IndexOutOufBoundException if the index is out of bounds.
     */
    public Entity get(final int index) {
        if (index <= 0 || index >= entities.length) {
            throw new IndexOutOfBoundsException();
        }
        return entities[index];
    }

    /**
     * Gets the index of an entity.
     *
     * @param entity The entity.
     * @return The index in the list.
     */
    public int indexOf(final Entity entity) {
        return entity.getIndex();
    }

    /**
     * Gets the next free id.
     *
     * @return The next free id.
     */
    private int getNextId() {
        for (int i = 1; i < entities.length; i++) {
            if (entities[i] == null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean add(final E arg0) {
        final int id = getNextId();
        if (id == -1) {
            return false;
        }
        entities[id] = arg0;
        arg0.setIndex(id);
        size++;
        return true;
    }

    @Override
    public boolean addAll(final Collection<? extends E> arg0) {
        boolean changed = false;
        for (final E entity : arg0) {
            if (add(entity)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        for (int i = 1; i < entities.length; i++) {
            entities[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(final Object arg0) {
        for (int i = 1; i < entities.length; i++) {
            if (entities[i] == arg0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(final Collection<?> arg0) {
        boolean failed = false;
        for (final Object o : arg0) {
            if (!contains(o)) {
                failed = true;
                break;
            }
        }
        return !failed;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new EntityListIterator<>(this);
    }

    @Override
    public boolean remove(final Object arg0) {
        for (int i = 1; i < entities.length; i++) {
            if (entities[i] == arg0) {
                entities[i] = null;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(final Collection<?> arg0) {
        boolean changed = false;
        for (final Object o : arg0) {
            if (remove(o)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(final Collection<?> arg0) {
        boolean changed = false;
        for (int i = 1; i < entities.length; i++) {
            if (entities[i] != null) {
                if (!arg0.contains(entities[i])) {
                    entities[i] = null;
                    size--;
                    changed = true;
                }
            }
        }
        return changed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Entity[] toArray() {
        final int size = size();
        final Entity[] array = new Entity[size];
        int ptr = 0;
        for (int i = 1; i < entities.length; i++) {
            if (entities[i] != null) {
                array[ptr++] = entities[i];
            }
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(final T[] arg0) {
        final Entity[] arr = toArray();
        return (T[]) Arrays.copyOf(arr, arr.length, arg0.getClass());
    }

}
