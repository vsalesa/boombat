package net.java.dev.boombat.game.state;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.ui.core.Button;
import net.java.dev.boombat.game.ui.core.CheckBox;
import net.java.dev.boombat.game.ui.core.TextField;
import net.java.dev.boombat.game.ui.ext.GameMapSlider;
import net.java.dev.boombat.game.ui.ext.LoadingArenaBox;
import net.java.dev.boombat.game.ui.ext.TankSlider;
import net.java.dev.boombat.game.ui.listener.ChangeGameStateListener;
import net.java.dev.boombat.game.util.MusicPlayer;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.multiplayer.Manager;

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

public class CreateGameMenuState extends BasicGameState implements
		ComponentListener {
	public static final int ID = 3;
	private StateBasedGame game;
	private Image bgImage;
	private TextField passwordField;
	private TextField maxPlayerField;
	private Button okButton;
	private Button cancelButton;
	private Font normalFont;
	private Font mediumFont;
	private GameMapSlider mapSlider;
	private CheckBox terroristCheckBox;
	private CheckBox counterTerroristCheckBox;
	private TankSlider tankSlider;
	private Color focusColor = new Color(107 / 255f, 18 / 255f, 12 / 255f,
			255 / 255f);
	protected LoadingArenaBox loadingArenaBox;
	private boolean showLoading;
	private boolean execute;
	private boolean showErr;
	private Font smallFont;

	
	public int getID() {

		return ID;
	}

	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.game = game;
		bgImage = ResourceManager.getImage("subMenuBgImage");
		normalFont = ResourceManager.getFont("dejavuNormalFont");
		mediumFont = ResourceManager.getFont("dejavuMediumFont");
		smallFont = ResourceManager.getFont("dejavuSmallFont");

		okButton = new Button(container, ResourceManager
				.getImage("okNormalImage"), 280, 545);
		cancelButton = new Button(container, ResourceManager
				.getImage("cancelNormalImage"), 420, 545);

		okButton.setMouseOverImage(ResourceManager.getImage("okOverImage"));
		cancelButton.setMouseOverImage(ResourceManager
				.getImage("cancelOverImage"));

		okButton.addListener(this);
		cancelButton.addListener(new ChangeGameStateListener(game,
				MainMenuState.ID));

		passwordField = new TextField(game, ID, normalFont, 160, 200, 240);
		passwordField.setPassword(true);
		maxPlayerField = new TextField(game, ID, normalFont, 160, 240, 50);
		maxPlayerField.setText(String.valueOf(Config.getMaxPlayer()));
		passwordField.setFocusBorderColor(focusColor);
		maxPlayerField.setFocusBorderColor(focusColor);

		maxPlayerField.setMaxLength(2);
		maxPlayerField.setIntegerOnly(true);

		passwordField.addListener(new ComponentListener() {
			
			public void componentActivated(AbstractComponent comp) {
				maxPlayerField.setFocus(true);
			}
		});

		counterTerroristCheckBox = new CheckBox(container, ResourceManager
				.getImage("checkboxFalseImage"), ResourceManager
				.getImage("checkboxTrueImage"), 160, 280);
		terroristCheckBox = new CheckBox(container, ResourceManager
				.getImage("checkboxFalseImage"), ResourceManager
				.getImage("checkboxTrueImage"), 160, 320);

		terroristCheckBox.addListener(new ComponentListener() {
			
			public void componentActivated(AbstractComponent comp) {
				counterTerroristCheckBox.setSelected(!terroristCheckBox
						.isSelected());
				tankSlider.setTerrorist(true);
			}
		});

		counterTerroristCheckBox.addListener(new ComponentListener() {
			
			public void componentActivated(AbstractComponent comp) {
				terroristCheckBox.setSelected(!counterTerroristCheckBox
						.isSelected());
				tankSlider.setTerrorist(false);
			}
		});

		counterTerroristCheckBox.setSelected(true);

		mapSlider = new GameMapSlider(container, 425, 240);
		tankSlider = new TankSlider(container, 155, 360);

		loadingArenaBox = new LoadingArenaBox(0, 0);
		loadingArenaBox.setVisible(true);

	}

	
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (execute) {
			execute();
		}
		updateForm(container, game, delta);
		MusicPlayer.update(container);
	}

	public void updateForm(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		passwordField.update(delta);
		maxPlayerField.update(delta);
		mapSlider.update(delta);
		tankSlider.update(delta);
	}

	
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if (showLoading) {
			loadingArenaBox.render(container, g);
			if (showErr) {
				g.setFont(smallFont);
				g
						.drawString(
								"Error: Can't create game session. Press Escape To Back.",
								10, 580);
			} else {
				execute = true;
			}
		} else {
			renderForm(container, game, g);
		}
	}

	public void renderForm(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		bgImage.draw(0, 0, container.getWidth(), container.getHeight());
		g.setFont(mediumFont);
		g.drawString("CREATE GAME", 142, 110);
		g.setFont(normalFont);
		g.drawString("Password", 20, 200);
		passwordField.render(container, g);
		g.drawString("Max Player", 20, 240);
		maxPlayerField.render(container, g);
		g.drawString("Select Combat Map", 480, 200);
		mapSlider.render(container, g);
		g.drawString("Team", 20, 280);
		g.drawString("Counter Terrorist", 195, 280);
		counterTerroristCheckBox.render(container, g);
		g.drawString("Terrorist", 195, 320);
		terroristCheckBox.render(container, g);
		g.drawString("Tank", 20, 360);
		tankSlider.render(container, g);
		okButton.render(container, g);
		cancelButton.render(container, g);

	}

	
	public void keyPressed(int key, char c) {
		if (showLoading) {
			if (Input.KEY_ESCAPE == key) {
				if (showErr) {
					showLoading = false;
					showErr = false;
					execute = false;
				} else if (!showLoading) {
					game.enterState(MainMenuState.ID, new FadeOutTransition(
							Color.black), new FadeInTransition(Color.black));
				}
			}
		}
	}

	
	public void componentActivated(AbstractComponent comp) {
		showLoading = true;
	}

	private void execute() {
		execute = false;
		// get max player
		int max = Integer.parseInt(maxPlayerField.getText());
		if (max > 20) {
			max = 20;
		}
		// get team
		byte team = Player.TEAM_COUNTER_TERRORIST;
		if (terroristCheckBox.isSelected()) {
			team = Player.TEAM_TERRORIST;
		}

		boolean ok = Manager.createGame(Config.getOption().getString(
				"playerName"), passwordField.getText(), max, mapSlider
				.getSelectedMap(), team, tankSlider.getSelectedTank());

		if (ok) {
			game.enterState(InGameCreatorState.ID);
		} else {
			// show error message
			showErr = true;
		}
	}

	
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		showLoading = false;
		showErr = false;
		execute = false;
	}

	
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		showLoading = false;
		showErr = false;
		execute = false;
		passwordField.setText("");
		container.setMouseGrabbed(false);
	}
}
