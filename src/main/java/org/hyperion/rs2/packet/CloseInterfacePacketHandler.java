package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

/**
 * A packet handler that is called when an interface is closed.
 *
 * @author Graham Edgecombe
 */
public class CloseInterfacePacketHandler implements PacketHandler {

    @Override
    public void handle(final Player player, final Packet packet) {
        player.getInterfaceState().interfaceClosed();
    }

}
