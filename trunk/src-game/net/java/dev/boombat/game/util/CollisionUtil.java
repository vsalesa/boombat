package net.java.dev.boombat.game.util;

import net.java.dev.boombat.game.Config;

import org.newdawn.slick.tiled.TiledMap;

public class CollisionUtil {

	public static final int MIN_TILE_X_Y = 1;

	public static boolean[][] createCollisionArea(TiledMap map) {
		if (map == null) {
			return null;
		}
		boolean[][] blocked = new boolean[map.getWidth()][map.getHeight()];
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				int tileID = map.getTileId(x, y, 0);
				String value = map.getTileProperty(tileID, "blocked", "false");
				if ("true".equals(value)) {
					blocked[x][y] = true;
				}
			}
		}

		return blocked;
	}

	public static boolean blocked(boolean[][] blocked, float x, float y) {

		return blocked[(int) x][(int) y];

	}

	public static boolean isCollideWithEdgeScreen(float x, float y) {
		TiledMap map = Config.getSelectedGameMap().getTiledMap();
		int maxTileY = map.getHeight() - 2;
		int maxTileX = map.getWidth() - 2;

		return ((int) x < CollisionUtil.MIN_TILE_X_Y
				|| (int) y < CollisionUtil.MIN_TILE_X_Y || (int) x > maxTileX || (int) y > maxTileY);
	}
}
