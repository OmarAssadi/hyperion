package com.grahamedgecombe.rs2.model;

public class Item {
	
	private int id;
	private int count;
	
	public Item(int id) {
		this(id, 1);
	}
	
	public Item(int id, int count) {
		this.id = id;
		this.count = count;
	}
	
	public int getId() {
		return id;
	}
	
	public int getCount() {
		return count;
	}

}
