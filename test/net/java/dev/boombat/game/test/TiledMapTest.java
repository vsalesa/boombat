/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.test;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 * 
 * @author objectworks
 */
public class TiledMapTest extends BasicGame {

	private static AppGameContainer game;
	private TiledMap map;

	public static void main(String... args) throws SlickException {
		game = new AppGameContainer(new TiledMapTest("TiledMap Scale Test"),
				1280, 768, true);
		game.setVerbose(false);
		game.setSmoothDeltas(true);
		game.setVSync(true);
		game.setShowFPS(false);
		game.start();
	}

	public TiledMapTest(String name) {
		super(name);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		map = new TiledMap("data/image/map/warehouse.tmx");

	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {

	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		g.scale(0.5f, 0.5f);
		map.render(0, 0);
	}

	@Override
	public void keyReleased(int key, char c) {
		if (Input.KEY_ESCAPE == key) {
			game.exit();
		}
	}
}
