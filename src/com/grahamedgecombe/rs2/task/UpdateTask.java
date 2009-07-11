package com.grahamedgecombe.rs2.task;

import java.util.Iterator;

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

}
