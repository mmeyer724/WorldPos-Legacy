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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Settings {
	public static File dataDir = null;
	public static boolean round = false;
	public static boolean portalSupport = false;
	public static boolean hostnameSupport = false;
	public static boolean hostnameMessage = false;
	public static ArrayList<Hostname> hostnames = new ArrayList<Hostname>();
	public static HashMap<String, Hostname> hostnameTeleport = new HashMap<String, Hostname>();
	public static Set<String> justHNTeleported = new HashSet<String>();
	public static HashMap<String, Location> previousLocations = new HashMap<String, Location>();
	
	public static void setPreviousLocation(Player p, Location loc) {
		Settings.previousLocations.put(p.getName(), loc);
	}
}
