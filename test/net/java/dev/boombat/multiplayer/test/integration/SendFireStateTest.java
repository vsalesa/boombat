package net.java.dev.boombat.multiplayer.test.integration;

import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.multiplayer.Manager;
import net.java.dev.boombat.multiplayer.message.FireBulletMsg;
import net.java.dev.boombat.multiplayer.message.FireRocketMsg;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class SendFireStateTest extends BasicGame{

	int testCase1Result;
	
	int testCase2Result;
	
	int testCase3Result;
	
	int testCase4Result;
	
	int testCase5Result;
	
	public SendFireStateTest() {
		super("SendFireStateTest");
	}

	@Override
	public void init(GameContainer c) throws SlickException {
		ResourceManager.init();
		Manager.createGame("Host Server", "password", 20,
				"camp", Player.TEAM_COUNTER_TERRORIST, "blitz.CT");
		Manager.joinGame("localhost", "player", "password",
				Player.TEAM_TERRORIST, "panzer.T");
		Manager.setClient(true);
		testCase1Result = Manager.sendFireState((byte) 0, FireBulletMsg.ID);
		Manager.setClient(false);
		testCase2Result = Manager.sendFireState((byte) 0, FireBulletMsg.ID);
		Manager.setClient(true);
		testCase3Result = Manager.sendFireState((byte) 0, FireRocketMsg.ID);
		Manager.setClient(false);
		testCase4Result = Manager.sendFireState((byte) 0, FireRocketMsg.ID);
		testCase5Result = Manager.sendFireState((byte) 0, (byte)33);
	}

	@Override
	public void update(GameContainer c, int d) throws SlickException {
		
	}

	public void render(GameContainer c, Graphics g) throws SlickException {
		g.drawString("sendFireState() test ...", 20, 100);
		g.drawString("Test Case 1 (1-2-3-5-11) Client send FireBulletMsg RETURN : " + testCase1Result, 20, 150);
		g.drawString("Test Case 2 (1-2-4-5-11) Server broadcast FireBulletMsg RETURN : " + testCase2Result, 20, 200);
		g.drawString("Test Case 3 (1-6-7-8-10-11) Client send FireRocketMsg RETURN : " + testCase3Result, 20, 250);
		g.drawString("Test Case 4 (1-6-7-9-10-11) Server broadcast FireRocketMsg RETURN : " + testCase4Result, 20, 300);
		g.drawString("Test Case 5 (1-6-11) No Send RETURN : " + testCase5Result, 20, 350);
	}
	
	public static void main(String[] args) throws Exception{
		AppGameContainer game = new AppGameContainer(new SendFireStateTest(), 640, 480, false);
		game.setShowFPS(false);
		game.setVerbose(false);
		game.start();
	}

}
