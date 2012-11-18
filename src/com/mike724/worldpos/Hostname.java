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

import org.bukkit.Bukkit;
import org.bukkit.World;

public class Hostname {
	
	private String hostname;
	private String key;
	private World world;
	
	public Hostname(String hm, String k, String w) throws Exception {
		if((world = Bukkit.getWorld(w))==null) {
			throw new Exception();
		}
		key = k;
		hostname = hm;
	}
	public String getHostname() {
		return hostname;
	}
	public String getKey() {
		return key;
	}
	public World getWorld() {
		return world;
	}

}
