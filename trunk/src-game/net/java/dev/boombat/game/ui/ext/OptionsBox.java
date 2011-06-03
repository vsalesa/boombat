package net.java.dev.boombat.game.ui.ext;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.ui.core.Button;
import net.java.dev.boombat.game.ui.core.CheckBox;
import net.java.dev.boombat.game.ui.core.SoundBar;
import net.java.dev.boombat.game.ui.core.TextField;
import net.java.dev.boombat.game.util.MusicPlayer;
import net.java.dev.boombat.game.util.PropertiesFile;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.ComponentListener;

public class OptionsBox {

	private boolean visible;

	private int x;

	private int y;

	private Image bgImage;

	private TextField playerNameField;

	private SoundBar musicVolumeBar;

	private SoundBar soundVolumeBar;

	private CheckBox shuffleMusicCheckBox;

	private Button okButton;

	private Color focusColor = new Color(107 / 255f, 18 / 255f, 12 / 255f,
			255 / 255f);

	private Font font;

	public OptionsBox(GameContainer container, int x, int y) {
		this.x = x;
		this.y = y;
		bgImage = ResourceManager.getImage("optionsBoxImage");
		font = ResourceManager.getFont("dejavuNormalFont");

		playerNameField = new TextField(container, font, x + 220, y + 60, 200);
		playerNameField.setFocusBorderColor(focusColor);

		soundVolumeBar = new SoundBar(container, x + 220, y + 135, focusColor,
				Color.lightGray, 9, 3, 2);
		musicVolumeBar = new SoundBar(container, x + 220, y + 185, focusColor,
				Color.lightGray, 9, 3, 2);

		shuffleMusicCheckBox = new CheckBox(container, ResourceManager
				.getImage("checkboxFalseImage"), ResourceManager
				.getImage("checkboxTrueImage"), x + 220, y + 210);

		okButton = new Button(container, ResourceManager
				.getImage("okNormalImage"), x + 200, y + 280);

		okButton.setMouseOverImage(ResourceManager.getImage("okOverImage"));

		// load options
		PropertiesFile option = Config.getOption();
		playerNameField.setText(option.getString("playerName"));
		shuffleMusicCheckBox.setSelected(MusicPlayer.isShuffle());

		musicVolumeBar.setValue(container.getMusicVolume() * 100f);
		soundVolumeBar.setValue(container.getSoundVolume() * 100f);

	}

	public void update(GameContainer container, int delta) {
		if (visible) {
			updateOptionsBox(container, delta);
		}
	}

	public void render(GameContainer container, Graphics g) {
		if (visible) {
			drawOptionsBox(container, g);
		}
	}

	private void updateOptionsBox(GameContainer container, int delta) {
		playerNameField.update(delta);
		container.setMusicVolume(musicVolumeBar.getValue() / 100f);
		container.setSoundVolume(soundVolumeBar.getValue() / 100f);
	}

	public void updateNewVolume(GameContainer container) {
		musicVolumeBar.setValue(container.getMusicVolume() * 100f);
		soundVolumeBar.setValue(container.getSoundVolume() * 100f);
	}

	private void drawOptionsBox(GameContainer container, Graphics g) {
		bgImage.draw(x, y);

		Font oldFont = g.getFont();
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString("Player Name", x + 30, y + 60);
		playerNameField.render(container, g);
		g.drawString("Sound Volume", x + 30, y + 110);
		soundVolumeBar.render(container, g);
		g.drawString("Music Volume", x + 30, y + 160);
		musicVolumeBar.render(container, g);
		g.drawString("Random Music", x + 30, y + 210);
		shuffleMusicCheckBox.render(container, g);
		okButton.render(container, g);

		g.setFont(oldFont);
	}

	public void addOkListener(ComponentListener listener) {
		okButton.addListener(listener);
	}

	public PropertiesFile getOptions() {
		PropertiesFile opt = Config.getOption();
		opt.setString("playerName", playerNameField.getText());
		if (MusicPlayer.isShuffle() != shuffleMusicCheckBox.isSelected()) {
			MusicPlayer.toggleShuffle();
		}
		opt.setBoolean("shuffleMusic", MusicPlayer.isShuffle());
		opt.setFloat("musicVolume", musicVolumeBar.getValue() / 100f);
		opt.setFloat("soundVolume", soundVolumeBar.getValue() / 100f);

		return opt;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		playerNameField.setFocus(true);
	}
}
