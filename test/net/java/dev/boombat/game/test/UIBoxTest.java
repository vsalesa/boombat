package net.java.dev.boombat.game.test;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.entity.Tank;
import net.java.dev.boombat.game.entity.layer.MissileList;
import net.java.dev.boombat.game.entity.layer.NodeList;
import net.java.dev.boombat.game.entity.layer.ParticleList;
import net.java.dev.boombat.game.entity.layer.PlayerList;
import net.java.dev.boombat.game.ui.ext.ChangeMapBox;
import net.java.dev.boombat.game.ui.ext.ChangeTeamBox;
import net.java.dev.boombat.game.ui.ext.ChatBox;
import net.java.dev.boombat.game.ui.ext.ConfirmationBox;
import net.java.dev.boombat.game.ui.ext.KeyInfoBox;
import net.java.dev.boombat.game.ui.ext.OptionsBox;
import net.java.dev.boombat.game.ui.ext.PlayerBar;
import net.java.dev.boombat.game.ui.ext.Radar;
import net.java.dev.boombat.game.ui.ext.ScoreList;
import net.java.dev.boombat.game.ui.ext.TimerBox;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.multiplayer.core.Util;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * 
 * @author objectworks
 */
public class UIBoxTest extends BasicGame {

	private ChangeMapBox mapBox;

	private ChangeTeamBox teamBox;

	private ChatBox chatBox;

	private ConfirmationBox confirmationBox;

	private KeyInfoBox keyInfoBox;

	private PlayerBar playerBar;

	private Radar radar;

	private TimerBox timer;

	private OptionsBox optionsBox;

	private ScoreList scoreList;

	private int startTime;

	public static void main(String... args) throws SlickException {
		AppGameContainer game = new AppGameContainer(
				new UIBoxTest("UIBox Test"), 800, 600, false);
		game.start();
	}

	public UIBoxTest(String name) {
		super(name);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		ResourceManager.init();
		Config.setSelectedGameMap("warehouse");
		container.setShowFPS(false);
		mapBox = new ChangeMapBox(container, 150, 80);
		teamBox = new ChangeTeamBox(container, 150, 80);
		chatBox = new ChatBox(container, 10, 560);
		confirmationBox = new ConfirmationBox(container, 190, 140, "End Game ?");
		keyInfoBox = new KeyInfoBox(container, 65, 30);
		playerBar = new PlayerBar(547, 490);
		playerBar.setVisible(true);
		radar = new Radar(663, 5);
		radar.setVisible(true);

		timer = new TimerBox(10, 10);
		timer.setVisible(true);

		optionsBox = new OptionsBox(container, 150, 80);

		scoreList = new ScoreList(120, 30);

		Player p = new Player((byte) 0, "Murti Daryandono", ResourceManager
				.createTank("heavy.CT"));
		p.setTeam(Player.TEAM_COUNTER_TERRORIST);
		p.getTank().setX(20);
		p.getTank().setY(40);
		p.setState(Player.STATE_LIFE);
		PlayerList.addPlayer(p);

		float[][] pos = Config.getSelectedGameMap().generateRandomPositions(19);
		for (int i = 0; i < 9; i++) {
			Player pCT = new Player((short) 0, (short) 0, (byte) 100,
					Player.NO_TEAM, Player.STATE_SPECTATOR, (byte) i, "T-bots"
							+ i, ResourceManager.createTank("destroyer.CT"),
					true);
			pCT.setTeam(Player.TEAM_COUNTER_TERRORIST);
			pCT.getTank().setX(pos[i][0]);
			pCT.getTank().setY(pos[i][1]);
			pCT.setState(Player.STATE_LIFE);
			PlayerList.addPlayer(pCT);
		}

		for (int i = 9; i < 19; i++) {
			Player pT = new Player((short) 0, (short) 0, (byte) 100,
					Player.NO_TEAM, Player.STATE_SPECTATOR, (byte) i, "CT-bots"
							+ i, ResourceManager.createTank("lite.T"), true);
			pT.setTeam(Player.TEAM_TERRORIST);
			pT.getTank().setX(pos[i][0]);
			pT.getTank().setY(pos[i][1]);
			pT.setState(Player.STATE_LIFE);
			PlayerList.addPlayer(pT);
		}

		Config
				.setSelectedServerName(Config.getOption().getString(
						"serverName"));

	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		Input input = container.getInput();
		startTime += delta;
		if (input.isKeyDown(Input.KEY_M)) {
			mapBox.setVisible(true);
		} else {
			mapBox.setVisible(false);
		}

		if (input.isKeyDown(Input.KEY_T)) {
			teamBox.setVisible(true);
		} else {
			teamBox.setVisible(false);
		}

		if (input.isKeyDown(Input.KEY_C)) {
			chatBox.setVisible(true);
		} else {
			chatBox.setVisible(false);
		}

		if (input.isKeyDown(Input.KEY_O)) {
			optionsBox.setVisible(true);
		} else {
			optionsBox.setVisible(false);
		}

		if (input.isKeyDown(Input.KEY_Q)) {
			confirmationBox.setVisible(true);
		} else {
			confirmationBox.setVisible(false);
		}

		if (input.isKeyDown(Input.KEY_K)) {
			keyInfoBox.setVisible(true);
		} else {
			keyInfoBox.setVisible(false);
		}

		if (input.isKeyDown(Input.KEY_TAB)) {
			scoreList.setVisible(true);
		} else {
			scoreList.setVisible(false);
		}

		if (input.isKeyDown(Input.KEY_F1)) {
			container.setFullscreen(!container.isFullscreen());
		}

		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			container.exit();
		}

		PlayerList.getLocalPlayer().getTank().update(input, delta);
		MissileList.updateAll(delta);
		ParticleList.updateAll(delta);
		timer.update(startTime);
		playerBar.update(delta);
		radar.update(delta);
		teamBox.update(delta);
		mapBox.update(delta);
		chatBox.update(delta);
		optionsBox.update(container, delta);

	}

	public void render(GameContainer container, Graphics g)
			throws SlickException {
		Player p = PlayerList.getLocalPlayer();
		Tank t = p.getTank();
		Config.getSelectedGameMap().render(g, t.getX(), t.getY(), 32);
		t.render(g);
		PlayerList.renderRemotePlayers(g);
		NodeList.renderAll(g);
		MissileList.renderAll(g);
		ParticleList.renderAll(g);

		g.resetTransform();
		playerBar.render(g);
		radar.render(g);
		timer.render(g);
		scoreList.render(g);
		mapBox.render(container, g);
		teamBox.render(container, g);
		chatBox.render(container, g);
		confirmationBox.render(container, g);
		keyInfoBox.render(container, g);
		optionsBox.render(container, g);

	}

	@Override
	public void keyReleased(int key, char c) {
		Player p = PlayerList.getLocalPlayer();
		Tank t = p.getTank();
		if (Config.getKey("fireBulletKey") == key) {
			t.fireBullet();
		}

		if (Config.getKey("fireRocketKey") == key) {
			t.fireRocket();
		}

		if (Input.KEY_X == key) {
			Util.applyRandomTankPosition();
		}
	}
}
