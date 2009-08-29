package org.hyperion.rs2.model;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel.MapMode;

/**
 * Manages all of the in-game objects.
 * @author Graham Edgecombe
 *
 */
public class ObjectManager {
	
	/**
	 * Loads the objects in the map.
	 * @throws IOException if an I/O error occurs.
	 */
	public void load() throws IOException {
		RandomAccessFile raf = new RandomAccessFile("data/worldmap.bin", "r");
		try {
			ByteBuffer bb = raf.getChannel().map(MapMode.READ_ONLY, 0, raf.length());
			while(bb.remaining() >= 9) {
				int id = bb.getShort() & 0xFFFF;
				int x = bb.getShort() & 0xFFFF;
				int y = bb.getShort() & 0xFFFF;
				int z = bb.get() & 0xFF;
				int type = bb.get() & 0xFF;
				int face = bb.get() & 0xFF;
				Location l = Location.create(x, y, z);
				GameObject obj = new GameObject(GameObjectDefinition.forId(id), l, type, face);
				World.getWorld().getRegionManager().getRegionByLocation(l).getGameObjects().add(obj);
			}
		} finally {
			raf.close();
		}
	}

}
