package org.hyperion.cache.obj;

import org.hyperion.rs2.model.GameObjectDefinition;

/**
 * An object definition listener, which is notified when object definitions
 * have been parsed.
 *
 * @author Graham Edgecombe
 */
public interface ObjectDefinitionListener {

    /**
     * Called when an object definition is parsed.
     *
     * @param def The definition that was parsed.
     */
    void objectDefinitionParsed(GameObjectDefinition def);

}
