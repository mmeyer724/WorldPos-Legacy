package com.mike724.worldpos;

import java.io.IOException;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class DelayedTeleport implements Runnable {
	
	private Player p;
	private World w;
	
	public DelayedTeleport(Player player, World world) {
		p = player;
		w = world;
	}

	@Override
	public void run() {
		try {
			p.teleport(LocationManager.getPastLocation(w, p));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
