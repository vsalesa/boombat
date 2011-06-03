package net.java.dev.boombat.multiplayer.test.integration;

import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.entity.layer.PlayerList;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.multiplayer.Manager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class SendTankPositionTest extends BasicGame{

	private boolean testCase1Result;
	
	private boolean testCase2Result;
	
	public SendTankPositionTest() {
		super("SendTankPositionTest");
	}

	@Override
	public void init(GameContainer c) throws SlickException {
		ResourceManager.init();
		Manager.createGame("Host Server", "password", 20,
				"camp", Player.TEAM_COUNTER_TERRORIST, "blitz.CT");
		Manager.joinGame("localhost", "player", "password",
				Player.TEAM_TERRORIST, "panzer.T");
		Manager.setClient(true);
		testCase1Result = Manager.sendTankPosition(PlayerList.getLocalPlayer());
		Manager.setClient(false);
		testCase2Result = Manager.sendTankPosition(PlayerList.getLocalPlayer());
		
	}

	@Override
	public void update(GameContainer c, int d) throws SlickException {
		
		
	}

	public void render(GameContainer c, Graphics g) throws SlickException {
		g.drawString("sendTankPosition() test ...", 20, 100);
		g.drawString("Test Case 1 Client Sending ... (1-2-3-5) RETURN : " + testCase1Result, 20, 150);
		g.drawString("Test Case 2 Server Broadcasting ... (1-2-4-5) RETURN : " + testCase2Result, 20, 200);
	}
	
	public static void main(String[] args)throws Exception {
		AppGameContainer game = new AppGameContainer(new SendTankPositionTest(), 640, 480, false);
		game.setShowFPS(false);
		game.setVerbose(false);
		game.start();
	}

}
