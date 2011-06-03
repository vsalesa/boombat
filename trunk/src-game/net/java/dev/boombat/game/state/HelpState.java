package net.java.dev.boombat.game.state;

import net.java.dev.boombat.game.ui.core.Button;
import net.java.dev.boombat.game.ui.listener.ChangeGameStateListener;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class HelpState extends BasicGameState {
	public static final int ID = 6;
	private StateBasedGame game;
	private Image bgImage;
	private Image helpImage;

	private Button okButton;
	private Font mediumFont;

	public int getID() {

		return ID;
	}

	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.game = game;
		bgImage = ResourceManager.getImage("subMenuBgImage");
		helpImage = ResourceManager.getImage("helpImage");
		mediumFont = ResourceManager.getFont("dejavuMediumFont");
		okButton = new Button(container, ResourceManager
				.getImage("okNormalImage"), 350, 540);

		okButton.setMouseOverImage(ResourceManager.getImage("okOverImage"));
		okButton
				.addListener(new ChangeGameStateListener(game, MainMenuState.ID));
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		bgImage.draw(0, 0, container.getWidth(), container.getHeight());
		helpImage.draw(142, 130);
		g.setFont(mediumFont);
		g.drawString("HELP", 142, 110);
		okButton.render(container, g);
	}

	public void keyPressed(int key, char c) {
		if (Input.KEY_ESCAPE == key) {
			game.enterState(MainMenuState.ID,
					new FadeOutTransition(Color.black), new FadeInTransition(
							Color.black));
		}
	}
}
