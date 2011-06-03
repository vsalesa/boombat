package net.java.dev.boombat.game.ui.core;

import mdes.slick.animation.Easing;
import mdes.slick.animation.Fx;
import mdes.slick.animation.Timeline;
import mdes.slick.animation.entity.AlphaEntity;
import mdes.slick.animation.fx.AlphaFx;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.state.StateBasedGame;

/**
 * A single text field supporting text entry password mask
 * 
 * @author kevin
 * @author eriq
 */
public class TextField extends AbstractComponent implements AlphaEntity {

	/** The key repeat interval */
	private static final int INITIAL_KEY_REPEAT_INTERVAL = 400;
	/** The key repeat interval */
	private static final int KEY_REPEAT_INTERVAL = 50;
	/** The width of the field */
	private int width;
	/** The height of the field */
	private int height;
	/** The location in the X coordinate */
	protected int x;
	/** The location in the Y coordinate */
	protected int y;
	/** The maximum number of characters allowed to be input */
	private int maxCharacter = 10000;
	/** The value stored in the text field */
	private String value = "";
	/** The font used to render text in the field */
	private Font font;
	/** The border color - null if no border */
	private Color border = Color.white;
	/** The Border when hasFocus */
	private Color focusBorder = new Color(165, 233, 233);
	/** The text color */
	private Color text = Color.white;
	/** The background color - null if no background */
	private Color background = new Color(0, 0, 0, 0.5f);
	/** The current cursor position */
	private int cursorPos;
	/** True if the cursor should be visible */
	private boolean visibleCursor = true;
	/** The last key pressed */
	private int lastKey = -1;
	/** The last character pressed */
	private char lastChar = 0;
	/** The time since last key repeat */
	private long repeatTimer;
	/** The text before the paste in */
	private String oldText;
	/** The cursor position before the paste */
	private int oldCursorPos;
	/** True if events should be consumed by the field */
	private boolean consume = true;
	/** Mask text with password character or not */
	private boolean password = false;
	/** String for password mask */
	private String passwordString = "*";
	/** Blink Cursor String */
	private String blinkCursor = "|";
	/** Image background for text field * */
	private Image bgImage;
	/** Easing Timeline */
	private Timeline timeline;
	/** Easing for blink cursor */
	private Easing ease;
	/** Fx for blink cursor */
	private Fx fx;
	/** Filter color as result from fx */
	private Color filter = new Color(Color.white);
	private StringBuffer oldMaskString = new StringBuffer("");

	private Rectangle area;
	private int stateID = -1;
	private StateBasedGame game;
	private boolean integerOnly;

	/**
	 * Create a new text field
	 * 
	 * @param container
	 *            The container rendering this field
	 * @param font
	 *            The font to use in the text field
	 * @param x
	 *            The x coordinate of the top left corner of the text field
	 * @param y
	 *            The y coordinate of the top left corner of the text field
	 * @param width
	 *            The width of the text field
	 * @param height
	 *            The height of the text field
	 * @param listener
	 *            The listener to add to the text field
	 */
	public TextField(GUIContext container, Font font, int x, int y, int width,
			ComponentListener listener) {
		this(container, font, x, y, width);
		addListener(listener);
	}

	public TextField(StateBasedGame game, int id, Font font, int x, int y,
			int width) {
		this(game.getContainer(), font, x, y, width);
		this.game = game;
		this.stateID = id;
	}

	/**
	 * Create a new text field
	 * 
	 * @param container
	 *            The container rendering this field
	 * @param font
	 *            The font to use in the text field
	 * @param x
	 *            The x coordinate of the top left corner of the text field
	 * @param y
	 *            The y coordinate of the top left corner of the text field
	 * @param width
	 *            The width of the text field
	 * @param height
	 *            The height of the text field
	 */
	public TextField(GUIContext container, Font font, int x, int y, int width) {
		super(container);
		this.font = font;
		setLocation(x, y);
		this.width = width;
		this.height = font.getHeight("|") + 4;
		area = new Rectangle(x, y, width, height);
		ease = Easing.LINEAR;

		timeline = new Timeline();
		fx = new AlphaFx(300, this, 1f, .0f, ease);
		timeline.add(fx);
		fx = new AlphaFx(300, this, .0f, 1f, ease);
		timeline.add(fx);
		timeline.setLooping(true);

	}

	/**
	 * Indicate if the input events should be consumed by this field
	 * 
	 * @param consume
	 *            True if events should be consumed by this field
	 */
	public void setConsumeEvents(boolean consume) {
		this.consume = consume;
	}

