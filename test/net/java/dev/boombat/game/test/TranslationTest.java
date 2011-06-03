/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.test;

import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.FastTrig;


/**
 * 
 * @author objectworks
 */
public class TranslationTest extends BasicGame {

	float x = 200, y = 200, angle = 0;
	float x2 = 200, y2 = 200;
	float x3 = 200, y3 = 200;

	public static void main(String... args) throws Exception {
		AppGameContainer game = new AppGameContainer(new TranslationTest(),
				800, 600, false);
		game.start();
	}

	public TranslationTest() {
		super("Just Test Translation (press left or right to rotate) ...");
	}

	@Override
	public void init(GameContainer c) throws SlickException {
		c.setMinimumLogicUpdateInterval(20);
		c.setMaximumLogicUpdateInterval(20);
		ResourceManager.init();
		x2 += (float) FastTrig.sin(Math.toRadians(angle)) * 30;
		y2 -= (float) FastTrig.cos(Math.toRadians(angle)) * 30;

	}

	@Override
	public void update(GameContainer c, int delta) throws SlickException {

		if (c.getInput().isKeyDown(Input.KEY_LEFT)) {
			angle = updateRotate(delta, angle, -1);
			x += 0.05 * delta;
			x2 += 0.05 * delta;
			y += 0.05 * delta;
			y2 += 0.05 * delta;

		}

		if (c.getInput().isKeyDown(Input.KEY_RIGHT)) {
			angle = updateRotate(delta, angle, 1);
			x -= 0.05 * delta;
			x2 -= 0.05 * delta;
			y -= 0.05 * delta;
			y2 -= 0.05 * delta;

		}

		x3 = x + (float) FastTrig.sin(Math.toRadians(angle)) * 80;
		y3 = y - (float) FastTrig.cos(Math.toRadians(angle)) * 80;

	}

	public void render(GameContainer c, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.rotate(x, y, angle);
		g.drawRect(x - 50, y - 50, 100, 100);
		g.setColor(Color.red);
		g.drawRect(x, y, 100, 100);
		g.drawLine(x2, y2, x2, y2);
		g.setColor(Color.red);
		g.rotate(x, y, -angle);
		g.setColor(Color.green);
		g.drawLine(x, y, x, y);
		g.setColor(Color.blue);
		g.drawLine(x3, y3, x3, y3);

	}

	protected float updateRotate(int delta, float angle, int dir) {
		float newDelta = delta * 0.05f * dir;
		angle += newDelta;
		return angle;
	}
}
