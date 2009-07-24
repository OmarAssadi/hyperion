package org.hyperion.rs2.model;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel.MapMode;

/**
 * Manages all of the in-game objects.
 * @author Graham
 *
 */
public class ObjectMap {
	
	/**
	 * The maximum number of allowed objects.
	 */
	public static final int MAX_OBJECTS = 1280618;
	
	/**
	 * The objects.
	 */
	private GameObject[] objects = new GameObject[MAX_OBJECTS];
	
	/**
	 * Loads the objects in the map.
	 * @throws IOException if an I/O error occurs.
	 */
	public void load() throws IOException {
		RandomAccessFile raf = new RandomAccessFile("data/worldmap.bin", "r");
		try {
			ByteBuffer bb = raf.getChannel().map(MapMode.READ_ONLY, 0, raf.length());
			for(int i = 0; i < MAX_OBJECTS; i++) {
				int id = bb.getShort() & 0xFFFF;
				int x = bb.getShort() & 0xFFFF;
				int y = bb.getShort() & 0xFFFF;
				int z = bb.get() & 0xFF;
				int type = bb.get() & 0xFF;
				int face = bb.get() & 0xFF;
				objects[i] = new GameObject(GameObjectDefinition.forId(id), Location.create(x, y, z), type, face);
			}
		} finally {
			raf.close();
		}
	}

}
