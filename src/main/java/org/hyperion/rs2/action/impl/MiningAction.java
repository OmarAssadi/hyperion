package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * An action for cutting down trees.
 *
 * @author Graham Edgecombe
 */
public class MiningAction extends HarvestingAction {

    /**
     * Whether or not this action grants periodic rewards.
     */
    private static final boolean PERIODIC = false;
    /**
     * The delay.
     */
    private final int delay = 3000;
    /**
     * The factor.
     */
    private final double factor = 0.5;
    /**
     * The node type.
     */
    private final Node node;
    /**
     * The axe type.
     */
    private Pickaxe pickaxe;
    /**
     * The cycle count.
     */
    private int cycleCount = 0;

    /**
     * Creates the <code>WoodcuttingAction</code>.
     *
     * @param player The player performing the action.#
     * @param tree   The tree.
     */
    public MiningAction(final Player player, final Location location, final Node node) {
        super(player, location);
        this.node = node;
    }

    @Override
    public long getHarvestDelay() {
        return delay;
    }

    @Override
    public void init() {
        final Player player = getPlayer();
        final int mining = player.getSkills().getLevel(Skills.MINING);
        for (final Pickaxe pickaxe : Pickaxe.values()) {
            if ((player.getEquipment().contains(pickaxe.getId()) || player.getInventory().contains(pickaxe.getId())) && mining >= pickaxe.getRequiredLevel()) {
                this.pickaxe = pickaxe;
                break;
            }
        }
        if (pickaxe == null) {
            player.getActionSender().sendMessage("You do not have a pickaxe for which you have the level to use.");
            stop();
            return;
        }
        if (mining < node.getRequiredLevel()) {
            player.getActionSender().sendMessage("You do not have the required level to mine this rock.");
            stop();
            return;
        }
        player.getActionSender().sendMessage("You swing your pick at the rock...");
        cycleCount = calculateCycles(player, node, pickaxe);

    }

    @Override
    public Animation getAnimation() {
        return Animation.create(pickaxe.getAnimation());
    }

    @Override
    public int getCycles() {
        return cycleCount;
    }

    @Override
    public Item getHarvestedItem() {
        return new Item(node.getOreId(), 1);
    }

    @Override
    public double getFactor() {
        return factor;
    }

    @Override
    public boolean getPeriodicRewards() {
        return PERIODIC;
    }

    @Override
    public int getSkill() {
        return Skills.MINING;
    }

    @Override
    public double getExperience() {
        return node.getExperience();
    }

    /**
     * Attempts to calculate the number of cycles to mine the ore based on mining level, ore level and axe speed modifier.
     * Needs heavy work. It's only an approximation.
     */
    public int calculateCycles(final Player player, final Node node, final Pickaxe pickaxe) {
        final int mining = player.getSkills().getLevel(Skills.MINING);
        final int difficulty = node.getRequiredLevel();
        final int modifier = pickaxe.getRequiredLevel();
        final int random = new Random().nextInt(3);
        double cycleCount = 1;
        cycleCount = Math.ceil((difficulty * 60 - mining * 20) / modifier * 0.25 - random * 4);
        if (cycleCount < 1) {
            cycleCount = 1;
        }
        //player.getActionSender().sendMessage("You must wait " + cycleCount + " cycles to mine this ore.");
        return (int) cycleCount;
    }

    /**
     * Represents types of axes.
     *
     * @author Graham Edgecombe
     */
    public enum Pickaxe {

        /**
         * Rune pickaxe.
         */
        RUNE(1275, 41, 624),

        /**
         * Adamant pickaxe.
         */
        ADAMANT(1271, 31, 628),

        /**
         * Mithril pickaxe.
         */
        MITHRIL(1273, 21, 629),

        /**
         * Steel pickaxe.
         */
        STEEL(1269, 11, 627),

        /**
         * Iron pickaxe.
         */
        IRON(1267, 5, 626),

        /**
         * Bronze pickaxe.
         */
        BRONZE(1265, 1, 625);

        /**
         * A map of object ids to axes.
         */
        private static final Map<Integer, Pickaxe> pickaxes = new HashMap<>();

