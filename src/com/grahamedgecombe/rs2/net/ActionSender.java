package com.grahamedgecombe.rs2.net;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;

import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.net.Packet.Type;

public class ActionSender {
	
	private Player player;
	
	public ActionSender(Player player) {
		this.player = player;
	}
	
	public ActionSender sendLogin() {
		player.setActive(true);
		sendDetails();
		sendMessage("Welcome to RuneScape.");
		sendMapRegion();
		return this;
	}
	
	public ActionSender sendDetails() {
		player.getSession().write(new PacketBuilder(249).putByteA(player.isMembers() ? 1 : 0).putLEShortA(player.getIndex()).toPacket());
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
