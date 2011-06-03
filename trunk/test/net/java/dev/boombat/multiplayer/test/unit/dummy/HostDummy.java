package net.java.dev.boombat.multiplayer.test.unit.dummy;

import net.java.dev.boombat.multiplayer.core.Host;

public class HostDummy {
	public static void main(String[] args) {
		Host.start();
		while(Host.acceptor.getManagedSessionCount()==0){
			System.out.println("Wait for clients");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		System.out.println("Clients : " + Host.acceptor.getManagedSessionCount());
	}
}
