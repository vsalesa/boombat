package net.java.dev.boombat.multiplayer.message;

import net.java.dev.boombat.multiplayer.core.Util;

import org.apache.mina.core.buffer.IoBuffer;

public class LoginReqMsg extends Message {
	public final static byte ID = 9;
	public String name;
	public String password;
	public String tank;
	public byte team;

	@Override
	public Message fill(IoBuffer buff) {
		name = Util.getStr(buff);
		password = Util.getStr(buff);
		tank = Util.getStr(buff);
		team = buff.get();
		return this;
	}

	@Override
	public IoBuffer toBuffer() {
		IoBuffer buff = IoBuffer.allocate(1);
		buff.setAutoExpand(true);
		buff.put(ID);
		Util.putStr(name, buff);
		Util.putStr(password, buff);
		Util.putStr(tank, buff);
		buff.put(team);
		buff.flip();
		return buff;
	}

	@Override
	public boolean isRelay() {
		
		return false;
	}
}
