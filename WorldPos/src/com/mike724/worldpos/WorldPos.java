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
		Set<String> keys = getConfig().getConfigurationSection("hostnames").getKeys(false);
		for(String key : keys) {
			Hostname hn = new Hostname(getConfig().getString("hostnames."+key+".hostname"),key,getConfig().getString("hostnames."+key+".world"));
			if(hn!=null) {
				Settings.hostnames.add(hn);
			}
		}
		WPCommands wpc = new WPCommands(this);
		this.getCommand("world").setExecutor(wpc);
		this.getCommand("worldwarp").setExecutor(wpc);
		this.getCommand("worldpos").setExecutor(wpc);
		this.getServer().getPluginManager().registerEvents(new WPListener(this), this);
	}
	
}
