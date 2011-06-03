package net.java.dev.boombat.game.test;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class TestBasic extends BasicGame {

	private Animation animation;
	private Sound sound;
	// private Image image;
	private float x;
	private float y;
	private Image bgImage;
	private GameContainer container;

	public TestBasic(String title) {
		super(title);
	}

	
	public void init(GameContainer c) throws SlickException {
		// image = new Image("testdata/fly1.png");
		this.container = c;
		animation = new Animation(true);
		for (int i = 1; i < 4; i++) {
			animation.addFrame(new Image("testdata/fly" + i + ".png"), 50);
		}
		bgImage = new Image("testdata/bg.jpg");
		sound = new Sound("testdata/fly-bzz.wav");
		sound.play();
		sound.loop();
		x = (c.getWidth() - 64) / 2;
		y = (c.getHeight() - 48) / 2;
	}

	
	public void update(GameContainer c, int delta) throws SlickException {
		if (c.getInput().isKeyDown(Input.KEY_RIGHT))
			x += 0.5f * delta;
		if (c.getInput().isKeyDown(Input.KEY_LEFT))
			x -= 0.5f * delta;
		if (c.getInput().isKeyDown(Input.KEY_DOWN))
			y += 0.5f * delta;
		if (c.getInput().isKeyDown(Input.KEY_UP))
			y -= 0.5 * delta;

		x = Math.min(x, c.getWidth() - 64);
		x = Math.max(0, x);
		y = Math.min(c.getHeight() - 48, y);
		y = Math.max(0, y);

		animation.update(delta);
	}

	
	public void render(GameContainer c, Graphics g) throws SlickException {
		bgImage.draw(0, 0, c.getWidth(), c.getHeight());
		// image.draw(x, y);
		animation.draw(x, y);
	}

	public static void main(String[] args) {
		try {
			AppGameContainer g = new AppGameContainer(new TestBasic(
					"Hii There's a fly!!!"), 640, 480, true);
			g.setVerbose(false);
			g.start();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void keyPressed(int key, char c) {
		if (Input.KEY_ESCAPE == key)
			container.exit();
	}

	
	public boolean closeRequested() {

		return false;
	}

}
