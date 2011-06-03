/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.test;

import net.java.dev.boombat.game.ui.ext.ServerList;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * 
 * @author objectworks
 */
public class ServerListTest extends BasicGame {

	private ServerList serverList;
	private String info;

	public static void main(String... args) throws SlickException {
		AppGameContainer game = new AppGameContainer(new ServerListTest(
				"ServerList Test"), 800, 600, false);
		game.start();
	}

	public ServerListTest(String name) {
		super(name);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		ResourceManager.init();
		serverList = new ServerList(container, 390, 160);
		for (int i = 0; i < 8; i++) {
			serverList.addServer("172.17.63.12" + i, "Electro Server-" + i,
					"20");
			serverList.addServer("202.162.208.10" + i, "UB DNS Server-" + i,
					"3");
			serverList.addServer("202.162.208.9" + i, "Another UB DNS-" + i,
					"8");
		}
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {

	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		ResourceManager.getImage("subMenuBgImage").draw(0, 0,
				container.getWidth(), container.getHeight());
		serverList.render(container, g);
		g.drawString("IP Address " + info, 400, 480);
	}
}
