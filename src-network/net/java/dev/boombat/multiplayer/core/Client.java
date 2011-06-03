package net.java.dev.boombat.multiplayer.core;

import java.net.InetSocketAddress;

import net.java.dev.boombat.multiplayer.message.Message;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

public class Client {

	public static final int TIMEOUT = 1000;

	public static NioDatagramConnector connector;
	public static IoSession session;
	private static boolean validConnected;

	public static boolean connect(String address) {
		InetSocketAddress addr = new InetSocketAddress(address, Host.PORT);
		if (addr.isUnresolved()) {
			return false;
		}

		IoBuffer.setUseDirectBuffer(Message.USE_DIRECT_BUFFER);
		IoBuffer.setAllocator(new SimpleBufferAllocator());
		connector = new NioDatagramConnector();
		connector.setHandler(new ClientHandler());
		connector.getSessionConfig().setReuseAddress(true);
		connector.getSessionConfig().setBroadcast(false);
		// connector.getFilterChain().addLast("log", new LoggingFilter());
		// connector.getFilterChain().addLast("threadPool",
		// new ExecutorFilter(Executors.newCachedThreadPool()));
		// connector.getFilterChain().addLast("tpool",
		// new ExecutorFilter(new UnorderedThreadPoolExecutor(20)));
		connector.setDefaultRemoteAddress(addr);
		ConnectFuture conn = connector.connect();
		conn.awaitUninterruptibly(TIMEOUT);

		session = conn.getSession();
		if (session == null) {
			return false;
		}

		return true;
	}

	public static WriteFuture send(IoBuffer msg) {
		return session.write(msg);
	}

	public static void destroy() {
		if (connector != null) {
			connector.dispose();
		}
	}

	public static boolean isValidConnected() {
		return validConnected;
	}

	public static void setValidConnected(boolean validConnected) {
		Client.validConnected = validConnected;
	}
}
