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

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;

public class DelayedTeleport implements Runnable {

    private Player p;
    private World w;

    public DelayedTeleport(Player player, World world) {
        p = player;
        w = world;
    }

    @Override
    public void run() {
        try {
            p.teleport(LocationManager.getPastLocation(w, p));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
