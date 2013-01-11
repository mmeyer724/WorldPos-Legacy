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

import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class WPCommands implements CommandExecutor {
	
	private WorldPos plugin;
	
	public WPCommands(WorldPos wp) {
		plugin = wp;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("worldpos")) {
			if(args.length==0) {
				PluginDescriptionFile pdf = plugin.getDescription();
				sender.sendMessage(ChatColor.AQUA+"WorldPos v"+pdf.getVersion()+" by Mike724 is "+ChatColor.GOLD+"running");
				return true;
			}
			return true;
		}
		if(command.getName().equalsIgnoreCase("world") || command.getName().equalsIgnoreCase("worldwarp")) {
			if(args.length==1) {
				if(args[0].equalsIgnoreCase("list")) {
					//Check if the world by the name "list" exists, if it doesn't continue
					if(Bukkit.getWorld("list")==null) {
						//Do they have permission?
						if(!sender.hasPermission("WorldPos.list")) {
							sender.sendMessage(ChatColor.RED+"You do not have permission to list all worlds!");
							return true;
						}
						List<World> worlds = Bukkit.getWorlds();
						String send = ChatColor.AQUA+"[WorldPos]"+ChatColor.WHITE+" List of worlds: "+ChatColor.GRAY;
						for(World w : worlds) {
							send += w.getName()+", ";
						}
						sender.sendMessage(send.substring(0, send.length()-2));
						return true;
					}
				}
			}
			World w; Player p;
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
			if(p==null) {
				sender.sendMessage(ChatColor.RED+"Target player could not be found"); return false;
			}
			if(w==null) {
				sender.sendMessage(ChatColor.RED+"Target world could not be found"); return false;
			}
			if(!p.hasPermission("WorldPos.world."+wn)) {
				sender.sendMessage(ChatColor.RED+"You do not have permission to access that world"); return true;
			}
			if(w!=p.getWorld()) {
				try {
					p.teleport(LocationManager.getPastLocation(w, p));
					p.sendMessage(ChatColor.AQUA+"Teleported to world "+ChatColor.YELLOW+wn);
					return true;
				} catch(IOException e) {
					e.printStackTrace();
					p.sendMessage(ChatColor.RED+"Could not read player data! Report to admin");
					plugin.getLogger().severe("ERROR Reading player position data, report this please.");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.AQUA+"You are already in world "+ChatColor.YELLOW+wn);
				return true;
			}
		}
		return false;
	}
	
}
