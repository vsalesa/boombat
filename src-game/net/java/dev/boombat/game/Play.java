package net.java.dev.boombat.game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 * 
 * @author objectworks
 */
public class Play {

	public static void main(String[] args) throws SlickException {
		AppGameContainer game = new AppGameContainer(new BoombatGame(
				"Boombat - EM Games"));
		BoombatGame.loadOptions(game);
		game.start();
	}
}
