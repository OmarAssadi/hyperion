package org.hyperion.rs2.model;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.hyperion.cache.Cache;
import org.hyperion.cache.InvalidCacheException;
import org.hyperion.cache.index.impl.MapIndex;
import org.hyperion.cache.map.LandscapeListener;
import org.hyperion.cache.map.LandscapeParser;

/**
 * Manages all of the in-game objects.
 * @author Graham Edgecombe
 *
 */
public class ObjectManager implements LandscapeListener {
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(ObjectManager.class.getName());
	
	/**
	 * The count of objects loaded.
	 */
	private int count = 0;
	
	/**
	 * Loads the objects in the map.
	 * @throws IOException if an I/O error occurs.
	 * @throws InvalidCacheException if the cache is invalid.
	 */
	public void load() throws IOException, InvalidCacheException {
		Cache cache = new Cache(new File("./data/cache/"));
		logger.info("Loading map...");
		try {
			MapIndex[] indices = cache.getIndexTable().getMapIndices();
			for(MapIndex index : indices) {
				new LandscapeParser(cache, index.getIdentifier(), this).parse();
			}
			logger.info("Loaded " + count + " objects.");
		} finally {
			cache.close();
		}
	}

	@Override
	public void objectParsed(GameObject obj) {
		count++;
		World.getWorld().getRegionManager().getRegionByLocation(obj.getLocation()).getGameObjects().add(obj);
	}

}
