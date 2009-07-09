package com.grahamedgecombe.rs2;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.PlayerDetails;

public interface WorldLoader {
	
	public static class LoadResult {
		
		private int returnCode;
		private Player player;
		
		public LoadResult(int returnCode) {
			this(returnCode, null);
		}
		
		public LoadResult(int returnCode, Player player) {
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
	
	public LoadResult loadPlayer(PlayerDetails pd);
	public boolean savePlayer(Player player);

}
