package com.grahamedgecombe.rs2.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.grahamedgecombe.rs2.model.Entity;

public class EntityList<E extends Entity> implements Collection<E>, Iterable<E> {
	
	private Entity[] entities;
	
	public EntityList(int capacity) {
		entities = new Entity[capacity+1]; // do not use idx 0
	}
	
	public Entity get(int index) {
		if(index < 0 || index >= entities.length) {
			throw new IndexOutOfBoundsException();
		}
		return entities[index];
	}
	
	public int indexOf(Entity entity) {
		return entity.getIndex();
	}
	
	private int getNextId() {
		for(int i = 1; i < entities.length; i++) {
			if(entities[i] == null) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean add(E arg0) {
		int id = getNextId();
		if(id == -1) {
			return false;
		}
		entities[id] = arg0;
		arg0.setIndex(id);
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> arg0) {
		boolean changed = false;
		for(E entity : arg0) {
			if(add(entity)) {
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public void clear() {
		for(int i = 1; i < entities.length; i++) {
			entities[i] = null;
		}
	}

	@Override
	public boolean contains(Object arg0) {
		for(int i = 1; i < entities.length; i++) {
			if(entities[i] == arg0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		boolean failed = false;
		for(Object o : arg0) {
			if(!contains(o)) {
				failed = true;
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
		return new EntityListIterator<E>(this);
	}

	@Override
	public boolean remove(Object arg0) {
		for(int i = 1; i < entities.length; i++) {
			if(entities[i] == arg0) {
				entities[i] = null;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		boolean changed = false;
		for(Object o : arg0) {
			if(remove(o)) {
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		boolean changed = false;
		for(int i = 1; i < entities.length; i++) {
			if(entities[i] != null) {
				if(!arg0.contains(entities[i])) {
					entities[i] = null;
					changed = true;
				}
			}
		}
		return changed;
	}

	@Override
	public int size() {
		int size = 0;
		for(int i = 1; i < entities.length; i++) {
			if(entities[i] != null) {
				size++;
			}
		}
		return size;
	}

	@Override
	public Entity[] toArray() {
		int size = size();
		Entity[] array = new Entity[size];
		int ptr = 0;
		for(int i = 1; i < entities.length; i++) {
			if(entities[i] != null) {
				array[ptr++] = entities[i];
			}
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] arg0) {
		Entity[] arr = toArray();
		return (T[]) Arrays.copyOf(arr, arr.length, arg0.getClass());
	}

}
