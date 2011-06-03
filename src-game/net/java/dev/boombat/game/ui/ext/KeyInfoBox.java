package net.java.dev.boombat.game.ui.ext;

import net.java.dev.boombat.game.ui.core.Button;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.ComponentListener;

public class KeyInfoBox {

	private boolean visible;

	private int x;
	private int y;

	private Button okButton;
	private Image bgImage;

	public KeyInfoBox(GameContainer container, int x, int y) {
		this.x = x;
		this.y = y;
		bgImage = ResourceManager.getImage("keyInfoBoxImage");
		okButton = new Button(container, ResourceManager
				.getImage("okNormalImage"), x + 280, y + 440);
		okButton.setMouseOverImage(ResourceManager.getImage("okOverImage"));
	}

	public void render(GameContainer container, Graphics g) {
		if (visible) {
			drawKeyInfo(container, g);
		}
	}

	private void drawKeyInfo(GameContainer container, Graphics g) {
		bgImage.draw(x, y);
		okButton.render(container, g);
	}

	public void addOkListener(ComponentListener listener) {
		okButton.addListener(listener);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
