package net.java.dev.boombat.multiplayer.test.unit;

import net.java.dev.boombat.multiplayer.core.Client;

public class ClientConnectorTest {
	public static void main(String[] args) {
		boolean connected = Client.connect("localhost");
		System.out.println("Test Case 1 - Success (1-2-3-4-5-6-7) : RETURN " + connected);
		connected = Client.connect("unknown");
		System.out.println("Test Case 2 - Fail, unresolved address (1-2-4-5-7) : RETURN " + connected);
		connected = Client.connect("10.100.100.10");
		System.out.println("Test Case 3 - Fail, inactive server (1-2-4-5-6-7) : RETURN " + connected);
	}
}
