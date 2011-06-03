package net.java.dev.boombat.game.ui.ext;

import java.util.List;

import net.java.dev.boombat.game.entity.layer.SystemMessageList;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

public class SystemMessageBoard {
	private int y;

	private boolean visible;

	private Font font;
	private int unitWidth;

	public SystemMessageBoard(int y, int chatSize) {
		this.y = y;
		font = ResourceManager.getFont("dejavuSuperTinyFont");
		SystemMessageList.setMaxSize(chatSize);
		unitWidth = font.getWidth("_");
	}

	public void render(Graphics g) {
		if (visible) {
			Font oldFont = g.getFont();
			g.setFont(font);
			int ypos = y;
			List<String> messages = SystemMessageList.getMessages();
			synchronized (messages) {
				for (String s : messages) {
					g.setColor(Color.white);
					g.drawString(s, 795 - (s.length() * unitWidth), ypos);
					ypos += 13;
				}
			}
			g.setFont(oldFont);
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setY(int y) {
		this.y = y;
	}

}
