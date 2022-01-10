package org.hyperion.rs2.model;

import org.hyperion.cache.Cache;
import org.hyperion.cache.InvalidCacheException;
import org.hyperion.cache.index.impl.MapIndex;
import org.hyperion.cache.index.impl.StandardIndex;
import org.hyperion.cache.map.LandscapeListener;
import org.hyperion.cache.map.LandscapeParser;
import org.hyperion.cache.obj.ObjectDefinitionListener;
import org.hyperion.cache.obj.ObjectDefinitionParser;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Manages all of the in-game objects.
 *
 * @author Graham Edgecombe
 */
public class ObjectManager implements LandscapeListener, ObjectDefinitionListener {

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(ObjectManager.class.getName());

    /**
     * The number of definitions loaded.
     */
    private int definitionCount = 0;

    /**
     * The count of objects loaded.
     */
    private int objectCount = 0;

    /**
     * Loads the objects in the map.
     *
     * @throws IOException           if an I/O error occurs.
     * @throws InvalidCacheException if the cache is invalid.
     */
    public void load() throws IOException, InvalidCacheException {
        try (final Cache cache = new Cache(new File("./data/cache/"))) {
            logger.info("Loading definitions...");
            final StandardIndex[] defIndices = cache.getIndexTable().getObjectDefinitionIndices();
            new ObjectDefinitionParser(cache, defIndices, this).parse();
            logger.info("Loaded " + definitionCount + " object definitions.");
            logger.info("Loading map...");
            final MapIndex[] mapIndices = cache.getIndexTable().getMapIndices();
            for (final MapIndex index : mapIndices) {
                new LandscapeParser(cache, index.getIdentifier(), this).parse();
            }
            logger.info("Loaded " + objectCount + " objects.");
        }
    }

    @Override
    public void objectParsed(final GameObject obj) {
        objectCount++;
        World.getWorld().getRegionManager().getRegionByLocation(obj.getLocation()).getGameObjects().add(obj);
    }

    @Override
    public void objectDefinitionParsed(final GameObjectDefinition def) {
        definitionCount++;
        GameObjectDefinition.addDefinition(def);
    }

}
