package net.java.dev.boombat.multiplayer.message;

import org.apache.mina.core.buffer.IoBuffer;

public class TankPositionMsg extends Message {

	public final static byte ID = 13;
	public byte playerId;
	public float x;
	public float y;
	public float bodyAng;
	public float turretAng;

	public IoBuffer toBuffer() {
		IoBuffer buff = IoBuffer.allocate(1);
		buff.setAutoExpand(true);
		buff.put(ID);
		buff.put(playerId);
		buff.putFloat(x);
		buff.putFloat(y);
		buff.putFloat(bodyAng);
		buff.putFloat(turretAng);
		buff.flip();
		return buff;
	}

	public TankPositionMsg fill(IoBuffer buff) {
		playerId = buff.get();
		x = buff.getFloat();
		y = buff.getFloat();
		bodyAng = buff.getFloat();
		turretAng = buff.getFloat();
		return this;
	}

	@Override
	public boolean isRelay() {
		
		return true;
	}
}
