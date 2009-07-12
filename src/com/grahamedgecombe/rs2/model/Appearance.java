package com.grahamedgecombe.rs2.model;

public class Appearance {
	
	private int sex;
	
	private int chest;
	private int arms;
	private int legs;
	private int head;
	private int hands;
	private int feet;
	private int beard;
	
	private int hairColour;
	private int torsoColour;
	private int legColour;
	private int feetColour;
	private int skinColour;
	
	public Appearance() {
		sex = 0;
		head = 0;
		chest = 18;
		arms = 26;
		hands = 33;
		legs = 36;
		feet = 42;
		beard = 10;
		hairColour = 7;
		torsoColour = 8;
		legColour = 9;
		feetColour = 5;
		skinColour = 0;
	}
	
	public int[] getLook() {
		return new int[] {
			sex,
			hairColour,
			torsoColour,
			legColour,
			feetColour,
			skinColour,
			head,
			chest,
			arms,
			hands,
			legs,
			feet,
			beard
		};
	}
	
	public void setLook(int[] look) {
		if(look.length != 13) {
			throw new IllegalArgumentException("Array length must be 13.");
		}
		sex = look[0];
		head = look[6];
		chest = look[7];
		arms = look[8];
		hands = look[9];
		legs = look[10];
		feet = look[11];
		beard = look[12];
		hairColour = look[1];
		torsoColour = look[2];
		legColour = look[3];
		feetColour = look[4];
		skinColour = look[5];
	}
	
	public int getHairColour() {
		return hairColour;
	}
	
	public int getTorsoColour() {
		return torsoColour;
	}
	
	public int getLegColour() {
		return legColour;
	}
	
	public int getFeetColour() {
		return feetColour;
	}
	
	public int getSkinColour() {
		return skinColour;
	}
	
	public int getSex() {
		return sex;
	}
	
	public int getChest() {
		return chest;
	}
	
	public int getArms() {
		return arms;
	}
	
	public int getHead() {
		return head;
	}
	
	public int getHands() {
		return hands;
	}
	
	public int getLegs() {
		return legs;
	}
	
	public int getFeet() {
		return feet;
	}
	
	public int getBeard() {
		return beard;
	}

}
