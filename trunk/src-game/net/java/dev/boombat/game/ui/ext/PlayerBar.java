package net.java.dev.boombat.game.ui.ext;

import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.entity.Tank;
import net.java.dev.boombat.game.entity.layer.PlayerList;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class PlayerBar {

	private boolean visible;

	private int x;

	private int y;

	private Image bgImage;

	private Image redAngle;

	private Image greenAngle;

	private Font font;

	private String lifeStr = "0";

	private String ammoStr = "0";

	private String playerName = "";

	private String teamName = "";

	private float bodyAngle = 0;

	private float turretAngle = 0;

	public PlayerBar(int x, int y) {
		this.x = x;
		this.y = y;

		font = ResourceManager.getFont("dejavuTinyFont");

		bgImage = ResourceManager.getImage("playerBarImage");

		redAngle = ResourceManager.getImage("redDotCompassImage");

		greenAngle = ResourceManager.getImage("greenDotCompassImage");
	}

	public void update(int delta) {
		if (visible) {
			updatePlayerBar(delta);
		}
	}

	private void updatePlayerBar(int delta) {
		Player localPlayer = PlayerList.getLocalPlayer();
		Tank localTank = localPlayer.getTank();
		int life = 0;
		int ammo = 0;
		lifeStr = "N/A";
		ammoStr = "N/A";

		if (localPlayer != null && localTank != null) {
			life = localPlayer.getLife();
			ammo = localTank.getCurrentRocketAmmo();
			playerName = localPlayer.getName();
			teamName = localPlayer.getTeamName();
			bodyAngle = localTank.getBodyAngle();
			turretAngle = localTank.getTurretAngle();
			
			if (Player.STATE_LIFE == localPlayer.getState()) {
				lifeStr = String.valueOf(life);
				ammoStr = String.valueOf(ammo);
			}
		}

	}

	public void render(Graphics g) {
		if (visible) {
			drawPlayerBar(g);
		}
	}

	private void drawPlayerBar(Graphics g) {
		bgImage.draw(x, y);
		float xpos = x + 167 + 42.5f;
		float ypos = y + 2.5f + +42.5f;
		g.rotate(xpos, ypos, turretAngle);
		greenAngle.draw(xpos - 42.5f, ypos - 42.5f);
		g.rotate(xpos, ypos, -turretAngle);
		g.rotate(xpos, ypos, bodyAngle);
		redAngle.draw(xpos - 42.5f, ypos - 42.5f);
		g.rotate(xpos, ypos, -bodyAngle);
		font.drawString(x + 40, y + 65, lifeStr);
		font.drawString(x + 120, y + 65, ammoStr);
		font.drawString(x + 5, y + 92, playerName + " | " + teamName);

	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
