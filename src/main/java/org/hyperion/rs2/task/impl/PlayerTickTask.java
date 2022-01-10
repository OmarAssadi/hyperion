package org.hyperion.rs2.task.impl;

import org.hyperion.rs2.GameEngine;
import org.hyperion.rs2.model.ChatMessage;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.task.Task;

import java.util.Queue;

/**
 * A task which is executed before an <code>UpdateTask</code>. It is similar to
 * the call to <code>process()</code> but you should use <code>Event</code>s
 * instead of putting timers in this class.
 *
 * @author Graham Edgecombe
 */
public class PlayerTickTask implements Task {

    /**
     * The player.
     */
    private final Player player;

    /**
     * Creates a tick task for a player.
     *
     * @param player The player to create the tick task for.
     */
    public PlayerTickTask(final Player player) {
        this.player = player;
    }

    @Override
    public void execute(final GameEngine context) {
        final Queue<ChatMessage> messages = player.getChatMessageQueue();
        if (messages.size() > 0) {
            player.getUpdateFlags().flag(UpdateFlag.CHAT);
            final ChatMessage message = player.getChatMessageQueue().poll();
            player.setCurrentChatMessage(message);
        } else {
            player.setCurrentChatMessage(null);
        }
        player.getWalkingQueue().processNextMovement();
    }

}
