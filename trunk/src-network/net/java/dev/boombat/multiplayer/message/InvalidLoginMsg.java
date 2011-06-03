package net.java.dev.boombat.multiplayer.message;

import org.apache.mina.core.buffer.IoBuffer;

public class InvalidLoginMsg extends Message {

	public static final byte ID = 7;

	@Override
	public Message fill(IoBuffer buff) {
		return this;
	}

	@Override
	public boolean isRelay() {
		return false;
	}

	@Override
	public IoBuffer toBuffer() {
		IoBuffer buff = IoBuffer.allocate(1);
		buff.setAutoExpand(true);
		buff.put(ID);
		buff.flip();
		
		return buff;
	}

}
