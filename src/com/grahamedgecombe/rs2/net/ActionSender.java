package com.grahamedgecombe.rs2.net;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;

import com.grahamedgecombe.rs2.Constants;
import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.Skills;
import com.grahamedgecombe.rs2.net.Packet.Type;

public class ActionSender {
	
	private Player player;
	
	public ActionSender(Player player) {
		this.player = player;
	}
	
	public ActionSender sendLogin() {
		player.setActive(true);
		sendDetails();
		sendSidebarInterfaces();
		sendSkills();
		sendMessage("Welcome to RuneScape.");
		sendMapRegion();
		return this;
	}

	public ActionSender sendDetails() {
		player.getSession().write(new PacketBuilder(249).putByteA(player.isMembers() ? 1 : 0).putLEShortA(player.getIndex()).toPacket());
		player.getSession().write(new PacketBuilder(107).toPacket());
		return this;
	}
	
	public ActionSender sendSkills() {
		for(int i = 0; i < Skills.SKILL_COUNT; i++) {
			sendSkill(i);
		}
		return this;
	}
	
	public ActionSender sendSkill(int skill) {
		PacketBuilder bldr = new PacketBuilder(134);
		bldr.put((byte) skill);
		bldr.putInt1((int) player.getSkills().getExperience(skill));
		bldr.put((byte) player.getSkills().getLevel(skill));
		player.getSession().write(bldr.toPacket());
		return this;
	}

	public ActionSender sendSidebarInterfaces() {
		final int[] icons = Constants.SIDEBAR_INTERFACES[0];
		final int[] interfaces = Constants.SIDEBAR_INTERFACES[1];
		for(int i = 0; i < icons.length; i++) {
			sendSidebarInterface(icons[i], interfaces[i]);
		}
		return this;
	}
	
	public ActionSender sendSidebarInterface(int icon, int interfaceId) {
		player.getSession().write(new PacketBuilder(71).putShort(interfaceId).putByteA(icon).toPacket());
		return this;
	}
		
	public ActionSender sendMessage(String message) {
		player.getSession().write(new PacketBuilder(253, Type.VARIABLE).putRS2String(message).toPacket());
		return this;
	}
	
	public ActionSender sendMapRegion() {
		player.setLastKnownRegion(player.getLocation());
		player.getSession().write(new PacketBuilder(73).putShortA(player.getLocation().getRegionX() + 6).putShort(player.getLocation().getRegionY() + 6).toPacket());
		return this;
	}
	
	public ActionSender sendLogout() {
		player.getSession().write(new PacketBuilder(109).toPacket()).addListener(new IoFutureListener<IoFuture>() {
			@Override
			public void operationComplete(IoFuture future) {
				future.getSession().close(false);
			}
		});
		return this;
	}

}
