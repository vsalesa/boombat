package net.java.dev.boombat.game.ui.ext;

import net.java.dev.boombat.game.ui.core.Button;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.ComponentListener;

public class ChangeMapBox {
	private boolean visible;

	private int x;

	private int y;

	private GameMapSlider slider;

	private Image bgImage;

	private Button okButton;

	private Button cancelButton;

	public ChangeMapBox(GameContainer container, int x, int y) {
		this.x = x;
		this.y = y;
		bgImage = ResourceManager.getImage("changeMapBoxImage");
		slider = new GameMapSlider(container, x + 80, y + 30);

		okButton = new Button(container, ResourceManager
				.getImage("okNormalImage"), x + 140, y + 320);
		cancelButton = new Button(container, ResourceManager
				.getImage("cancelNormalImage"), x + 260, y + 320);

		okButton.setMouseOverImage(ResourceManager.getImage("okOverImage"));
		cancelButton.setMouseOverImage(ResourceManager
				.getImage("cancelOverImage"));
	}

	public void update(int delta) {
		if (visible) {
			updateChangeMapBox(delta);
		}
	}

	private void updateChangeMapBox(int delta) {
		slider.update(delta);
	}

	public void render(GameContainer container, Graphics g) {
		if (visible) {
			drawChangeMapBox(container, g);
		}
	}

	private void drawChangeMapBox(GameContainer container, Graphics g) {
		bgImage.draw(x, y);
		slider.render(container, g);
		okButton.render(container, g);
		cancelButton.render(container, g);
	}

	public void addOkListener(ComponentListener listener) {
		okButton.addListener(listener);
	}

	public void addCancelListener(ComponentListener listener) {
		cancelButton.addListener(listener);
	}

	public String getSelectedMap() {
		return slider.getSelectedMap();
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
