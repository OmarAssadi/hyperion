package org.hyperion.cache.map;

import org.hyperion.cache.Cache;
import org.hyperion.cache.index.impl.MapIndex;
import org.hyperion.cache.util.ByteBufferUtils;
import org.hyperion.cache.util.ZipUtils;
import org.hyperion.rs2.model.GameObject;
import org.hyperion.rs2.model.GameObjectDefinition;
import org.hyperion.rs2.model.Location;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A class which parses landscape files and fires events to a listener class.
 *
 * @author Graham Edgecombe
 */
public class LandscapeParser {

    /**
     * The cache.
     */
    private final Cache cache;

    /**
     * The cache file.
     */
    private final int area;

    /**
     * The listener.
     */
    private final LandscapeListener listener;

    /**
     * Creates the parser.
     *
     * @param cache    The cache.
     * @param area     The area id.
     * @param listener The listener.
     */
    public LandscapeParser(final Cache cache, final int area, final LandscapeListener listener) {
        this.cache = cache;
        this.area = area;
        this.listener = listener;
    }

    /**
     * Parses the landscape file.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void parse() throws IOException {
        final int x = ((area >> 8) & 0xFF) * 64;
        final int y = (area & 0xFF) * 64;

        final MapIndex index = cache.getIndexTable().getMapIndex(area);

        final ByteBuffer buf = ZipUtils.unzip(cache.getFile(4, index.getLandscapeFile()));
        int objId = -1;
        while (true) {
            final int objIdOffset = ByteBufferUtils.getSmart(buf);
            if (objIdOffset == 0) {
                break;
            } else {
                objId += objIdOffset;
                int objPosInfo = 0;
                while (true) {
                    final int objPosInfoOffset = ByteBufferUtils.getSmart(buf);
                    if (objPosInfoOffset == 0) {
                        break;
                    } else {
                        objPosInfo += objPosInfoOffset - 1;

                        final int localX = objPosInfo >> 6 & 0x3f;
                        final int localY = objPosInfo & 0x3f;
                        final int plane = objPosInfo >> 12;

                        final int objOtherInfo = buf.get() & 0xFF;

                        final int type = objOtherInfo >> 2;
                        final int rotation = objOtherInfo & 3;

                        final Location loc = Location.create(localX + x, localY + y, plane);

                        listener.objectParsed(new GameObject(GameObjectDefinition.forId(objId), loc, type, rotation));
                    }
                }
            }
        }
    }

}
