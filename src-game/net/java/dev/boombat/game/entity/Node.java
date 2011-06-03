package net.java.dev.boombat.game.entity;

import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Node {
	protected Image image;
	protected float x;
	protected float y;
	protected float angle;

	public Node(String imageName, float x, float y, float angle) {
		image = ResourceManager.getImage(imageName);
		this.x = x;
		this.y = y;
		this.angle = angle;
	}

	public void render(Graphics g) {
		float xtile = x * GameMap.TILE_SIZE;
		float ytile = y * GameMap.TILE_SIZE;
		g.rotate(xtile, ytile, angle);
		image.draw(xtile - 16, ytile - 16);
		g.rotate(xtile, ytile, -angle);
	}
}
