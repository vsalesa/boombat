package net.java.dev.boombat.multiplayer.message;

import java.util.HashMap;
import java.util.Map;

import net.java.dev.boombat.multiplayer.core.Util;

import org.apache.mina.core.buffer.IoBuffer;

public class SessionFlagMsg extends Message {
	public static final byte ID = 11;
	public static final byte RESTART_ROUND = 1;
	public static final byte NEW_ROUND = 2;
	public static final byte END_SESSION = 3;

	public byte flags;
	public Map<Byte, float[]> pos = new HashMap<Byte, float[]>();;

	@Override
	public Message fill(IoBuffer buff) {
		flags = buff.get();
		pos = Util.getTankPositionList(buff);
		return this;
	}

	@Override
	public IoBuffer toBuffer() {
		IoBuffer buff = IoBuffer.allocate(1);
		buff.setAutoExpand(true);
		buff.put(ID);
		buff.put(flags);
		Util.putTankPositionList(pos, buff);
		buff.flip();
		return buff;
	}

	@Override
	public boolean isRelay() {

		return false;
	}
}
