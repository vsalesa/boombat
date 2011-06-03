package net.java.dev.boombat.multiplayer.test.integration;

import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.multiplayer.Manager;
import net.java.dev.boombat.multiplayer.core.Client;
import net.java.dev.boombat.multiplayer.message.LoginReqMsg;

import org.apache.mina.core.session.IoSession;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class ValidateLoginTest extends BasicGame {

	boolean testCase1Result;

	boolean testCase2Result;

	public ValidateLoginTest() {
		super("ValidateLoginTest ...");
	}

	public static void main(String[] args) throws Exception {
		AppGameContainer game = new AppGameContainer(new ValidateLoginTest(),
				640, 480, false);
		game.setVerbose(false);
		game.setShowFPS(false);
		game.start();

	}

	
	public void init(GameContainer c) throws SlickException {
		ResourceManager.init();
		Manager.createGame("Host Server", "password", 20,
				"camp", Player.TEAM_COUNTER_TERRORIST, "blitz.CT");
		Manager.joinGame("localhost", "player", "password",
				Player.TEAM_TERRORIST, "panzer.T");
		IoSession session = Client.session;
		LoginReqMsg rightMessage = new LoginReqMsg();
		rightMessage.name = "player";
		rightMessage.password = "password";
		rightMessage.tank = "blitz.T";
		rightMessage.team = Player.TEAM_COUNTER_TERRORIST;
		testCase1Result = Manager.validateLogin(session, rightMessage);
		LoginReqMsg wrongMessage = new LoginReqMsg();
		wrongMessage.name = "player";
		wrongMessage.password = "wrong";
		wrongMessage.tank = "blitz.T";
		wrongMessage.team = Player.TEAM_COUNTER_TERRORIST;
		testCase2Result = Manager.validateLogin(session, wrongMessage);

	}

	
	public void update(GameContainer c, int delta) throws SlickException {
		
	}

	
	public void render(GameContainer c, Graphics g) throws SlickException {
		g.drawString("validateLogin() test ...", 20, 100);
		g.drawString("Test Case 1 Success (1-2-3-5) return : " + testCase1Result, 20, 150);
		g.drawString("Test Case 2 Fail (1-2-4-5) return : " + testCase2Result, 20, 200);
	}
}
