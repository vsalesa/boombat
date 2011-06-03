/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.dev.boombat.game.ui.fx;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

/**
 * 
 * @author Admin
 */
public class ImageFxFadeInOut extends AbstractComponent {
	private int x;
	private int y;
	private int width;
	private int heigth;
	private ImageFxFadeIn fxFadeIn;
	private ImageFxFadeOut fxFadeOut;

	public ImageFxFadeInOut(GUIContext container, Image image, int x, int y,
			int width, int height, int startAt, int longUpdate) {
		super(container);
		fxFadeIn = new ImageFxFadeIn(container, image, x, y, width, height,
				startAt, longUpdate - startAt);
		fxFadeOut = new ImageFxFadeOut(container, image, x, y, width, height,
				startAt + (longUpdate - startAt), longUpdate);
		this.x = x;
		this.y = y;
		this.width = width;
		this.heigth = height;
	}

	@Override
	public void render(GUIContext container, Graphics g) {
		fxFadeIn.render(container, g);
		fxFadeOut.render(container, g);
	}

	public void update(int delta) {
		fxFadeIn.setLocation(x, y);
		fxFadeOut.setLocation(x, y);
		fxFadeIn.update(delta);
		fxFadeOut.update(delta);
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return heigth;
	}
}
