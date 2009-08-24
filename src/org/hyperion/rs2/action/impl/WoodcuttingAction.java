package org.hyperion.rs2.action.impl;

import java.util.HashMap;
import java.util.Map;

import org.hyperion.rs2.model.Player;

/**
 * An action for cutting down trees.
 * @author Graham Edgecombe
 *
 */
public class WoodcuttingAction extends HarvestingAction {
	
	/**
	 * Represents types of axes.
	 * @author Graham Edgecombe
	 *
	 */
	private static enum Axe {
		
		BRONZE(1351, 1, 879),
		
		IRON(1349, 1, 877),
		
		STEEL(1353, 6, 875),
		
		MITHRIL(1355, 21, 871),
		
		ADAMANT(1357, 31, 869),
		
		RUNE(1359, 41, 867);
		
		/**
		 * The id.
		 */
		private int id;
		
		/**
		 * The level.
		 */
		private int level;
		
		/**
		 * The animation.
		 */
		private int animation;
		
		/**
		 * A map of object ids to axes.
		 */
		private static Map<Integer, Axe> axes = new HashMap<Integer, Axe>();
		
		/**
		 * Gets a axe by an object id.
		 * @param object The object id.
		 * @return The axe, or <code>null</code> if the object is not a axe.
		 */
		public static Axe forId(int object) {
			return axes.get(object);
		}
		
		/**
		 * Populates the tree map.
		 */
		static {
			for(Axe axe : Axe.values()) {
				axes.put(axe.id, axe);
			}
		}
		
		/**
		 * Creates the axe.
		 * @param id The id.
		 * @param level The required level.
		 * @param animation The animation id.
		 */
		private Axe(int id, int level, int animation) {
			this.id = id;
			this.level = level;
			this.animation = animation;
		}
		
		/**
		 * Gets the id.
		 * @return The id.
		 */
		public int getId() {
			return id;
		}
		
		/**
		 * Gets the required level.
		 * @return The required level.
		 */
		public int getRequiredLevel() {
			return level;
		}
		
		/**
		 * Gets the animation id.
		 * @return The animation id.
		 */
		public int getAnimation() {
			return animation;
		}
		
	}
	
	/**
	 * Represents types of trees.
	 * @author Graham Edgecombe
	 *
	 */
	private static enum Tree {
		
		/**
		 * Normal tree.
		 */
		NORMAL(1511, 1, new int[] { 1276, 1277, 1278, 1279, 1280,
			1282, 1283, 1284, 1285, 1286, 1289, 1290, 1291, 1315, 1316, 1318,
			1319, 1330, 1331, 1332, 1365, 1383, 1384, 2409, 3033, 3034, 3035,
			3036, 3881, 3882, 3883, 5902, 5903, 5904 }),
		
		/**
		 * Willow tree.
		 */
		WILLOW(1519, 30, new int[] { 1308, 5551, 5552, 5553 }),
		
		/**
		 * Oak tree.
		 */
		OAK(1521, 15, new int[] { 1281, 3037 }),
		
		/**
		 * Magic tree.
		 */
		MAGIC(1513, 75, new int[] { 1292, 1306 }),
		
		/**
		 * Maple tree.
		 */
		MAPLE(1517, 45, new int[] { 1307, 4677 }),
		
		/**
		 * Mahogany tree.
		 */
		MAHOGANY(6332, 50, new int[] { 9034 }),
		
		/**
		 * Teak tree.
		 */
		TEAK(6333, 35, new int[] { 9036 }),
		
		/**
		 * Achey tree.
		 */
		ACHEY(2862, 1, new int[] { 2023 }),
		
		/**
		 * Yew tree.
		 */
		YEW(1515, 60, new int[] { 1309 });
		
		/**
		 * A map of object ids to trees.
		 */
		private static Map<Integer, Tree> trees = new HashMap<Integer, Tree>();
		
		/**
		 * Gets a tree by an object id.
		 * @param object The object id.
		 * @return The tree, or <code>null</code> if the object is not a tree.
		 */
		public static Tree forId(int object) {
			return trees.get(object);
		}
		
		/**
		 * Populates the tree map.
		 */
		static {
			for(Tree tree : Tree.values()) {
				for(int object : tree.objects) {
					trees.put(object, tree);
				}
			}
		}
		
		/**
		 * The object ids of this tree.
		 */
		private int[] objects;
		
		/**
		 * The minimum level to cut this tree down.
		 */
		private int level;
		
		/**
		 * The log of this tree.
		 */
		private int log;
		
		/**
		 * Creates the 
		 * @param log
		 * @param level
		 * @param objects
		 */
		private Tree(int log, int level, int[] objects) {
			this.objects = objects;
			this.level = level;
			this.log = log;
		}
		
		/**
		 * Gets the log id.
		 * @return The log id.
		 */
		public int getLogId() {
			return log;
		}
		
		/**
		 * Gets the object ids.
		 * @return The object ids.
		 */
		public int[] getObjectIds() {
			return objects;
		}
		
		/**
		 * Gets the required level.
		 * @return The required level.
		 */
		public int getRequiredLevel() {
			return level;
		}
		
	}
	
	/**
	 * The delay.
	 */
	private static final int DELAY = 3500;
	
	/**
	 * The factor.
	 */
	private static final double FACTOR = 0.5;

	/**
	 * Creates the <code>WoodcuttingAction</code>.
	 * @param player The player performing the action.
	 */
	public WoodcuttingAction(Player player) {
		super(player);
	}

	@Override
	public long getHarvestDelay() {
		return DELAY;
	}

}
