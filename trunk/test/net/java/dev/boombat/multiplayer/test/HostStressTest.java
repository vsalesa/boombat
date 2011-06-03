package net.java.dev.boombat.multiplayer.test;

import net.java.dev.boombat.multiplayer.core.Host;
import net.java.dev.boombat.multiplayer.message.ChatMsg;

import org.apache.mina.core.buffer.IoBuffer;

public class HostStressTest {
	public static void main(String[] args) throws Exception{
		Host.start();
		ChatMsg o = new ChatMsg();
		o.content = "halo from server";
		IoBuffer b = o.toBuffer();
		
		for(;;){
			Host.broadcast(b);
			System.out.println(Host.acceptor.getManagedSessionCount());
			Thread.sleep(250);
		}
	}
}
