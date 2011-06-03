package net.java.dev.boombat.game.ui.ext;

import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class LoadingArenaBox {

	private boolean visible;

	private int x;

	private int y;

	private Image bgImage;

	public LoadingArenaBox(int x, int y) {
		this.x = x;
		this.y = y;
		bgImage = ResourceManager.getImage("loadingArenaImage");
	}

	public void render(GameContainer container, Graphics g) {
		if (visible) {
			drawLoadingArenaBox(container, g);
		}
	}

	private void drawLoadingArenaBox(GameContainer container, Graphics g) {
		bgImage.draw(x, y, container.getWidth(), container.getHeight());
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
