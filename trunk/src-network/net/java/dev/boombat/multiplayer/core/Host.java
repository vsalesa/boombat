package net.java.dev.boombat.multiplayer.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Executors;

import net.java.dev.boombat.multiplayer.message.Message;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

public class Host {

	public final static int PORT = 3003;

	public final static String BOOMBAT_GROUP = "224.2.8.8";

	public static NioDatagramAcceptor acceptor;

	private static Announcer announcer;

	public static boolean start() {
		try {
			IoBuffer.setUseDirectBuffer(Message.USE_DIRECT_BUFFER);
			IoBuffer.setAllocator(new SimpleBufferAllocator());
			acceptor = new NioDatagramAcceptor();
			acceptor.setHandler(new HostHandler());
			acceptor.getSessionConfig().setReuseAddress(false);
			acceptor.getSessionConfig().setBroadcast(false);
			acceptor.getFilterChain().addLast("security", new SecurityFilter());
			// acceptor.getFilterChain().addLast("thread pool executors",
			// new ExecutorFilter(new UnorderedThreadPoolExecutor(20)));
			acceptor.bind(new InetSocketAddress(PORT));
			announcer = new Announcer();
			Executors.newCachedThreadPool().execute(announcer);
			return true;
		} catch (IOException e) {
			return false;
		}
		
	}

	public static Set<WriteFuture> broadcast(IoBuffer msg) {
		return acceptor.broadcast(msg);
	}

	public static WriteFuture reply(IoBuffer msg, IoSession sender) {
		return sender.write(msg);
	}

	public static Set<WriteFuture> relay(IoBuffer msg, IoSession sender) {
		Set<WriteFuture> futures = new HashSet<WriteFuture>();
		for (Entry<Long, IoSession> e : acceptor.getManagedSessions()
				.entrySet()) {
			if (!sender.equals(e.getValue())) {
				futures.add(e.getValue().write(msg));
			}
		}
		return futures;
	}

	public static void destroy() {
		if (announcer != null) {
			announcer.stopSend();
		}
		if (acceptor != null) {
			acceptor.unbind(new InetSocketAddress(PORT));
			acceptor.dispose();
		}
	}

}
