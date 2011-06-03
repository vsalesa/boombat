package net.java.dev.boombat.game.state;

import net.java.dev.boombat.game.util.MusicPlayer;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class LoadingState extends BasicGameState {

	public static final int ID = 1;
	private GameContainer game;
	private Image bgImage;
	private DeferredResource res;
	private float total;
	private float loaded;
	private long ctr;
	private boolean show = true;
	private Font font;
	private Color loadingColor = new Color(107 / 255f, 18 / 255f, 12 / 255f,
			255 / 255f);

	
	public int getID() {
		return ID;
	}

	
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.game = container;
		bgImage = ResourceManager.getImage("loadingBgImage");
		total = LoadingList.get().getTotalResources();
		font = ResourceManager.getFont("dejavuSmallFont");
	}

	
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		if (loaded == total) {
			Input input = container.getInput();

			if (input.isKeyDown(Input.KEY_ENTER)) {
				container.setMouseGrabbed(false);
				MusicPlayer.first();
				game.enterState(MainMenuState.ID, new FadeOutTransition(
						Color.black), new FadeInTransition(Color.black));
			}

			if (ctr >= 700) {
				show = !show;
				ctr = 0;
			}
			ctr += delta;
			return;
		}

		if (LoadingList.get().getRemainingResources() > 0) {
			res = LoadingList.get().getNext();
			try {
				res.load();
				loaded = LoadingList.get().getTotalResources()
						- LoadingList.get().getRemainingResources();
				// Thread.sleep(100);
			} catch (Exception e) {
				throw new SlickException("failed loading resource ...");
			}
		} else {
			loaded = total;
		}

	}

	
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		bgImage.draw(0, 0, container.getWidth(), container.getHeight());
		g.setFont(font);
		g.setColor(loadingColor);
		g.fillRect(270, 540, (loaded / total) * 300, 10);
		g.setColor(Color.black);
		g.drawRect(270, 540, 300, 10);
		g.setColor(Color.white);
		if (loaded == total) {
			if (show) {
				g.drawString("Press Enter to Play", 340, 510);
			}
		} else {
			g.drawString("Loading. Please wait ...", 340, 510);
		}
	}

	
	public void keyPressed(int key, char c) {
		if (Input.KEY_ESCAPE == key && loaded == total) {
			game.exit();
		}
	}

}
