package com.mike724.worldpos;

import java.io.File;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldPos extends JavaPlugin {

	@Override
	public void onDisable() {
		
	}

	@Override
	public void onEnable() {
		if(!this.getDataFolder().exists()) {
			this.getDataFolder().mkdir();
		}
		Settings.dataDir = new File(this.getDataFolder().toString()+File.separator+"players");
		if(!Settings.dataDir.exists()) {
			Settings.dataDir.mkdir();
		}
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		FileConfiguration config = this.getConfig();
		Settings.round = config.getBoolean("roundPosition");
		Settings.portalSupport = config.getBoolean("portalSupport");
		Settings.hostnameSupport = config.getBoolean("hostnameSupport");
		Settings.hostnameMessage = config.getBoolean("messageOnHostnameTeleportToWorld");
		if(Settings.hostnameSupport) {
			Set<String> keys = config.getConfigurationSection("hostnames").getKeys(false);
			Settings.hostnames.clear();
			for(String key : keys) {
				String wn = config.getString("hostnames."+key+".world");
				try {
					Hostname hn = new Hostname(config.getString("hostnames."+key+".hostname"),key,wn);
					Settings.hostnames.add(hn);
				} catch (Exception e) {
					this.getLogger().warning("The world "+wn+" does not exist! Skipping");
				}
			}
		}
		this.getLogger().info("Loaded configuration successfully");
		WPCommands wpc = new WPCommands(this);
		this.getCommand("world").setExecutor(wpc);
		this.getCommand("worldwarp").setExecutor(wpc);
		this.getCommand("worldpos").setExecutor(wpc);
		this.getServer().getPluginManager().registerEvents(new WPListener(this), this);
		this.getLogger().info("Enabled successfully");
	}
}
