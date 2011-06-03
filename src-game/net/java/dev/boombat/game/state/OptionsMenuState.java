package net.java.dev.boombat.game.state;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.ui.core.Button;
import net.java.dev.boombat.game.ui.core.CheckBox;
import net.java.dev.boombat.game.ui.core.SoundBar;
import net.java.dev.boombat.game.ui.core.TextField;
import net.java.dev.boombat.game.util.MusicPlayer;
import net.java.dev.boombat.game.util.PropertiesFile;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class OptionsMenuState extends BasicGameState implements
		ComponentListener {
	public static final int ID = 5;
	private StateBasedGame game;
	private Image bgImage;
	private Button okButton;
	private TextField serverNameField;
	private TextField playerNameField;
	private CheckBox fullscreenCheckBox;
	private CheckBox shuffleMusicCheckBox;
	private CheckBox friendlyFireCheckBox;
	private SoundBar musicVolumeBar;
	private SoundBar soundVolumeBar;
	private CheckBox radarCheckBox;
	private Font normalFont;
	private Font mediumFont;
	private Color focusColor = new Color(107 / 255f, 18 / 255f, 12 / 255f,
			255 / 255f);
	private AppGameContainer container;

	
	public int getID() {

		return ID;
	}

	
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.game = game;
		this.container = (AppGameContainer) container;
		bgImage = ResourceManager.getImage("subMenuBgImage");
		normalFont = ResourceManager.getFont("dejavuNormalFont");
		mediumFont = ResourceManager.getFont("dejavuMediumFont");

		okButton = new Button(container, ResourceManager
				.getImage("okNormalImage"), 350, 530);

		okButton.setMouseOverImage(ResourceManager.getImage("okOverImage"));

		okButton.addListener(this);

		serverNameField = new TextField(game, ID, normalFont, 190, 230, 200);
		serverNameField.setMaxLength(20);
		playerNameField = new TextField(game, ID, normalFont, 190, 280, 200);
		playerNameField.setMaxLength(17);

		serverNameField.setFocusBorderColor(focusColor);
		playerNameField.setFocusBorderColor(focusColor);

		serverNameField.addListener(new ComponentListener() {
			
			public void componentActivated(AbstractComponent comp) {
				playerNameField.setFocus(true);
			}
		});

		fullscreenCheckBox = new CheckBox(container, ResourceManager
				.getImage("checkboxFalseImage"), ResourceManager
				.getImage("checkboxTrueImage"), 190, 330);

		friendlyFireCheckBox = new CheckBox(container, ResourceManager
				.getImage("checkboxFalseImage"), ResourceManager
				.getImage("checkboxTrueImage"), 190, 380);

		soundVolumeBar = new SoundBar(container, 630, 250, focusColor,
				Color.lightGray, 9, 3, 2);
		musicVolumeBar = new SoundBar(container, 630, 300, focusColor,
				Color.lightGray, 9, 3, 2);
		shuffleMusicCheckBox = new CheckBox(container, ResourceManager
				.getImage("checkboxFalseImage"), ResourceManager
				.getImage("checkboxTrueImage"), 630, 330);

		radarCheckBox = new CheckBox(container, ResourceManager
				.getImage("checkboxFalseImage"), ResourceManager
				.getImage("checkboxTrueImage"), 630, 380);

		// load options
		PropertiesFile option = Config.getOption();
		serverNameField.setText(option.getString("serverName"));
		playerNameField.setText(option.getString("playerName"));
		friendlyFireCheckBox.setSelected(option.getBoolean("friendlyFire"));
		shuffleMusicCheckBox.setSelected(MusicPlayer.isShuffle());
		fullscreenCheckBox.setSelected(this.container.isFullscreen());
		radarCheckBox.setSelected(option.getBoolean("playerRadar"));

		musicVolumeBar.setValue(container.getMusicVolume() * 100f);
		soundVolumeBar.setValue(container.getSoundVolume() * 100f);

	}

	
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		serverNameField.update(delta);
		playerNameField.update(delta);
		container.setMusicVolume(musicVolumeBar.getValue() / 100f);
		container.setSoundVolume(soundVolumeBar.getValue() / 100f);
		MusicPlayer.update(container);
	}

	
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		bgImage.draw(0, 0, container.getWidth(), container.getHeight());
		g.setFont(mediumFont);
		g.drawString("OPTIONS", 142, 110);
		g.setFont(normalFont);
		g.drawString("Server Name", 30, 230);
		serverNameField.render(container, g);
		g.drawString("Player Name", 30, 280);
		playerNameField.render(container, g);
		g.drawString("Fullscreen", 30, 330);
		fullscreenCheckBox.render(container, g);
		g.drawString("Friendly Fire", 30, 380);
		friendlyFireCheckBox.render(container, g);
		g.drawString("Sound Volume", 430, 230);
		soundVolumeBar.render(container, g);
		g.drawString("Music Volume", 430, 280);
		musicVolumeBar.render(container, g);
		g.drawString("Random Music", 430, 330);
		shuffleMusicCheckBox.render(container, g);
		g.drawString("Player Radar", 430, 380);
		radarCheckBox.render(container, g);
		okButton.render(container, g);
	}

	
	public void keyPressed(int key, char c) {
		if (Input.KEY_ESCAPE == key) {
			game.enterState(MainMenuState.ID,
					new FadeOutTransition(Color.black), new FadeInTransition(
							Color.black));
		}
	}

	
	public void componentActivated(AbstractComponent comp) {
		boolean noGrabbed = false;
		if (MusicPlayer.isShuffle() != shuffleMusicCheckBox.isSelected()) {
			MusicPlayer.toggleShuffle();
		}

		if (fullscreenCheckBox.isSelected() != container.isFullscreen()) {
			try {
				this.container.setMouseGrabbed(true);
				this.container.setFullscreen(fullscreenCheckBox.isSelected());
				noGrabbed = true;
			} catch (SlickException e) {
				// should never happen
				fullscreenCheckBox.setSelected(this.container.isFullscreen());
			}
		}

		// save modified options
		PropertiesFile option = Config.getOption();
		if (serverNameField.getText().trim().equals("")) {
			serverNameField.setText("server");
		}
		option.setString("serverName", serverNameField.getText());
		if (playerNameField.getText().trim().equals("")) {
			playerNameField.setText("player");
		}
		option.setString("playerName", playerNameField.getText());
		option.setBoolean("fullscreen", fullscreenCheckBox.isSelected());
		option.setBoolean("shuffle", shuffleMusicCheckBox.isSelected());
		option.setBoolean("friendlyFire", friendlyFireCheckBox.isSelected());
                option.setBoolean("playerRadar", radarCheckBox.isSelected());
		option.setFloat("soundVolume", container.getSoundVolume());
		option.setFloat("musicVolume", container.getMusicVolume());
		option.save();

		game.enterState(MainMenuState.ID, new FadeOutTransition(Color.black),
				new FadeInTransition(Color.black));

		if (noGrabbed) {
			this.container.setMouseGrabbed(false);
		}
	}

	
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		musicVolumeBar.setValue(container.getMusicVolume() * 100f);
		soundVolumeBar.setValue(container.getSoundVolume() * 100f);
                playerNameField.setText(Config.getOption().getString("playerName"));
                shuffleMusicCheckBox.setSelected(Config.getOption().getBoolean("shuffle"));
	}
}
