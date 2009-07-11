package com.grahamedgecombe.rs2.task;

import java.util.Iterator;

import com.grahamedgecombe.rs2.Constants;
import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.World;
import com.grahamedgecombe.rs2.net.Packet;
import com.grahamedgecombe.rs2.net.PacketBuilder;

public class UpdateTask implements Task {
	
	private Player player;
	
	public UpdateTask(Player player) {
		this.player = player;
	}

	@Override
	public void execute(GameEngine context) {
		PacketBuilder packet = new PacketBuilder(81, Packet.Type.VARIABLE_SHORT);
		packet.startBitAccess();
		
		updateThisPlayerMovement(packet);
		updatePlayer(packet, player);
		
		packet.putBits(8, player.getLocalEntities().size());
		
		PacketBuilder updateBlock = new PacketBuilder();
		for(Iterator<Player> it$ = player.getLocalEntities().iterator(); it$.hasNext();) {
			Player otherPlayer = it$.next();
			if(otherPlayer.getLocation().isWithinDistance(player.getLocation())) {
				
			} else {
				it$.remove();
			}
		}
		for(Player otherPlayer : World.getWorld().getPlayers()) {
			if(otherPlayer == player || otherPlayer.getLocalEntities().contains(player)) {
				continue;
			}
			if(otherPlayer.getLocation().isWithinDistance(player.getLocation())) {
				
			}
		}
		if(!updateBlock.isEmpty()) {
			packet.putBits(11, 2047);
			packet.finishBitAccess();
			packet.put(updateBlock.toPacket().getPayload());
		} else {
			packet.finishBitAccess();
		}
		player.getSession().write(packet.toPacket());
	}

	public void updatePlayer(PacketBuilder packet, Player otherPlayer) {
		if(!otherPlayer.getUpdateFlags().isUpdateRequired()) {
			return;
		}
		int mask = 0;
		
		if(mask >= 0x100) {
			mask |= 0x40;
			packet.put((byte) (mask & 0xFF));
			packet.put((byte) (mask >> 8));
		} else {
			packet.put((byte) (mask));
		}
	}

	public void updateThisPlayerMovement(PacketBuilder packet) {
		if(player.isTeleporting()) {
			packet.putBits(1, 1);
			packet.putBits(2, 3);
			packet.putBits(2, player.getLocation().getZ());
			packet.putBits(1, 1);
			packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
			packet.putBits(7, player.getLocation().getLocalY(player.getLastKnownRegion()));
			packet.putBits(7, player.getLocation().getLocalX(player.getLastKnownRegion()));
		} else {
			if(player.getSprites().getPrimarySprite() == -1) {
				if(player.getUpdateFlags().isUpdateRequired()) {
					packet.putBits(1, 1);
					packet.putBits(2, 0);
				} else {
					packet.putBits(1, 0);
				}
			} else {
				if(player.getSprites().getSecondarySprite() == -1) {
					packet.putBits(1, 1);
					packet.putBits(2, 1);
					packet.putBits(3, Constants.SERVER_DIRECTION_TO_CLIENT[player.getSprites().getPrimarySprite()]);
					packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
				} else {
					packet.putBits(1, 1);
					packet.putBits(2, 2);
					packet.putBits(3, Constants.SERVER_DIRECTION_TO_CLIENT[player.getSprites().getPrimarySprite()]);
					packet.putBits(3, Constants.SERVER_DIRECTION_TO_CLIENT[player.getSprites().getSecondarySprite()]);
					packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
				}
			}
		}
	}

}
