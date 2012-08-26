package com.mike724.worldpos;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldPos extends JavaPlugin {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("world") || command.getName().equalsIgnoreCase("worldwarp")) {
			try {
				World w;
				Player p;
				if(args.length==1) {
					w = Bukkit.getWorld(args[0]);
					p = Bukkit.getPlayerExact(sender.getName());
				} else if(args.length==2) {
					w = Bukkit.getWorld(args[1]);
					p = Bukkit.getPlayer(args[0]);
				} else {
					return false;
				}
				String wn = (w==null) ? "null" : w.getName();
				boolean perm = false;
				if(p!=null && w!=null && (perm = p.hasPermission("WorldPos.world."+wn))) {
					if(w!=p.getWorld()) {
						p.teleport(LocationManager.getPastLocation(w, p));
						p.sendMessage(ChatColor.AQUA+"Teleported to world "+ChatColor.YELLOW+wn);
						return true;
					} else {
						sender.sendMessage(ChatColor.AQUA+"You are already in world "+ChatColor.YELLOW+wn);
						return false;
					}
				} else {
					if(p==null) {
						sender.sendMessage(ChatColor.RED+"Target Player could not be found");
					}
					if(w==null) {
						sender.sendMessage(ChatColor.RED+"Target world could not be found");
					}
					if(!perm) {
						sender.sendMessage(ChatColor.RED+"You do not have permission to access that world");
					}
					return false;
				}
			}
			catch(IOException ex) {
			}
		}
		return false;
	}

	@Override
	public void onDisable() {
		
	}

	@Override
	public void onEnable() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		Settings.dataDir = new File(this.getDataFolder().toString()+File.separator+"players");
		Settings.dataDir.mkdir();
		Settings.round = getConfig().getBoolean("roundPosition");
		Settings.portalSupport = getConfig().getBoolean("portalSupport");
		this.getServer().getPluginManager().registerEvents(new WPListener(this), this);
	}
}
