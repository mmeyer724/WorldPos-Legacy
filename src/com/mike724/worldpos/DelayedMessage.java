package com.mike724.worldpos;

import org.bukkit.entity.Player;

public class DelayedMessage implements Runnable {
	
	private Player p;
	private String s;
	
	public DelayedMessage(Player player, String string) {
		p = player;
		s = string;
	}

	@Override
	public void run() {
		p.sendMessage(s);
	}

}
