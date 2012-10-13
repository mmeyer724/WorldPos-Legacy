package com.mike724.worldpos;

import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WPListener implements Listener {
	
	private WorldPos plugin;
	
	public WPListener(WorldPos wp) {
		plugin = wp;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player p = event.getPlayer();
		if(Settings.hostnameTeleport.containsKey(p)) {
			World w = Settings.hostnameTeleport.get(p).getWorld();
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new DelayedTeleport(p,w), 1L);
			if(Settings.hostnameMessage) {
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new DelayedMessage(p,ChatColor.AQUA+"Welcome to world "+ChatColor.YELLOW+w.getName()), 3L);
			}
			Settings.hostnameTeleport.remove(p);
			return;
		}
		
		String wnF = event.getFrom().getWorld().getName();
		String wnT = event.getTo().getWorld().getName();
		
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
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLogin(PlayerLoginEvent event) {
		if(Settings.hostnameSupport) {
			for(Hostname hn : Settings.hostnames) {
				String host = (hn.getHostname().split(":").length==2) ? hn.getHostname() : hn.getHostname()+":"+plugin.getServer().getPort();
				if(event.getHostname().equalsIgnoreCase(host)) {
					Player p = event.getPlayer();
					if(!p.hasPermission("WorldPos.hostname."+hn.getKey())) {
						event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
						event.setKickMessage("You do not have permission to access that world");
						return;
					}
					Settings.hostnameTeleport.put(p, hn);
					return;
				}
			}
		}
	}
	
	

}
