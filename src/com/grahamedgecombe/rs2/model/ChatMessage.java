package com.grahamedgecombe.rs2.model;

public class ChatMessage {
	
	private int colour;
	private int effects;
	private byte[] text;
	
	public ChatMessage(int colour, int effects, byte[] text) {
		this.colour = colour;
		this.effects = effects;
		this.text = text;
	}
	
	public int getColour() {
		return colour;
	}
	
	public int getEffects() {
		return effects;
	}
	
	public byte[] getText() {
		return text;
	}

}
