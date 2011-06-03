package net.java.dev.boombat.multiplayer.test.integration;

import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.multiplayer.Manager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class CreateGameTest extends BasicGame {

	private boolean testCase1Result;

	private boolean testCase2Result;

	public CreateGameTest() {
		super("CreateGameTest");
	}

	
	public void init(GameContainer c) throws SlickException {
		ResourceManager.init();
		testCase1Result = Manager.createGame("Host Server", "password", 20,
				"camp", Player.TEAM_COUNTER_TERRORIST, "blitz.CT");
		testCase2Result = Manager.createGame("Host Server", "password", 20,
				"camp", Player.TEAM_COUNTER_TERRORIST, "blitz.CT");
	}

	
	public void update(GameContainer c, int delta) throws SlickException {

	}

	
	public void render(GameContainer c, Graphics g) throws SlickException {
		g.drawString("createGame() test ...", 20, 100);
		g.drawString(
				"Test Case 1 Success (1-2-3-5) : CALL createGame() RETURN "
						+ testCase1Result, 20, 150);
		g.drawString(
				"Test Case 2 Fail (1-2-4-5) : CALL createGame() RETURN "
						+ testCase2Result, 20, 200);
	}

	public static void main(String[] args) throws Exception {
		AppGameContainer test = new AppGameContainer(new CreateGameTest(), 640,
				480, false);
		test.setShowFPS(false);
		test.setVerbose(false);
		test.start();
	}
}
