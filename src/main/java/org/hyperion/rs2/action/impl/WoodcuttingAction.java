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
public class WoodcuttingAction extends HarvestingAction {

    /**
     * The delay.
     */
    private static final int DELAY = 3000;
    /**
     * The factor.
     */
    private static final double FACTOR = 0.5;
    /**
     * Whether or not this action grants periodic rewards.
     */
    private static final boolean PERIODIC = true;
    /**
     * The tree type.
     */
    private final Tree tree;
    /**
     * The axe type.
     */
    private Axe axe;

    /**
     * Creates the <code>WoodcuttingAction</code>.
     *
     * @param player The player performing the action.#
     * @param tree   The tree.
     */
    public WoodcuttingAction(final Player player, final Location location, final Tree tree) {
        super(player, location);
        this.tree = tree;
    }

    @Override
    public long getHarvestDelay() {
        return DELAY;
    }

    @Override
    public void init() {
        final Player player = getPlayer();
        final int wc = player.getSkills().getLevel(Skills.WOODCUTTING);
        for (final Axe axe : Axe.values()) {
            if ((player.getEquipment().contains(axe.getId()) || player.getInventory().contains(axe.getId())) && wc >= axe.getRequiredLevel()) {
                this.axe = axe;
                break;
            }
        }
        if (axe == null) {
            player.getActionSender().sendMessage("You do not have an axe that you can use.");
            stop();
            return;
        }
        if (wc < tree.getRequiredLevel()) {
            player.getActionSender().sendMessage("You do not have the required level to cut down that tree.");
            stop();
            return;
        }
        player.getActionSender().sendMessage("You swing your axe at the tree...");
    }

    @Override
    public Animation getAnimation() {
        return Animation.create(axe.getAnimation());
    }

    @Override
    public int getCycles() {
        if (tree == Tree.NORMAL) {
            return 1;
        } else {
            return new Random().nextInt(5) + 5;
        }
    }

    @Override
    public Item getHarvestedItem() {
        return new Item(tree.getLogId(), 1);
    }

    @Override
    public double getFactor() {
        return FACTOR;
    }

    @Override
    public boolean getPeriodicRewards() {
        return PERIODIC;
    }

    @Override
    public int getSkill() {
        return Skills.WOODCUTTING;
    }

    @Override
    public double getExperience() {
        return tree.getExperience();
    }

    /**
     * Represents types of axes.
     *
     * @author Graham Edgecombe
     */
    public enum Axe {

        /**
         * Rune axe.
         */
        RUNE(1359, 41, 867),

        /**
         * Adamant axe.
         */
        ADAMANT(1357, 31, 869),

        /**
         * Mithril axe.
         */
        MITHRIL(1355, 21, 871),

        /**
         * Black axe.
         */
        BLACK(1361, 6, 873),

        /**
         * Steel axe.
         */
        STEEL(1353, 6, 875),

        /**
         * Iron axe.
         */
        IRON(1349, 1, 877),

        /**
         * Bronze axe.
         */
        BRONZE(1351, 1, 879);

        /**
         * A map of object ids to axes.
         */
        private static final Map<Integer, Axe> axes = new HashMap<>();

        /**
         * Populates the tree map.
         */
        static {
            for (final Axe axe : Axe.values()) {
                axes.put(axe.id, axe);
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
        Axe(final int id, final int level, final int animation) {
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
        public static Axe forId(final int object) {
            return axes.get(object);
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
     * Represents types of trees.
     *
     * @author Graham Edgecombe
     */
    public enum Tree {

        /**
         * Normal tree.
         */
        NORMAL(1511, 1, 50, new int[]{1276, 1277, 1278, 1279, 1280,
            1282, 1283, 1284, 1285, 1286, 1289, 1290, 1291, 1315, 1316, 1318,
            1319, 1330, 1331, 1332, 1365, 1383, 1384, 2409, 3033, 3034, 3035,
            3036, 3881, 3882, 3883, 5902, 5903, 5904}),

        /**
         * Willow tree.
         */
        WILLOW(1519, 30, 135, new int[]{1308, 5551, 5552, 5553}),

        /**
         * Oak tree.
         */
        OAK(1521, 15, 75, new int[]{1281, 3037}),

        /**
         * Magic tree.
         */
        MAGIC(1513, 75, 500, new int[]{1292, 1306}),

        /**
         * Maple tree.
         */
        MAPLE(1517, 45, 200, new int[]{1307, 4677}),

        /**
         * Mahogany tree.
         */
        MAHOGANY(6332, 50, 250, new int[]{9034}),

        /**
         * Teak tree.
         */
        TEAK(6333, 35, 170, new int[]{9036}),

        /**
         * Achey tree.
         */
        ACHEY(2862, 1, 50, new int[]{2023}),

        /**
         * Yew tree.
         */
        YEW(1515, 60, 350, new int[]{1309});

        /**
         * A map of object ids to trees.
         */
        private static final Map<Integer, Tree> trees = new HashMap<>();

        /**
         * Populates the tree map.
         */
        static {
            for (final Tree tree : Tree.values()) {
                for (final int object : tree.objects) {
                    trees.put(object, tree);
                }
            }
        }

        /**
         * The object ids of this tree.
         */
        private final int[] objects;
        /**
         * The minimum level to cut this tree down.
         */
        private final int level;
        /**
         * The log of this tree.
         */
        private final int log;
        /**
         * The experience.
         */
        private final double experience;

        /**
         * Creates the tree.
         *
         * @param log        The log id.
         * @param level      The required level.
         * @param experience The experience per log.
         * @param objects    The object ids.
         */
        Tree(final int log, final int level, final double experience, final int[] objects) {
            this.objects = objects;
            this.level = level;
            this.experience = experience;
            this.log = log;
        }

        /**
         * Gets a tree by an object id.
         *
         * @param object The object id.
         * @return The tree, or <code>null</code> if the object is not a tree.
         */
        public static Tree forId(final int object) {
            return trees.get(object);
        }

        /**
         * Gets the log id.
         *
         * @return The log id.
         */
        public int getLogId() {
            return log;
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
