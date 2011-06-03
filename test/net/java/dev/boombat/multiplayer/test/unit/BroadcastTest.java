package net.java.dev.boombat.multiplayer.test.unit;

import java.util.Set;

import net.java.dev.boombat.multiplayer.core.Host;
import net.java.dev.boombat.multiplayer.message.ChatMsg;

import org.apache.mina.core.future.WriteFuture;

public class BroadcastTest {
	public static void main(String[] args) {
		Host.start();
		while(Host.acceptor.getManagedSessionCount()==0){
			System.out.println("Wait for clients");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		ChatMsg chat = new ChatMsg();
		chat.content = "test send";
		Set<WriteFuture> futures = Host.broadcast(chat.toBuffer());
		System.out.println("TEST CASE 1 : CALL broadcast() RETURN " + futures);
	}
}
