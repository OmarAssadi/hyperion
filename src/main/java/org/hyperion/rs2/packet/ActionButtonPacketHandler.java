package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

import java.util.logging.Logger;

/**
 * Handles clicking on most buttons in the interface.
 *
 * @author Graham Edgecombe
 */
public class ActionButtonPacketHandler implements PacketHandler {

    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger(ActionButtonPacketHandler.class.getName());

    @Override
    public void handle(final Player player, final Packet packet) {
        final int button = packet.getShort();
        switch (button) {
            case 161 -> player.playAnimation(Animation.CRY);
            case 162 -> player.playAnimation(Animation.THINKING);
            case 163 -> player.playAnimation(Animation.WAVE);
            case 164 -> player.playAnimation(Animation.BOW);
            case 165 -> player.playAnimation(Animation.ANGRY);
            case 166 -> player.playAnimation(Animation.DANCE);
            case 167 -> player.playAnimation(Animation.BECKON);
            case 168 -> player.playAnimation(Animation.YES_EMOTE);
            case 169 -> player.playAnimation(Animation.NO_EMOTE);
            case 170 -> player.playAnimation(Animation.LAUGH);
            case 171 -> player.playAnimation(Animation.CHEER);
            case 172 -> player.playAnimation(Animation.CLAP);
            case 13362 -> player.playAnimation(Animation.PANIC);
            case 13363 -> player.playAnimation(Animation.JIG);
            case 13364 -> player.playAnimation(Animation.SPIN);
            case 13365 -> player.playAnimation(Animation.HEADBANG);
            case 13366 -> player.playAnimation(Animation.JOYJUMP);
            case 13367 -> player.playAnimation(Animation.RASPBERRY);
            case 13368 -> player.playAnimation(Animation.YAWN);
            case 13383 -> player.playAnimation(Animation.GOBLIN_BOW);
            case 13384 -> player.playAnimation(Animation.GOBLIN_DANCE);
            case 13369 -> player.playAnimation(Animation.SALUTE);
            case 13370 -> player.playAnimation(Animation.SHRUG);
            case 11100 -> player.playAnimation(Animation.BLOW_KISS);
            case 667 -> player.playAnimation(Animation.GLASS_BOX);
            case 6503 -> player.playAnimation(Animation.CLIMB_ROPE);
            case 6506 -> player.playAnimation(Animation.LEAN);
            case 666 -> player.playAnimation(Animation.GLASS_WALL);
            case 2458 -> player.getActionSender().sendLogout();
            case 5387 -> player.getSettings().setWithdrawAsNotes(false);
            case 5386 -> player.getSettings().setWithdrawAsNotes(true);
            case 8130 -> player.getSettings().setSwapping(true);
            case 8131 -> player.getSettings().setSwapping(false);
            default -> logger.info("Unhandled action button : " + button);
        }
    }

}
