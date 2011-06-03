package net.java.dev.boombat.game.state;

import net.java.dev.boombat.game.ui.fx.ImageFxFadeInOut;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class IntroState extends BasicGameState {

	private ImageFxFadeInOut fxFadeInOutLogo;
	private ImageFxFadeInOut fxFadeInOutTitle;
	private int counter = 0;
	private int maxCounter = 8000;

	public static final int ID = 0;

	
	public int getID() {

		return ID;
	}

	
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
//		container.setMouseCursor(ResourceManager.getImage("cursorImage"), 0, 0);

		fxFadeInOutLogo = new ImageFxFadeInOut(container, ResourceManager
				.getImage("emLogoImage"), 320, 275, 48, 51, 0, 4000);
		fxFadeInOutTitle = new ImageFxFadeInOut(container, ResourceManager
				.getImage("emTitleImage"), 380, 275, 250, 51, 2000, 4000);
	}

	
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		fxFadeInOutLogo.update(delta);
		fxFadeInOutTitle.update(delta);
		counter += delta;

		if (counter > maxCounter
				|| container.getInput().isKeyDown(Input.KEY_ESCAPE)) {
			// container.setMouseCursor(ResourceManager.getImage("cursorImage"),
			// 0, 0);
			game.enterState(LoadingState.ID);
		}

	}

	
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		fxFadeInOutLogo.render(container, g);
		fxFadeInOutTitle.render(container, g);
	}

}
