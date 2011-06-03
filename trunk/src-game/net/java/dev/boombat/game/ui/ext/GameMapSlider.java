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

public class GameMapSlider {

	private int x;

	private int y;

	private ImageSlider<String> slider;

	private Button prevButton;

	private Button nextButton;

	private Image bgImage;

	private Font font;

	public GameMapSlider(GameContainer container, int x, int y) {
		this.x = x;
		this.y = y;
		bgImage = ResourceManager.getImage("mapBgImage");
		font = ResourceManager.getFont("dejavuSmallFont");
		prevButton = new Button(container, ResourceManager.getImage(
				"arrowNormalImage").getFlippedCopy(true, false), x + 140,
				y + 255);
		nextButton = new Button(container, ResourceManager
				.getImage("arrowNormalImage"), x + 180, y + 255);

		prevButton.setMouseOverImage(ResourceManager.getImage("arrowOverImage")
				.getFlippedCopy(true, false));
		nextButton
				.setMouseOverImage(ResourceManager.getImage("arrowOverImage"));

		prevButton.addListener(new ComponentListener() {
			
			public void componentActivated(AbstractComponent comp) {
				slider.previous();
			}
		});

		nextButton.addListener(new ComponentListener() {
			
			public void componentActivated(AbstractComponent comp) {
				slider.next();
			}
		});

		slider = new ImageSlider<String>(container, x + 20, y + 20, 100, 100);

		for (String key : ResourceManager.getMapKeys()) {
			slider.addImageInfo(ResourceManager.getMapImages(key), key);
		}

		slider.next();
	}

	public void update(int delta) {
		slider.update(delta);
	}

	public void render(GUIContext container, Graphics g) {
		bgImage.draw(x, y);
		slider.render(container, g);
		String mapKey = slider.getSelectedValue();
		if (mapKey != null) {
			font.drawString(x + 20, y + 225, ResourceManager
					.getMapDescription(mapKey));
		}
		prevButton.render(container, g);
		nextButton.render(container, g);

	}

	public void next() {
		slider.next();
	}

	public void previous() {
		slider.previous();
	}

	public String getSelectedMap() {
		return slider.getSelectedValue();
	}

}
