package com.grahamedgecombe.rs2.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A region manager manages all of the regions in the world.
 * @author Graham
 *
 */
public class RegionManager {
	
	/**
	 * The width and height of the map.
	 */
	public static final int MAP_SIZE = Short.MAX_VALUE;
	
	/**
	 * The size each region block should be.
	 */
	public static final int REGION_SIZE = 32;
	
	/**
	 * The number of regions in either direction (horizontal or vertical).
	 */
	public static final int REGIONS = MAP_SIZE / REGION_SIZE;
	
	/**
	 * The number of regions in the client's view.
	 */
	// this all is rounded up to be safe!
	public static final int CLIENT_VIEW_SIZE = ((int) Math.ceil(33D / (double) REGION_SIZE)) + 1;
	
	/**
	 * An array of active regions.
	 */
	private Region[][] regions = new Region[REGIONS][REGIONS];
	
	/**
	 * Checks if a region is valid.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return The region.
	 */
	public synchronized boolean isValidRegion(int x, int y) {
		return x >= 0 && y >= 0 && x < REGIONS && y < REGIONS;
	}
	
	/**
	 * Gets a region.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return The region.
	 * @throws RuntimeException if the region is invalid.
	 */
	public synchronized Region getRegion(int x, int y) {
		if(isValidRegion(x, y)) {
			Region region = regions[x][y];
			if(region == null) {
				region = new Region(x, y);
				regions[x][y] = region;
			}
			return region;
		} else {
			throw new RuntimeException("Invalid region: " + x + "," + y + ".");
		}
	}
	
	/**
	 * Purges old, unused, empty regions from the cache.
	 */
	public synchronized void purgeOldRegions() {
		for(int i = 0; i < regions.length; i++) {
			for(int j = 0; j < regions[i].length; j++) {
				if(regions[i][j] != null) {
					if(regions[i][j].isEmpty()) {
						regions[i][j] = null;
					}
				}
			}
		}
	}

	/**
	 * Gets the region for a specific location.
	 * @param location The location.
	 * @return The region.
	 */
	public synchronized Region getRegionFor(Location location) {
		int x = location.getX() / REGION_SIZE;
		int y = location.getY() / REGION_SIZE;
		return getRegion(x, y);
	}

	/**
	 * Gets players near a region.
	 * @param player The player.
	 * @return The players near the specified player.
	 */
	public synchronized Collection<Player> getNearbyPlayers(Player player) {
		int size = CLIENT_VIEW_SIZE;
		if(size % 2 == 0) {
			size++;
		}
		List<Player> players = new LinkedList<Player>();
		Region root = player.getRegion();
		int min = 0 - (size-1) / 2;
		int max = (size-1) / 2;
		for(int x = min; x <= max; x++) {
			for(int y = min; y <= max; y++) {
				int rx = root.getX() + x;
				int ry = root.getY() + y;
				if(isValidRegion(rx, ry)) {
					Region region = getRegion(rx, ry);
					for(Entity entity : region.getEntities()) {
						if(entity instanceof Player) {
							players.add((Player) entity);
						}
					}
				}
			}
		}
		return Collections.unmodifiableCollection(players);
	}

}
