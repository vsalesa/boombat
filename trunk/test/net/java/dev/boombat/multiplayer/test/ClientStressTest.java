package net.java.dev.boombat.multiplayer.test;

import net.java.dev.boombat.multiplayer.core.Client;
import net.java.dev.boombat.multiplayer.message.ChatMsg;

import org.apache.mina.core.buffer.IoBuffer;

public class ClientStressTest {
	public static void main(String[] args)throws Exception {
		Client.connect("localhost");
		ChatMsg o = new ChatMsg();
		o.content = "halo from client";
		IoBuffer b = o.toBuffer();
		for(;;){
			Client.send(b);
			Thread.sleep(0);
		}
	}
}
