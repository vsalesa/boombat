package net.java.dev.boombat.game.state;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.ui.core.Button;
import net.java.dev.boombat.game.ui.core.CheckBox;
import net.java.dev.boombat.game.ui.core.TextField;
import net.java.dev.boombat.game.ui.ext.LoadingArenaBox;
import net.java.dev.boombat.game.ui.ext.ServerList;
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

public class JoinGameMenuState extends BasicGameState implements
		ComponentListener {
	public static final int ID = 4;
	private StateBasedGame game;
	private Image bgImage;
	private TextField ipAddressField;
	private TextField passwordField;
	private ServerList serverList;
	private TankSlider tankSlider;
	private Button okButton;
	private Button cancelButton;
	private CheckBox terroristCheckBox;
	private CheckBox counterTerroristCheckBox;
	private Button scanButton;
	private Font normalFont;
	private Font mediumFont;
	protected LoadingArenaBox loadingArenaBox;
	private boolean showLoading;
	private boolean execute;
	private Color focusColor = new Color(107 / 255f, 18 / 255f, 12 / 255f,
			255 / 255f);
	private boolean showErr;

	public int getID() {

		return ID;
	}

	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.game = game;
		bgImage = ResourceManager.getImage("subMenuBgImage");
		normalFont = ResourceManager.getFont("dejavuNormalFont");
		mediumFont = ResourceManager.getFont("dejavuMediumFont");

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

		ipAddressField = new TextField(game, ID, normalFont, 150, 200, 240);
		ipAddressField.setMaxLength(15);
		passwordField = new TextField(game, ID, normalFont, 150, 240, 240);
		passwordField.setPassword(true);

		ipAddressField.setFocusBorderColor(focusColor);
		passwordField.setFocusBorderColor(focusColor);

		ipAddressField.addListener(new ComponentListener() {

			public void componentActivated(AbstractComponent comp) {
				passwordField.setFocus(true);
			}
		});

		counterTerroristCheckBox = new CheckBox(container, ResourceManager
				.getImage("checkboxFalseImage"), ResourceManager
				.getImage("checkboxTrueImage"), 150, 280);
		terroristCheckBox = new CheckBox(container, ResourceManager
				.getImage("checkboxFalseImage"), ResourceManager
				.getImage("checkboxTrueImage"), 150, 320);

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

		serverList = new ServerList(container, 405, 185);
		tankSlider = new TankSlider(container, 145, 357);

		scanButton = new Button(container, ResourceManager
				.getImage("scanNormalImage"), 402, 490);
		scanButton.setMouseOverImage(ResourceManager.getImage("scanOverImage"));
		scanButton.addListener(new ComponentListener() {

			public void componentActivated(AbstractComponent comp) {
				serverList.setServers(Manager.findServers());
			}
		});

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

	private void updateForm(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		ipAddressField.update(delta);
		passwordField.update(delta);
		tankSlider.update(delta);
		MusicPlayer.update(container);
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if (showLoading) {
			loadingArenaBox.render(container, g);
			if (showErr) {
				g.setFont(normalFont);
				g
						.drawString(
								"Error: Can't join game session. Press Escape to Back..",
								10, 570);
			} else {
				execute = true;
			}
		} else {
			renderForm(container, game, g);
		}
	}

	private void renderForm(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		bgImage.draw(0, 0, container.getWidth(), container.getHeight());
		g.setFont(mediumFont);
		g.drawString("JOIN GAME", 142, 110);
		g.setFont(normalFont);
		serverList.render(container, g);
		g.drawString("Server IP", 20, 200);
		ipAddressField.render(container, g);
		g.drawString("Password", 20, 240);
		passwordField.render(container, g);
		g.drawString("Team", 20, 280);
		g.drawString("Counter Terrorist", 185, 280);
		counterTerroristCheckBox.render(container, g);
		g.drawString("Terrorist", 185, 320);
		terroristCheckBox.render(container, g);
		g.drawString("Tank", 20, 360);
		tankSlider.render(container, g);
		okButton.render(container, g);
		cancelButton.render(container, g);
		scanButton.render(container, g);

	}

	public void keyPressed(int key, char c) {
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

	public void componentActivated(AbstractComponent comp) {
		showLoading = true;
	}

	private void execute() {
		execute = false;
		// get team
		byte team = Player.TEAM_COUNTER_TERRORIST;
		if (terroristCheckBox.isSelected()) {
			team = Player.TEAM_TERRORIST;
		}

		boolean ok = Manager.joinGame(ipAddressField.getText(), Config
				.getOption().getString("playerName"), passwordField.getText(),
				team, tankSlider.getSelectedTank());
		if (ok) {
			game.enterState(InGameJoinerState.ID);
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
		ipAddressField.setText("");
		passwordField.setText("");
		container.setMouseGrabbed(false);
	}

}
