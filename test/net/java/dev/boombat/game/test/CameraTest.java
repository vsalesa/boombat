/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.test;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.Camera;
import net.java.dev.boombat.game.ui.ext.FlashMessageBox;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * 
 * @author objectworks
 */
public class CameraTest extends BasicGame {

	private static AppGameContainer game;
	private FlashMessageBox flashBox;

	public static void main(String... args) throws SlickException {
		game = new AppGameContainer(new CameraTest("ResourceManagerTest"), 800,
				600, true);
		game.setVerbose(false);
		game.setSmoothDeltas(true);
		game.setVSync(true);
		game.start();
	}

	public CameraTest(String name) {
		super(name);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		ResourceManager.init();
		Camera.instance().setX(20);
		Camera.instance().setY(10);
		Config.setSelectedGameMap("warehouse");
		flashBox = FlashMessageBox.instance();
		flashBox.showFlashMessage(2000, "Camera Mode");
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		Camera.instance().update(container.getInput(), delta);
		flashBox.update(delta);
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		Config.getSelectedGameMap().render(g, Camera.instance().getX(),
				Camera.instance().getY(), 64);
		Camera.instance().render(g);
		g.resetTransform();
		flashBox.render(g);
	}

	@Override
	public void keyReleased(int key, char c) {
		if (Input.KEY_ESCAPE == key) {
			game.exit();
		}
	}
}
