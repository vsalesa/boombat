package net.java.dev.boombat.game.entity;

import net.java.dev.boombat.game.util.ResourceManager;

/**
 * 
 * @author objectworks
 */
public class Bullet extends Missile {

	public Bullet(byte missileOwnerId, float x, float y, float distance,
			float angle) {
		super(ResourceManager.getImage("bulletImage"), 0.015f, 2, 500,
				"bulletExplosionConfEmitter", "bulletExplosionSound",
				missileOwnerId, x, y, distance, angle, 9);
	}

	@Override
	protected void drawImagePosition(float x, float y) {
		super.drawImagePosition(x, y - 10);
	}

}
