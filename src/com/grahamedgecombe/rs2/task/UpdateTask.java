package com.grahamedgecombe.rs2.task;

import java.util.Iterator;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.World;
import com.grahamedgecombe.rs2.model.UpdateFlags.UpdateFlag;
import com.grahamedgecombe.rs2.net.Packet;
import com.grahamedgecombe.rs2.net.PacketBuilder;
import com.grahamedgecombe.rs2.util.NameUtils;

public class UpdateTask implements Task {
	
	private Player player;
	
	public UpdateTask(Player player) {
		this.player = player;
	}

	@Override
	public void execute(GameEngine context) {
		if(player.isMapRegionChanging()) {
			player.getActionSender().sendMapRegion();
		}
		
		PacketBuilder updateBlock = new PacketBuilder();
		
		PacketBuilder packet = new PacketBuilder(81, Packet.Type.VARIABLE_SHORT);
		packet.startBitAccess();
		
		updateThisPlayerMovement(packet);
		updatePlayer(updateBlock, player, false);
		
		packet.putBits(8, player.getLocalPlayers().size());
		
		for(Iterator<Player> it$ = player.getLocalPlayers().iterator(); it$.hasNext();) {
			Player otherPlayer = it$.next();
			if(!otherPlayer.isTeleporting() && otherPlayer.getLocation().isWithinDistance(player.getLocation())) {
				updatePlayerMovement(packet, otherPlayer);
				if(otherPlayer.getUpdateFlags().isUpdateRequired()) {
					updatePlayer(updateBlock, otherPlayer, false);
				}
			} else {
				it$.remove();
				packet.putBits(1, 1);
				packet.putBits(2, 3);
			}
		}
		for(Player otherPlayer : World.getWorld().getPlayers()) {
			if(otherPlayer == player || player.getLocalPlayers().contains(otherPlayer)) {
				continue;
			}
			if(otherPlayer.getLocation().isWithinDistance(player.getLocation())) {
				player.getLocalPlayers().add(otherPlayer);
				addNewPlayer(packet, otherPlayer);
				updatePlayer(updateBlock, otherPlayer, true);
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

	public void updatePlayerMovement(PacketBuilder packet, Player otherPlayer) {
		if(otherPlayer.getSprites().getPrimarySprite() == -1) {
			if(otherPlayer.getUpdateFlags().isUpdateRequired()) {
				packet.putBits(1, 1);
				packet.putBits(2, 0);
			} else {
				packet.putBits(1, 0);
			}
		} else if(otherPlayer.getSprites().getSecondarySprite() == -1) {
			packet.putBits(1, 1);
			packet.putBits(2, 1);
			packet.putBits(3, otherPlayer.getSprites().getPrimarySprite());
			packet.putBits(1, otherPlayer.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		} else {
			packet.putBits(1, 1);
			packet.putBits(2, 2);
			packet.putBits(3, otherPlayer.getSprites().getPrimarySprite());
			packet.putBits(3, otherPlayer.getSprites().getSecondarySprite());
			packet.putBits(1, otherPlayer.getUpdateFlags().isUpdateRequired() ? 1 : 0);
		}
	}

	public void addNewPlayer(PacketBuilder packet, Player otherPlayer) {
		packet.putBits(11, otherPlayer.getIndex());
		packet.putBits(1, 1);
		packet.putBits(1, 1);
		int yPos = otherPlayer.getLocation().getY() - player.getLocation().getY();
		int xPos = otherPlayer.getLocation().getX() - player.getLocation().getX();
		if(xPos < 0) {
			xPos += 32;
		}
		if(yPos < 0) {
			yPos += 32;
		}
		packet.putBits(5, yPos);
		packet.putBits(5, xPos);
	}

	public void updatePlayer(PacketBuilder packet, Player otherPlayer, boolean forceAppearance) {
		if(!otherPlayer.getUpdateFlags().isUpdateRequired() && !forceAppearance) {
			return;
		}
		int mask = 0;
		
		if(otherPlayer.getUpdateFlags().get(UpdateFlag.APPEARANCE) || forceAppearance) {
			mask |= 0x10;
		}
		
		if(mask >= 0x100) {
			mask |= 0x40;
			packet.put((byte) (mask & 0xFF));
			packet.put((byte) (mask >> 8));
		} else {
			packet.put((byte) (mask));
		}
		
		if(otherPlayer.getUpdateFlags().get(UpdateFlag.APPEARANCE) || forceAppearance) {
			appendPlayerAppearance(packet, otherPlayer);
		}
	}
	
	public void appendPlayerAppearance(PacketBuilder packet, Player otherPlayer) {
		PacketBuilder playerProps = new PacketBuilder();
		playerProps.put((byte) 0); // gender
		playerProps.put((byte) 0); // skull icon
		
		playerProps.put((byte) 0); // hat
		playerProps.put((byte) 0); // cape
		playerProps.put((byte) 0); // amulet
		playerProps.put((byte) 0); // weapon
		playerProps.putShort((short) 0x100 + 19); // chest
		playerProps.putShort((short) 0x100 + 29); // shield
		playerProps.put((byte) 0); // chest
		playerProps.putShort((short) 0x100 + 39); // legs
		playerProps.putShort((short) 0x100 + 3); // head
		playerProps.putShort((short) 0x100 + 35); // hands
		playerProps.putShort((short) 0x100 + 44); // feet
		playerProps.putShort((short) 0x100 + 0); // beard
		
		playerProps.put((byte) 7); // hairc
		playerProps.put((byte) 8); // torsoc
		playerProps.put((byte) 9); // legc
		playerProps.put((byte) 5); // feetc
		playerProps.put((byte) 0); // skinc
		
		playerProps.putShort((short) 0x328); // stand
		playerProps.putShort((short) 0x337); // stand turn
		playerProps.putShort((short) 0x333); // walk
		playerProps.putShort((short) 0x334); // turn 180
		playerProps.putShort((short) 0x335); // turn 90 cw
		playerProps.putShort((short) 0x336); // turn 90 ccw
		playerProps.putShort((short) 0x338); // run
		
		playerProps.putLong(NameUtils.nameToLong(otherPlayer.getName()));
		playerProps.put((byte) 3); // combat level
		playerProps.putShort(0);
		
		Packet propsPacket = playerProps.toPacket();
		
		packet.putByteC(propsPacket.getLength());
		packet.put(propsPacket.getPayload());
	}

	public void updateThisPlayerMovement(PacketBuilder packet) {
		if(player.isTeleporting() || player.isMapRegionChanging()) {
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
					packet.putBits(3, player.getSprites().getPrimarySprite());
					packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
				} else {
					packet.putBits(1, 1);
					packet.putBits(2, 2);
					packet.putBits(3, player.getSprites().getPrimarySprite());
					packet.putBits(3, player.getSprites().getSecondarySprite());
					packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
				}
			}
		}
	}

}
