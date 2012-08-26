package com.mike724.worldpos;

import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WPListener implements Listener {
	
	public WPListener(WorldPos wp) {
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		String wnF = event.getFrom().getWorld().getName();
		String wnT = event.getTo().getWorld().getName();
		Player p = event.getPlayer();
		p.getName();
		
		if(Settings.portalSupport) {
			if(!wnF.equalsIgnoreCase(wnT) && event.getTo().getY()==300) {
				try {
					event.setTo(LocationManager.getPastLocation(event.getTo().getWorld(), p));
					p.sendMessage(ChatColor.AQUA+"Teleported to world "+ChatColor.YELLOW+wnT+ChatColor.AQUA+" via portal.");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		}
		
		if(!wnF.equalsIgnoreCase(wnT)) {
			try {
				LocationManager.setPastLocation(event.getFrom(), event.getPlayer());
				p.sendMessage(ChatColor.AQUA+"Your previous position in world "+ChatColor.YELLOW+wnF+ChatColor.AQUA+" has been saved.");
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		try {
			LocationManager.setPastLocation(p.getLocation(), p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		for(Hostname hn : Settings.hostnames) {
			if(hn.getHostname().equalsIgnoreCase(event.getHostname())) {
				Player p = event.getPlayer();
				if(!p.hasPermission("WorldPos.hostname."+hn.getKey())) {
					p.sendMessage(ChatColor.RED+"You do not have permission to access that world via hostname/domain");
					return;
				}
				p.sendMessage("Teleport to world "+hn.getWorld().getName()+" due to hostname "+event.getHostname());
			}
		}
	}
	
	

}
