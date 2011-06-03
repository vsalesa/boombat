package net.java.dev.boombat.game.ui.ext;

import java.util.ArrayList;
import java.util.List;

import net.java.dev.boombat.game.ui.core.Button;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

public class ServerList implements ComponentListener {

	private int x;

	private int y;

	private Button prevButton;

	private Button nextButton;

	private Font font;

	private List<Button> rowButtons;

	private Image bgImage;

	private int startIdx;

	private int currIdx = 0;

	private List<String[]> servers;

	public ServerList(GameContainer container, int x, int y) {
		this.x = x;
		this.y = y;
		servers = new ArrayList<String[]>();
		rowButtons = new ArrayList<Button>(10);
		bgImage = ResourceManager.getImage("tableImage");
		font = ResourceManager.getFont("dejavuSmallFont");
		prevButton = new Button(container, ResourceManager.getImage(
				"arrowNormalImage").getFlippedCopy(true, false), x + 325,
				y + 310);
		nextButton = new Button(container, ResourceManager
				.getImage("arrowNormalImage"), x + 360, y + 310);

		prevButton.setMouseOverImage(ResourceManager.getImage("arrowOverImage")
				.getFlippedCopy(true, false));
		nextButton
				.setMouseOverImage(ResourceManager.getImage("arrowOverImage"));

		prevButton.addListener(new ComponentListener() {
			
			public void componentActivated(AbstractComponent comp) {
				prev();
			}
		});

		nextButton.addListener(new ComponentListener() {
			
			public void componentActivated(AbstractComponent comp) {
				next();
			}
		});

		int xb = x;
		int yb = y + 25;
		for (int i = 0; i < 10; i++) {
			Button b = new Button(container, ResourceManager
					.getImage("rowNormalImage"), xb, yb);
			b.setMouseOverImage(ResourceManager.getImage("rowOverImage"));
			b.addListener(this);
			rowButtons.add(b);
			yb += 28;
		}

	}

	private void next() {
		int newIdx = startIdx + 10;
		if (newIdx < servers.size()) {
			startIdx = newIdx;
		}
	}

	private void prev() {
		int newIdx = startIdx - 10;
		if (newIdx >= 0) {
			startIdx = newIdx;
		}
	}

	public void addServer(String ipAddress, String serverName, String players) {
		servers.add(new String[] { ipAddress, serverName, players });
	}

	public void clear() {
		servers.clear();
	}

	public String[] getSelectedServer() {
		int idx = currIdx + startIdx;
		if (idx < servers.size()) {
			return servers.get(idx);
		}

		return new String[] { "", "", "" };
	}

	public void render(GUIContext container, Graphics g) {
		bgImage.draw(x, y);

		int length = servers.size() - startIdx;
		if (length > 10) {
			length = 10;
		}
		for (Button b : rowButtons) {
			b.render(container, g);
		}

		float x0 = rowButtons.get(0).getX();
		float y0 = rowButtons.get(0).getY();
		for (int idx = startIdx; idx < (startIdx + length); idx++) {
			String[] s = servers.get(idx);
			font.drawString(x0, y0, s[0]);
			font.drawString(x0 + 140, y0, s[1]);
			font.drawString(x0 + 360, y0, s[2]);
			y0 += 28;
		}

		prevButton.render(container, g);
		nextButton.render(container, g);
	}

	
	public void componentActivated(AbstractComponent comp) {
		Button b = (Button) comp;
		currIdx = rowButtons.indexOf(b);
	}

	public void setServers(List<String[]> servers) {
		this.servers = servers;
	}
}
