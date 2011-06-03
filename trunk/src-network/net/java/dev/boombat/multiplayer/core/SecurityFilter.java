package net.java.dev.boombat.multiplayer.core;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.layer.PlayerList;
import net.java.dev.boombat.multiplayer.message.InvalidLoginMsg;
import net.java.dev.boombat.multiplayer.message.LoginReqMsg;
import net.java.dev.boombat.multiplayer.message.ValidLoginMsg;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

/**
 * A filter to protecting game session from malicious activities.
 * 
 * @author Eriq Adams (eriq.adams@gmail.com)
 */
public class SecurityFilter extends IoFilterAdapter {

	public static final String VALID_KEY = "valid";

	@Override
	public void sessionCreated(NextFilter nextFilter, IoSession session)
			throws Exception {
	
		int playerNum = PlayerList.getRemotePlayers().size() + 1;
		if (playerNum < Config.getMaxPlayer()) {
			// initialize attribute session valid to false
			session.setAttribute(VALID_KEY, false);
			nextFilter.sessionCreated(session);
		}
	}

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception {
		// check if message type equal LoginReqMsg or session valid don't block.
		// otherwise vice versa.
		IoBuffer buff = (IoBuffer) message;
		if (buff.hasRemaining()) {
			byte msgId = buff.get();
			buff.rewind();
			boolean validSess = (Boolean) session.getAttribute(VALID_KEY);
			if (validSess || LoginReqMsg.ID == msgId) {
				nextFilter.messageReceived(session, message);
			}
		}

	}

	@Override
	public void filterWrite(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		// check if message type equal ValidLoginMsg or InvalidLoginMsg or
		// session valid don't block.
		// otherwise vice versa.
		Object message = writeRequest.getMessage();
		IoBuffer buff = (IoBuffer) message;
		if (buff.hasRemaining()) {
			byte msgId = buff.get();
			buff.rewind();
			boolean validSess = (Boolean) session.getAttribute(VALID_KEY);
			if (validSess || ValidLoginMsg.ID == msgId
					|| InvalidLoginMsg.ID == msgId) {
				nextFilter.filterWrite(session, writeRequest);
			}
		}
	}
}
