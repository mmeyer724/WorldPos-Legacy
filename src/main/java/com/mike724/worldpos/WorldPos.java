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

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class WorldPos extends JavaPlugin {

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        WPSettings.dataDir = new File(this.getDataFolder().toString() + File.separator + "players");
        if (!WPSettings.dataDir.exists()) {
            WPSettings.dataDir.mkdir();
        }
        FileConfiguration config = this.getConfig();
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            config.options().copyDefaults(true);
            this.saveConfig();
        }
        WPSettings.round = config.getBoolean("roundPosition");
        WPSettings.portalSupport = config.getBoolean("portalSupport");
        WPSettings.hostnameSupport = config.getBoolean("hostnameSupport");
        WPSettings.hostnameMessage = config.getBoolean("messageOnHostnameTeleportToWorld");
        WPSettings.teleportMessage = config.getBoolean("teleportMessages");
        WPSettings.saveMessage = config.getBoolean("saveMessages");
        if (WPSettings.hostnameSupport) {
            Set<String> keys = config.getConfigurationSection("hostnames").getKeys(false);
            WPSettings.hostnames.clear();
            for (String key : keys) {
                String wn = config.getString("hostnames." + key + ".world");
                try {
                    Hostname hn = new Hostname(config.getString("hostnames." + key + ".hostname"), key, wn);
                    WPSettings.hostnames.add(hn);
                } catch (Exception e) {
                    this.getLogger().warning("The world " + wn + " does not exist! Skipping");
                }
            }
        }
        this.getLogger().info("Loaded configuration successfully");
        WPCommands wpc = new WPCommands(this);
        this.getCommand("world").setExecutor(wpc);
        this.getCommand("worldwarp").setExecutor(wpc);
        this.getCommand("worldpos").setExecutor(wpc);
        this.getCommand("wp").setExecutor(wpc);
        this.getServer().getPluginManager().registerEvents(new WPListener(this), this);
        this.getLogger().info("Enabled successfully");
    }
}
