package net.java.dev.boombat.multiplayer.message;

import org.apache.mina.core.buffer.IoBuffer;

public class KeepAliveMsg extends Message {
	public static final byte ID = 15;

	@Override
	public Message fill(IoBuffer buff) {

		return this;
	}

	@Override
	public IoBuffer toBuffer() {
		IoBuffer buff = IoBuffer.allocate(1);
		buff.setAutoExpand(true);
		buff.put(ID);
		buff.flip();

		return buff;
	}

	@Override
	public boolean isRelay() {

		return false;
	}

}
