package net.java.dev.boombat.multiplayer.message;

import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.multiplayer.core.Util;

import org.apache.mina.core.buffer.IoBuffer;

public class AddNewPlayerMsg extends Message {
	public final static byte ID = 1;
	public Player player;

	@Override
	public Message fill(IoBuffer buff) {
		player = Util.getPlayer(buff);
		return this;
	}

	@Override
	public IoBuffer toBuffer() {
		IoBuffer buff = IoBuffer.allocate(1);
		buff.setAutoExpand(true);
		buff.put(ID);
		Util.putPlayer(player, buff);
		buff.flip();
		return buff;
	}

	@Override
	public boolean isRelay() {
		
		return false;
	}
}
