package net.java.dev.boombat.multiplayer.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

public class Finder {

	private static MulticastSocket multicastSocket;
	private static InetAddress address;

	public static List<String[]> findServers() {
		boolean ok = joinGroup();
		byte[] buffer = new byte[8192];
		List<String[]> results = new ArrayList<String[]>();
		long start = System.currentTimeMillis();
		if (ok) {
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
			while (System.currentTimeMillis() - start < 1000) {
				try {
					multicastSocket.receive(dp);
					String data = new String(dp.getData(), "8859_1").trim();
					String[] parsedData = Util
							.parseGameAnnouncementMessage(data);
					boolean add = true;
					for (String[] d : results) {
						if (d[0].equals(parsedData[0])) {
							add = false;
							break;
						}
					}
					if (add) {
						results.add(parsedData);
					}
				} catch (IOException e) {
				}
			}
			try {
				multicastSocket.leaveGroup(address);
			} catch (IOException e) {
			}
		}
		return results;
	}

	private static boolean joinGroup() {
		try {
			address = InetAddress.getByName(Host.BOOMBAT_GROUP);
			multicastSocket = new MulticastSocket(Host.PORT);
			multicastSocket.setTimeToLive(16);
			multicastSocket.joinGroup(address);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
