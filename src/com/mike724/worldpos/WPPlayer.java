package com.mike724.worldpos;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WPPlayer {
	
	private Player player;
	private boolean isPendingHNTeleport = false;
	private Hostname hnTeleport = null;
	private WPReason lastTPReason = null;
	
	public WPPlayer(Player player) {
		this.player = player;
	}
	
	public void sendTeleportMessage(String worldName, WPReason reason) {
		ChatColor wpColor = ChatColor.AQUA;
		ChatColor worldColor = ChatColor.YELLOW;
		String resp = "";
		switch(reason) {
			case COMMAND:
				resp = wpColor+"Teleported to "+worldColor+worldName;
				break;
			case FORCED:
				resp = wpColor+"Your position in "+worldColor+worldName+wpColor+" has been saved";
				break;
			case ALREADYINWORLD:
				resp = wpColor+"You are already in "+worldColor+worldName;
				break;
			case HOSTNAME:
				resp = wpColor+"Welcome to "+worldColor+worldName;
				break;
			case PORTAL:
				resp = wpColor+"Teleported to "+worldColor+worldName+" via portal";
				break;
			default:
				resp = ChatColor.RED+worldName+" : "+reason.name();
				break;
		}
		if(!(getLastTeleportReason()==WPReason.HOSTNAME && reason == WPReason.FORCED)) {
			player.sendMessage(resp);
		}
	}
	
	public void teleport(World world, WPReason reason) {
		teleport(LocationManager.getPastLocation(world, player.getName()), reason);
	}
	
	public void teleport(Location loc, WPReason reason) {
		if(loc!=null) {
			player.teleport(loc);
			setLastTeleportReason(reason);
			if(reason != WPReason.HOSTNAME) {
				sendTeleportMessage(loc.getWorld().getName(),reason);
			} else if(Settings.hostnameMessage) {
				sendTeleportMessage(loc.getWorld().getName(),reason);
			}
		}
	}
	
	public void saveCurrentPosition() {
		LocationManager.setPastLocation(player.getLocation(), player.getName());
	}
	
	public void doCommandTeleport(World world) {
		teleport(world, WPReason.COMMAND);
	}
	
	public void resetHostnameTeleport() {
		hnTeleport = null;
		isPendingHNTeleport = false;
	}
	
	public void setHostnameTeleportPending(Hostname hostname) {
		hnTeleport = hostname;
		isPendingHNTeleport = true;
	}
	
	public Hostname getHostnameTeleport() {
		return(hnTeleport);
	}
	
	public void doHostnameTeleport() {
		Hostname hnTele = getHostnameTeleport();
		if(hnTele!=null && isPendingHostnameTeleport()) {
			teleport(hnTele.getWorld(), WPReason.HOSTNAME);
			resetHostnameTeleport();
		}
	}
	
	public boolean isPendingHostnameTeleport() {
		return isPendingHNTeleport;
	}
	
	public Location getLastLocationInWorld(World w) {
		return(LocationManager.getPastLocation(w, player.getName()));
	}
	
	public void setLastLocationForced(Location loc) {
		LocationManager.setPastLocation(loc, player.getName());
		sendTeleportMessage(loc.getWorld().getName(), WPReason.FORCED);
	}
	
	public void handlePortalTeleport(PlayerTeleportEvent event) {
		World to = event.getTo().getWorld();
		event.setTo(getLastLocationInWorld(to));
		sendTeleportMessage(to.getName(), WPReason.PORTAL);
	}

	public WPReason getLastTeleportReason() {
		return lastTPReason;
	}

	public void setLastTeleportReason(WPReason lastTPReason) {
		this.lastTPReason = lastTPReason;
	}
	
}
