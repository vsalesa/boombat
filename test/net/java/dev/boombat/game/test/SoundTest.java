/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.test;

import net.java.dev.boombat.game.util.SoundFilter;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;


/**
 * 
 * @author objectworks
 */
public class SoundTest extends BasicGame {

	Sound sound;
	Animation fly;
	Image ear;
	float xear;
	float year;
	float xfly;
	float yfly;

	public static void main(String... args) throws Exception {
		AppGameContainer game = new AppGameContainer(new SoundTest(), 800, 600,
				false);
		game.start();
	}

	public SoundTest() {
		super("Just Test 3D Filter Sound");
	}

	@Override
	public void init(GameContainer c) throws SlickException {
		c.setMinimumLogicUpdateInterval(20);
		c.setMaximumLogicUpdateInterval(20);
		c.setMouseCursor("testdata/trans-cursor.png", 0, 0);
		sound = new Sound("testdata/fly-bzz.wav");
		fly = new Animation(true);
		for (int i = 1; i <= 3; i++) {
			fly.addFrame(new Image("testdata/fly" + i + ".png"), 50);
		}
		ear = new Image("testdata/ear.png");
		xear = (c.getWidth() - ear.getWidth()) / 2;
		year = (c.getHeight() - ear.getHeight()) / 2;
	}

	@Override
	public void update(GameContainer c, int delta) throws SlickException {
		Input input = c.getInput();
		fly.update(delta);
		SoundFilter.filter3D(xear, year, xfly, yfly, sound, c.getHeight());
		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			c.exit();
		}
	}

	public void render(GameContainer c, Graphics g) throws SlickException {
		g.setBackground(Color.pink);
		ear.draw(xear, year);
		fly.draw(xfly, yfly);
		g.drawString("Move fly with mouse to test Sound Filter 3D ...", 10, 30);
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		xfly = newx;
		yfly = newy;
	}
}
