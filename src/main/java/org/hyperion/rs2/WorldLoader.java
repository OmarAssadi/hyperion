package org.hyperion.rs2;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.PlayerDetails;

/**
 * An interface which describes the methods for loading persistent world
 * information such as players.
 *
 * @author Graham Edgecombe
 */
public interface WorldLoader {

    /**
     * Checks if a set of login details are correct. If correct, creates but
     * does not load, the player object.
     *
     * @param pd The login details.
     * @return The login result.
     */
    LoginResult checkLogin(PlayerDetails pd);

    /**
     * Loads player information.
     *
     * @param player The player object.
     * @return <code>true</code> on success, <code>false</code> on failure.
     */
    boolean loadPlayer(Player player);

    /**
     * Saves player information.
     *
     * @param player The player object.
     * @return <code>true</code> on success, <code>false</code> on failure.
     */
    boolean savePlayer(Player player);

    /**
     * Represents the result of a login request.
     *
     * @author Graham Edgecombe
     */
    class LoginResult {

        /**
         * The return code.
         */
        private final int returnCode;

        /**
         * The player object, or <code>null</code> if the login failed.
         */
        private final Player player;

        /**
         * Creates a login result that failed.
         *
         * @param returnCode The return code.
         */
        public LoginResult(final int returnCode) {
            this(returnCode, null);
        }

        /**
         * Creates a login result that succeeded.
         *
         * @param returnCode The return code.
         * @param player     The player object.
         */
        public LoginResult(final int returnCode, final Player player) {
            this.returnCode = returnCode;
            this.player = player;
        }

        /**
         * Gets the return code.
         *
         * @return The return code.
         */
        public int getReturnCode() {
            return returnCode;
        }

        /**
         * Gets the player.
         *
         * @return The player.
         */
        public Player getPlayer() {
            return player;
        }

    }

}
