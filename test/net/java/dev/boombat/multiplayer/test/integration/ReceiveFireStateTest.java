package net.java.dev.boombat.multiplayer.test.integration;

import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.multiplayer.Manager;
import net.java.dev.boombat.multiplayer.message.FireBulletMsg;
import net.java.dev.boombat.multiplayer.message.FireRocketMsg;
import net.java.dev.boombat.multiplayer.message.KeepAliveMsg;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class ReceiveFireStateTest extends BasicGame {
	
	boolean testCase1Result;
	boolean testCase2Result;
	boolean testCase3Result;
	boolean testCase4Result;
	boolean testCase5Result;

	public ReceiveFireStateTest() {
		super("ReceiveFireStateTest");
	}

	@Override
	public void init(GameContainer c) throws SlickException {
		ResourceManager.init();
		Manager.createGame("Host Server", "password", 20, "camp",
				Player.TEAM_COUNTER_TERRORIST, "blitz.CT");
		FireBulletMsg message = new FireBulletMsg();
		message.playerId = (byte)33;
		testCase1Result =  Manager.receiveFireState(message, FireBulletMsg.ID);
		FireBulletMsg message2 = new FireBulletMsg();
		message.playerId = (byte)0;
		testCase2Result =  Manager.receiveFireState(message2, FireBulletMsg.ID);
		FireRocketMsg message3 = new FireRocketMsg();
		message.playerId = (byte)0;
		testCase4Result =  Manager.receiveFireState(message3, FireRocketMsg.ID);
		FireRocketMsg message4 = new FireRocketMsg();
		message.playerId = (byte)33;
		testCase3Result =  Manager.receiveFireState(message4, FireRocketMsg.ID);
		KeepAliveMsg message5 = new KeepAliveMsg();
		testCase3Result =  Manager.receiveFireState(message5, (byte)99);
	}

	@Override
	public void update(GameContainer c, int d) throws SlickException {

	}

	public void render(GameContainer c, Graphics g) throws SlickException {
		 g.drawString("receiveFireState() test ...", 20, 100);
		 g.drawString("Test Case 1 (1-2-3-5-11) CALL fireBullet() - RETURN  : " + testCase1Result, 20, 150);
		 g.drawString("Test Case 2 (1-2-3-4-5-11) Invalid Player ID, No Update - RETURN  : " + testCase2Result, 20, 200);
		 g.drawString("Test Case 3 (1-2-6-11) CALL fireRocket()- RETURN  : " + testCase3Result, 20, 250);
		 g.drawString("Test Case 4 (1-2-6-7-8-10-11) Invalid Player ID, No Update - RETURN  : " + testCase4Result, 20, 300);
		 g.drawString("Test Case 5 (1-2-7-6-8-9-10-11) Invalid Message ID - RETURN  : " + testCase5Result, 20, 350);
	}
	
	public static void main(String[] args) throws Exception{
		AppGameContainer game = new AppGameContainer(new ReceiveFireStateTest(), 640, 480, false);
		game.setShowFPS(false);
		game.setVerbose(false);
		game.start();
	}

}
