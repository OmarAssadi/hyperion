package com.grahamedgecombe.rs2.model;

public class Sprites {
	
	private int primary = -1;
	private int secondary = -1;
	
	public int getPrimarySprite() {
		return primary;
	}
	
	public int getSecondarySprite() {
		return secondary;
	}
	
	public void setSprite(int sprite) {
		primary = sprite;
		secondary = -1;
	}
	
	public void setSprites(int primary, int secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}

}
