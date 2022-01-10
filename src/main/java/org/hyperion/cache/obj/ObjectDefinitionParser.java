package org.hyperion.cache.obj;

import org.hyperion.cache.Archive;
import org.hyperion.cache.Cache;
import org.hyperion.cache.index.impl.StandardIndex;
import org.hyperion.cache.util.ByteBufferUtils;
import org.hyperion.rs2.model.GameObjectDefinition;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A class which parses object definitions in the game cache.
 *
 * @author Graham Edgecombe
 */
public class ObjectDefinitionParser {

    /**
     * The cache.
     */
    private final Cache cache;

    /**
     * The index.
     */
    private final StandardIndex[] indices;

    /**
     * The listener.
     */
    private final ObjectDefinitionListener listener;

    /**
     * Creates the object definition parser.
     *
     * @param cache    The cache.
     * @param indices  The indices in the cache.
     * @param listener The object definition listener.
     */
    public ObjectDefinitionParser(final Cache cache, final StandardIndex[] indices, final ObjectDefinitionListener listener) {
        this.cache = cache;
        this.indices = indices;
        this.listener = listener;
    }

    /**
     * Parses the object definitions in the cache.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void parse() throws IOException {
        final ByteBuffer buf = new Archive(cache.getFile(0, 2)).getFileAsByteBuffer("loc.dat");

        for (final StandardIndex index : indices) {
            final int id = index.getIdentifier();
            final int offset = index.getFile(); // bad naming, should be getOffset()
            buf.position(offset);

            // TODO read the object definition now

            String name = "null";
            String desc = "null";
            int sizeX = 1;
            int sizeY = 1;
            boolean isSolid = true;
            boolean isWalkable = true;
            boolean hasActions = false;

            outer_loop:
            do {
                int configCode;
                do {
                    configCode = buf.get() & 0xFF;
                    if (configCode == 0) {
                        break outer_loop;
                    }
                    switch (configCode) {
                        case 1:
                            int someCounter = buf.get() & 0xFF;
                            for (int i = 0; i < someCounter; i++) {
                                buf.getShort();
                                buf.get();
                            }
                            break;
                        case 2:
                            name = ByteBufferUtils.getString(buf);
                            break;
                        case 3:
                            desc = ByteBufferUtils.getString(buf);
                            break;
                        case 5:
                            someCounter = buf.get() & 0xFF;
                            for (int i = 0; i < someCounter; i++) {
                                buf.getShort();
                            }
                            break;
                        case 14:
                            sizeX = buf.get() & 0xFF;
                            break;
                        case 15:
                            sizeY = buf.get() & 0xFF;
                            break;
                        case 17:
                            isSolid = false;
                            break;
                        case 18:
                            isWalkable = false;
                            break;
                        case 19:
                            // has actions?
                            if (buf.get() == 1) {
                                hasActions = true;
                            }
                            break;
                        case 21:
                        case 23:
                        case 22:
                            // some boolean
                            break;
                        case 24:
                        case 72:
                        case 71:
                        case 70:
                        case 68:
                        case 67:
                        case 66:
                        case 65:
                        case 60:
                            buf.getShort();
                            break;
                        case 30:
                        case 31:
                        case 32:
                        case 33:
                        case 34:
                        case 35:
                        case 36:
                        case 37:
                        case 38:
                            ByteBufferUtils.getString(buf); // actions
                            break;
                        case 40:
                            someCounter = buf.get() & 0xFF; // model colours
                            for (int i = 0; i < someCounter; i++) {
                                buf.getShort();
                                buf.getShort();
                            }
                            break;
                        case 62:
                        case 75:
                        case 74:
                        case 73:
                        case 64:
                            break;
                        case 69:
                        case 39:
                        case 29:
                        case 28:
                        default:
                            buf.get();
                            break;
                    }
                } while (configCode != 77);

                buf.getShort();
                buf.getShort();

                final int counter = buf.get();
                for (int i = 0; i <= counter; i++) {
                    buf.getShort();
                }
            } while (true);

            listener.objectDefinitionParsed(new GameObjectDefinition(id, name, desc, sizeX, sizeY, isSolid, isWalkable, hasActions));
        }
    }

}
