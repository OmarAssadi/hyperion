package com.grahamedgecombe.rs2.model;

public class Container<E extends Item> {

	private int capacity;
	private Item[] items;
	
	public Container(int capacity) {
		this.capacity = capacity;
		this.items = new Item[capacity];
	}
	
	@SuppressWarnings("unchecked")
	public E get(int index) {
		return (E) items[index];
	}
	
	public void set(int index, E item) {
		items[index] = item;
	}
	
	public int getCapacity() {
		return capacity;
	}

}
