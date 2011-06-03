/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.test;

import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * 
 * @author objectworks
 */
public class ParticleTest extends BasicGame {

	ParticleEmitter e;
	ParticleSystem[] s;
	ParticleSystem sys;
	float x[][];

	public static void main(String... args) throws SlickException {
		AppGameContainer game = new AppGameContainer(new ParticleTest(
				"ParticleTest"), 800, 600, false);
		game.setVerbose(false);
		game.setSmoothDeltas(true);
		game.setVSync(true);
		game.start();
	}

	public ParticleTest(String name) {
		super(name);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		ResourceManager.init();
		e = ResourceManager
				.loadConfigurableEmitter("rocketExplosionConfEmitter");
		sys = new ParticleSystem(ResourceManager
				.getImage("defaultParticleImage"), 500);
		for (int i = 0; i < 300; i++) {
			ConfigurableEmitter e0 = duplicateEmitter(((ConfigurableEmitter) e), i+"name");
			//ConfigurableEmitter e0 = ((ConfigurableEmitter) e).duplicate();
			e0.setPosition((float) Math.random() * 800,
					(float) Math.random() * 600);
			sys.addEmitter(e0);
		}
		// s = new ParticleSystem[300];
		// x = new float[300][2];
		// for (int i = 0; i < 300; i++) {
		// s[i] = new ParticleSystem(ResourceManager
		// .getImage("defaultParticleImage"), 500);
		// s[i].addEmitter(e);
		// x[i][0] = (float) Math.random() * 800;
		// x[i][1] = (float) Math.random() * 600;
		// }
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		// for (ParticleSystem sx : s)
		// sx.update(delta);
		sys.update(delta);
	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		// for (int i = 0; i < s.length; i++) {
		// s[i].render(x[i][0], x[i][1]);
		// }
		sys.render(20,20);
	}
	
	public ConfigurableEmitter duplicateEmitter(
			ConfigurableEmitter emitterToDuplicate, String name) {
		
		ConfigurableEmitter newOne = new ConfigurableEmitter(name);
		newOne.scaleY = emitterToDuplicate.scaleY;
		newOne.alpha = emitterToDuplicate.alpha;
		newOne.angularOffset = emitterToDuplicate.angularOffset;
		newOne.colors = emitterToDuplicate.colors;
		newOne.emitCount = emitterToDuplicate.emitCount;
		newOne.endAlpha = emitterToDuplicate.endAlpha;
		newOne.gravityFactor = emitterToDuplicate.gravityFactor;
		newOne.growthFactor = emitterToDuplicate.growthFactor;
		newOne.imageName = emitterToDuplicate.imageName;
		newOne.initialDistance = emitterToDuplicate.initialDistance;
		newOne.initialLife = emitterToDuplicate.initialLife;
		newOne.initialSize = emitterToDuplicate.initialSize;
		newOne.length = emitterToDuplicate.length;
		newOne.size = emitterToDuplicate.size;
		newOne.spawnCount = emitterToDuplicate.spawnCount;
		newOne.spawnInterval = emitterToDuplicate.spawnInterval;
		newOne.speed = emitterToDuplicate.speed;
		newOne.spread = emitterToDuplicate.spread;
		newOne.startAlpha = emitterToDuplicate.startAlpha;
		return newOne;
	}
}
