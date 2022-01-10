package org.hyperion.ls;

import org.hyperion.rs2.util.NameUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a single player in the login server.
 *
 * @author Graham Edgecombe
 */
public class PlayerData {

    /**
     * The player name.
     */
    private final String name;

    /**
     * The player rights.
     */
    private final int rights;

    /**
     * The player's friends.
     */
    private final List<String> friends = new LinkedList<>();

    /**
     * The player's ignores.
     */
    private final List<String> ignores = new LinkedList<>();

    /**
     * Creates the player.
     *
     * @param name   The name.
     * @param rights The rights.
     */
    public PlayerData(final String name, final int rights) {
        this.name = NameUtils.formatNameForProtocol(name);
        this.rights = rights;
    }

    /**
     * Gets the list of friends.
     *
     * @return The list of friends.
     */
    public List<String> getFriends() {
        return friends;
    }

    /**
     * Gets the list of ignores.
     *
     * @return The list of ignores.
     */
    public List<String> getIgnores() {
        return ignores;
    }

    /**
     * Gets the player name.
     *
     * @return The player name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player rights.
     *
     * @return The player rights.
     */
    public int getRights() {
        return rights;
    }

}
