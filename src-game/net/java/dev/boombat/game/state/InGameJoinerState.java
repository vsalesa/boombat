package net.java.dev.boombat.game.state;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.multiplayer.core.GameSession;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class InGameJoinerState extends InGameState {

	public static final int ID = 10;

	
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);
	}

	
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (GameSession.isSessionActive()) {
			GameSession.update(delta);
			playGame(container, game, delta);
			// siwtch map
			if (!Config.getSelectedGameMap().getKey().equals(
					Config.getMapKeyName())) {
				Config.setSelectedGameMap(Config.getMapKeyName());
			}
		} else {
			stateBasedGame.enterState(JoinGameMenuState.ID,
					new FadeOutTransition(Color.black), new FadeInTransition(
							Color.black));
		}
		
	}

	
	public int getID() {

		return ID;
	}

}
