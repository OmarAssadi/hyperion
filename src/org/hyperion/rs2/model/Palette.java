package org.hyperion.rs2.model;

/**
 * Manages a palette of map regions for use in the constructed map region
 * packet.
 * @author Graham Edgecombe
 *
 */
public class Palette {
	
	/**
	 * Normal direction.
	 */
	public static final int DIRECTION_NORMAL = 0;
	
	/**
	 * Rotation direction clockwise by 0 degrees.
	 */
	public static final int DIRECTION_CW_0 = 0;
	
	/**
	 * Rotation direction clockwise by 90 degrees.
	 */
	public static final int DIRECTION_CW_90 = 1;
	
	/**
	 * Rotation direction clockwise by 180 degrees.
	 */
	public static final int DIRECTION_CW_180 = 2;
	
	/**
	 * Rotation direction clockwise by 270 degrees.
	 */
	public static final int DIRECTION_CW_270 = 3;
	
	public static class Tile {
		
		private int x;
		
		private int y;
		
		private int z;
		
		private int rot;
		
		public Tile(int x, int y) {
			this(x, y, 0);
		}
		
		public Tile(int x, int y, int z) {
			this(x, y, z, DIRECTION_NORMAL);
		}
		
		public Tile(int x, int y, int z, int rot) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.rot = rot;
		}
		
		public int getX() {
			return x / 8;
		}
		
		public int getY() {
			return y / 8;
		}
		
		public int getZ() {
			return z % 4;
		}
		
		public int getRotation() {
			return rot % 4;
		}
		
	}
	
	/**
	 * The array of tiles.
	 */
	private Tile[][][] tiles = new Tile[13][13][4];
	
	/**
	 * Gets a tile.
	 * @param x X position.
	 * @param y Y position.
	 * @param z Z position.
	 * @return The tile.
	 */
	public Tile getTile(int x, int y, int z) {
		return tiles[x][y][z];
	}
	
	/**
	 * Sets a tile.
	 * @param x X position.
	 * @param y Y position.
	 * @param z Z position.
	 * @param tile The tile.
	 */
	public void setTile(int x, int y, int z, Tile tile) {
		tiles[x][y][z] = tile;
	}

}
