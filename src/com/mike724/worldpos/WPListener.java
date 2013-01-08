/*
    This file is part of WorldPos.

    WorldPos is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    WorldPos is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with WorldPos.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.mike724.worldpos;

import org.bukkit.ChatColor;
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
		WPPlayer wpPlayer = Settings.getWPPlayer(p);
		
		if(wpPlayer.isPendingHostnameTeleport()) {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new DelayedHostnameTeleport(wpPlayer));
			return;
		}
		
		String worldNameFrom = event.getFrom().getWorld().getName();
		String worldNameTo = event.getTo().getWorld().getName();
		boolean diffWorlds = worldNameFrom!=worldNameTo;
		
		if(Settings.portalSupport) {
			if(diffWorlds && event.getTo().getY()==300) {
				if(!p.hasPermission("WorldPos.portal."+worldNameTo)) {
					p.sendMessage(ChatColor.RED+"You do not have permission to use this portal");
					event.setCancelled(true);
				} else {
					wpPlayer.handlePortalTeleport(event);
				}
			}
		}
		
		if(diffWorlds) {
			wpPlayer.setLastLocationForced(event.getFrom());
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Settings.getWPPlayer(event.getPlayer()).saveCurrentPosition();
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
					Settings.getWPPlayer(p).setHostnameTeleportPending(hn);
					return;
				}
			}
		}
	}
}
