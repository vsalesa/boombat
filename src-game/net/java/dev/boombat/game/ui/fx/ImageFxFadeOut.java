/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.dev.boombat.game.ui.fx;

import mdes.slick.animation.Easing;
import mdes.slick.animation.Fx;
import mdes.slick.animation.Timeline;
import mdes.slick.animation.entity.AlphaEntity;
import mdes.slick.animation.fx.AlphaFx;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

/**
 * 
 * @author Admin
 */
public class ImageFxFadeOut extends AbstractComponent implements AlphaEntity {
	private int x;
	private int y;
	private int startAt;
	private int longUpdate;
	private int counter;
	private Image image;
	private Rectangle area;
	private Fx fx;
	private Timeline timeline;
	private Color filter = new Color(Color.white);

	public ImageFxFadeOut(GUIContext container, Image image, int x, int y,
			int width, int height, int startAt, int longUpdate) {
		super(container);
		this.x = x;
		this.y = y;
		this.startAt = startAt;
		this.longUpdate = longUpdate;
		this.image = image;
		setLocation(x, y);
		area = new Rectangle(x, y, width, height);
		timeline = new Timeline();
		Easing ease = Easing.CUBIC_IN;
		fx = new AlphaFx(2000, this, 1f, .0f, ease);
		timeline.add(fx);
	}

	private void fxFadeOut() {
		timeline.rewind();
		timeline.setActive(true);
	}

	
	public void render(GUIContext container, Graphics g) {
		if (counter >= startAt && counter < startAt + longUpdate) {
			image.draw(x, y, filter);
		}
	}

	public void update(int delta) {
		if (counter == startAt) {
			fxFadeOut();
		} else if (counter > startAt && counter < startAt + longUpdate) {
			timeline.update(delta);
		}
		counter += delta;

	}

	
	public void setLocation(int xPos, int yPos) {
		if (area != null) {
			area.setX(xPos);
			area.setY(yPos);
		}
	}

	
	public int getX() {
		return (int) area.getX();
	}

	
	public int getY() {
		return (int) area.getY();
	}

	
	public int getWidth() {
		return (int) (area.getMaxX() - area.getX());
	}

	
	public int getHeight() {
		return (int) (area.getMaxY() - area.getY());
	}

	
	public float getAlpha() {
		// TODO Auto-generated method stub
		return filter.a;
	}

	
	public void setAlpha(float alpha) {
		// TODO Auto-generated method stub
		filter.a = alpha;
	}
}
