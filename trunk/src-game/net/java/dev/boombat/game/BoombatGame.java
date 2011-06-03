package net.java.dev.boombat.game;

import net.java.dev.boombat.game.state.CreateGameMenuState;
import net.java.dev.boombat.game.state.CreditsState;
import net.java.dev.boombat.game.state.HelpState;
import net.java.dev.boombat.game.state.InGameCreatorState;
import net.java.dev.boombat.game.state.InGameJoinerState;
import net.java.dev.boombat.game.state.IntroState;
import net.java.dev.boombat.game.state.JoinGameMenuState;
import net.java.dev.boombat.game.state.LoadingState;
import net.java.dev.boombat.game.state.MainMenuState;
import net.java.dev.boombat.game.state.OptionsMenuState;
import net.java.dev.boombat.game.state.QuitGameState;
import net.java.dev.boombat.game.util.MusicPlayer;
import net.java.dev.boombat.game.util.PropertiesFile;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.StateBasedGame;

/**
 * 
 * @author objectworks
 */
public class BoombatGame extends StateBasedGame {

	public BoombatGame(String name) {
		super(name);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		// hide mouse
		container.setMouseGrabbed(true);
		// deferred loading = true
		LoadingList.setDeferredLoading(true);
		// init resources
		ResourceManager.init();
		// init MusicPlayer
		MusicPlayer.init();
		// add states
		addState(new IntroState());
		addState(new LoadingState());
		addState(new MainMenuState());
		addState(new CreateGameMenuState());
		addState(new JoinGameMenuState());
		addState(new OptionsMenuState());
		addState(new HelpState());
		addState(new CreditsState());
		addState(new QuitGameState());
		addState(new InGameCreatorState());
		addState(new InGameJoinerState());

	}

	public static void loadOptions(AppGameContainer game) throws SlickException {
		// load options
		PropertiesFile opt = Config.getOption();
		boolean fullscreen = opt.getBoolean("fullscreen");
		boolean vSync = opt.getBoolean("vSync");
		boolean showFPS = opt.getBoolean("showFPS");
		boolean shuffle = opt.getBoolean("shuffle");
		float sound = opt.getFloat("soundVolume");
		float music = opt.getFloat("musicVolume");
		if (shuffle) {
			MusicPlayer.toggleShuffle();
		} else {
			MusicPlayer.toggleRepeatAll();
		}
		// set icon
		// game.setIcon("data/image/ui/icon.png");
		game.setUpdateOnlyWhenVisible(false);
		game.setMusicVolume(music);
		game.setSoundVolume(sound);
		game.setDisplayMode(800, 600, fullscreen);
		game.setShowFPS(showFPS);
		game.setVSync(vSync);
		game.setVerbose(false);
		game.setMaximumLogicUpdateInterval(20);
		game.setMinimumLogicUpdateInterval(20);
		// game.setTargetFrameRate(100);
	}

	@Override
	public boolean closeRequested() {
		return false;
	}

}
