package net.java.dev.boombat.game.ui.ext;

import net.java.dev.boombat.game.ui.core.Button;
import net.java.dev.boombat.game.ui.core.TextField;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;

public class ChatBox {

	private boolean visible;
	private TextField chatField;
	private Button closeButton;
	private Font font;
	private Color focusColor = new Color(107 / 255f, 18 / 255f, 12 / 255f,
			255 / 255f);

	public ChatBox(GameContainer container, int x, int y) {
		font = ResourceManager.getFont("dejavuSmallFont");

		chatField = new TextField(container, font, x, y, 400);
		chatField.setFocusBorderColor(focusColor);

		chatField.setMaxLength(48);
		
		closeButton = new Button(container, ResourceManager
				.getImage("closeNormalImage"), x + 400, y - 2);

		closeButton.setMouseOverImage(ResourceManager
				.getImage("closeOverImage"));

		closeButton.addListener(new ComponentListener() {
			
			public void componentActivated(AbstractComponent comp) {
				visible = false;
				chatField.setText("");
			}
		});
	}

	public void update(int delta) {
		if (visible) {
			updateChatBox(delta);
		}
	}

	public void render(GameContainer container, Graphics g) {
		if (visible) {
			drawChatBox(container, g);
		}
	}

	private void updateChatBox(int delta) {
		chatField.update(delta);
	}

	private void drawChatBox(GameContainer container, Graphics g) {
		g.setColor(Color.white);
		chatField.render(container, g);
		closeButton.render(container, g);
	}

	public void addCloseListener(ComponentListener listener) {
		closeButton.addListener(listener);
	}

	public void addListener(ComponentListener listener) {
		chatField.addListener(listener);
	}

	public void clearText() {
		chatField.setText("");
	}

	public String getText() {
		return chatField.getText();
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		chatField.setFocus(true);
                clearText();
	}
}
