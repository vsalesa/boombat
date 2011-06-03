package net.java.dev.boombat.game.entity.layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.java.dev.boombat.game.entity.BaseParticle;

import org.newdawn.slick.Graphics;

/**
 * 
 * @author objectworks
 */
public class ParticleList {

	private static List<BaseParticle> particles = Collections
			.synchronizedList(new ArrayList<BaseParticle>());

	public static void updateAll(int delta) {
		synchronized (particles) {
			Iterator<BaseParticle> iter = particles.iterator();
			while (iter.hasNext()) {
				BaseParticle p = iter.next();
				p.update(delta);
				if (p.isCompleted()) {
					iter.remove();
				}
			}
		}
	}

	public static void renderAll(Graphics g) {
		synchronized (particles) {
			for (BaseParticle p : particles) {
				p.render(g);
			}
		}
	}

	public static void add(BaseParticle particle) {
		synchronized (particles) {
			particles.add(particle);
		}
	}

	public static void clear() {
		synchronized (particles) {
			particles.clear();
		}
	}
}
