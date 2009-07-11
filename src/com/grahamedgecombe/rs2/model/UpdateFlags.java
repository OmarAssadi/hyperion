package com.grahamedgecombe.rs2.model;

import java.util.BitSet;

public class UpdateFlags {
	
	private BitSet flags = new BitSet();
	
	public enum UpdateFlag {
		APPEARANCE(0);
		
		private int index;
		
		private UpdateFlag(int index) {
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}
	}
	
	public boolean isUpdateRequired() {
		return !flags.isEmpty();
	}
	
	public void flag(UpdateFlag flag) {
		flags.set(flag.getIndex(), true);
	}
	
	public void set(UpdateFlag flag, boolean value) {
		flags.set(flag.getIndex(), value);
	}
	
	public boolean get(UpdateFlag flag) {
		return flags.get(flag.getIndex());
	}
	
	public void reset() {
		flags.clear();
	}

}
