package com.mike724.worldpos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class LocationManager {
	
	public static Location getPastLocation(World world, Player player) throws IOException {
		File file = new File(Settings.dataDir.toString()+File.separator+player.getName()+".txt");
		if(!file.exists()) {
			file.createNewFile();
		}
		BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
		String line;
		while((line = in.readLine())!=null) {
			String[] params = line.split(",");
			if(params[0].equalsIgnoreCase(world.getName())) {
				in.close();
				return(new Location(world, Double.parseDouble(params[1]), Double.parseDouble(params[2]), Double.parseDouble(params[3])));
			}
		}
		in.close();
		return(world.getSpawnLocation());
	}
	public static boolean setPastLocation(Location loc, Player player) throws IOException {
		String needle = loc.getWorld().getName();
		File file = new File(Settings.dataDir.toString()+File.separator+player.getName()+".txt");
		if(!file.exists()) {
			file.createNewFile();
		}
		BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
		List<String> oldLines = new ArrayList<String>();
		String line;
		while((line = in.readLine()) != null) {
			if(!line.split(",")[0].equalsIgnoreCase(needle)) {
				oldLines.add(line+"\r\n");
			}
		}
		in.close();
		String newLocLine = (Settings.round) ? needle+","+loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ() : needle+","+loc.getX()+","+loc.getY()+","+loc.getZ();
		oldLines.add(newLocLine);
		BufferedWriter br = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
		String finalTxt = "";
		for(String li : oldLines) {
			finalTxt += li;
		}
		br.write(finalTxt);
		br.close();
		return true;
	}

}
