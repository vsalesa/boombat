/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.entity;

import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Graphics;

/**
 * 
 * @author objectworks
 */
public class Rocket extends Missile {

	private BaseParticle smokeParticle;

	public Rocket(byte missileOwnerId, float x, float y, float distance,
			float angle) {
		super(ResourceManager.getImage("rocketImage"), 0.012f, 6, 600,
				"rocketExplosionConfEmitter", "rocketExplosionSound",
				missileOwnerId, x, y, distance, angle);
		smokeParticle = new SmokeRocketParticle("rocketSmokeNonConfEmitter");
	}

	@Override
	protected void updateIfNotCollide(int delta, float newX, float newY) {
		super.updateIfNotCollide(delta, newX, newY);
		smokeParticle.setPosition(x, y, angle);
		smokeParticle.update(delta);
	}

	@Override
	public void render(Graphics g) {
		super.render(g);
		smokeParticle.render(g);
	}

	@Override
	protected void drawImagePosition(float x, float y) {
		super.drawImagePosition(x - 2.8f, y - 12);
	}

	public BaseParticle getSmokeParticle() {
		return smokeParticle;
	}
}
