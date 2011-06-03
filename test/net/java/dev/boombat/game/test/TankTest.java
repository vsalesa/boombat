/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.test;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.GameMap;
import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.entity.Tank;
import net.java.dev.boombat.game.entity.layer.MissileList;
import net.java.dev.boombat.game.entity.layer.NodeList;
import net.java.dev.boombat.game.entity.layer.ParticleList;
import net.java.dev.boombat.game.entity.layer.PlayerList;
import net.java.dev.boombat.game.util.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * 
 * @author Administrator
 */
public class TankTest extends BasicGame {

        private float x;
        private float y;        
        private float bodyAngle;        
        private float turretAngle;        
            
	public static void main(String... args) throws SlickException {
		AppGameContainer game = new AppGameContainer(new TankTest(),800,600,false);
                game.start();
	}

	public TankTest() {
		super("Tank Test");
	}

	@Override
	public void init(GameContainer c) throws SlickException {

		ResourceManager.init();
                x = 10;
                y = 10;
                bodyAngle = 90;
                turretAngle = 90;

		Config.setSelectedGameMap("warehouse");
		GameMap map = Config.getSelectedGameMap();
		float pos[][] = map.generateRandomPositions(2);

		Tank tank = ResourceManager.createTank("heavy.T");
		//tank.setX(pos[0][0]);
		//tank.setY(pos[0][1]);
                tank.setX(x);
		tank.setY(y);
                tank.setBodyAngle(bodyAngle);
                tank.setTurretAngle(turretAngle);

		Player player = new Player((byte) 1, "TankTest", tank);
		player.setState(Player.STATE_LIFE);
		player.setTeam(Player.TEAM_TERRORIST);
                PlayerList.addPlayer(player);
	}

	@Override
	public void update(GameContainer c, int delta) throws SlickException {
		Player p = PlayerList.getLocalPlayer();
		
		Tank tank = p.getTank();
		Input input = c.getInput();
		if (Player.STATE_LIFE == p.getState()) {
			tank.update(input, delta);
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
	}

	public void render(GameContainer c, Graphics g) throws SlickException {
		GameMap campMap = Config.getSelectedGameMap();
		Player p = PlayerList.getLocalPlayer();
		
		Tank tank = p.getTank();
		
		campMap.render(g, tank.getX(), tank.getY(), Tank.SIZE);

		if (Player.STATE_SPECTATOR != p.getState()) {
			tank.render(g);
		}
		
		NodeList.renderAll(g);
		MissileList.renderAll(g);
		ParticleList.renderAll(g);
		g.resetTransform();
		drawStatusBar(g, p);
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
		g.drawString("Tank : " + tank.getName(), 10, 90);
		g.drawString("Team : " + player.getTeamName(), 10, 105);
		g.drawString("Current Bullet : " + tank.getCurrentBulletAmmo(),
						10, 135);
		g.drawString("Current Rocket : " + tank.getCurrentRocketAmmo(),
						10, 150);
		g.drawString("Current Position (x, y, body angle, turret angle) : ("
				+ tank.getX() + ", " + tank.getY() + ", " + tank.getBodyAngle()
				+ ", " + tank.getTurretAngle() + " )", 10, 165);
		g.drawString("Press Space or Escape to Toggle Fullscreen or Quit Game ", 10,
				195);

	}

	@Override
	public void keyReleased(int key, char c) {

		Player p = PlayerList.getLocalPlayer();

		if (Player.STATE_LIFE != p.getState()) {
			return;
		}

		Tank tank = p.getTank();

		if (Config.getKey("fireBulletKey") == key) {
			tank.fireBullet();
		} else if (Config.getKey("fireRocketKey") == key) {
			tank.fireRocket();
		}

	}
}
