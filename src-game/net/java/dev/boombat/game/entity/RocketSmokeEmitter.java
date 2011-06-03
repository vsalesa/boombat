/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.entity;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * 
 * @author Admin
 */
public class RocketSmokeEmitter implements ParticleEmitter {

	private boolean enabled;
	public static final int COLOR_TYPE_DARK = 0;
	public static final int COLOR_TYPE_LIGHT = 1;
	private int colorType = COLOR_TYPE_DARK;
	private float[] color = new float[] { 255, 255, 255 };
	private static final Map<String, float[]> colors = new HashMap<String, float[]>();
	private int lastPart = 0;

	public RocketSmokeEmitter() {
		enabled = true;
		putColor("black", Color.black);
		putColor("blue", Color.blue);
		putColor("cyan", Color.cyan);
		putColor("darkGray", Color.darkGray);
		putColor("gray", Color.gray);
		putColor("green", Color.green);
		putColor("lightGray", Color.lightGray);
		putColor("magenta", Color.magenta);
		putColor("orange", Color.orange);
		putColor("pink", Color.pink);
		putColor("red", Color.red);
		putColor("white", Color.white);
		putColor("yellow", Color.yellow);
		putColor("darkyellow", new Color(181 / 255f, 170 / 255f, 51 / 255f,
				255 / 255f));
		putColor("lightpurple", new Color(177/ 255f, 108 / 255f, 142 / 255f,
				255 / 255f));
		putColor("oceanblue", new Color(100/ 255f, 175 / 255f, 238 / 255f,
				255 / 255f));
		putColor("lightgreen", new Color(90/ 255f, 217 / 255f, 56 / 255f,
				255 / 255f));
	}

	private void putColor(String name, Color color) {
		colors.put(name, new float[] { color.r * 255, color.g * 255,
				color.b * 255 });
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void update(ParticleSystem system, int delta) {
	}

	public void updateParticle(Particle particle, int delta) {
		lastPart -= delta;
		if (lastPart < 0) {
			lastPart = 15;
			float d = delta;

			float v = (particle.getLife() / (float) particle.getOriginalLife());

			if (COLOR_TYPE_DARK == colorType) {
				particle.setColor((v * color[0]) / 255, (v * color[1]) / 255,
						(v * color[2]) / 255, (v * 255) / 255);
			} else if (COLOR_TYPE_LIGHT == colorType) {
				particle.setColor((color[0]) / 255, (v * color[1]) / 255,
						(v * color[2]) / 255, (v * 255) / 255);
			}

			particle.adjustSize(d * 0.045f);
		}
	}

	public void setColor(float[] c, int colorType) {
		color = c;
		this.colorType = colorType;
	}

	public void setColor(String name, int colorType) {
		color = colors.get(name);
		this.colorType = colorType;
	}

	public boolean completed() {
		return false;
	}

	public Image getImage() {
		return null;
	}

	public boolean isOriented() {
		return false;
	}

	public void resetState() {
	}

	public boolean useAdditive() {
		return false;
	}

	public boolean usePoints(ParticleSystem system) {
		return false;
	}

	public void wrapUp() {
	}
}
