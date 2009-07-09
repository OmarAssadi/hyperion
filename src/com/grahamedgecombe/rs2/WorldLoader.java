package com.grahamedgecombe.rs2;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.PlayerDetails;

public interface WorldLoader {
	
	public static class LoginResult {
		
		private int returnCode;
		private Player player;
		
		public LoginResult(int returnCode) {
			this(returnCode, null);
		}
		
		public LoginResult(int returnCode, Player player) {
			this.returnCode = returnCode;
			this.player = player;
		}
		
		public int getReturnCode() {
			return returnCode;
		}
		
		public Player getPlayer() {
			return player;
		}
		
	}
	
	public LoginResult checkLogin(PlayerDetails pd);
	public boolean loadPlayer(Player player);
	public boolean savePlayer(Player player);

}
