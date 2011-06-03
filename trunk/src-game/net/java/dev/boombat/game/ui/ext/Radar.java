package net.java.dev.boombat.game.ui.ext;

import java.util.ArrayList;
import java.util.List;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.GameMap;
import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.entity.Tank;
import net.java.dev.boombat.game.entity.layer.PlayerList;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Radar {

	private int x;

	private int y;

	private boolean visible;

	private Image bgImage;

	public static final int LOCAL_PLAYER = 0;

	public static final int TERRORIST_PLAYER = Player.TEAM_TERRORIST;

	public static final int COUNTER_TERRORIST_PLAYER = Player.TEAM_COUNTER_TERRORIST;

	private List<float[]> data = new ArrayList<float[]>();

	public Radar(int x, int y) {
		this.x = x;
		this.y = y;
		bgImage = ResourceManager.getImage("radarImage");
	}

	public void render(Graphics g) {
		if (visible) {
			drawRadar(g);
		}
	}

	public void update(int delta) {
		if (visible) {
			updateRadar(delta);
		}
	}

	private void updateRadar(int delta) {
		Player localPlayer = PlayerList.getLocalPlayer();
		GameMap map = Config.getSelectedGameMap();
		data.clear();
		if (localPlayer != null
				&& localPlayer.getState() != Player.STATE_SPECTATOR) {
			Tank tank = localPlayer.getTank();
			if (tank != null) {
				float xpos = x + tank.getX() * map.getUnitRadarTile();
				float ypos = y + tank.getY() * map.getUnitRadarTile();
				data.add(new float[] { xpos, ypos, LOCAL_PLAYER });
			}
		}

		for (Player remotePlayer : PlayerList.getRemotePlayers()) {
			Tank tank = remotePlayer.getTank();
			if (remotePlayer.getState() != Player.STATE_SPECTATOR
					&& tank != null) {
				float xpos = x + tank.getX() * map.getUnitRadarTile();
				float ypos = y + tank.getY() * map.getUnitRadarTile();
				data.add(new float[] { xpos, ypos, remotePlayer.getTeam() });
			}
		}
	}

	private void drawRadar(Graphics g) {
		bgImage.draw(x, y);

		// get color
		Color oldColor = g.getColor();
		for (float[] d : data) {
			drawPoint(g, d[0], d[1], d[2]);
		}
		// reset color
		g.setColor(oldColor);
	}

	private void drawPoint(Graphics g, float xpoint, float ypoint, float team) {
		// draw point representing player

		if (Player.TEAM_COUNTER_TERRORIST == (int) team) {
			g.setColor(Color.green);
		} else if (Player.TEAM_TERRORIST == (int) team) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.white);
		}

		g.fillRect(xpoint, ypoint, 2, 2);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
