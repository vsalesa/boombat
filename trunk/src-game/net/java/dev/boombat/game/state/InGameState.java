package net.java.dev.boombat.game.state;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.Camera;
import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.entity.Tank;
import net.java.dev.boombat.game.entity.layer.MissileList;
import net.java.dev.boombat.game.entity.layer.NodeList;
import net.java.dev.boombat.game.entity.layer.ParticleList;
import net.java.dev.boombat.game.entity.layer.PlayerList;
import net.java.dev.boombat.game.ui.ext.ChangeMapBox;
import net.java.dev.boombat.game.ui.ext.ChangeTeamBox;
import net.java.dev.boombat.game.ui.ext.ChatBoard;
import net.java.dev.boombat.game.ui.ext.ChatBox;
import net.java.dev.boombat.game.ui.ext.ConfirmationBox;
import net.java.dev.boombat.game.ui.ext.FlashMessageBox;
import net.java.dev.boombat.game.ui.ext.KeyInfoBox;
import net.java.dev.boombat.game.ui.ext.OptionsBox;
import net.java.dev.boombat.game.ui.ext.PlayerBar;
import net.java.dev.boombat.game.ui.ext.Radar;
import net.java.dev.boombat.game.ui.ext.ScoreList;
import net.java.dev.boombat.game.ui.ext.SystemMessageBoard;
import net.java.dev.boombat.game.ui.ext.TimerBox;
import net.java.dev.boombat.game.util.MusicPlayer;
import net.java.dev.boombat.multiplayer.Manager;
import net.java.dev.boombat.multiplayer.core.GameSession;
import net.java.dev.boombat.multiplayer.message.FireBulletMsg;
import net.java.dev.boombat.multiplayer.message.FireRocketMsg;
import net.java.dev.boombat.multiplayer.message.SessionFlagMsg;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * 
 * @author objectworks
 */
public abstract class InGameState extends BasicGameState {

