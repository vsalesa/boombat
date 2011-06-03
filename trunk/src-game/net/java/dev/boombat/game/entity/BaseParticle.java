package net.java.dev.boombat.game.entity;

import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * 
 * @author objectworks
 */
public class BaseParticle {

	protected float x;
	protected float y;
	protected float angle;
	protected boolean completed;
	protected ParticleSystem system;
	protected ParticleEmitter emitter;
	protected int xTile;
	protected int yTile;
	protected int maxLifeTime;
	protected int lifeTime;
	protected float centerXAdd;
	protected float centerYAdd;
	protected boolean lifeForever;

	public BaseParticle(ConfigurableEmitter emitter) {
		this(new ParticleSystem(ResourceManager
				.getImage("defaultParticleImage"), (int) emitter.spawnCount
				.getMax()), emitter, 0);
		this.lifeForever = true;
	}

	public BaseParticle(ParticleEmitter emitter, int numParticle) {
		this(new ParticleSystem(ResourceManager
				.getImage("defaultParticleImage"), numParticle), emitter, 0);
		this.lifeForever = true;
	}

	public BaseParticle(ConfigurableEmitter emitter, int maxLifeTime) {
		this(new ParticleSystem(ResourceManager
				.getImage("defaultParticleImage"), (int) emitter.spawnCount
				.getMax()), emitter, maxLifeTime);
	}

	public BaseParticle(ParticleEmitter emitter, int numParticle,
			int maxLifeTime) {
		this(new ParticleSystem(ResourceManager
				.getImage("defaultParticleImage"), numParticle), emitter,
				maxLifeTime);
	}

	public BaseParticle(ParticleSystem system, ParticleEmitter emitter,
			int maxLifeTime) {
		this.system = system;
		this.emitter = emitter;
		this.maxLifeTime = maxLifeTime;
		this.system.addEmitter(emitter);
		this.system.setRemoveCompletedEmitters(true);
	}

	public void update(int delta) {
		if (!lifeForever && lifeTime > maxLifeTime) {
			completed = true;
		}

		updateParticle(delta);
		lifeTime += delta;
	}

	protected void updateParticle(int delta) {
		system.update(delta);
	}

	public void render(Graphics g) {
		g.rotate(xTile, yTile, angle);
		renderParticle(g);
		g.rotate(xTile, yTile, -angle);
	}

	protected void renderParticle(Graphics g) {
		system.render(xTile + centerXAdd, yTile + centerYAdd);
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setPosition(float x, float y, float angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.xTile = Math.round(x * GameMap.TILE_SIZE);
		this.yTile = Math.round(y * GameMap.TILE_SIZE);
	}

	public void addPosToDrawCenter(float xadd, float yadd) {
		centerXAdd = xadd;
		centerYAdd = yadd;
	}

	public ParticleEmitter getEmitter() {
		return emitter;
	}
}
