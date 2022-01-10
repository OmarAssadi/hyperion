package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.ChatMessage;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.util.TextUtils;

/**
 * Handles public chat messages.
 *
 * @author Graham Edgecombe
 */
public class ChatPacketHandler implements PacketHandler {

    private static final int CHAT_QUEUE_SIZE = 4;

    @Override
    public void handle(final Player player, final Packet packet) {
        final int effects = packet.getByteA() & 0xFF;
        final int colour = packet.getByteA() & 0xFF;
        final int size = packet.getLength() - 2;
        final byte[] rawChatData = new byte[size];
        packet.get(rawChatData);
        final byte[] chatData = new byte[size];
        for (int i = 0; i < size; i++) {
            chatData[i] = (byte) (rawChatData[size - i - 1] - 128);
        }
        if (player.getChatMessageQueue().size() >= CHAT_QUEUE_SIZE) {
            return;
        }
        String unpacked = TextUtils.textUnpack(chatData, size);
        unpacked = TextUtils.filterText(unpacked);
        unpacked = TextUtils.optimizeText(unpacked);
        final byte[] packed = new byte[size];
        TextUtils.textPack(packed, unpacked);
        player.getChatMessageQueue().add(new ChatMessage(effects, colour, packed));
    }

}
