package com.grahamedgecombe.rs2.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.grahamedgecombe.rs2.model.Entity;

public class EntityListIterator<E extends Entity> implements Iterator<E> {
	
	private Entity[] entities;
	private EntityList<E> entityList;
	private int lastIndex = -1;
	private int cursor = 0;
	private int size;

	public EntityListIterator(EntityList<E> entityList) {
		this.entityList = entityList;
		entities = entityList.toArray(new Entity[0]);
		size = entities.length;
	}

	@Override
	public boolean hasNext() {
		return cursor < size;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E next() {
		if(!hasNext()) {
			throw new NoSuchElementException();
		}
		lastIndex = cursor++;
		return (E) entities[lastIndex];
	}

	@Override
	public void remove() {
		if(lastIndex == -1) {
			throw new IllegalStateException();
		}
		entityList.remove(entities[lastIndex]);
	}

}
