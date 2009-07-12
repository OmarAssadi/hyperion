package com.grahamedgecombe.rs2.model;

import com.grahamedgecombe.rs2.model.UpdateFlags.UpdateFlag;

/**
 * Represents a player's skill and experience levels.
 * @author Graham
 *
 */
public class Skills {
	
	/**
	 * The number of skills.
	 */
	public static final int SKILL_COUNT = 21;
	
	/**
	 * The largest allowed experience.
	 */
	public static final double MAXIMUM_EXP = 200000000;
	
	/**
	 * The skill names.
	 */
	public static final String[] SKILL_NAME	= { "Attack", "Defence",
		"Strength", "Hitpoints", "Range", "Prayer",
		"Magic", "Cooking", "Woodcutting", "Fletching",
		"Fishing", "Firemaking", "Crafting", "Smithing",
		"Mining", "Herblore", "Agility", "Thieving",
		"Slayer", "Farming", "Runecrafting" };
	
	/**
	 * Constants for the skill numbers.
	 */
	public static final int	ATTACK	= 0, DEFENCE = 1, STRENGTH = 2,
		HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6,
		COOKING = 7, WOODCUTTING = 8, FLETCHING = 9,
		FISHING = 10, FIREMAKING = 11, CRAFTING = 12,
		SMITHING = 13, MINING = 14, HERBLORE = 15,
		AGILITY = 16, THIEVING = 17, SLAYER = 18,
		FARMING = 19, RUNECRAFTING = 20;
	
	/**
	 * The player object.
	 */
	private Player player;
	
	/**
	 * The levels array.
	 */
	private int[] levels = new int[SKILL_COUNT];
	
	/**
	 * The experience array.
	 */
	private double[] exps = new double[SKILL_COUNT];
	
	/**
	 * Creates a skills object.
	 * @param player The player whose skills this object represents.
	 */
	public Skills(Player player) {
		this.player = player;
		for(int i = 0; i < SKILL_COUNT; i++) {
			levels[i] = 1;
			exps[i] = 0;
		}
		levels[3] = 10;
		exps[3] = 1184;
	}
	
	/**
	 * Gets the total level.
	 * @return The total level.
	 */
	public int getTotalLevel() {
		int total = 0;
		for(int i = 0; i < levels.length; i++) {
			total += getLevelForExperience(i);
		}
		return total;
	}
	
	/**
	 * Gets the combat level.
	 * @return The combat level.
	 */
	public int getCombatLevel() {
		final int attack = getLevelForExperience(0);
		final int defence = getLevelForExperience(1);
		final int strength = getLevelForExperience(2);
		final int hp = getLevelForExperience(3);
		final int prayer = getLevelForExperience(5);
		final int ranged = getLevelForExperience(4);
		final int magic = getLevelForExperience(6);
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.25) + 1;
		final double melee = (attack + strength) * 0.325;
		final double ranger = Math.floor(ranged * 1.5) * 0.325;
		final double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		return combatLevel;
	}
	
	/**
	 * Sets a level.
	 * @param skill The skill id.
	 * @param level The level.
	 */
	public void setLevel(int skill, int level) {
		levels[skill] = level;
		if(player.isActive()) {
			player.getActionSender().sendSkill(skill);
		}
	}
	
	/**
	 * Sets experience.
	 * @param skill The skill id.
	 * @param exp The experience.
	 */
	public void setExperience(int skill, double exp) {
		exps[skill] = exp;
		if(player.isActive()) {
			player.getActionSender().sendSkill(skill);
		}
	}
	
	/**
	 * Increments a level.
	 * @param skill The skill to increment.
	 */
	public void incrementLevel(int skill) {
		levels[skill]++;
		if(player.isActive()) {
			player.getActionSender().sendSkill(skill);
		}
	}
	
	/**
	 * Decrements a level.
	 * @param skill The skill to decrement.
	 */
	public void decrementLevel(int skill) {
		levels[skill]--;
		if(player.isActive()) {
			player.getActionSender().sendSkill(skill);
		}
	}
	
	/**
	 * Normalizes a level (adjusts it until it is at its normal value).
	 * @param skill The skill to normalize.
	 */
	public void normalizeLevel(int skill) {
		int norm = getLevelForExperience(skill);
		if(levels[skill] > norm) {
			levels[skill]--;
			if(player.isActive()) {
				player.getActionSender().sendSkill(skill);
			}
		} else if(levels[skill] < norm) {
			levels[skill]++;
			if(player.isActive()) {
				player.getActionSender().sendSkill(skill);
			}
		}
	}
	
	/**
	 * Gets a level.
	 * @param skill The skill id.
	 * @return The level.
	 */
	public int getLevel(int skill) {
		return levels[skill];
	}
	
	/**
	 * Gets a level by experience.
	 * @param skill The skill id.
	 * @return The level.
	 */
	public int getLevelForExperience(int skill) {
		double exp = exps[skill];
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp)
				return lvl;
		}
		return 99;
	}
	/**
	 * Gets experience.
	 * @param skill The skill id.
	 * @return The experience.
	 */
	public double getExperience(int skill) {
		return exps[skill];
	}
	
	/**
	 * Adds experience.
	 * @param skill The skill.
	 * @param exp The experience to add.
	 */
	public void addExperience(int skill, double exp) {
		int oldLevel = levels[skill];
		exps[skill] += exp;
		if(exps[skill] > MAXIMUM_EXP) {
			exps[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForExperience(skill);
		int levelDiff = newLevel - oldLevel;
		if(levelDiff > 0) {
			levels[skill] += levelDiff;
			player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
			player.getActionSender().sendSkill(skill);
		}
	}

}