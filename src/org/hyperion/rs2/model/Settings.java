package org.hyperion.rs2.model;

/**
 * Contains client-side settings.
 * @author Graham Edgecombe
 *
 */
public class Settings {
	
	/**
	 * Withdraw as notes flag.
	 */
	private boolean withdrawAsNotes = false;
	
	/**
	 * Sets the withdraw as notes flag.
	 * @param withdrawAsNotes The flag.
	 */
	public void setWithdrawAsNotes(boolean withdrawAsNotes) {
		this.withdrawAsNotes = withdrawAsNotes;
	}
	
	/**
	 * Checks if the player is withdrawing as notes.
	 * @return The withdrawing as notes flag.
	 */
	public boolean isWithdrawingAsNotes() {
		return withdrawAsNotes;
	}

}