        /**
         * Populates the tree map.
         */
        static {
            for (final Pickaxe pickaxe : Pickaxe.values()) {
                pickaxes.put(pickaxe.id, pickaxe);
            }
        }

        /**
         * The id.
         */
        private final int id;
        /**
         * The level.
         */
        private final int level;
        /**
         * The animation.
         */
        private final int animation;

        /**
         * Creates the axe.
         *
         * @param id        The id.
         * @param level     The required level.
         * @param animation The animation id.
         */
        Pickaxe(final int id, final int level, final int animation) {
            this.id = id;
            this.level = level;
            this.animation = animation;
        }

        /**
         * Gets a axe by an object id.
         *
         * @param object The object id.
         * @return The axe, or <code>null</code> if the object is not a axe.
         */
        public static Pickaxe forId(final int object) {
            return pickaxes.get(object);
        }

        /**
         * Gets the id.
         *
         * @return The id.
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the required level.
         *
         * @return The required level.
         */
        public int getRequiredLevel() {
            return level;
        }

        /**
         * Gets the animation id.
         *
         * @return The animation id.
         */
        public int getAnimation() {
            return animation;
        }
    }

    /**
     * Represents types of nodes.
     *
     * @author Graham Edgecombe
     */
    public enum Node {

        /**
         * Copper ore.
         */
        COPPER(436, 1, 17.5, new int[]{2090, 2091}),

        /**
         * Tin ore.
         */
        TIN(438, 1, 17.5, new int[]{2094, 2095}),

        /**
         * Blurite ore.
         */
        BLURITE(668, 10, 17.5, new int[]{2110}),

        /**
         * Iron ore.
         */
        IRON(440, 15, 35, new int[]{2092, 2093}),

        /**
         * Silver ore.
         */
        SILVER(442, 20, 40, new int[]{2100, 2101}),

        /**
         * Gold ore.
         */
        GOLD(444, 40, 65, new int[]{2098, 2099}),

        /**
         * Coal ore.
         */
        COAL(453, 30, 50, new int[]{2096, 2097}),

        /**
         * Mithril ore.
         */
        MITHRIL(447, 55, 80, new int[]{2102, 2103}),

        /**
         * Adamantite ore.
         */
        ADAMANTITE(449, 70, 95, new int[]{2104, 2105}),

        /**
         * Rune ore.
         */
        RUNE(451, 85, 125, new int[]{2106, 2107}),

        /**
         * Clay ore.
         */
        CLAY(434, 1, 5, new int[]{2108, 2109});

        /**
         * A map of object ids to nodes.
         */
        private static final Map<Integer, Node> nodes = new HashMap<>();

        /**
         * Populates the node map.
         */
        static {
            for (final Node node : Node.values()) {
                for (final int object : node.objects) {
                    nodes.put(object, node);
                }
            }
        }

        /**
         * The object ids of this node.
         */
        private final int[] objects;
        /**
         * The minimum level to mine this node.
         */
        private final int level;
        /**
         * The ore this node contains.
         */
        private final int ore;
        /**
         * The experience.
         */
        private final double experience;

        /**
         * Creates the node.
         *
         * @param ore        The ore id.
         * @param level      The required level.
         * @param experience The experience per ore.
         * @param objects    The object ids.
         */
        Node(final int ore, final int level, final double experience, final int[] objects) {
            this.objects = objects;
            this.level = level;
            this.experience = experience;
            this.ore = ore;
        }

        /**
         * Gets a node by an object id.
         *
         * @param object The object id.
         * @return The node, or <code>null</code> if the object is not a node.
         */
        public static Node forId(final int object) {
            return nodes.get(object);
        }

        /**
         * Gets the ore id.
         *
         * @return The ore id.
         */
        public int getOreId() {
            return ore;
        }

        /**
         * Gets the object ids.
         *
         * @return The object ids.
         */
        public int[] getObjectIds() {
            return objects;
        }

        /**
         * Gets the required level.
         *
         * @return The required level.
         */
        public int getRequiredLevel() {
            return level;
        }

        /**
         * Gets the experience.
         *
         * @return The experience.
         */
        public double getExperience() {
            return experience;
        }

    }

}
