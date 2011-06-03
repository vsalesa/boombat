package net.java.dev.boombat.multiplayer.message;

import net.java.dev.boombat.multiplayer.core.Util;

import org.apache.mina.core.buffer.IoBuffer;

public class ChangePlayerNameMsg extends Message {
	public final static byte ID = 2;
	public byte playerId;
	public String name;

	@Override
	public Message fill(IoBuffer buff) {
		playerId = buff.get();
		name = Util.getStr(buff);
		return this;
	}

	@Override
	public IoBuffer toBuffer() {
		IoBuffer buff = IoBuffer.allocate(1);
		buff.setAutoExpand(true);
		buff.put(ID);
		buff.put(playerId);
		Util.putStr(name, buff);
		buff.flip();
		return buff;
	}

	@Override
	public boolean isRelay() {
		return true;
	}
}
