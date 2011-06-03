package net.java.dev.boombat.multiplayer.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Announcer extends Thread {

	private DatagramPacket announcePacket;

	private InetAddress address;

	private MulticastSocket multicastSocket;

	private boolean send = true;

	private boolean connect = false;

	public Announcer() {
	}

	@Override
	public void run() {
		joinGroup();
		if (connect) {
			while (send) {
				try {
					String message = Util.createGameAnnouncementMessage();
					announcePacket = new DatagramPacket(message.getBytes(),
							message.length(), address, Host.PORT);
					multicastSocket.send(announcePacket);
					Thread.sleep(250);
				} catch (Exception e) {
				}
			}
		}
	}

	private void joinGroup() {
		try {
			address = InetAddress.getByName(Host.BOOMBAT_GROUP);
			multicastSocket = new MulticastSocket();
			multicastSocket.setTimeToLive(16);
			multicastSocket.joinGroup(address);
			connect = true;
		} catch (IOException e) {
			connect = false;
		}

	}

	public void stopSend() {
		this.send = false;
	}

}
