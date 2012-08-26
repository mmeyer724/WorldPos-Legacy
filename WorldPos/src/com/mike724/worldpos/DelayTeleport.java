package com.mike724.worldpos;

import java.io.IOException;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class DelayTeleport implements Runnable {
	
	private Player p;
	private World w;
	
	public DelayTeleport(Player pl, World wo) {
		p = pl;
		w = wo;
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
