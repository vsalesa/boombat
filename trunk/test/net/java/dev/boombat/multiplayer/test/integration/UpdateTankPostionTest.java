package net.java.dev.boombat.multiplayer.test.integration;

import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.multiplayer.Manager;
import net.java.dev.boombat.multiplayer.message.TankPositionMsg;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class UpdateTankPostionTest extends BasicGame {

	boolean testCase1Result;
	
	boolean testCase2Result;
	
	public UpdateTankPostionTest() {
		super("UpdateTankPositionTest");
	}

	public static void main(String[] args) throws Exception {
		AppGameContainer game = new AppGameContainer(
				new UpdateTankPostionTest(), 640, 480, false);
		game.setShowFPS(false);
		game.setVerbose(false);
		game.start();
	}

	public void init(GameContainer c) throws SlickException {
		ResourceManager.init();
		Manager.createGame("Host Server", "password", 20,
				"camp", Player.TEAM_COUNTER_TERRORIST, "blitz.CT");
		TankPositionMsg message = new TankPositionMsg();
		message.bodyAng = 0.2f;
		message.turretAng = 0.5f;
		message.playerId = (byte)0;
		message.x = 2.77f;
		message.y = 4.67f;
		testCase1Result = Manager.updateTankPosition(message);
		TankPositionMsg message2 = new TankPositionMsg();
		message2.bodyAng = 0.2f;
		message2.turretAng = 0.5f;
		message2.playerId = (byte)11;
		message2.x = 2.77f;
		message2.y = 4.67f;
		testCase2Result = Manager.updateTankPosition(message2);
	}

	public void update(GameContainer c, int d) throws SlickException {

	}

	public void render(GameContainer c, Graphics g) throws SlickException {
		g.drawString("updateTankPosition() test ...", 20, 100);
		g.drawString("Test Case 1 (1-2-3-4) Valid Player ID - Update Tank Position RETURN : " + testCase1Result, 20, 150);
		g.drawString("Test Case 2 (1-2-4) Invalid Player ID - No Update RETURN : " + testCase2Result, 20, 200);
	}
}
