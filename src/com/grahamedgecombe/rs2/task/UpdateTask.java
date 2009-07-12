package com.grahamedgecombe.rs2.task;

import java.util.Iterator;

import com.grahamedgecombe.rs2.GameEngine;
import com.grahamedgecombe.rs2.model.Appearance;
import com.grahamedgecombe.rs2.model.ChatMessage;
import com.grahamedgecombe.rs2.model.Equipment;
import com.grahamedgecombe.rs2.model.Item;
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
		
		boolean chat = player.getUpdateFlags().get(UpdateFlag.CHAT);
		player.getUpdateFlags().set(UpdateFlag.CHAT, false);
		updateThisPlayerMovement(packet);
		updatePlayer(updateBlock, player, false);
		player.getUpdateFlags().set(UpdateFlag.CHAT, chat);
		
		packet.putBits(8, player.getLocalPlayers().size());
		
		for(Iterator<Player> it$ = player.getLocalPlayers().iterator(); it$.hasNext();) {
			Player otherPlayer = it$.next();
			if(World.getWorld().getPlayers().contains(otherPlayer) && !otherPlayer.isTeleporting() && otherPlayer.getLocation().isWithinDistance(player.getLocation())) {
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
		if(xPos > 15) {
			xPos -= 32;
		}
		if(yPos > 15) {
			yPos -= 32;
		}
		packet.putBits(5, yPos);
		packet.putBits(5, xPos);
	}

	public void updatePlayer(PacketBuilder packet, Player otherPlayer, boolean forceAppearance) {
		if(!otherPlayer.getUpdateFlags().isUpdateRequired() && !forceAppearance) {
			return;
		}
		int mask = 0;
		
		if(otherPlayer.getUpdateFlags().get(UpdateFlag.CHAT)) {
			mask |= 0x80;
		}
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
		
		if(otherPlayer.getUpdateFlags().get(UpdateFlag.CHAT)) {
			appendChatUpdate(packet, otherPlayer);
		}
		if(otherPlayer.getUpdateFlags().get(UpdateFlag.APPEARANCE) || forceAppearance) {
			appendPlayerAppearance(packet, otherPlayer);
		}
	}
	
	public void appendChatUpdate(PacketBuilder packet, Player otherPlayer) {
		ChatMessage cm = otherPlayer.getCurrentChatMessage();
		
		byte[] bytes = cm.getText();
		
		packet.putLEShort(((cm.getColour() & 0xFF) << 8) | (cm.getEffects() & 0xFF));
		packet.put((byte) otherPlayer.getRights().toInteger());
		packet.putByteC(bytes.length);
		for(int ptr = bytes.length -1; ptr >= 0; ptr--) {
			packet.put(bytes[ptr]);
		}
	}

	public void appendPlayerAppearance(PacketBuilder packet, Player otherPlayer) {
		Appearance app = otherPlayer.getAppearance();
		Equipment eq = otherPlayer.getEquipment();
		
		PacketBuilder playerProps = new PacketBuilder();
		playerProps.put((byte) 0); // gender
		playerProps.put((byte) 0); // skull icon
		
		for(int i = 0; i < 4; i++) {
			if(eq.isEquipped(i)) {
				playerProps.putShort((short) 0x200 + eq.getEquipment(i).getId());
			} else {
				playerProps.put((byte) 0);
			}
		}
		if(eq.isEquipped(Equipment.SLOT_CHEST)) {
			playerProps.putShort((short) 0x200 + eq.getEquipment(Equipment.SLOT_CHEST).getId());
		} else {
			playerProps.putShort((short) 0x100 + app.getChest()); // chest
		}
		if(eq.isEquipped(Equipment.SLOT_SHIELD)){
			playerProps.putShort((short) 0x200 + eq.getEquipment(Equipment.SLOT_SHIELD).getId());
		} else {
			playerProps.put((byte) 0);
		}
		Item chest = eq.getEquipment(Equipment.SLOT_CHEST);
		if(chest != null) {
			if(!Equipment.is(Equipment.PLATEBODY, chest)) {
				playerProps.putShort((short) 0x100 + app.getArms());
			} else {
				playerProps.putShort((short) 0x200 + chest.getId());
			}
		} else {
			playerProps.putShort((short) 0x100 + app.getArms());
		}
		if(eq.isEquipped(Equipment.SLOT_BOTTOMS)) {
			playerProps.putShort((short) 0x200 + eq.getEquipment(Equipment.SLOT_BOTTOMS).getId());
		} else {
			playerProps.putShort((short) 0x100 + app.getLegs());
		}
		Item helm = eq.getEquipment(Equipment.SLOT_HELM);
		if(helm != null) {
			if(!Equipment.is(Equipment.FULL_HELM, helm) && !Equipment.is(Equipment.FULL_MASK, helm)) {
				playerProps.putShort((short) 0x100 + app.getHead());
			} else {
				playerProps.put((byte) 0);
			}
		} else {
			playerProps.putShort((short) 0x100 + app.getHead());
		}
		if(eq.isEquipped(Equipment.SLOT_GLOVES)) {
			playerProps.putShort((short) 0x200 + eq.getEquipment(Equipment.SLOT_GLOVES).getId());
		} else {
			playerProps.putShort((short) 0x100 + app.getHands());
		}
		if(eq.isEquipped(Equipment.SLOT_BOOTS)) {
			playerProps.putShort((short) 0x200 + eq.getEquipment(Equipment.SLOT_BOOTS).getId());
		} else {
			playerProps.putShort((short) 0x100 + app.getFeet());
		}
		boolean fullHelm = false;
		if(helm != null) {
			fullHelm = !Equipment.is(Equipment.FULL_HELM, helm);
		}
		if(fullHelm || app.getSex() == 1) {
			playerProps.put((byte) 0);
		} else {
			playerProps.putShort((short) 0x100 + app.getBeard());
		}
		
		playerProps.put((byte) app.getHairColour()); // hairc
		playerProps.put((byte) app.getTorsoColour()); // torsoc
		playerProps.put((byte) app.getLegColour()); // legc
		playerProps.put((byte) app.getFeetColour()); // feetc
		playerProps.put((byte) app.getSkinColour()); // skinc
		
		playerProps.putShort((short) 0x328); // stand
		playerProps.putShort((short) 0x337); // stand turn
		playerProps.putShort((short) 0x333); // walk
		playerProps.putShort((short) 0x334); // turn 180
		playerProps.putShort((short) 0x335); // turn 90 cw
		playerProps.putShort((short) 0x336); // turn 90 ccw
		playerProps.putShort((short) 0x338); // run
		
		playerProps.putLong(NameUtils.nameToLong(otherPlayer.getName())); // player name
		playerProps.put((byte) player.getSkills().getCombatLevel()); // combat level
		playerProps.putShort(player.getSkills().getTotalLevel()); // total level
		
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
