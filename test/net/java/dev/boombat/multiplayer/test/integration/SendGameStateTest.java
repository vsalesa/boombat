package net.java.dev.boombat.multiplayer.test.integration;

import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.multiplayer.Manager;
import net.java.dev.boombat.multiplayer.message.SessionFlagMsg;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


public class SendGameStateTest extends BasicGame{

	int testCase1Result;
	
	int testCase2Result;
	
	int testCase3Result;
	
	int testCase4Result;
	
	int testCase5Result;
	
	
	public SendGameStateTest() {
		super("SendGameStateTest");
		
	}

	@Override
	public void init(GameContainer c) throws SlickException {
		ResourceManager.init();
		Manager.createGame("Host Server", "password", 20,
				"camp", Player.TEAM_COUNTER_TERRORIST, "blitz.CT");
		Manager.joinGame("localhost", "player", "password",
				Player.TEAM_TERRORIST, "panzer.T");
		Manager.setClient(true);
		testCase1Result = Manager.sendGameState(SessionFlagMsg.NEW_ROUND);
		testCase2Result = Manager.sendGameState(SessionFlagMsg.RESTART_ROUND);
		testCase3Result = Manager.sendGameState((byte)99);
		testCase5Result = Manager.sendGameState(SessionFlagMsg.END_SESSION);
		Manager.setClient(false);
		Manager.leaveGame();
		testCase4Result = Manager.sendGameState(SessionFlagMsg.END_SESSION);
	}

	@Override
	public void update(GameContainer c, int d) throws SlickException {
		
		
	}

	public void render(GameContainer c, Graphics g) throws SlickException {
		g.drawString("sendGameState() test ...", 20, 100);
		g.drawString("Test Case 1 (1-2-10) Broadcast New Round Notification - RETURN " + testCase1Result, 20, 150);
		g.drawString("Test Case 2 (1-3-4-10) Broadcast Restart Round Notification - RETURN " + testCase2Result, 20, 200);
		g.drawString("Test Case 3 (1-3-5-10) No Broadcast, Invalid Message ID - RETURN " + testCase3Result, 20, 250);
		g.drawString("Test Case 4 (1-3-5-6-7-9-10) No Broadcast End Session Notification Because No Client - RETURN " + testCase4Result, 20, 300);
		g.drawString("Test Case 5 (1-3-5-6-7-8-...) Broadcast End Session Notification - RETURN " + testCase5Result, 20, 350);
		
	}

	public static void main(String[] args) throws Exception {
		AppGameContainer game = new AppGameContainer(new SendGameStateTest(), 640, 480, false);
		game.setShowFPS(false);
		game.setVerbose(false);
		game.start();
	}
}
