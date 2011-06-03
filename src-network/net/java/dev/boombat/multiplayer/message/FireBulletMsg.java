package net.java.dev.boombat.multiplayer.message;

import org.apache.mina.core.buffer.IoBuffer;

public class FireBulletMsg extends Message {

	public final static byte ID = 5;
	public byte playerId;

	public IoBuffer toBuffer() {
		IoBuffer buff = IoBuffer.allocate(1);
		buff.setAutoExpand(true);
		buff.put(ID);
		buff.put(playerId);
		buff.flip();
		return buff;
	}

	public FireBulletMsg fill(IoBuffer buff) {
		playerId = buff.get();
		return this;
	}

	@Override
	public boolean isRelay() {
		
		return true;
	}
}
