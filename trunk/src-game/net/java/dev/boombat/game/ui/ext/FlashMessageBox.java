package net.java.dev.boombat.game.ui.ext;

import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

public class FlashMessageBox {

	private String message;
	private int showTime;
	private Font smallFont;
	private boolean visible;
	private int unitWidth;
	private int unitHeight;
	private float x;
	private float y;
	private static FlashMessageBox instance = new FlashMessageBox();

	public static FlashMessageBox instance() {
		return instance;
	}

	private FlashMessageBox() {
		smallFont = ResourceManager.getFont("dejavuSmallFont");
		unitWidth = smallFont.getWidth("_");
		unitHeight = smallFont.getHeight("|");
	}

	public void update(int delta) {
		if (showTime < 0) {
			visible = false;
		}
		showTime -= delta;
	}

	public void render(Graphics g) {
		if (visible) {
			g.setColor(Color.white);
			g.setFont(smallFont);
			g.drawString(message, x, y);
		}
	}

	public void showFlashMessage(int showTime, String message) {
		this.message = message;
		this.showTime = showTime;
		visible = true;
		int pixelLength = message.length() * unitWidth;
		x = 400 - (pixelLength / 2);
		y = 240 - (unitHeight / 2);
	}

	public void hideFlashMessage() {
		this.visible = false;
	}
}
