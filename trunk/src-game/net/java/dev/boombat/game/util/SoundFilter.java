package net.java.dev.boombat.game.util;

import org.newdawn.slick.Sound;

/**
 * 
 * @author objectworks
 */
public class SoundFilter {

	public static void filter3D(float xSource, float ySource, float xListener,
			float yListener, Sound sound, float maxDistance) {

		float dx = (xSource - xListener);
		float dy = (ySource - yListener);
		float distance = (float) Math.sqrt(dx * dx + dy * dy);

		// set volume from 0 (no sound) to 1
		float newVolume = (maxDistance - distance) / maxDistance;
		if (newVolume <= 0) {
			newVolume = 0;
		}

		sound.play(1.0f, newVolume);
	}
}
