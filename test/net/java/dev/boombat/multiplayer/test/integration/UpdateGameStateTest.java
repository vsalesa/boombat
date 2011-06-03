package net.java.dev.boombat.multiplayer.test.integration;

import java.util.Map;

import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.multiplayer.Manager;
import net.java.dev.boombat.multiplayer.core.Util;
import net.java.dev.boombat.multiplayer.message.SessionFlagMsg;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class UpdateGameStateTest extends BasicGame {

	int testCase1Result;

	int testCase2Result;

	int testCase3Result;

	int testCase4Result;

	public UpdateGameStateTest() {
		super("UpdateGameStateTest");
	}

	@Override
	public void init(GameContainer c) throws SlickException {
		ResourceManager.init();
		Manager.createGame("Host Server", "password", 20, "camp",
				Player.TEAM_COUNTER_TERRORIST, "blitz.CT");
		Map<Byte, float[]> pos = Util.applyRandomTankPosition();
		SessionFlagMsg msg = new SessionFlagMsg();
		msg.flags = SessionFlagMsg.NEW_ROUND;
		msg.pos = pos;
		testCase1Result = Manager.updateGameState(msg);
		Map<Byte, float[]> pos2 = Util.applyRandomTankPosition();
		SessionFlagMsg msg2 = new SessionFlagMsg();
		msg2.flags = SessionFlagMsg.RESTART_ROUND;
		msg2.pos = pos2;
		testCase2Result = Manager.updateGameState(msg2);
		SessionFlagMsg msg3 = new SessionFlagMsg();
		msg3.flags = SessionFlagMsg.END_SESSION;
		testCase3Result = Manager.updateGameState(msg3);
		SessionFlagMsg msg4 = new SessionFlagMsg();
		msg4.flags = (byte) 33;
		testCase4Result = Manager.updateGameState(msg4);
	}

	@Override
	public void update(GameContainer c, int d) throws SlickException {

	}

	public void render(GameContainer c, Graphics g) throws SlickException {
		g.drawString("updateGameState() test ...", 20, 100);
		g.drawString("Test Case 1 (1-2-3-8) CALL newRound() - RETURN "
				+ testCase1Result, 20, 150);
		g.drawString("Test Case 2 (1-2-4-5-8) CALL restartRound() - RETURN "
				+ testCase2Result, 20, 200);
		g.drawString("Test Case 3 (1-2-4-6-7-8) CALL endSession() - RETURN "
				+ testCase3Result, 20, 250);
		g.drawString("Test Case 4 (1-2-4-6-8) No Update - RETURN "
				+ testCase4Result, 20, 300);
	}

	public static void main(String[] args) throws Exception {
		AppGameContainer game = new AppGameContainer(new UpdateGameStateTest(),
				640, 480, false);
		game.setShowFPS(false);
		game.setVerbose(false);
		game.start();
	}
}
