/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.test;

import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;


/**
 * 
 * @author objectworks
 */
public class ResourceManagerTest extends BasicGame {

	Image tankImage;
	int ctx;

	public static void main(String... args) throws SlickException {
		AppGameContainer game = new AppGameContainer(new ResourceManagerTest(
				"ResourceManagerTest"), 800, 600, false);
		game.start();
	}

	public ResourceManagerTest(String name) {
		super(name);
	}

	public void loadResource() {
		ResourceManager.init();
		tankImage = ResourceManager.getImage("normalTankImage");
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		loadResource();

	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		if (ctx == 0) {
			Sound sound = ResourceManager.getSound("rocketExplosionSound");
			sound.play();
			ctx++;
		}
	}

	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		tankImage.draw(400, 300);
		arg1.drawString(
				"Just Test Resource Manager (Displaying normalTankImage) ...",
				230, 200);
	}
}
