package net.java.dev.boombat.multiplayer.test;

import java.util.HashMap;
import java.util.Map;

import net.java.dev.boombat.multiplayer.core.Util;

public class RegexTest {
	public static void main(String[] args) {
		for (String s : Util.parseGameAnnouncementMessage(Util
				.createGameAnnouncementMessage()))
			System.out.println(s);
		Map<String, String[]> temps = new HashMap<String, String[]>();
		String [] res = Util.parseGameAnnouncementMessage(Util
				.createGameAnnouncementMessage());
		temps.put(res[0], res);
	}
}