	protected GameContainer gameContainer;
	protected StateBasedGame stateBasedGame;
	protected KeyInfoBox keyInfoBox;
	protected Radar radar;
	protected PlayerBar playerBar;
	protected ChatBox chatBox;
	protected ChangeTeamBox changeTeamBox;
	protected ScoreList scoreList;
	protected OptionsBox optionsBox;
	protected TimerBox timerBox;
	protected ChatBoard chatBoard;
	protected SystemMessageBoard systemMessageBoard;
	protected ConfirmationBox confirmationBox;
	protected ChangeMapBox changeMapBox;
	protected boolean enableInput = true;



	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		initUI(container, game);
		
	}

	public void initUI(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.gameContainer = container;
		this.stateBasedGame = game;
		keyInfoBox = new KeyInfoBox(container, 65, 30);
		radar = new Radar(673, 5);
		radar.setVisible(Config.getOption().getBoolean("playerRadar"));
		playerBar = new PlayerBar(547, 490);
		playerBar.setVisible(true);
		chatBox = new ChatBox(container, 10, 560);
		changeTeamBox = new ChangeTeamBox(container, 150, 80);
		scoreList = new ScoreList(120, 30);
		optionsBox = new OptionsBox(container, 150, 80);
		timerBox = new TimerBox(10, 10);
		timerBox.setVisible(true);

		// add UI Listeners ...
		keyInfoBox.addOkListener(new ComponentListener() {

			public void componentActivated(AbstractComponent comp) {
				keyInfoBox.setVisible(false);
				enableInput = true;
				gameContainer.setMouseGrabbed(true);
			}
		});

		chatBox.addCloseListener(new ComponentListener() {

			public void componentActivated(AbstractComponent comp) {
				chatBox.setVisible(false);
				enableInput = true;
				gameContainer.setMouseGrabbed(true);
			}
		});

		chatBox.addListener(new ComponentListener() {

			public void componentActivated(AbstractComponent comp) {
				if (chatBox.getText().trim().equals("")) {
					return;
				}

				// add to chat board
				String chat = Config.getOption().getString("playerName")
						+ " : " + chatBox.getText();

				// send chat message
				Manager.sendChat(chat);

				// reset chat text
				chatBox.clearText();
			}
		});

		changeTeamBox.addCancelListener(new ComponentListener() {

			public void componentActivated(AbstractComponent comp) {
				changeTeamBox.setVisible(false);
				enableInput = true;
				gameContainer.setMouseGrabbed(true);
			}
		});

		changeTeamBox.addOkListener(new ComponentListener() {

			public void componentActivated(AbstractComponent comp) {
				// change team and tank
				Manager.changeLocalPlayerTeam(changeTeamBox.getSelectedTeam(),
						changeTeamBox.getSelectedTank());

				// enable input
				changeTeamBox.setVisible(false);
				enableInput = true;
				gameContainer.setMouseGrabbed(true);
			}
		});

		optionsBox.addOkListener(new ComponentListener() {

			public void componentActivated(AbstractComponent comp) {
				// save options
				optionsBox.setVisible(false);
				String name = optionsBox.getOptions().getString("playerName");
				optionsBox.getOptions().save();
				// change local player name
				Manager.changeLocalPlayerName(name);
				enableInput = true;
				gameContainer.setMouseGrabbed(true);
			}
		});

		chatBoard = new ChatBoard(10, 50, 5);
		chatBoard.setVisible(true);

		systemMessageBoard = new SystemMessageBoard(135, 5);
		systemMessageBoard.setVisible(true);

		FlashMessageBox.instance();

		confirmationBox = new ConfirmationBox(container, 190, 140, "");
		confirmationBox.addOkListener(new ComponentListener() {

			public void componentActivated(AbstractComponent comp) {
				confirmationBox.setVisible(false);
				enableInput = false;
				if (Manager.isClient()) {
					Manager.leaveGame();
					stateBasedGame.enterState(JoinGameMenuState.ID,
							new FadeOutTransition(Color.black),
							new FadeInTransition(Color.black));
				} else {
					Manager.endGame();
					stateBasedGame.enterState(CreateGameMenuState.ID,
							new FadeOutTransition(Color.black),
							new FadeInTransition(Color.black));

				}
			}
		});

		confirmationBox.addCancelListener(new ComponentListener() {

			public void componentActivated(AbstractComponent comp) {
				confirmationBox.setVisible(false);
				enableInput = true;
				gameContainer.setMouseGrabbed(true);
			}
		});

		changeMapBox = new ChangeMapBox(container, 150, 80);
		changeMapBox.addOkListener(new ComponentListener() {

			public void componentActivated(AbstractComponent comp) {
				changeMapBox.setVisible(false);
				enableInput = false;
				Manager.switchMap(changeMapBox.getSelectedMap());
				Manager.sendGameState(SessionFlagMsg.NEW_ROUND);
				GameSession.newRound();
				enableInput = true;
			}
		});
		changeMapBox.addCancelListener(new ComponentListener() {

			public void componentActivated(AbstractComponent comp) {
				changeMapBox.setVisible(false);
				enableInput = true;
			}
		});

	}

	protected void playGame(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		// update localTank
		Player player = PlayerList.getLocalPlayer();
		if (enableInput) {
			if (Player.STATE_LIFE == player.getState()) {
				player.getTank().update(container.getInput(), delta);

				Camera.instance().setX(player.getTank().getX());
				Camera.instance().setY(player.getTank().getY());

				// send tank's flags over network
				Manager.sendTankPosition(player);

			} else {
				Camera.instance().update(container.getInput(), delta);
				Manager.keepAlive();
				if (Camera.instance().getX() < 2
						&& Camera.instance().getY() < 2) {
					Camera.instance().setX(2);
					Camera.instance().setY(2);
				}
			}
		}
		PlayerList.updateRemotePlayers(delta);
		MissileList.updateAll(delta);
		ParticleList.updateAll(delta);
		updateUI(container, game, delta);
		MusicPlayer.update(container);
	}

	protected void updateUI(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		if (Config.isPlayerRadar()) {
			radar.update(delta);
		}
		playerBar.update(delta);
		chatBox.update(delta);
		changeTeamBox.update(delta);
		changeMapBox.update(delta);
		optionsBox.update(container, delta);
		timerBox.update(GameSession.getCurrentRemainingTime());

		// update score list visible ...
		if (container.getInput().isKeyDown(Config.getKey("showScoreKey"))) {
			scoreList.setVisible(true);
		} else {
			scoreList.setVisible(false);
		}
		FlashMessageBox.instance().update(delta);
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if (GameSession.isSessionActive()) {
			renderGameObject(container, game, g);
			renderUI(container, game, g);
		}
	}

	protected void renderGameObject(GameContainer container,
			StateBasedGame game, Graphics g) throws SlickException {
		Tank refTank = PlayerList.getLocalPlayer().getTank();
		float xpos = refTank.getX();
		float ypos = refTank.getY();
		int size = Tank.SIZE;
		if (Player.STATE_LIFE != PlayerList.getLocalPlayer().getState()) {
			xpos = Camera.instance().getX();
			ypos = Camera.instance().getY();
			size = 64;
		}
		Config.getSelectedGameMap().render(g, xpos, ypos, size);
		PlayerList.renderLocalPlayer(g);
		PlayerList.renderRemotePlayers(g);
		NodeList.renderAll(g);
		MissileList.renderAll(g);
		ParticleList.renderAll(g);
		if (Player.STATE_LIFE != PlayerList.getLocalPlayer().getState()) {
			Camera.instance().render(g);
		}
		g.resetTransform();
	}

	protected void renderUI(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		playerBar.render(g);
		if (Config.isPlayerRadar()) {
			radar.render(g);
		}
		timerBox.render(g);
		chatBoard.render(g);
		keyInfoBox.render(container, g);
		chatBox.render(container, g);
		systemMessageBoard.render(g);
		changeTeamBox.render(container, g);
		changeMapBox.render(container, g);
		optionsBox.render(container, g);
		scoreList.render(g);
		confirmationBox.render(container, g);
		FlashMessageBox.instance().render(g);
	}

	public void keyReleased(int key, char c) {
		if (enableInput) {
			Player player = PlayerList.getLocalPlayer();
			if (Player.STATE_LIFE == player.getState()) {
				if (Config.getKey("fireBulletKey") == key) {
					player.getTank().fireBullet();
					Manager.sendFireState(player.getId(), FireBulletMsg.ID);
				}
				if (Config.getKey("fireRocketKey") == key) {
					player.getTank().fireRocket();
					Manager.sendFireState(player.getId(), FireRocketMsg.ID);
				}
			}
			if (Config.getKey("showControlKey") == key) {
				keyInfoBox.setVisible(true);
				enableInput = false;
				gameContainer.setMouseGrabbed(false);
			}
			if (Config.getKey("showChatKey") == key) {
				chatBox.setVisible(true);
				enableInput = false;
				gameContainer.setMouseGrabbed(false);
			}
			if (Config.getKey("showTeamKey") == key) {
				changeTeamBox.setVisible(true);
				enableInput = false;
				gameContainer.setMouseGrabbed(false);
			}
			if (Config.getKey("showOptionsKey") == key) {
				optionsBox.setVisible(true);
				enableInput = false;
				gameContainer.setMouseGrabbed(false);
			}

			if (Config.getKey("endGameKey") == key) {
				confirmationBox.setVisible(true);
				enableInput = false;
				gameContainer.setMouseGrabbed(false);
			}

			if (!Manager.isClient()) {
				if (Config.getKey("restartRoundKey") == key) {
					Manager.sendGameState(SessionFlagMsg.RESTART_ROUND);
					FlashMessageBox.instance().showFlashMessage(2000,
							"Round restarted");
					GameSession.restartRound();
				}
				if (Config.getKey("newRoundKey") == key) {
					GameSession.calculateScore();
					GameSession.roundNotify();
					Manager.sendGameState(SessionFlagMsg.NEW_ROUND);
					GameSession.newRound();
				}
				if (Config.getKey("showMapKey") == key) {
					changeMapBox.setVisible(true);
					enableInput = false;
					gameContainer.setMouseGrabbed(false);
				}
			}
		}
	}

	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		container.setMouseGrabbed(true);
		optionsBox.updateNewVolume(container);
		if (Config.isPlayerRadar()) {
			systemMessageBoard.setY(135);
		} else {
			systemMessageBoard.setY(5);
		}
		if (Manager.isClient()) {
			confirmationBox.setConfirmation("Leave Session ?");
		} else {
			confirmationBox.setConfirmation("End Session?");
		}
		enableInput = true;
	}

	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		container.setMouseGrabbed(false);
	}

	
	
}
