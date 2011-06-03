package net.java.dev.boombat.multiplayer.core;

import net.java.dev.boombat.multiplayer.Manager;
import net.java.dev.boombat.multiplayer.message.AddNewPlayerMsg;
import net.java.dev.boombat.multiplayer.message.InvalidLoginMsg;
import net.java.dev.boombat.multiplayer.message.Message;
import net.java.dev.boombat.multiplayer.message.RemovePlayerMsg;
import net.java.dev.boombat.multiplayer.message.SessionFlagMsg;
import net.java.dev.boombat.multiplayer.message.SwitchMapMsg;
import net.java.dev.boombat.multiplayer.message.ValidLoginMsg;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

public class ClientHandler extends Handler {
	
	@Override
	public Message processMessage(byte msgId, IoSession session, IoBuffer buff) {
		Message msg = super.processMessage(msgId, session, buff);
		if (msg != null) {
			return msg;
		}

		// process client message only
		if (AddNewPlayerMsg.ID == msgId) {
			msg = new AddNewPlayerMsg();
			msg.fill(buff);
			Manager.addNewPlayer(msg);
		} else if (ValidLoginMsg.ID == msgId) {
			msg = new ValidLoginMsg();
			msg.fill(buff);
			Manager.processValidLogin(msg);
		} else if (InvalidLoginMsg.ID == msgId) {
			msg = new InvalidLoginMsg();
			msg.fill(buff);
			Manager.processInvalidLogin();
		} else if (SessionFlagMsg.ID == msgId) {
			msg = new SessionFlagMsg();
			msg.fill(buff);
			Manager.updateGameState(msg);
		} else if (RemovePlayerMsg.ID == msgId) {
			msg = new RemovePlayerMsg();
			msg.fill(buff);
			Manager.removePlayer(msg);
		} else if (SwitchMapMsg.ID == msgId) {
			msg = new SwitchMapMsg();
			msg.fill(buff);
			Manager.updateGameMap(msg);
		}

		return msg;
	}
}
