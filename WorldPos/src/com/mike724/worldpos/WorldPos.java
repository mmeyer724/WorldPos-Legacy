package com.mike724.worldpos;

import java.io.File;
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
		WPCommands wpc = new WPCommands();
		this.getCommand("world").setExecutor(wpc);
		this.getCommand("worldwarp").setExecutor(wpc);
		this.getServer().getPluginManager().registerEvents(new WPListener(this), this);
	}
	
}
