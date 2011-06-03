package net.java.dev.boombat.game.state;

import net.java.dev.boombat.game.ui.core.Button;
import net.java.dev.boombat.game.ui.listener.ChangeGameStateListener;
import net.java.dev.boombat.game.util.MusicPlayer;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class QuitGameState extends BasicGameState implements ComponentListener {
	public static final int ID = 8;
	private Image bgImage;
	private Font font;
	private Button yesButton;
	private Button noButton;
	private GameContainer container;

	
	public int getID() {

		return ID;
	}

	
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.container = container;
		bgImage = ResourceManager.getImage("menuBgImage");
		font = ResourceManager.getFont("dejavuMediumFont");
		yesButton = new Button(container, ResourceManager
				.getImage("yesNormal2Image"), 280, 250);
		noButton = new Button(container, ResourceManager
				.getImage("noNormal2Image"), 400, 250);

		yesButton.setMouseOverImage(ResourceManager.getImage("yesOver2Image"));
		noButton.setMouseOverImage(ResourceManager.getImage("noOver2Image"));

		yesButton.addListener(this);
		noButton
				.addListener(new ChangeGameStateListener(game, MainMenuState.ID));
	}

	
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		MusicPlayer.update(container);
	}

	
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		bgImage.draw(0, 0, container.getWidth(), container.getHeight());
		g.setFont(font);
		g.drawString("Are You Sure Wanna Quit ? ", 230, 200);
		yesButton.render(container, g);
		noButton.render(container, g);
	}

	
	public void componentActivated(AbstractComponent comp) {
		this.container.exit();
	}
}
