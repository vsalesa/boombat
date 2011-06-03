/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.GameMap;
import net.java.dev.boombat.game.entity.Tank;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleIO;

/**
 * 
 * @author objectworks
 */
public class ResourceManager {

	private static Map<String, Sound> sounds = new HashMap<String, Sound>();
	private static Map<String, Music> musics = new HashMap<String, Music>();
	private static Map<String, Image> images = new HashMap<String, Image>();
	private static Map<String, Image> mapImages = new HashMap<String, Image>();
	private static Map<String, Image> tankImages = new HashMap<String, Image>();
	private static Map<String, String> mapPaths = new HashMap<String, String>();
	private static Map<String, String> mapDescriptions = new HashMap<String, String>();
	private static Map<String, Tank> tanks = new HashMap<String, Tank>();
	private static Map<String, ParticleEmitter> emitters = new HashMap<String, ParticleEmitter>();
	private static Map<String, Font> fonts = new HashMap<String, Font>();
	private static PropertiesFile resources;

	public static void init() {
		parseGameObjects();
		parseMapImages();
		parseTanks();
	}

	private static void parseGameObjects() {
		resources = Config.getResources();
		Set<Object> keys = resources.getAllProperties();
		for (Object k : keys) {
			String strKey = (String) k;
			try {
				createObject(strKey);
			} catch (Exception ex) {
				// ex.printStackTrace();
				throw new RuntimeException("Can't create resource : " + strKey);
			}
		}
	}

	private static void createObject(String strKey) throws Exception {
		String value = resources.getString(strKey);
		if (strKey.endsWith("Sound")) {
			sounds.put(strKey, new Sound(value));
		} else if (strKey.endsWith("Music")) {
			musics.put(strKey, new Music(value, true));
		} else if (strKey.endsWith("Image")) {
			images.put(strKey, new Image(value));
		} else if (strKey.endsWith("NonConfEmitter")) {
			emitters.put(strKey, (ParticleEmitter) Class.forName(value)
					.newInstance());
		} else if (strKey.endsWith("Font")) {
			fonts.put(strKey, new AngelCodeFont(value + ".fnt", value + ".png",
					true));
		} else if (strKey.endsWith("ConfEmitter")) {
			emitters.put(strKey, ParticleIO.loadEmitter(value));
		}
	}

	private static void parseMapImages() {
		PropertiesFile gameMapScripts = Config.getMapScripts();
		Set<Object> gameMapFileKeys = gameMapScripts.getAllProperties();
		for (Object k : gameMapFileKeys) {
			String strKey = (String) k;
			String value = gameMapScripts.getString(strKey);
			PropertiesFile props = new PropertiesFile(value);
			try {
				mapImages.put(strKey, new Image(props.getString("thumbImage")));
				mapPaths.put(strKey, props.getString("tiledMap"));
				mapDescriptions.put(strKey, props.getString("name"));
			} catch (SlickException e) {
				// ex.printStackTrace();
				throw new RuntimeException("Can't create resource : " + value);
			}
		}
	}

	private static void parseTanks() {
		PropertiesFile tanksScripts = Config.getTankScripts();
		Set<Object> tankFileKeys = tanksScripts.getAllProperties();
		for (Object k : tankFileKeys) {
			String strKey = (String) k;
			String value = tanksScripts.getString(strKey);
			PropertiesFile props = new PropertiesFile(value);
			Tank t = parseTank(props);
			t.setKey(strKey);
			tanks.put(strKey, t);
			try {
				tankImages.put(strKey, new Image(props.getString("fullImage")));
			} catch (SlickException e) {
				// should never happen
				throw new RuntimeException("Can't create resource : "
						+ props.getString("fullImage"));
			}
		}
	}

	public static Tank createTank(String tankName) {

		return tanks.get(tankName).clone();
	}

	private static Tank parseTank(PropertiesFile props) {
		Tank tank = null;
		try {
			String tankName = props.getString("tankName");
			Image bodyImage = new Image(props.getString("bodyImage"));
			Image turretImage = new Image(props.getString("turretImage"));
			float moveSpeed = props.getFloat("moveSpeed");
			float rotateSpeed = props.getFloat("rotateSpeed");
			float xTurretSpace = props.getFloat("xTurretSpace");
			float yTurretSpace = props.getFloat("yTurretSpace");
			float rocketStartDistance = props.getFloat("rocketStartDistance");
			float bulletStartDistance = props.getFloat("bulletStartDistance");
			boolean infiniteBulletAmmo = props.getBoolean("infiniteBulletAmmo");
			boolean infiniteRocketAmmo = props.getBoolean("infiniteRocketAmmo");
			int reloadTime = props.getInt("reloadTime");
			byte numBullet = (byte) props.getInt("numBullet");
			byte numRocket = (byte) props.getInt("numRocket");
			byte maxAmmoBullet = (byte) props.getInt("maxAmmoBullet");
			byte maxAmmoRocket = (byte) props.getInt("maxAmmoRocket");
			String rocketSmokeColor = props.getString("rocketSmokeColor");
			int rocketSmokeColorType = props.getInt("rocketSmokeColorType");
			tank = new Tank(tankName, bodyImage, turretImage, 0, 0, 0, 0,
					moveSpeed, rotateSpeed, xTurretSpace, yTurretSpace,
					rocketStartDistance, bulletStartDistance,
					infiniteBulletAmmo, infiniteRocketAmmo, reloadTime,
					numBullet, numRocket, maxAmmoBullet, maxAmmoRocket);
			tank.setRocketSmokeColor(rocketSmokeColor, rocketSmokeColorType);
		} catch (Exception e) {
			throw new RuntimeException("Can't load tank : " + props.getFile());
		}
		return tank;
	}

	public static GameMap createGameMap(String mapKey) {
		String tiledMap = mapPaths.get(mapKey);
		String name = mapDescriptions.get(mapKey);
		GameMap map = new GameMap(tiledMap, name);
		map.setKey(mapKey);
		return map;
	}

	public static Sound getSound(String name) {
		return sounds.get(name);
	}

	public static Music getMusic(String name) {
		return musics.get(name);
	}

	public static Image getImage(String name) {
		return images.get(name);
	}

	public static ParticleEmitter getNonConfigurableEmitter(String name) {
		return emitters.get(name);
	}

	public static ConfigurableEmitter loadConfigurableEmitter(String name) {
		/**
		 * ParticleEmitter emitter = null; String resourceName =
		 * resources.getString(name); try { emitter =
		 * ParticleIO.loadEmitter(resourceName); } catch (IOException ex) { //
		 * ex.printStackTrace(); throw new
		 * RuntimeException("Can't create ParticleEmitter : " + resourceName); }
		 */
		return (ConfigurableEmitter) emitters.get(name);
	}

	public static Map<String, Sound> getSounds() {
		return sounds;
	}

	public static Map<String, Music> getMusics() {
		return musics;
	}

	public static Map<String, Image> getImages() {
		return images;
	}

	public static String getMapDescription(String key) {
		return mapDescriptions.get(key);
	}

	public static Image getMapImages(String key) {
		return mapImages.get(key);
	}

	public static Image getTankImages(String key) {
		return tankImages.get(key);
	}

	public static String getTankDescription(String key) {
		return tanks.get(key).getName();
	}

	public static Set<String> getMapKeys() {
		return mapImages.keySet();
	}

	public static Set<String> getTankKeys() {
		return tanks.keySet();
	}

	public static Font getFont(String fontName) {
		return fonts.get(fontName);
	}

}
