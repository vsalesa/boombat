package net.java.dev.boombat.game.ui.ext;

import java.util.List;

import net.java.dev.boombat.game.entity.layer.ChatList;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

public class ChatBoard {

	private int x;

	private int y;

	private boolean visible;

	private Font font;

	public ChatBoard(int x, int y, int chatSize) {
		this.x = x;
		this.y = y;
		font = ResourceManager.getFont("dejavuSuperTinyFont");
		ChatList.setMaxSize(chatSize);
	}

	public void render(Graphics g) {
		if (visible) {
			Font oldFont = g.getFont();
			g.setFont(font);
			int ypos = y;
			List<String> chats = ChatList.getChats();
			synchronized (chats) {
				for (String c : ChatList.getChats()) {
					g.setColor(Color.white);
					g.drawString(c, x, ypos);
					ypos += 15;
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

}
