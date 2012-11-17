package com.mike724.worldpos;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

public class Settings {
	public static File dataDir = null;
	public static boolean round = false;
	public static boolean portalSupport = false;
	public static boolean hostnameSupport = false;
	public static boolean hostnameMessage = false;
	public static ArrayList<Hostname> hostnames = new ArrayList<Hostname>();
	public static HashMap<Player, Hostname> hostnameTeleport = new HashMap<Player, Hostname>();
	public static Set<String> justHNTeleported = new HashSet<String>();
}
