package net.java.dev.boombat.multiplayer.message;

import org.apache.mina.core.buffer.IoBuffer;

public abstract class Message {

	public static final boolean USE_DIRECT_BUFFER = false;

	public abstract IoBuffer toBuffer();

	public abstract Message fill(IoBuffer buff);

	public abstract boolean isRelay();

}
