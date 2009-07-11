package com.grahamedgecombe.rs2.model;

public class Location {
	
	private int x;
	private int y;
	private int z;
	
	public static Location create(int x, int y, int z) {
		return new Location(x, y, z);
	}
	
	private Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public int getLocalX() {
		return x - 8 * getRegionX();
	}
	
	public int getLocalY() {
		return y - 8 * getRegionY();
	}
	
	public int getRegionX() {
		return x >> 3;
	}
	
	public int getRegionY() {
		return y >> 3;
	}
	
	public boolean isWithinDistance(Location other) {
		if(z != other.z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

}
