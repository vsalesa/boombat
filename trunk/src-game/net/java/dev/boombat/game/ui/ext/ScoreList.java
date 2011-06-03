package net.java.dev.boombat.game.ui.ext;

import java.util.List;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.entity.layer.PlayerList;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.multiplayer.core.GameSession;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class ScoreList {

	private boolean visible;

	private int x;

	private int y;

	private Image bgImage;

	private Font font;

	private int xpos;

	private int ypos;

	public ScoreList(int x, int y) {
		this.x = x;
		this.y = y;

		bgImage = ResourceManager.getImage("scoreListImage");

		font = ResourceManager.getFont("dejavuTinyFont");

	}

	public void render(Graphics g) {
		if (visible) {
			drawScoreList(g);
		}
	}

	private void drawScoreList(Graphics g) {
		Font oldFont = g.getFont();
		g.setFont(font);
		bgImage.draw(x, y);

		xpos = x + 15;
		ypos = y;

		g.setColor(Color.white);
		ypos += 5;
		g.drawString(Config.getSelectedServerName(), xpos + 55, ypos);

		g.setColor(Color.red);
		// draw terrorist win
		ypos += 35;
		g.drawString("Terrorist Team", xpos, ypos);
		g.drawString(String.valueOf(GameSession.getTerroristWinCount()),
				xpos + 440, ypos);

		ypos += 25;
		// draw terrorist players first
		drawPlayersData(g, PlayerList.getTerroristPlayers());

		g.setColor(Color.green);
		ypos += 20;
		// draw counter counter terrorist win
		g.drawString("Counter Terrorist Team", xpos, ypos);
		g.drawString(String.valueOf(GameSession.getCounterTerroristWinCount()),
				xpos + 440, ypos);

		ypos += 25;
		// then draw counter terrorist players
		drawPlayersData(g, PlayerList.getCounterTerroristPlayers());

		g.setFont(oldFont);

	}

	private void drawPlayersData(Graphics g, List<Player> players) {
		Player localPlayer = PlayerList.getLocalPlayer();

		boolean stopSearch = false;
		if (localPlayer == null) {
			stopSearch = true;
		}

		for (Player p : players) {
			g.setColor(Color.white);
			if (!stopSearch && p.getId() == localPlayer.getId()) {
				g.setColor(Color.yellow);
				stopSearch = true;
			}

			g.drawString(p.getName()+" ("+p.getId()+")", xpos, ypos);
			g.drawString(p.getStateName(), xpos + 320, ypos);
			g.drawString(String.valueOf(p.getKill()), xpos + 440, ypos);
			g.drawString(String.valueOf(p.getDead()), xpos + 490, ypos);
			ypos += 20;
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
