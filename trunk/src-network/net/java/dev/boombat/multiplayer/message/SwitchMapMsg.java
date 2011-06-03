package net.java.dev.boombat.multiplayer.message;

import net.java.dev.boombat.multiplayer.core.Util;

import org.apache.mina.core.buffer.IoBuffer;

public class SwitchMapMsg extends Message {

	public static final byte ID = 12;
	public String map;
	
	@Override
	public Message fill(IoBuffer buff) {
		map = Util.getStr(buff);
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
		Util.putStr(map, buff);
		buff.flip();
		
		return buff;
	}

}
