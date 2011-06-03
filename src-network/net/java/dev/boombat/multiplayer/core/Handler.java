package net.java.dev.boombat.multiplayer.core;

import net.java.dev.boombat.multiplayer.Manager;
import net.java.dev.boombat.multiplayer.message.ChangePlayerNameMsg;
import net.java.dev.boombat.multiplayer.message.ChangeTeamMsg;
import net.java.dev.boombat.multiplayer.message.ChatMsg;
import net.java.dev.boombat.multiplayer.message.FireBulletMsg;
import net.java.dev.boombat.multiplayer.message.FireRocketMsg;
import net.java.dev.boombat.multiplayer.message.Message;
import net.java.dev.boombat.multiplayer.message.TankPositionMsg;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public abstract class Handler extends IoHandlerAdapter {

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		IoBuffer buff = (IoBuffer) message;

		if (buff.hasRemaining()) {
			byte id = buff.get();
			try {
				processMessage(id, session, buff);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Message processMessage(byte msgId, IoSession session, IoBuffer buff) {
		Message msg = null;

		// process common message (both for server and client)
		if (TankPositionMsg.ID == msgId) {
			msg = new TankPositionMsg();
			msg.fill(buff);
			Manager.updateTankPosition(msg);
		} else if (FireRocketMsg.ID == msgId) {
			msg = new FireRocketMsg();
			msg.fill(buff);
			Manager.receiveFireState(msg, msgId);
		} else if (FireBulletMsg.ID == msgId) {
			msg = new FireBulletMsg();
			msg.fill(buff);
			Manager.receiveFireState(msg, msgId);
		} else if (ChatMsg.ID == msgId) {
			msg = new ChatMsg();
			msg.fill(buff);
			Manager.receiveChat(msg);
		} else if (ChangePlayerNameMsg.ID == msgId) {
			msg = new ChangePlayerNameMsg();
			msg.fill(buff);
			Manager.updatePlayerName(msg);
		} else if (ChangeTeamMsg.ID == msgId) {
			msg = new ChangeTeamMsg();
			msg.fill(buff);
			Manager.updatePlayerTeam(msg);
		}

		return msg;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		
	}
}
