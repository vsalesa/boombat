/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.test;

import net.java.dev.boombat.game.ui.core.SoundBar;
import net.java.dev.boombat.game.util.MusicPlayer;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;


/**
 * 
 * @author objectworks
 */
public class MusicPlayerTest extends BasicGame {
	Image bg;

	SoundBar soundScroller;

	public static void main(String... args) throws Exception {
		AppGameContainer game = new AppGameContainer(new MusicPlayerTest(),
				800, 600, false);
		game.start();
	}

	public MusicPlayerTest() {
		super("Music Player Test");
	}

	@Override
	public void init(GameContainer c) throws SlickException {
		c.setMaximumLogicUpdateInterval(20);
		c.setMinimumLogicUpdateInterval(20);
		ResourceManager.init();
		MusicPlayer.init();
		bg = new Image("testdata/music.jpg");
		soundScroller = new SoundBar(c, 700, 590, Color.darkGray,
				Color.lightGray, 7, 3, 2);
		soundScroller.setValue(0f);
	}

	@Override
	public void update(GameContainer c, int delta) throws SlickException {
		Input input = c.getInput();

		if (input.isKeyPressed(Input.KEY_LEFT)) {
			MusicPlayer.prev();
		}

		if (input.isKeyPressed(Input.KEY_RIGHT)) {
			MusicPlayer.next();
		}

		if (input.isKeyPressed(Input.KEY_UP)) {
			MusicPlayer.first();
		}

		if (input.isKeyPressed(Input.KEY_DOWN)) {
			MusicPlayer.last();
		}

		if (input.isKeyPressed(Input.KEY_S)) {
			MusicPlayer.toggleShuffle();
		}

		if (input.isKeyPressed(Input.KEY_R)) {
			MusicPlayer.toggleRepeatAll();
		}

		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			MusicPlayer.stop();
			c.exit();
		}

		MusicPlayer.update(c);
		c.setMusicVolume(soundScroller.getValue() / 100);

	}

	public void render(GameContainer c, Graphics g) throws SlickException {
		bg.draw(0, 0);
		g.setColor(Color.darkGray);
		g.drawString("Music Player Testing .... ", 20, 30);
		g.drawString("Toggle Shuffle : S ", 20, 50);
		g.drawString("Toggle RepeatAll or RepeatOne : R ", 20, 70);
		g.drawString("Next Music : RIGHT ARROW ", 20, 90);
		g.drawString("Prev Music : LEFT ARROW ", 20, 110);
		g.drawString("First Music : UP ARROW ", 20, 130);
		g.drawString("Last Music : DOWN ARROW ", 20, 150);
		g.drawString("Number of Musics : " + MusicPlayer.getMusicCount(), 20,
				170);
		g.drawString("Current Index :  " + MusicPlayer.getCurrentIndex(), 20,
				190);
		g.drawString("Repeat All :  " + MusicPlayer.isRepeatAll(), 20, 210);
		g.drawString("Shuffle :  " + MusicPlayer.isShuffle(), 20, 230);
		g.drawString("Press Escape to Quit .... ", 580, 30);
		g.drawString("mailto : eriq.adams@yahoo.com or aryaspy@yahoo.com", 20,
				580);
		soundScroller.render(c, g);
	}

}
