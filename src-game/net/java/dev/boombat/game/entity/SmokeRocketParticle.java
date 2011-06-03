/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.entity;

import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.particles.Particle;

/**
 * 
 * @author objectworks
 */
public class SmokeRocketParticle extends BaseParticle {

	private String colorName;
	private int colorType;

	public SmokeRocketParticle(String emitterName) {
		super(ResourceManager.getNonConfigurableEmitter(emitterName), 35, 0);

	}

	@Override
	protected void updateParticle(int delta) {
		Particle p = system.getNewParticle(emitter,
				(int) (150 + (Math.random() * 250)));
		p.setPosition(xTile, yTile);
		p.setSize(7);
		p.setColor(1, 1, 1, 1);
		((RocketSmokeEmitter) emitter).setColor(colorName, colorType);
		system.update(delta);
	}

	@Override
	public void render(Graphics g) {
		system.render();
	}

	public void setColor(String name, int colorType) {
		this.colorName = name;
		this.colorType = colorType;
	}

}
