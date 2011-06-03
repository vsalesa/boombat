package net.java.dev.boombat.game.entity;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.util.CollisionUtil;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

public class Camera {

	private static Camera instance = new Camera();

	private float x;

	private float y;

	public static final float SPEED = 0.02f;

	private float speed = SPEED;

	private Image bgImage;

	public static Camera instance() {
		return instance;
	}

	private Camera() {
		this.bgImage = ResourceManager.getImage("cameraImage");
	}

	public void update(Input input, int delta) {
		if (input.isKeyDown(Config.getKey("cameraUpKey"))) {
			moveUp(delta);
		}
		if (input.isKeyDown(Config.getKey("cameraDownKey"))) {
			moveDown(delta);
		}
		if (input.isKeyDown(Config.getKey("cameraLeftKey"))) {
			moveLeft(delta);
		}
		if (input.isKeyDown(Config.getKey("cameraRightKey"))) {
			moveRight(delta);
		}
	}

	public void render(Graphics g) {
		bgImage
				.draw((x * GameMap.TILE_SIZE) - 32,
						(y * GameMap.TILE_SIZE) - 32);
	}

	private void moveLeft(int delta) {
		updateHorizontalMove(-1, delta);
	}

	private void moveRight(int delta) {
		updateHorizontalMove(1, delta);
	}

	private void moveUp(int delta) {
		updateVerticalMove(-1, delta);
	}

	private void moveDown(int delta) {
		updateVerticalMove(1, delta);
	}

	private void updateHorizontalMove(int dir, int delta) {
		float newdelta = (delta * speed) * dir;
		float newx = x + newdelta;
		if (!CollisionUtil.isCollideWithEdgeScreen(newx, y)) {
			x = newx;
		}
	}

	private void updateVerticalMove(int dir, int delta) {
		float newdelta = (delta * speed) * dir;
		float newy = y + newdelta;
		if (!CollisionUtil.isCollideWithEdgeScreen(x, newy)) {
			y = newy;
		}
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

}
