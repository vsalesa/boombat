package net.java.dev.boombat.multiplayer.test;

import net.java.dev.boombat.multiplayer.core.Announcer;
import net.java.dev.boombat.multiplayer.core.Finder;

public class FinderTest {
	public static void main(String[] args) {
		new Announcer().start();
		Finder.findServers();
	}
}
