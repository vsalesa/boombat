/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.test;

import net.java.dev.boombat.game.ui.ext.GameMapSlider;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * 
 * @author objectworks
 */
public class GameMapSliderTest extends BasicGame {

	private GameMapSlider slider;

	public static void main(String... args) throws SlickException {
		AppGameContainer game = new AppGameContainer(new GameMapSliderTest(
				"Game Map Slider Test"), 800, 600, false);
		game.start();
	}

	public GameMapSliderTest(String name) {
		super(name);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		ResourceManager.init();
		slider = new GameMapSlider(container, 400, 180);

	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		slider.update(delta);

	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		ResourceManager.getImage("subMenuBgImage").draw(0, 0,
				container.getWidth(), container.getHeight());
		slider.render(container, g);
	}
}