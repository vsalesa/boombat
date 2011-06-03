package net.java.dev.boombat.game.state;

import net.java.dev.boombat.game.ui.core.Button;
import net.java.dev.boombat.game.ui.listener.ChangeGameStateListener;
import net.java.dev.boombat.game.util.MusicPlayer;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class MainMenuState extends BasicGameState {

	public static final int ID = 2;
	private StateBasedGame game;
	private Image bgImage;
	private Button createGameButton;
	private Button joinGameButton;
	private Button optionsButton;
	private Button helpButton;
	private Button creditsButton;
	private Button quitGameButton;

	
	public int getID() {

		return ID;
	}

	
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.game = game;
		bgImage = ResourceManager.getImage("menuBgImage");

		// create buttons
		createGameButton = new Button(container, ResourceManager
				.getImage("createGameNormalImage"), 300, 200);
		joinGameButton = new Button(container, ResourceManager
				.getImage("joinGameNormalImage"), 300, 250);
		optionsButton = new Button(container, ResourceManager
				.getImage("optionsNormalImage"), 300, 300);
		helpButton = new Button(container, ResourceManager
				.getImage("helpNormalImage"), 300, 350);
		creditsButton = new Button(container, ResourceManager
				.getImage("creditsNormalImage"), 300, 400);
		quitGameButton = new Button(container, ResourceManager
				.getImage("quitGameNormalImage"), 300, 450);
		// set mouse over images
		createGameButton.setMouseOverImage(ResourceManager
				.getImage("createGameOverImage"));
		joinGameButton.setMouseOverImage(ResourceManager
				.getImage("joinGameOverImage"));
		optionsButton.setMouseOverImage(ResourceManager
				.getImage("optionsOverImage"));
		helpButton.setMouseOverImage(ResourceManager.getImage("helpOverImage"));
		creditsButton.setMouseOverImage(ResourceManager
				.getImage("creditsOverImage"));
		quitGameButton.setMouseOverImage(ResourceManager
				.getImage("quitGameOverImage"));
		// add changestatelisteners
		createGameButton.addListener(new ChangeGameStateListener(game,
				CreateGameMenuState.ID));
		joinGameButton.addListener(new ChangeGameStateListener(game,
				JoinGameMenuState.ID));
		optionsButton.addListener(new ChangeGameStateListener(game,
				OptionsMenuState.ID));
		helpButton.addListener(new ChangeGameStateListener(game, HelpState.ID));
		creditsButton.addListener(new ChangeGameStateListener(game,
				CreditsState.ID));
		quitGameButton.addListener(new ChangeGameStateListener(game,
				QuitGameState.ID));

	}

	
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		container.setMusicVolume(container.getMusicVolume());
		MusicPlayer.update(container);
	}

	
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		bgImage.draw(0, 0, container.getWidth(), container.getHeight());
		createGameButton.render(container, g);
		joinGameButton.render(container, g);
		optionsButton.render(container, g);
		helpButton.render(container, g);
		creditsButton.render(container, g);
		quitGameButton.render(container, g);
	}

	
	public void keyPressed(int key, char c) {
		if (Input.KEY_ESCAPE == key) {
			game.enterState(QuitGameState.ID,
					new FadeOutTransition(Color.black), new FadeInTransition(
							Color.black));
		}
	}

}
