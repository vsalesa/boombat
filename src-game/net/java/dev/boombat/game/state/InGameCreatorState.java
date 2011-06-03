package net.java.dev.boombat.game.state;

import net.java.dev.boombat.multiplayer.Manager;
import net.java.dev.boombat.multiplayer.core.GameSession;
import net.java.dev.boombat.multiplayer.message.SessionFlagMsg;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class InGameCreatorState extends InGameState {

	public static final int ID = 9;

	public int getID() {

		return ID;
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (GameSession.isSessionActive()) {
			boolean run = GameSession.update(delta);
			if (!run) {
				GameSession.calculateScore();
				GameSession.roundNotify();
				// notifiy all players to start new round.
				Manager.sendGameState(SessionFlagMsg.NEW_ROUND);
				GameSession.newRound();
			} else {
				playGame(container, game, delta);
			}
		}
		
	}

}
