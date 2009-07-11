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
		return getLocalX(this);
	}
	
	public int getLocalY() {
		return getLocalY(this);
	}
	
	public int getLocalX(Location l) {
		return x - 8 * l.getRegionX();
	}
	
	public int getLocalY(Location l) {
		return y - 8 * l.getRegionY();
	}
	
	public int getRegionX() {
		return (x >> 3) - 6;
	}
	
	public int getRegionY() {
		return (y >> 3) - 6;
	}
	
	public boolean isWithinDistance(Location other) {
		if(z != other.z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}
	
	@Override
	public int hashCode() {
		return z << 30 | x << 15 | y;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Location)) {
			return false;
		}
		Location loc = (Location) other;
		return loc.x == x && loc.y == y && loc.z == z;
	}
	
	@Override
	public String toString() {
		return "["+x+","+y+","+z+"]";
	}

}
