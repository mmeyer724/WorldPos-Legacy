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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
			if(params.length!=4) {
				continue;
			}
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
		DecimalFormat df = new DecimalFormat("#.######", new DecimalFormatSymbols(Locale.US));
		String newLocLine = (Settings.round) ?
				needle+","+loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ() : 
				needle+","+df.format(loc.getX())+","+df.format(loc.getY())+","+df.format(loc.getZ());
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
