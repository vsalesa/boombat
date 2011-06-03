package net.java.dev.boombat.multiplayer.test.integration;

import java.io.IOException;
import java.net.InetSocketAddress;

import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.multiplayer.Manager;
import net.java.dev.boombat.multiplayer.core.Host;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class JoinGameTest extends BasicGame {

	private boolean testCase1Result;

	private boolean testCase2Result;

	private boolean testCase4Result;

	private boolean testCase5Result;

	public JoinGameTest() {
		super("JoinGameTest");

	}

	
	public void init(GameContainer arg0) throws SlickException {
		ResourceManager.init();
		Manager.createGame("Host Server", "password", 20, "camp",
				Player.TEAM_COUNTER_TERRORIST, "blitz.CT");
		testCase1Result = Manager.joinGame("unknown", "player", "password",
				Player.TEAM_TERRORIST, "panzer.T");
		testCase5Result = Manager.joinGame("localhost", "player", "pass",
				Player.TEAM_TERRORIST, "panzer.T");
		testCase4Result = Manager.joinGame("localhost", "player", "password",
				Player.TEAM_TERRORIST, "panzer.T");
		// deactivate host or server ...
		Host.destroy();
		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
		acceptor.setHandler(new IoHandlerAdapter());
		try {
			acceptor.bind(new InetSocketAddress(3003));
		} catch (IOException e1) {
			
		}
		testCase2Result = Manager.joinGame("localhost", "player", "password",
				Player.TEAM_TERRORIST, "panzer.T");
		
	}

	
	public void update(GameContainer c, int d) throws SlickException {
		
	}

	
	public void render(GameContainer c, Graphics g) throws SlickException {
		g.drawString("joinGame() test ...", 20, 150);
		g.drawString("Test Case 1 - unresolved IP address (1-2-12) : return " + testCase1Result, 20, 200);
		g.drawString("Test Case 2 - wait timeout (1-2-3-4-12) return " + testCase2Result, 20, 250);
		g.drawString("Test Case 3 - still waiting normally (1-2-3-4-5-10-11-4-...) no return ", 20, 300);
		g.drawString("Test Case 4 - success (1-2-3-4-5-6-7-9-10-11-4-...) return " + testCase4Result, 20, 350);
		g.drawString("Test Case 5 - wrong password (1-2-3-4-5-6-8-9-10-11-4-...) return " + testCase5Result, 20, 400);
	}

	
	public static void main(String[] args)throws Exception {
		AppGameContainer game = new AppGameContainer(new JoinGameTest(), 640, 480, false);
		game.setShowFPS(false);
		game.setVerbose(false);
		game.start();
	}
}
