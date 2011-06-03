package net.java.dev.boombat.game.ui.core;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

public class SoundBar extends AbstractComponent {

	private float value;

	private Color fillColor;

	private Color blankColor;

	private int x;

	private int y;

	private int widthGrowth;

	private int heightGrowth;

	private int space;

	private int ws;

	private int allWidth;

	private int allHeight;

	float procent;

	boolean press;

	private int currentX;

	/** The shape defining the area */
	private Shape area;

	public SoundBar(GUIContext container, int x, int y, Color fill,
			Color blank, int w, int h, int s) {
		super(container);
		this.x = x;
		this.y = y;
		setLocation(x, y);
		fillColor = fill;
		blankColor = blank;
		widthGrowth = w;
		heightGrowth = h;
		space = s;
		ws = (widthGrowth + space);
		allWidth = (ws * 9) + widthGrowth;
		allHeight = heightGrowth * 10;
		area = new Rectangle(x, y - allHeight, allWidth + 5, allHeight);
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		if (value > 100) {
			value = 100;
		}
		this.value = value;
	}

	public void render(GUIContext ctx, Graphics g) {

		int wSpace = (widthGrowth + space);
		int allOfWidth = (wSpace * 9) + widthGrowth;
		int allOfHeight = heightGrowth * 10;
		int y2 = y - heightGrowth;
		int x2 = x;
		int y0 = y - allOfHeight;

		if (press) {
			procent = currentX - x;
			float val = (procent * 100) / allOfWidth;
			setValue(val);
		} else {
			procent = allOfWidth * (value / 100);
		}

		int h2 = heightGrowth;
		Color oldColor = g.getColor();

		for (int i = 0; i < 10; i++) {
			// set clip dynamically
			g.setClip(x2, y2, widthGrowth, h2);
			// draw background first
			g.setColor(blankColor);
			g.fillRect(x, y0, allOfWidth, allOfHeight);
			// now draw cool sound bar
			g.setColor(fillColor);
			g.fillRect(x, y0, procent, allOfHeight);
			g.clearClip();
			x2 += wSpace;
			y2 -= heightGrowth;
			h2 += heightGrowth;

		}

		g.setColor(oldColor);
	}

	/**
	 * Moves the component.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 */
	public void setLocation(int x, int y) {
		if (area != null) {
			area.setX(x);
			area.setY(y);
		}
	}

	/**
	 * Returns the position in the X coordinate
	 * 
	 * @return x
	 */
	public int getX() {
		return (int) area.getX();
	}

	/**
	 * Returns the position in the Y coordinate
	 * 
	 * @return y
	 */
	public int getY() {
		return (int) area.getY();
	}

	/**
	 * @see org.newdawn.slick.gui.AbstractComponent#getHeight()
	 */
	public int getHeight() {
		return (int) (area.getMaxY() - area.getY());
	}

	/**
	 * @see org.newdawn.slick.gui.AbstractComponent#getWidth()
	 */
	public int getWidth() {
		return (int) (area.getMaxX() - area.getX());
	}

	/**
	 * @see org.newdawn.slick.util.InputAdapter#mousePressed(int, int, int)
	 */
	public void mousePressed(int button, int mx, int my) {
		press = area.contains(mx, my);
		currentX = mx;
	}

	/**
	 * @see org.newdawn.slick.util.InputAdapter#mouseReleased(int, int, int)
	 */
	public void mouseReleased(int button, int mx, int my) {
		press = false;
	}

	/**
	 * @see org.newdawn.slick.util.InputAdapter#mouseMoved(int, int, int, int)
	 */
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		boolean move = area.contains(newx, newy);
		if (move && press) {
			currentX = newx;
		}
	}
}
