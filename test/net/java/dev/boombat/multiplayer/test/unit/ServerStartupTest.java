package net.java.dev.boombat.multiplayer.test.unit;

import net.java.dev.boombat.multiplayer.core.Host;

public class ServerStartupTest {
	public static void main(String[] args) {
		boolean started = Host.start();
		System.out.println("Test Case 1 - success (1-2-4) : RETURN " + started);
		started = Host.start();
		System.out.println("Test Case 2 - fail (1-3-4) : RETURN " + started);
	}
}
