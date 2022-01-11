package org.hyperion.rs2.net;

import org.apache.mina.core.session.IoSession;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.packet.DefaultPacketHandler;
import org.hyperion.rs2.packet.PacketHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Managers <code>PacketHandler</code>s.
 *
 * @author Graham Edgecombe
 */
public class PacketManager {

    /**
     * The logger class.
     */
    private static final Logger logger = Logger.getLogger(PacketManager.class.getName());

    /**
     * The instance.
     */
    private static final PacketManager INSTANCE = new PacketManager();
    /**
     * The packet handler array.
     */
    private final PacketHandler[] packetHandlers = new PacketHandler[256];

    /**
     * Creates the packet manager.
     */
    public PacketManager() {
        /*
         * Set default handlers.
         */
        final PacketHandler defaultHandler = new DefaultPacketHandler();
        for (int i = 0; i < packetHandlers.length; i++) {
            if (packetHandlers[i] == null) {
                packetHandlers[i] = defaultHandler;
            }
        }
    }

    /**
     * Gets the packet manager instance.
     *
     * @return The packet manager instance.
     */
    public static PacketManager getPacketManager() {
        return INSTANCE;
    }

    /**
     * Binds an opcode to a handler.
     *
     * @param id      The opcode.
     * @param handler The handler.
     */
    public void bind(final int id, final PacketHandler handler) {
        packetHandlers[id] = handler;
    }

    /**
     * Handles a packet.
     *
     * @param session The session.
     * @param packet  The packet.
     */
    public void handle(final IoSession session, final Packet packet) {
        try {
            packetHandlers[packet.getOpcode()].handle((Player) session.getAttribute("player"), packet);
        } catch (final Exception ex) {
            logger.log(Level.SEVERE, "Exception handling packet.", ex);
            session.close(false);
        }
    }

}
