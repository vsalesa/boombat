package net.java.dev.boombat.game.entity;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.util.CollisionUtil;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 * 
 * @author objectworks
 */
public class GameMap {

	public static final int TILE_SIZE = 32;
	public static final int SCREEN_WIDTH_IN_TILES = Math
			.round(Config.SCREEN_WIDTH / TILE_SIZE);
	public static final int SCREEN_HEIGTH_IN_TILES = Math
			.round(Config.SCREEN_HEIGTH / TILE_SIZE);
	public static final int SCREEN_TOP_OFFSET_IN_TILES = Math
			.round(SCREEN_HEIGTH_IN_TILES / 2);
	public static final int SCREEN_LEFT_OFFSET_IN_TILES = Math
			.round(SCREEN_WIDTH_IN_TILES / 2);
	private TiledMap map;
	private boolean[][] blocked;
	private String name;
	private SecureRandom random;
	private float unitRadarTile;
	private String key;

	public GameMap(String mapFile, String mapName) {
		initCollisionArea(mapFile);
		initRandom();
		this.name = mapName;
	}

	private void initRandom() {
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException ex) {
			// should never happen ...
		}
	}

	private void initCollisionArea(String mapPath) {
		try {
			map = new TiledMap(mapPath);
		} catch (SlickException e) {
			// e.printStackTrace();
			throw new RuntimeException("Can't create TiledMap : " + mapPath);
		}
		blocked = CollisionUtil.createCollisionArea(map);
		unitRadarTile = 120f / map.getWidth();
	}

	public void render(Graphics g, float x, float y, int size) {
		int newX = Math.round(x);
		int newY = Math.round(y);
		int offsetX = Math.round((newX - x) * TILE_SIZE);
		int offsetY = Math.round((newY - y) * TILE_SIZE);
		int halfSize = size / 2;
		int xpos = offsetX - halfSize;
		int ypos = offsetY - halfSize;
		int sx = newX - SCREEN_LEFT_OFFSET_IN_TILES - 1;
		int sy = newY - SCREEN_TOP_OFFSET_IN_TILES - 1;
		int w = SCREEN_WIDTH_IN_TILES + 3;
		int h = SCREEN_HEIGTH_IN_TILES + 3;
		map.render(xpos, ypos, sx, sy, w, h);
		g.translate(Config.HALF_SCREEN_WIDTH - Math.round(x * TILE_SIZE),
				Config.HALF_SCREEN_HEIGHT - Math.round(y * TILE_SIZE));
	}

	public boolean isCollide(float x, float y, float newX, float newY) {
		float newx = x + newX;
		float newy = y + newY;

		if (CollisionUtil.blocked(blocked, newx, newy)) {
			return true;
		} else {
			return false;
		}
	}

	public float[][] generateRandomPositions(int numberPosToGenerate) {
		float[][] pos = new float[numberPosToGenerate][2];

		int i = 0;
		while (i < pos.length) {
			int x = random.nextInt(map.getWidth());
			int y = random.nextInt(map.getHeight());
			if (!CollisionUtil.blocked(blocked, x, y)
					&& !CollisionUtil.isCollideWithEdgeScreen(x, y)) {
				for (int j = 0; j < i; j++) {
					if (pos[j][0] == x && pos[j][1] == y) {
						continue;
					}
				}
				pos[i][0] = x;
				pos[i][1] = y;
				i++;
			}
		}

		return pos;
	}

	public String getName() {
		return name;
	}

	public TiledMap getTiledMap() {
		return map;
	}

	public float getUnitRadarTile() {
		return unitRadarTile;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
