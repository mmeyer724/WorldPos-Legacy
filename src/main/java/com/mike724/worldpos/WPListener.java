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
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.IOException;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class WPListener implements Listener {

    private WorldPos plugin;

    public WPListener(WorldPos wp) {
        plugin = wp;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player p = event.getPlayer();
        if (WPSettings.hostnameTeleport.containsKey(p.getName())) {
            World w = WPSettings.hostnameTeleport.get(p.getName()).getWorld();
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new DelayedTeleport(p, w), 1L);
            if (WPSettings.hostnameMessage) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new DelayedMessage(p, ChatColor.AQUA + "Welcome to world " + ChatColor.YELLOW + w.getName()), 5L);
            }
            WPSettings.hostnameTeleport.remove(p.getName());
            WPSettings.justHNTeleported.add(p.getName());
            return;
        }

        String wnF = event.getFrom().getWorld().getName();
        String wnT = event.getTo().getWorld().getName();

        if (WPSettings.portalSupport) {
            float maxDiff = 0.55f;
            if (!wnF.equalsIgnoreCase(wnT) && Math.abs(event.getTo().getY() - 300) <= maxDiff) {
                try {
                    if (!p.hasPermission("WorldPos.portal." + wnT)) {
                        p.sendMessage(ChatColor.RED + "You do not have permission to use this portal");
                        event.setCancelled(true);
                    } else {
                        event.setTo(LocationManager.getPastLocation(event.getTo().getWorld(), p));
                        if (WPSettings.teleportMessage)
                            p.sendMessage(ChatColor.AQUA + "Teleported to world " + ChatColor.YELLOW + wnT + ChatColor.AQUA + " via portal.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }

        if (!wnF.equalsIgnoreCase(wnT)) {
            try {
                LocationManager.setPastLocation(event.getFrom(), p);
                WPSettings.setPreviousLocation(p, event.getFrom());
                if (!WPSettings.justHNTeleported.contains(p.getName())) {
                    if (WPSettings.saveMessage)
                        p.sendMessage(ChatColor.AQUA + "Your previous position in world " + ChatColor.YELLOW + wnF + ChatColor.AQUA + " has been saved.");
                } else {
                    WPSettings.justHNTeleported.remove(p.getName());
                }
            } catch (IOException ex) {
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
        if (WPSettings.hostnameSupport) {
            for (Hostname hn : WPSettings.hostnames) {
                String host = (hn.getHostname().split(":").length == 2) ? hn.getHostname() : hn.getHostname() + ":" + plugin.getServer().getPort();

                //Remove oddity in some hostnames
                String hnClient = event.getHostname().replaceFirst(Pattern.quote(".:"), ":");

                boolean match = hnClient.equalsIgnoreCase(host);

                if (match) {
                    Player p = event.getPlayer();
                    String perm = "WorldPos.hostname." + hn.getKey();
                    boolean hasPerm = p.hasPermission(perm);
                    if (!hasPerm) {
                        event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                        event.setKickMessage("You do not have permission to access that world");
                        return;
                    }
                    WPSettings.hostnameTeleport.put(p.getName(), hn);
                    return;
                }
            }
        }
    }
}
