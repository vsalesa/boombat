package net.java.dev.boombat.game.ui.core;

import java.util.ArrayList;
import java.util.List;

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

public class ImageSlider<T> extends AbstractComponent implements AlphaEntity {
	private int x;
	private int y;
	private Rectangle area;
	private Fx fx;
	private Timeline timeline;
	private List<ImageInfo<T>> imageList = new ArrayList<ImageInfo<T>>();
	private int currentShowIndex = -1;

	/** Filter color as result from fx */
	private Color filter = new Color(Color.white);

	public ImageSlider(GUIContext container, int x, int y, int width, int height) {
		super(container);
		this.x = x;
		this.y = y;
		setLocation(x, y);
		area = new Rectangle(x, y, width, height);
		timeline = new Timeline();
		Easing ease = Easing.LINEAR;
		fx = new AlphaFx(1000, this, .0f, 1f, ease);
		timeline.add(fx);
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

	
	public void render(GUIContext container, Graphics g) {
		Image selectedImage = getSelectedImage();
		if (selectedImage != null) {
			selectedImage.draw(x, y, filter);
		}
	}

	public void update(int delta) {
		timeline.update(delta);
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

	public void addImageInfo(Image image, T value) {
		imageList.add(new ImageInfo<T>(image, value));
	}

	public void removeImageInfo(int idx) {
		imageList.remove(idx);
		if (imageList.isEmpty()) {
			currentShowIndex = -1;
		} else {
			currentShowIndex = 0;
		}

	}

	private ImageInfo<T> getImageInfo(int idx) {
		if (currentShowIndex == -1) {
			return null;
		}
		return imageList.get(idx);
	}

	public Image getImage(int idx) {
		if (getImageInfo(idx) == null) {
			return null;
		}
		return getImageInfo(idx).getImage();
	}

	public T getImageValue(int idx) {
		if (getImageInfo(idx) == null) {
			return null;
		}
		return getImageInfo(idx).getValue();
	}

	public Image getSelectedImage() {
		return getImage(currentShowIndex);
	}

	public T getSelectedValue() {
		return getImageValue(currentShowIndex);
	}

	public void next() {
		if (currentShowIndex < imageList.size() - 1) {
			currentShowIndex += 1;
			startFx();
		}
	}

	public void previous() {
		if (currentShowIndex > 0) {
			currentShowIndex -= 1;
			startFx();
		}
	}

	private void startFx() {
		timeline.rewind();
		timeline.setActive(true);
	}

	public T getValue(int idx) {
		if (getImageInfo(idx) == null) {
			return null;
		}
		return getImageInfo(idx).getValue();
	}

	private class ImageInfo<V> {
		private Image image;
		private V value;

		public ImageInfo(Image image, V value) {
			this.image = image;
			this.value = value;
		}

		public Image getImage() {
			return image;
		}

		public V getValue() {
			return value;
		}

	}

	/**
	 * @see mdes.slick.animation.entity.AlphaEntity#getAlpha()
	 */
	
	public float getAlpha() {
		// TODO Auto-generated method stub
		return filter.a;
	}

	/**
	 * @see mdes.slick.animation.entity.AlphaEntity#setAlpha(float)
	 */
	
	public void setAlpha(float alpha) {
		// TODO Auto-generated method stub
		filter.a = alpha;
	}

}
