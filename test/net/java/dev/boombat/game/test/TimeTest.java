/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.test;

import net.java.dev.boombat.game.ui.ext.TimerBox;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * 
 * @author objectworks
 */
public class TimeTest extends BasicGame {

	int elapsedTime = 1000*240;

	int displayTimeInSeconds;
	
	private TimerBox timerBox;

	public static void main(String... args) throws SlickException {
		AppGameContainer game = new AppGameContainer(new TimeTest("Time Test"),
				800, 600, false);
		game.setShowFPS(false);
		game.start();
	}

	public TimeTest(String name) {
		super(name);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		ResourceManager.init();
		timerBox = new TimerBox(10,10);
		timerBox.setVisible(true);

	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		elapsedTime -= delta;
		displayTimeInSeconds = Math.round(elapsedTime / 1000f);
		if (elapsedTime <= 0) {
			container.exit();
		}
		timerBox.update(elapsedTime);
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		g.scale(2, 2);
		g.setColor(Color.green);
		g.drawString("The window will be closed in :", 100, 100);
		g.setColor(Color.red);
		g.drawString(String.valueOf(displayTimeInSeconds)+" seconds", 160, 150);
		timerBox.render(g);
	}
}
