package net.java.dev.boombat.multiplayer.test.unit;

import net.java.dev.boombat.multiplayer.core.Client;
import net.java.dev.boombat.multiplayer.message.ChatMsg;

import org.apache.mina.core.future.WriteFuture;

public class SendTest {
	public static void main(String[] args) {
		Client.connect("localhost");
		ChatMsg chat = new ChatMsg();
		chat.content = "test send";
		WriteFuture future = Client.send(chat.toBuffer());
		System.out.println("TEST CASE 1 : CALL send() RETURN " + future);
	}
}
