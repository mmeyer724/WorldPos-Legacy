package com.mike724.worldpos;

import java.io.File;
import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;

public class WorldPos extends JavaPlugin {

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
		Settings.hostnameSupport = getConfig().getBoolean("hostnameSupport");
		Settings.hostnameMessage = getConfig().getBoolean("messageOnHostnameTeleportToWorld");
		if(Settings.hostnameSupport) {
			Set<String> keys = getConfig().getConfigurationSection("hostnames").getKeys(false);
			for(String key : keys) {
				String wn = getConfig().getString("hostnames."+key+".world");
				try {
					Hostname hn = new Hostname(getConfig().getString("hostnames."+key+".hostname"),key,wn);
					Settings.hostnames.add(hn);
				} catch (Exception e) {
					this.getLogger().warning("The world "+wn+" does not exist! Skipping");
				}
			}
		}
		WPCommands wpc = new WPCommands(this);
		this.getCommand("world").setExecutor(wpc);
		this.getCommand("worldwarp").setExecutor(wpc);
		this.getCommand("worldpos").setExecutor(wpc);
		this.getServer().getPluginManager().registerEvents(new WPListener(this), this);
		this.getLogger().info("Enabled successfully");
	}
	
}
