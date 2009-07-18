package org.hyperion.rs2.model;

import java.util.BitSet;

/**
 * Holds update flags.
 * @author Graham
 *
 */
public class UpdateFlags {
	
	/**
	 * The bitset (flag data).
	 */
	private BitSet flags = new BitSet();
	
	/**
	 * Represents a single type of update flag.
	 * @author Graham
	 *
	 */
	public enum UpdateFlag {
		
		/**
		 * Appearance update.
		 */
		APPEARANCE(0),
		
		/**
		 * Chat update.
		 */
		CHAT(1);
		
		/**
		 * The index in the bitset.
		 */
		private int index;
		
		/**
		 * Creates an update flag.
		 * @param index The index in the bitset.
		 */
		private UpdateFlag(int index) {
			this.index = index;
		}
		
		/**
		 * Gets an update flag.
		 * @return The index in the bitset.
		 */
		public int getIndex() {
			return index;
		}
	}
	
	/**
	 * Checks if an update required.
	 * @return <code>true</code> if 1 or more flags are set,
	 * <code>false</code> if not.
	 */
	public boolean isUpdateRequired() {
		return !flags.isEmpty();
	}
	
	/**
	 * Flags (sets to true) a flag.
	 * @param flag The flag to flag.
	 */
	public void flag(UpdateFlag flag) {
		flags.set(flag.getIndex(), true);
	}
	
	/**
	 * Sets a flag.
	 * @param flag The flag.
	 * @param value The value.
	 */
	public void set(UpdateFlag flag, boolean value) {
		flags.set(flag.getIndex(), value);
	}
	
	/**
	 * Gets the value of a flag.
	 * @param flag The flag to get the value of.
	 * @return The flag value.
	 */
	public boolean get(UpdateFlag flag) {
		return flags.get(flag.getIndex());
	}
	
	/**
	 * Resest all update flags.
	 */
	public void reset() {
		flags.clear();
	}

}
