
package net.java.dev.boombat.game.test;

import java.io.FileWriter;
import java.io.IOException;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.GameMap;
import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.entity.Tank;
import net.java.dev.boombat.game.entity.layer.MissileList;
import net.java.dev.boombat.game.entity.layer.NodeList;
import net.java.dev.boombat.game.entity.layer.ParticleList;
import net.java.dev.boombat.game.entity.layer.PlayerList;
import net.java.dev.boombat.game.ui.core.SoundBar;
import net.java.dev.boombat.game.util.MusicPlayer;
import net.java.dev.boombat.game.util.PropertiesFile;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

/**
 * 
 * @author Administrator
 */
public class SingleGameTest extends BasicGame {

	private boolean player2Active;

	SoundBar soundScroller;
	
	FileWriter writer;

	public static void main(String... args) throws SlickException {
		Log.setVerbose(false);
		AppGameContainer game = new AppGameContainer(new SingleGameTest(),
				Config.SCREEN_WIDTH, Config.SCREEN_HEIGTH, Config.getOption()
						.getBoolean("fullscreen"));
		game.setMusicVolume(0.2f);
		game.setVerbose(false);
		game.start();
	}

	public SingleGameTest() {
		super("just test to make sure that everything are fine");
	}

	@Override
	public void init(GameContainer c) throws SlickException {
		Long s = System.currentTimeMillis();
		PropertiesFile option = Config.getOption();
		// c.setMaximumLogicUpdateInterval(20);
		c.setShowFPS(option.getBoolean("showFPS"));
		c.setVSync(option.getBoolean("VSync"));
		c.setSoundOn(option.getBoolean("sound"));
		c.setMusicOn(option.getBoolean("music"));
		c.setMouseGrabbed(false);

		Long start = System.currentTimeMillis();
		ResourceManager.init();
		Log.info("Resource Initialisation took "
				+ (System.currentTimeMillis() - start) + " ms");

		Config.setSelectedGameMap("warehouse");
		GameMap map = Config.getSelectedGameMap();
		float pos[][] = map.generateRandomPositions(2);

		start = System.currentTimeMillis();
		Tank normalTank = ResourceManager.createTank("heavy.T");
		normalTank.setX(pos[0][0]);
		normalTank.setY(pos[0][1]);
		Log.info("Tank" + normalTank.getName() + "creation took "
				+ (System.currentTimeMillis() - start) + " ms");

		start = System.currentTimeMillis();
		Player localPlayer = new Player((byte) 1, option
				.getString("playerName"), normalTank);
		localPlayer.setState(Player.STATE_LIFE);
		localPlayer.setTeam(Player.TEAM_TERRORIST);
		Log.info("create Player " + localPlayer.getName() + " took "
				+ (System.currentTimeMillis() - start) + " ms");

		start = System.currentTimeMillis();
		Tank normalTank2 = ResourceManager.createTank("destroyer.T");
		normalTank2.setX(pos[1][0]);
		normalTank2.setY(pos[1][1]);
		Log.info("Tank" + normalTank2.getName() + "creation took "
				+ (System.currentTimeMillis() - start) + " ms");

		start = System.currentTimeMillis();
		Player remotePlayer = new Player((short) 0, (short) 0, (byte) 100,
				Player.NO_TEAM, Player.STATE_SPECTATOR, (byte) 3,
				"Spider Plant Man", normalTank2, true);
		remotePlayer.setState(Player.STATE_LIFE);
		remotePlayer.setTeam(Player.TEAM_TERRORIST);
		Log.info("create Player " + remotePlayer.getName() + " took "
				+ (System.currentTimeMillis() - start) + " ms");

		start = System.currentTimeMillis();
		Tank normalTank3 = ResourceManager.createTank("blitz.CT");
		normalTank3.setX(20);
		normalTank3.setY(40);
		Log.info("Tank" + normalTank3.getName() + "creation took "
				+ (System.currentTimeMillis() - start) + " ms");

		start = System.currentTimeMillis();
		Player remotePlayer2 = new Player((short) 0, (short) 0, (byte) 100,
				Player.NO_TEAM, Player.STATE_SPECTATOR, (byte) 4,
				"Robot No Move", normalTank3, true);
		remotePlayer2.setState(Player.STATE_LIFE);
		remotePlayer2.setTeam(Player.TEAM_COUNTER_TERRORIST);
		Log.info("create Player " + remotePlayer2.getName() + " took "
				+ (System.currentTimeMillis() - start) + " ms");

		start = System.currentTimeMillis();
		PlayerList.addPlayer(localPlayer);
		PlayerList.addPlayer(remotePlayer);
		PlayerList.addPlayer(remotePlayer2);
		Log.info("Adding Players took " + (System.currentTimeMillis() - start)
				+ " ms");

		Log.info("All Initialisation and write logs took  "
				+ (System.currentTimeMillis() - s) + " ms");
		MusicPlayer.init();
		MusicPlayer.next();
		// MusicPlayer.next();
		MusicPlayer.toggleShuffle();
		soundScroller = new SoundBar(c, 700, 590, Color.darkGray,
				Color.lightGray, 7, 3, 2);
		soundScroller.setValue(c.getMusicVolume() * 100f);
		
		try {
			writer = new FileWriter(ResourceLoader.getResource("config/result.txt").toURI().getPath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void update(GameContainer c, int delta) throws SlickException {
		Player p = PlayerList.getLocalPlayer();
		if (player2Active) {
			p = PlayerList.getPlayer((byte) 3);
		}
		Tank normalTank = p.getTank();
		Input input = c.getInput();
		if (Player.STATE_LIFE == p.getState()) {
			normalTank.update(input, delta);
		}
		MissileList.updateAll(delta);
		ParticleList.updateAll(delta);
		if (input.isKeyDown(Input.KEY_SPACE)) {
			AppGameContainer ac = (AppGameContainer) c;
			boolean fs = !ac.isFullscreen();
			ac.setFullscreen(fs);
			Config.getOption().setBoolean("fullscreen", fs);
		}

		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			AppGameContainer ac = (AppGameContainer) c;
			Config.getOption().save();
			ac.exit();
		}

		MusicPlayer.update(c);
		c.setMusicVolume(soundScroller.getValue() / 100f);
		try {
			writer.append("\nFPS: "+c.getFPS());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void render(GameContainer c, Graphics g) throws SlickException {
		GameMap campMap = Config.getSelectedGameMap();
		Player p = PlayerList.getLocalPlayer();
		Player p2 = PlayerList.getPlayer((byte) 3);
		Player p3 = PlayerList.getPlayer((byte) 4);
		if (player2Active) {
			Player tmp = p;
			p = p2;
			p2 = tmp;
		}
		Tank normalTank = p.getTank();
		Tank normalTank2 = p2.getTank();
		Tank normalTank3 = p3.getTank();
		campMap.render(g, normalTank.getX(), normalTank.getY(), Tank.SIZE);

		if (Player.STATE_SPECTATOR != p.getState()) {
			normalTank.render(g);
		}
		if (Player.STATE_SPECTATOR != p2.getState()) {
			normalTank2.render(g);
		}
		if (Player.STATE_SPECTATOR != p3.getState()) {
			normalTank3.render(g);
		}

		NodeList.renderAll(g);
		MissileList.renderAll(g);
		ParticleList.renderAll(g);
		g.resetTransform();
		drawStatusBar(g, p);
		soundScroller.render(c, g);
	}

	public void drawStatusBar(Graphics g, Player player) {
		GameMap map = Config.getSelectedGameMap();
		Image image = ResourceManager.getMapImages(Config
				.getSelectedGameMapKey());
		String mapName = map.getName();
		Tank tank = player.getTank();
		image.draw(590, 20);
		g.drawRect(590, 20, 200, image.getHeight());
		g.drawString("Map Info : " + mapName, 590, image.getHeight() + 30);
		g.drawString("Player Name : " + player.getName(), 10, 30);
		g.drawString("Kill : " + player.getKill(), 10, 45);
		g.drawString("Dead : " + player.getDead(), 10, 60);
		g.drawString("Life : " + player.getLife(), 10, 75);
		g.drawString("Tank : " + tank.getName(), 10, 90);
		g.drawString("Team : " + player.getTeamName(), 10, 105);
		g.drawString("Friendly Fire : "
				+ Config.getOption().getBoolean("friendlyFire"), 10, 120);
		g
				.drawString("Current Bullet : " + tank.getCurrentBulletAmmo(),
						10, 135);
		g
				.drawString("Current Rocket : " + tank.getCurrentRocketAmmo(),
						10, 150);
		g.drawString("Current Position (x, y, body angle, turret angle) : ("
				+ tank.getX() + ", " + tank.getY() + ", " + tank.getBodyAngle()
				+ ", " + tank.getTurretAngle() + " )", 10, 165);
		g.drawString("Press 1 or 2 to activate Player 1 or 2 ", 10, 180);
		g.drawString(
				"Press Space or Escape to Toggle Fullscreen or Quit Game ", 10,
				195);

	}

	@Override
	public void keyReleased(int key, char c) {

		if (Input.KEY_2 == key) {
			player2Active = true;
		}

		if (Input.KEY_1 == key) {
			player2Active = false;
		}

		Player p = PlayerList.getLocalPlayer();
		if (player2Active) {
			p = PlayerList.getPlayer((byte) 3);
		}

		if (Player.STATE_LIFE != p.getState()) {
			return;
		}

		Tank normalTank = p.getTank();

		if (Config.getKey("fireBulletKey") == key) {
			normalTank.fireBullet();
		} else if (Config.getKey("fireRocketKey") == key) {
			normalTank.fireRocket();
		}

	}
	
	@Override
	public boolean closeRequested() {
		try {
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.closeRequested();
	}
}
