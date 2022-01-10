package org.hyperion.rs2.login;

import org.hyperion.rs2.WorldLoader;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.PlayerDetails;
import org.hyperion.rs2.model.World;

/**
 * A <code>WorldLoader</code> which loads from the login server.
 *
 * @author Graham Edgecombe
 */
public class LoginServerWorldLoader implements WorldLoader {

    @Override
    public LoginResult checkLogin(final PlayerDetails pd) {
        if (World.getWorld().getLoginServerConnector().isAuthenticated()) {
            return World.getWorld().getLoginServerConnector().checkLogin(pd);
        } else {
            return new LoginResult(8);
        }
    }

    @Override
    public boolean loadPlayer(final Player player) {
        return World.getWorld().getLoginServerConnector().loadPlayer(player);
    }

    @Override
    public boolean savePlayer(final Player player) {
        return World.getWorld().getLoginServerConnector().savePlayer(player);
    }

}
