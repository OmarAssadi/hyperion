package org.hyperion.rs2.action.impl;
import java.util.HashMap;
import java.util.Map;

import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;

public class ProspectingAction extends InspectAction {
    
    /**
     * The node type.
     */
    private Node node;
    
    /**
     * The delay.
     */
    private static final int DELAY = 3000;
    
    public ProspectingAction(Player player, Location location, Node node) {
        super(player, location);
        this.node = node;
    }
    
    /**
     * Represents types of nodes.
     * @author Graham Edgecombe
     *
     */
    public static enum Node {
        
        /**
         * Copper ore.
         */
        COPPER(436, new int[] { 2090, 2091 }),
        
        /**
         * Tin ore.
         */
        TIN(438, new int[] { 2094, 2095 }),
        
        /**
         * Blurite ore.
         */
        BLURITE(668, new int[] { 2110 }),
        
        /**
         * Iron ore.
         */
        IRON(440, new int[] { 2092, 2093 }),
        
        /**
         * Silver ore.
         */
        SILVER(442, new int[] { 2100, 2101 }),
        
        /**
         * Gold ore.
         */
        GOLD(444, new int[] { 2098, 2099 }),
        
        /**
         * Coal ore.
         */
        COAL(453, new int[] { 2096, 2097 }),
        
        /**
         * Mithril ore.
         */
        MITHRIL(447, new int[] { 2102, 2103 }),
        
        /**
         * Adamantite ore.
         */
        ADAMANTITE(449, new int[] { 2104, 2105 }),
        
        /**
         * Rune ore.
         */
        RUNE(451, new int[] { 2106, 2107}),
        
        /**
         * Clay ore.
         */
        CLAY(434, new int[] { 2108, 2109 });
        
        /**
         * A map of object ids to nodes.
         */
        private static Map<Integer, Node> nodes = new HashMap<Integer, Node>();
        
        /**
         * Gets a node by an object id.
         * @param object The object id.
         * @return The node, or <code>null</code> if the object is not a node.
         */
        public static Node forId(int object) {
            return nodes.get(object);
        }
        
        /**
         * Populates the node map.
         */
        static {
            for(Node node : Node.values()) {
                for(int object : node.objects) {
                    nodes.put(object, node);
                }
            }
        }
        
        /**
         * The object ids of this node.
         */
        private int[] objects;
        
        /**
         * The ore this node contains.
         */
        private int ore;
        
        /**
         * Creates the node.
         * @param ore The ore id.
         * @param level The required level.
         * @param experience The experience per ore.
         * @param objects The object ids.
         */
        private Node(int ore,  int[] objects) {
            this.objects = objects;
            this.ore = ore;
        }
        
        /**
         * Gets the ore id.
         * @return The ore id.
         */
        public int getOreId() {
            return ore;
        }
        
        /**
         * Gets the object ids.
         * @return The object ids.
         */
        public int[] getObjectIds() {
            return objects;
        }
        
    }

    @Override
    public long getInspectDelay() {
        return DELAY;
    }
    
    @Override
    public void init() {        
        final Player player = getPlayer();    
        player.getActionSender().sendMessage("You examine the rock for ores...");
    }

    @Override
    public Node getInspectItem() {
        return node;
    }

}