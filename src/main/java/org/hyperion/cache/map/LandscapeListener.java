package org.hyperion.cache.map;

import org.hyperion.rs2.model.GameObject;

/**
 * A landscape listener is notified when an object is parsed by a
 * <code>LandscapeParser</code>.
 *
 * @author Graham Edgecombe
 */
public interface LandscapeListener {

    /**
     * Handles actions when an object is parsed.
     *
     * @param obj The object that was parsed.
     */
    void objectParsed(GameObject obj);

}
