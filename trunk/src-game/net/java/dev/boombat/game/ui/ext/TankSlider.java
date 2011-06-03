package net.java.dev.boombat.game.ui.ext;

import net.java.dev.boombat.game.ui.core.Button;
import net.java.dev.boombat.game.ui.core.ImageSlider;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;

public class TankSlider {

	private int x;

	private int y;

	private ImageSlider<String> tSlider;

	private ImageSlider<String> ctSlider;

	private Button prevButton;

	private Button nextButton;

	private Image bgImage;

	private Font font;

	private boolean terrorist;

	public TankSlider(GameContainer container, int x, int y) {
		this.x = x;
		this.y = y;
		bgImage = ResourceManager.getImage("tankBgImage");
		font = ResourceManager.getFont("dejavuSmallFont");
		prevButton = new Button(container, ResourceManager.getImage(
				"arrowNormalImage").getFlippedCopy(true, false), x + 80,
				y + 135);
		nextButton = new Button(container, ResourceManager
				.getImage("arrowNormalImage"), x + 130, y + 135);

		prevButton.setMouseOverImage(ResourceManager.getImage("arrowOverImage")
				.getFlippedCopy(true, false));
		nextButton
				.setMouseOverImage(ResourceManager.getImage("arrowOverImage"));

		prevButton.addListener(new ComponentListener() {
			
			public void componentActivated(AbstractComponent comp) {
				if (terrorist) {
					tSlider.previous();
				} else {
					ctSlider.previous();
				}
			}
		});

		nextButton.addListener(new ComponentListener() {
			
			public void componentActivated(AbstractComponent comp) {
				if (terrorist) {
					tSlider.next();
				} else {
					ctSlider.next();
				}
			}
		});

		tSlider = new ImageSlider<String>(container, x + 20, y + 20, 200, 100);

		for (String key : ResourceManager.getTankKeys()) {
			if (key.endsWith(".T")) {
				tSlider.addImageInfo(ResourceManager.getTankImages(key), key);
			}
		}

		tSlider.next();

		ctSlider = new ImageSlider<String>(container, x + 20, y + 20, 200, 100);

		for (String key : ResourceManager.getTankKeys()) {
			if (key.endsWith(".CT")) {
				ctSlider.addImageInfo(ResourceManager.getTankImages(key), key);
			}
		}

		ctSlider.next();
	}

	public void update(int delta) {
		if (terrorist) {
			tSlider.update(delta);
		} else {
			ctSlider.update(delta);
		}
	}

	public void render(GUIContext container, Graphics g) {
		bgImage.draw(x, y);
		if (terrorist) {
			tSlider.render(container, g);
		} else {
			ctSlider.render(container, g);
		}
		String tankKey = getSelectedTank();
		if (tankKey != null) {
			font.drawString(x + 20, y + 110, ResourceManager
					.getTankDescription(tankKey));
		}
		prevButton.render(container, g);
		nextButton.render(container, g);

	}

	public void next() {
		if (terrorist) {
			tSlider.next();
		} else {
			ctSlider.next();
		}
	}

	public void previous() {
		if (terrorist) {
			tSlider.previous();
		} else {
			ctSlider.previous();
		}
	}

	public String getSelectedTank() {
		if (terrorist) {
			return tSlider.getSelectedValue();
		} else {
			return ctSlider.getSelectedValue();
		}
	}

	public void setTerrorist(boolean terrorist) {
		this.terrorist = terrorist;
	}

}
