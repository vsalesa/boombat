package net.java.dev.boombat.multiplayer.message;

import net.java.dev.boombat.multiplayer.core.Util;

import org.apache.mina.core.buffer.IoBuffer;

public class ChatMsg extends Message {
	public final static byte ID = 4;
	public String content;

	@Override
	public Message fill(IoBuffer buff) {
		content = Util.getStr(buff);
		return this;
	}

	@Override
	public IoBuffer toBuffer() {
		IoBuffer buff = IoBuffer.allocate(1);
		buff.setAutoExpand(true);
		buff.put(ID);
		Util.putStr(content, buff);
		buff.flip();
		return buff;
	}

	@Override
	public boolean isRelay() {

		return true;
	}
}
