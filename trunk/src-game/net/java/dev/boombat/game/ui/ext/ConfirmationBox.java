package net.java.dev.boombat.game.ui.ext;

import net.java.dev.boombat.game.ui.core.Button;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.ComponentListener;

public class ConfirmationBox {

	private boolean visible;

	private int x;

	private int y;

	private Button okButton;

	private Button cancelButton;

	private Image bgImage;

	private Font font;

	private String confirmation;

	public ConfirmationBox(GameContainer container, int x, int y,
			String confirmation) {
		this.x = x;
		this.y = y;
		this.confirmation = confirmation;

		bgImage = ResourceManager.getImage("confirmationBoxImage");

		font = ResourceManager.getFont("dejavuNormalFont");

		okButton = new Button(container, ResourceManager
				.getImage("yesNormalImage"), x + 110, y + 150);
		cancelButton = new Button(container, ResourceManager
				.getImage("noNormalImage"), x + 220, y + 150);

		okButton.setMouseOverImage(ResourceManager.getImage("yesOverImage"));
		cancelButton.setMouseOverImage(ResourceManager.getImage("noOverImage"));

	}

	public void render(GameContainer container, Graphics g) {
		if (visible) {
			drawConfirmationBox(container, g);
		}
	}

	private void drawConfirmationBox(GameContainer container, Graphics g) {
		bgImage.draw(x, y);

		g.setColor(Color.white);
		// draw confirmation
		font.drawString(x + 130, y + 80, confirmation);

		okButton.render(container, g);
		cancelButton.render(container, g);
	}

	public void addOkListener(ComponentListener listener) {
		okButton.addListener(listener);
	}

	public void addCancelListener(ComponentListener listener) {
		cancelButton.addListener(listener);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}

}
