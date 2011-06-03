package net.java.dev.boombat.multiplayer.test.unit.dummy;

import net.java.dev.boombat.multiplayer.core.Client;
import net.java.dev.boombat.multiplayer.message.ChatMsg;

public class ClientDummy {
	public static void main(String[] args) {
		boolean ok = Client.connect("localhost");
		ChatMsg chat = new ChatMsg();
		chat.content = "test send";
		Client.send(chat.toBuffer());
		System.out.println("Connect OK? " + ok);
	}
}
