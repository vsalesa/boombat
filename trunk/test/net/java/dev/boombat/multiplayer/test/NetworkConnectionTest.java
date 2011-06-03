package net.java.dev.boombat.multiplayer.test;

import java.net.InetAddress;

public class NetworkConnectionTest {
	public static void main(String[] args) throws Exception {
		System.out.println("connectin into network ? "
				+ !InetAddress.getLocalHost().isLoopbackAddress());
	}
}
