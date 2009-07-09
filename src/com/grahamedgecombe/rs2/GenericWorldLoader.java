package com.grahamedgecombe.rs2;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.PlayerDetails;

public class GenericWorldLoader implements WorldLoader {

	@Override
	public LoginResult checkLogin(PlayerDetails pd) {
		return new LoginResult(2, new Player(pd));
	}

	@Override
	public boolean savePlayer(Player player) {
		return true;
	}

	@Override
	public boolean loadPlayer(Player player) {
		return true;
	}

}
