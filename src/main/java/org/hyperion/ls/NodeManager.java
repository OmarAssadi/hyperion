package org.hyperion.ls;

import org.hyperion.rs2.util.NameUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Manages all of the nodes in the login server.
 *
 * @author Graham Edgecombe
 */
public class NodeManager {

    /**
     * The node manager instance.
     */
    private static final NodeManager INSTANCE = new NodeManager();

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(NodeManager.class.getName());
    /**
     * A map of nodes.
     */
    private final Map<Integer, Node> nodes = new HashMap<>();
    /**
     * A map of player names to nodes.
     */
    private final Map<String, Node> players = new HashMap<>();

    /**
     * Gets the node manager instance.
     *
     * @return The node manager instance.
     */
    public static NodeManager getNodeManager() {
        return INSTANCE;
    }

    /**
     * Gets a player.
     *
     * @param name The player name.
     * @return The player object.
     */
    public PlayerData getPlayer(String name) {
        name = NameUtils.formatNameForProtocol(name);
        final Node n = getPlayersNode(name);
        if (n == null) {
            return null;
        }
        return n.getPlayer(name);
    }

    /**
     * Gets the node a player is on.
     *
     * @param player The player.
     * @return The node.
     */
    public Node getPlayersNode(final String player) {
        return players.get(player);
    }

    /**
     * Registers a node.
     *
     * @param node The node to add.
     */
    public void register(final Node node) {
        logger.info("Registering node : World-" + node.getId() + ".");
        nodes.put(node.getId(), node);
    }

    /**
     * Unregisters a node.
     *
     * @param node The node to remove.
     */
    public void unregister(final Node node) {
        logger.info("Unregistering node : World-" + node.getId() + ".");
        nodes.remove(node.getId());
        for (final PlayerData p : node.getPlayers()) {
            players.remove(p.getName());
        }
    }

    /**
     * Gets a node by its id.
     *
     * @param id The id.
     * @return The node.
     */
    public Node getNode(final int id) {
        return nodes.get(id);
    }

    /**
     * Registers a player.
     *
     * @param player The player.
     * @param node   The node.
     */
    public void register(final PlayerData player, final Node node) {
        logger.info("Registering player : " + player.getName() + "...");
        players.put(player.getName(), node);
        node.register(player);
    }

    /**
     * Unregisters a player.
     *
     * @param player The player.
     */
    public void unregister(final PlayerData player) {
        logger.info("Unregistering player : " + player.getName() + "...");
        if (players.containsKey(player.getName())) {
            players.remove(player.getName()).unregister(player);
        }
    }

    /**
     * Gets the collection of all the connected nodes.
     *
     * @return The collection of connected nodes.
     */
    public Collection<Node> getNodes() {
        return nodes.values();
    }

    /**
     * Checks if a login is valid.
     *
     * @param node     The node id.
     * @param password The password.
     * @return Valid flag.
     */
    public boolean isNodeAuthenticationValid(final int node, final String password) {
        // TODO password check
        return !nodes.containsKey(node);
    }

}