	/**
	 * Deactivate the key input handling for this field
	 */
	public void deactivate() {
		setFocus(false);
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
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the position in the X coordinate
	 * 
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the position in the Y coordinate
	 * 
	 * @return y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Get the width of the component
	 * 
	 * @return The width of the component
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of the component
	 * 
	 * @return The height of the component
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set the background color. Set to null to disable the background
	 * 
	 * @param color
	 *            The color to use for the background
	 */
	public void setBackgroundColor(Color color) {
		background = color;
	}

	/**
	 * Set the border color. Set to null to disable the border
	 * 
	 * @param color
	 *            The color to use for the border
	 */
	public void setBorderColor(Color color) {
		border = color;
	}

	/**
	 * Set the text color.
	 * 
	 * @param color
	 *            The color to use for the text
	 */
	public void setTextColor(Color color) {
		text = color;
	}

	/**
	 * Set the focus border color.
	 * 
	 * @param color
	 *            The color to use for text border when hasFocus
	 */
	public void setFocusBorderColor(Color color) {
		focusBorder = color;
	}

	/**
	 * Set enable or disable password mask.
	 * 
	 * @param password
	 *            boolean to enable or disable password mask.
	 */
	public void setPassword(boolean password) {
		this.password = password;
	}

	/**
	 * Set Background Image.
	 * 
	 * @param bgImage
	 *            The image for text background.
	 */
	public void setBgImage(Image bgImage) {
		this.bgImage = bgImage;
	}

	/**
	 * Check Input Key before do any render.
	 * 
	 */
	public void checkInputBeforeRender() {
		if (lastKey != -1) {
			if (input.isKeyDown(lastKey)) {
				if (repeatTimer < System.currentTimeMillis()) {
					repeatTimer = System.currentTimeMillis()
							+ KEY_REPEAT_INTERVAL;
					keyPressed(lastKey, lastChar);

				}
			} else {
				lastKey = -1;
			}
		}
	}

	public void update(int delta) {
		timeline.update(delta);
	}

	/**
	 * @see org.newdawn.slick.gui.AbstractComponent#render(org.newdawn.slick.gui.GUIContext,
	 *      org.newdawn.slick.Graphics)
	 */
	public void render(GUIContext container, Graphics g) {
		checkInputBeforeRender();

		Rectangle oldClip = g.getClip();

		g.setWorldClip(x, y, width - 2, height);

		// Someone could have set a color for me to blend...
		Color clr = g.getColor();

		if (bgImage != null) {
			bgImage.draw(x, y, width - 2, height - 2);
		}

		if (background != null) {
			g.setColor(background.multiply(clr));
			g.fillRoundRect(x, y, width, height, 3);
		}

		g.setColor(text.multiply(clr));
		Font temp = g.getFont();

		int cpos = oldCursorPos;

		String str = oldMaskString.toString();
		if (password) {
			int oldLength = oldMaskString.length();
			int newLength = value.length();

			if (newLength > oldLength) {
				oldMaskString.append(passwordString);
			} else if (newLength < oldLength && oldLength != 0) {
				oldMaskString.replace(0, 1, "");
			}
			str = oldMaskString.toString();
			cursorPos = oldMaskString.length();
			cpos = font.getWidth(str.substring(0, cursorPos));
		} else {
			cpos = font.getWidth(value.substring(0, cursorPos));
		}

		int tx = 0;
		if (cpos > width) {
			tx = width - cpos - font.getWidth(blinkCursor);
		}

		g.translate(tx + 2, 0);
		g.setFont(font);

		if (password) {
			g.drawString(str, x + 1, y + 1);
		} else {
			g.drawString(value, x + 1, y + 1);
		}

		if (hasFocus() && visibleCursor) {
			g.setColor(filter);
			g.drawString(blinkCursor, x + 1 + cpos + 2, y + 1);
			timeline.setActive(true);
		} else {
			timeline.setActive(false);
		}

		g.translate(-tx - 2, 0);

		if (border != null) {
			if (hasFocus()) {
				g.setLineWidth(2);
				g.setColor(focusBorder);
			} else {
				g.setLineWidth(2);
				g.setColor(border);
			}
			g.drawRoundRect(x, y, width - 2, height - 2, 0);

		}
		g.setColor(clr);
		g.setFont(temp);
		g.clearWorldClip();
		g.setClip(oldClip);
	}

	/**
	 * Get the value in the text field
	 * 
	 * @return The value in the text field
	 */
	public String getText() {
		return value;
	}

	/**
	 * Set the value to be displayed in the text field
	 * 
	 * @param value
	 *            The value to be displayed in the text field
	 */
	public void setText(String value) {
		this.value = value;
		cursorPos = value.length();
	}

	/**
	 * Set the position of the cursor
	 * 
	 * @param pos
	 *            The new position of the cursor
	 */
	public void setCursorPos(int pos) {
		cursorPos = pos;
		if (cursorPos >= value.length()) {
			cursorPos = value.length();
		}
	}

	/**
	 * Indicate whether the mouse cursor should be visible or not
	 * 
	 * @param visibleCursor
	 *            True if the mouse cursor should be visible
	 */
	public void setCursorVisible(boolean visibleCursor) {
		this.visibleCursor = visibleCursor;
	}

	/**
	 * Set the length of the allowed input
	 * 
	 * @param length
	 *            The length of the allowed input
	 */
	public void setMaxLength(int length) {
		maxCharacter = length;
		if (value.length() > maxCharacter) {
			value = value.substring(0, maxCharacter);
		}
	}

	/**
	 * Do the paste into the field, overrideable for custom behaviour
	 * 
	 * @param text
	 *            The text to be pasted in
	 */
	protected void doPaste(String text) {
		recordOldPosition();

		for (int i = 0; i < text.length(); i++) {
			keyPressed(-1, text.charAt(i));
		}
	}

	/**
	 * Record the old position and content
	 */
	protected void recordOldPosition() {
		oldText = getText();
		oldCursorPos = cursorPos;
	}

	/**
	 * Do the undo of the paste, overrideable for custom behaviour
	 * 
	 * @param oldCursorPos
	 *            before the paste
	 * @param oldText
	 *            The text before the last paste
	 */
	protected void doUndo(int oldCursorPos, String oldText) {
		if (oldText != null) {
			setText(oldText);
			setCursorPos(oldCursorPos);
		}
	}

	/**
	 * @see org.newdawn.slick.gui.AbstractComponent#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c) {
		if (hasFocus()) {
			if (key != -1) {
				if (integerOnly && key != Input.KEY_DELETE
						&& key != Input.KEY_BACK && (key < 2 || key > 11)) {
					return;
				}
				if ((key == Input.KEY_V)
						&& ((input.isKeyDown(Input.KEY_LCONTROL)) || (input
								.isKeyDown(Input.KEY_RCONTROL)))) {
					String text = Sys.getClipboard();
					if (text != null) {
						doPaste(text);
					}
					return;
				}
				if ((key == Input.KEY_Z)
						&& ((input.isKeyDown(Input.KEY_LCONTROL)) || (input
								.isKeyDown(Input.KEY_RCONTROL)))) {
					if (oldText != null) {
						doUndo(oldCursorPos, oldText);
					}
					return;
				}

				// alt and control keys don't come through here
				if (input.isKeyDown(Input.KEY_LCONTROL)
						|| input.isKeyDown(Input.KEY_RCONTROL)) {
					return;
				}
				if (input.isKeyDown(Input.KEY_LALT)
						|| input.isKeyDown(Input.KEY_RALT)) {
					return;
				}
			}

			if (lastKey != key) {
				lastKey = key;
				repeatTimer = System.currentTimeMillis()
						+ INITIAL_KEY_REPEAT_INTERVAL;
			} else {
				repeatTimer = System.currentTimeMillis() + KEY_REPEAT_INTERVAL;
			}
			lastChar = c;

			if (key == Input.KEY_LEFT) {
				if (cursorPos > 0) {
					cursorPos--;
				}
				// Nobody more will be notified
				if (consume) {
					container.getInput().consumeEvent();
				}
			} else if (key == Input.KEY_RIGHT) {
				if (cursorPos < value.length()) {
					cursorPos++;
				}
				// Nobody more will be notified
				if (consume) {
					container.getInput().consumeEvent();
				}
			} else if (key == Input.KEY_BACK) {
				if ((cursorPos > 0) && (value.length() > 0)) {
					if (cursorPos < value.length()) {
						value = value.substring(0, cursorPos - 1)
								+ value.substring(cursorPos);
					} else {
						value = value.substring(0, cursorPos - 1);
					}
					cursorPos--;
				}
				// Nobody more will be notified
				if (consume) {
					container.getInput().consumeEvent();
				}
			} else if (key == Input.KEY_DELETE) {
				if (value.length() > cursorPos) {
					value = value.substring(0, cursorPos)
							+ value.substring(cursorPos + 1);
				}
				// Nobody more will be notified
				if (consume) {
					container.getInput().consumeEvent();
				}
			} else if ((c < 127) && (c > 31) && (value.length() < maxCharacter)) {
				if (cursorPos < value.length()) {
					value = value.substring(0, cursorPos) + c
							+ value.substring(cursorPos);
				} else {
					value = value.substring(0, cursorPos) + c;
				}
				cursorPos++;
				// Nobody more will be notified
				if (consume) {
					container.getInput().consumeEvent();
				}
			} else if (key == Input.KEY_RETURN) {
				notifyListeners();
				// Nobody more will be notified
				if (consume) {
					container.getInput().consumeEvent();
				}
			}

		}
	}

	/**
	 * @see org.newdawn.slick.gui.AbstractComponent#setFocus(boolean)
	 */
	public void setFocus(boolean focus) {
		lastKey = -1;
		super.setFocus(focus);
	}

	/**
	 * @see mdes.slick.animation.entity.AlphaEntity#getAlpha()
	 */
	
	public float getAlpha() {

		return filter.a;
	}

	/**
	 * @see mdes.slick.animation.entity.AlphaEntity#setAlpha(float)
	 */
	
	public void setAlpha(float alpha) {

		filter.a = alpha;
	}

	
	public void mouseReleased(int button, int mx, int my) {
		if (game != null) {
			if (stateID != game.getCurrentStateID()) {
				return;
			}
		}
		setFocus(area.contains(mx, my));
	}

	public boolean isIntegerOnly() {
		return integerOnly;
	}

	public void setIntegerOnly(boolean integerOnly) {
		this.integerOnly = integerOnly;
	}
}
