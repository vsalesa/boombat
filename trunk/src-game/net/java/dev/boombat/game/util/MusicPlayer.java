package net.java.dev.boombat.game.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;

public class MusicPlayer {

	private static List<String> names = new ArrayList<String>();
	private static int currIdx = -1;
	private static int maxIdx = -1;
	private static int minIdx = -1;
	private static boolean repeatAll = false;
	private static boolean shuffle = false;
	private static Music currentMusic;
	transient static SecureRandom random = null;

	static {
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException ex) {
			// should never happen ...
		}
	}

	public static void init() {
		for (String s : ResourceManager.getMusics().keySet()) {
			names.add(s);
			maxIdx++;
		}
		if (maxIdx > -1) {
			minIdx = 0;
		}
	}

	public static void toggleShuffle() {
		shuffle = !shuffle;
	}

	public static void toggleRepeatAll() {
		repeatAll = !repeatAll;
	}

	public static void last() {
		if (currIdx < maxIdx) {
			currIdx = maxIdx;
			changeMusic();
		}
	}

	public static void first() {
		if (currIdx != 0 && minIdx == 0) {
			currIdx = minIdx;
			changeMusic();
		}
	}

	public static void next() {
		if (currIdx < maxIdx) {
			currIdx++;
			changeMusic();
		}
	}

	public static void prev() {
		if (currIdx > minIdx) {
			currIdx--;
			changeMusic();
		}
	}

	public static void update(GameContainer container) {
		
		if (currentMusic != null) {
			if (!currentMusic.playing()) {
				if (repeatAll) {
					int tmp = currIdx;
					if (tmp < maxIdx) {
						currIdx++;
					} else {
						currIdx = minIdx;
					}

				}

				if (shuffle && maxIdx > 0) {
					currIdx = random.nextInt(maxIdx);
				}
				
				changeMusic();
			}
			currentMusic.setVolume(container.getMusicVolume());
		}
	}

	public static void stop() {
		if (currentMusic != null) {
			if (currentMusic.playing()) {
				currentMusic.stop();
			}
		}
	}

	public static void pause() {
		if (currentMusic != null) {
			if (currentMusic.playing()) {
				currentMusic.pause();
			}
		}
	}

	public static void resume() {
		if (currentMusic != null) {
			if (currentMusic.playing()) {
				currentMusic.resume();
			}
		}
	}

	private static void changeMusic() {
		if (currentMusic != null) {
			currentMusic.stop();
			currentMusic = null;
		}

		String musicName = names.get(currIdx);
		currentMusic = ResourceManager.getMusic(musicName);
		currentMusic.play();
	}

	public static int getMusicCount() {
		return names.size();
	}

	public static int getCurrentIndex() {
		return currIdx;
	}

	public static boolean isRepeatAll() {
		return repeatAll;
	}

	public static boolean isShuffle() {
		return shuffle;
	}
}
