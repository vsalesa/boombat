package net.java.dev.boombat.game.ui.ext;

import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class TimerBox {

	private boolean visible;

	private int x;

	private int y;

	private Image bgImage;

	private Font font;

	private String deltaStr = "";

	public TimerBox(int x, int y) {
		this.x = x;
		this.y = y;
		bgImage = ResourceManager.getImage("timerBoxImage");
		font = ResourceManager.getFont("dejavuNormalFont");
	}

	public void update(int delta) {
		if (visible) {
			updateTimer(delta);
		}
	}

	private void updateTimer(int time) {
		int timeInSeconds = time / 1000;
		int seconds = timeInSeconds % 60;
		int minutes = (timeInSeconds - seconds) / 60;

		if (seconds < 10) {
			deltaStr = minutes + ":0" + seconds;
		} else {
			deltaStr = minutes + ":" + seconds;
		}

	}

	public void render(Graphics g) {
		if (visible) {
			drawTimer(g);
		}
	}

	private void drawTimer(Graphics g) {
		bgImage.draw(x, y);
		font.drawString(x + 30, y + 3, deltaStr);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
