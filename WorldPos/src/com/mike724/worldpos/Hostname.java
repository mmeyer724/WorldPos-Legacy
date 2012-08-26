package com.mike724.worldpos;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class Hostname {
	
	private String hostname;
	private World world;
	
	public Hostname(String hm, String w) {
		if((world = Bukkit.getWorld(w))==null) {
			return;
		}
		hostname = hm;
	}
	public String getHostname() {
		return hostname;
	}
	public World getWorld() {
		return world;
	}

}
