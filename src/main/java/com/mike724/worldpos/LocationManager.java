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

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationManager {

    public static Location getPastLocation(World world, Player player) throws IOException {
        File file = new File(WPSettings.dataDir.toString() + File.separator + player.getUniqueId() + ".txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        String line;
        while ((line = in.readLine()) != null) {
            String[] params = line.split(",");
            if (!(params.length >= 4)) {
                continue;
            }
            if (params[0].equalsIgnoreCase(world.getName())) {
                in.close();

                Location loc = new Location(world, Double.parseDouble(params[1]), Double.parseDouble(params[2]), Double.parseDouble(params[3]));

                //Are yaw and pitch included?
                if (params.length > 4) {
                    loc.setYaw(Float.parseFloat(params[4]));
                    loc.setPitch(Float.parseFloat(params[5]));
                }
                return loc;
            }
        }
        in.close();
        return (world.getSpawnLocation());
    }

    public static boolean setPastLocation(Location loc, Player player) throws IOException {
        String needle = loc.getWorld().getName();
        File file = new File(WPSettings.dataDir.toString() + File.separator + player.getUniqueId().toString() + ".txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        List<String> oldLines = new ArrayList<String>();
        String line;
        while ((line = in.readLine()) != null) {
            if (!line.split(",")[0].equalsIgnoreCase(needle)) {
                oldLines.add(line + "\r\n");
            }
        }
        in.close();
        DecimalFormat df = new DecimalFormat("#.######", new DecimalFormatSymbols(Locale.US));
        String newLocLine = (WPSettings.round) ?
                needle + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() :
                needle + "," + df.format(loc.getX()) + "," + df.format(loc.getY()) + "," + df.format(loc.getZ());
        newLocLine += ("," + loc.getYaw() + "," + loc.getPitch());
        oldLines.add(newLocLine);
        BufferedWriter br = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
        String finalTxt = "";
        for (String li : oldLines) {
            finalTxt += li;
        }
        br.write(finalTxt);
        br.close();
        return true;
    }

}
