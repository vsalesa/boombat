package net.java.dev.boombat.multiplayer.test.unit;

import net.java.dev.boombat.multiplayer.core.Host;
import net.java.dev.boombat.multiplayer.message.ChatMsg;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

public class ReplyTest {
	public static void main(String[] args)throws Exception {
		Host.start();
		while(Host.acceptor.getManagedSessionCount()==0){
			System.out.println("Wait for clients");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		IoSession sender = (IoSession)Host.acceptor.getManagedSessions().values().toArray()[0];
		ChatMsg msg = new ChatMsg();
		msg.content = "test reply";
		WriteFuture future = Host.reply(msg.toBuffer(), sender);
		System.out.println("TEST CASE 1 : CALL reply() RETURN " + future);
	}
}
