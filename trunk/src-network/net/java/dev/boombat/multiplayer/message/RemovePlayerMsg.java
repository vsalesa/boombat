package net.java.dev.boombat.multiplayer.message;

import org.apache.mina.core.buffer.IoBuffer;

public class RemovePlayerMsg extends Message {
	public final static byte ID = 10;
	public byte playerId;

	@Override
	public Message fill(IoBuffer buff) {
		playerId = buff.get();
		return this;
	}

	@Override
	public IoBuffer toBuffer() {
		IoBuffer buff = IoBuffer.allocate(1);
		buff.setAutoExpand(true);
		buff.put(ID);
		buff.put(playerId);
		buff.flip();
		return buff;
	}

	@Override
	public boolean isRelay() {
		return true;
	}
}
