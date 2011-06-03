package net.java.dev.boombat.multiplayer.message;

import net.java.dev.boombat.multiplayer.core.Util;

import org.apache.mina.core.buffer.IoBuffer;

public class ChangeTeamMsg extends Message {
	public final static byte ID = 3;
	public byte playerId;
	public byte team;
	public String tank;

	@Override
	public Message fill(IoBuffer buff) {
		playerId = buff.get();
		team = buff.get();
		tank = Util.getStr(buff);
		return this;
	}

	@Override
	public IoBuffer toBuffer() {
		IoBuffer buff = IoBuffer.allocate(1);
		buff.setAutoExpand(true);
		buff.put(ID);
		buff.put(playerId);
		buff.put(team);
		Util.putStr(tank, buff);
		buff.flip();
		return buff;
	}

	@Override
	public boolean isRelay() {
		return true;
	}
}
