package net.java.dev.boombat.multiplayer.core;

import net.java.dev.boombat.multiplayer.Manager;
import net.java.dev.boombat.multiplayer.message.LeaveGameReqMsg;
import net.java.dev.boombat.multiplayer.message.LoginReqMsg;
import net.java.dev.boombat.multiplayer.message.Message;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

public class HostHandler extends Handler {

	
	@Override
	public Message processMessage(byte msgId, IoSession session, IoBuffer buff) {
		Message msg = super.processMessage(msgId, session, buff);

		if (msg != null) {
			// relay message to other clients
			if (msg.isRelay()) {
				buff.rewind();
				Host.relay(buff, session);
			}
		} else {
			// process incoming host message
			if (LoginReqMsg.ID == msgId) {
				msg = new LoginReqMsg();
				msg.fill(buff);
				Manager.validateLogin(session, msg);
			} else if (LeaveGameReqMsg.ID == msgId) {
				msg = new LeaveGameReqMsg();
				msg.fill(buff);
				Manager.processLeaveGameRequest(session, msg);
			}
		}

		return msg;
	}

}
