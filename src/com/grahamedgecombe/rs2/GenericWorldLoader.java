package com.grahamedgecombe.rs2;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.PlayerDetails;

public class GenericWorldLoader implements WorldLoader {

	@Override
	public LoadResult loadPlayer(PlayerDetails pd) {
		int code = 2;
		Player p = new Player(pd);
		return new LoadResult(code, p);
	}

	@Override
	public boolean savePlayer(Player player) {
		return true;
	}

}
