package net.java.dev.boombat.game;

import java.util.HashMap;
import java.util.Map;

import net.java.dev.boombat.game.entity.GameMap;
import net.java.dev.boombat.game.util.PropertiesFile;
import net.java.dev.boombat.game.util.ResourceManager;

/**
 * 
 * @author objectworks
 */
public class Config {

	private static PropertiesFile config;
	private static PropertiesFile resources;
	private static PropertiesFile option;
	private static PropertiesFile tankScripts;
	private static PropertiesFile gameMaps;
	private static GameMap selectedGameMap;
	private static String selectedServerName = "no server";
	private static String mapKeyName;
	private static String password="";
	private static int maxPlayer = 20;
	public static final int SCREEN_WIDTH = 800;
	public static final int HALF_SCREEN_WIDTH = SCREEN_WIDTH / 2;
	public static final int SCREEN_HEIGTH = 600;
	public static final int HALF_SCREEN_HEIGHT = SCREEN_HEIGTH / 2;
	public static final float MAX_SOUND_DISTANCE_IN_TILES = 18.7f;
	private static boolean playerRadar;

	public static Map<String, Integer> keys = new HashMap<String, Integer>();

	static {
		config = new PropertiesFile("config/boombat.cfg");
		initKeys();
	}

	private static void initKeys() {
		for (Object k : config.getAllProperties()) {
			String val = (String) k;
			if (val.endsWith("Key")) {
				keys.put(val, config.getInt(val));
			}
		}
	}

	public static PropertiesFile getResources() {
		if (resources == null) {
			resources = new PropertiesFile(config.getString("resources"));
		}
		return resources;
	}

	public static void setSelectedGameMap(String keyName) {
		selectedGameMap = ResourceManager.createGameMap(keyName);
		mapKeyName = keyName;
	}

	public static GameMap getSelectedGameMap() {
		return selectedGameMap;
	}

	public static String getSelectedGameMapKey() {
		return mapKeyName;
	}

	public static int getKey(String keyName) {
		return keys.get(keyName);
	}

	public static PropertiesFile getOption() {
		if (option == null) {
			option = new PropertiesFile(config.getString("option"));
		}
		return option;
	}

	public static PropertiesFile getMapScripts() {
		if (gameMaps == null) {
			gameMaps = new PropertiesFile(config.getString("gameMaps"));
		}
		return gameMaps;
	}

	public static PropertiesFile getTankScripts() {
		if (tankScripts == null) {
			tankScripts = new PropertiesFile(config.getString("tanks"));
		}
		return tankScripts;
	}

	public static String getSelectedServerName() {
		return selectedServerName;
	}

	public static void setSelectedServerName(String selectedServerName) {
		Config.selectedServerName = selectedServerName;
	}

	public static void setMapKeyName(String mapKeyName) {
		Config.mapKeyName = mapKeyName;
	}

	public static String getMapKeyName() {
		return mapKeyName;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		Config.password = password;
	}

	public static int getMaxPlayer() {
		return maxPlayer;
	}

	public static void setMaxPlayer(int maxp) {
		maxPlayer = maxp;
	}

	public static boolean isPlayerRadar() {
		return playerRadar;
	}

	public static void setPlayerRadar(boolean playerRadar) {
		Config.playerRadar = playerRadar;
	}
}
