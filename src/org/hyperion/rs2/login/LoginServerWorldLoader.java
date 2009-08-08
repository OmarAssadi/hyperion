package org.hyperion.rs2.login;

import org.hyperion.rs2.WorldLoader;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.PlayerDetails;
import org.hyperion.rs2.model.World;

/**
 * A <code>WorldLoader</code> which loads from the login server.
 * @author Graham
 *
 */
public class LoginServerWorldLoader implements WorldLoader {

	@Override
	public LoginResult checkLogin(PlayerDetails pd) {
		if(!World.getWorld().getLoginServerConnector().isConnected()) {
			return new LoginResult(8);
		} else {
			// TODO
			return new LoginResult(11);
		}
	}

	@Override
	public boolean loadPlayer(Player player) {
		return false;
	}

	@Override
	public boolean savePlayer(Player player) {
		return false;
	}

}
