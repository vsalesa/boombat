package net.java.dev.boombat.game.entity.layer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.java.dev.boombat.game.entity.Missile;

import org.newdawn.slick.Graphics;

/**
 * 
 * @author objectworks
 */
public class MissileList {

	private static List<Missile> missiles = Collections
			.synchronizedList(new ArrayList<Missile>());

	public static void updateAll(int delta) {
		synchronized (missiles) {
			Iterator<Missile> iter = missiles.iterator();
			while (iter.hasNext()) {
				Missile m = iter.next();
				if (m.isCompleted()) {
					iter.remove();
					continue;
				}
				m.update(delta);
			}
		}
	}

	public static void renderAll(Graphics g) {
		synchronized (missiles) {
			for (Missile m : missiles) {
				m.render(g);
			}
		}
	}

	public static void add(Missile missile) {
		synchronized (missiles) {
			missiles.add(missile);
		}
	}

	public static void add(Collection<Missile> m) {
		missiles.addAll(m);
	}

	public static void clear() {
		missiles.clear();
	}
}
