package net.java.dev.boombat.game.entity.layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.java.dev.boombat.game.entity.GameMap;
import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.entity.Tank;

import org.newdawn.slick.Graphics;

/**
 * 
 * @author objectworks
 */
public class PlayerList {

	private static Player localPlayer;
	private static List<Player> remotePlayers = Collections
			.synchronizedList(new ArrayList<Player>());

	public static Player isCollide(byte sourceId, float newX, float newY,
			float newRadius) {
		Tank tank = localPlayer.getTank();
		float xpos;
		float ypos;
		float radius = Tank.HALF_SIZE;
		// check with local player first
		if (tank != null) {
			xpos = tank.getX();
			ypos = tank.getY();
			if (Player.STATE_SPECTATOR != localPlayer.getState()
					&& sourceId != localPlayer.getId()
					&& collide(newX, newY, newRadius, xpos, ypos, radius)) {
				return localPlayer;
			}
		}

		for (Player p : remotePlayers) {
			tank = p.getTank();
			if (tank != null) {
				xpos = tank.getX();
				ypos = tank.getY();

				if (Player.STATE_SPECTATOR != p.getState()
						&& sourceId != p.getId()
						&& collide(newX, newY, newRadius, xpos, ypos, radius)) {
					return p;
				}
			}
		}

		return null;
	}

	private static boolean collide(float newX, float newY, float newRadius,
			float xpos, float ypos, float distance) {
		newX = newX * GameMap.TILE_SIZE;
		newY = newY * GameMap.TILE_SIZE;
		xpos = xpos * GameMap.TILE_SIZE;
		ypos = ypos * GameMap.TILE_SIZE;
		float dx = xpos - newX;
		float dy = ypos - newY;
		float minDistance = newRadius + distance;
		return ((dx * dx + dy * dy) < minDistance * minDistance);
	}

	public static void addPlayer(Player player) {
		if (!player.isRemotePlayer()) {
			localPlayer = player;
			return;
		}
		remotePlayers.add(player);

	}

	public static Player getPlayer(byte playerId) {
		if (localPlayer != null && localPlayer.getId() == playerId) {
			return localPlayer;
		}

		for (Player p : remotePlayers) {
			if (playerId == p.getId()) {
				return p;
			}
		}
		return null;
	}

	public static void renderLocalPlayer(Graphics g) {
		if (localPlayer != null) {
			if (localPlayer.getTank() != null
					&& Player.STATE_SPECTATOR != localPlayer.getState()) {
				localPlayer.getTank().render(g);
			}
		}
	}

	public static void renderRemotePlayers(Graphics g) {
		for (Player p : remotePlayers) {
			Tank t = p.getTank();
			if (t != null && Player.STATE_SPECTATOR != p.getState()) {
				t.render(g);
			}
		}
	}

	public static void updateRemotePlayers(int delta) {
		for (Player p : remotePlayers) {
			Tank t = p.getTank();
			if (t != null && Player.STATE_SPECTATOR != p.getState()) {
				t.update(delta);
			}
		}
	}

	public static List<Player> getRemotePlayers() {
		return remotePlayers;
	}

	public static List<Player> getTerroristPlayers() {
		List<Player> terroristPlayer = new ArrayList<Player>();

		// add local player first if match
		Player localPlayer = PlayerList.getLocalPlayer();
		if (localPlayer != null) {
			if (Player.TEAM_TERRORIST == localPlayer.getTeam()) {
				terroristPlayer.add(localPlayer);
			}
		}

		// add remote players if match
		for (Player remotePlayer : remotePlayers) {
			if (Player.TEAM_TERRORIST == remotePlayer.getTeam()) {
				terroristPlayer.add(remotePlayer);
			}
		}

		return terroristPlayer;
	}

	public static List<Player> getCounterTerroristPlayers() {
		List<Player> counterTerroristPlayer = new ArrayList<Player>();

		// add local player first if match
		Player localPlayer = PlayerList.getLocalPlayer();
		if (localPlayer != null) {
			if (Player.TEAM_COUNTER_TERRORIST == localPlayer.getTeam()) {
				counterTerroristPlayer.add(localPlayer);
			}
		}

		// add remote players if match
		for (Player remotePlayer : remotePlayers) {
			if (Player.TEAM_COUNTER_TERRORIST == remotePlayer.getTeam()) {
				counterTerroristPlayer.add(remotePlayer);
			}
		}

		return counterTerroristPlayer;
	}

	public static void clear() {
		remotePlayers.clear();
	}

	public static Player getLocalPlayer() {
		return localPlayer;
	}

	public static void setRemotePlayers(List<Player> remotePlayers) {
		PlayerList.remotePlayers = remotePlayers;
	}

}
