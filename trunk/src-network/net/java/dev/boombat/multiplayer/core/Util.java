package net.java.dev.boombat.multiplayer.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.entity.Tank;
import net.java.dev.boombat.game.entity.layer.PlayerList;
import net.java.dev.boombat.game.util.ResourceManager;

import org.apache.mina.core.buffer.IoBuffer;

public class Util {

	private static byte playerId;

	private static Pattern[] patterns = {
			Pattern.compile("[0-9.]*\t[\\d\\D]*\t\\d*"),
			Pattern.compile("[0-9.]*\t"), Pattern.compile("\t[\\d\\D]*\t"),
			Pattern.compile("\t\\d*$") };

	public static void putBool(boolean bool, IoBuffer buff) {
		byte b = 0;
		if (bool) {
			b = 1;
		}
		buff.put(b);
	}

	public static boolean getBool(IoBuffer buff) {
		byte b = buff.get();
		if (b == 1) {
			return true;
		} else {
			return false;
		}
	}

	public static void putStr(String chars, IoBuffer buff) {
		buff.putShort((short) chars.length());
		buff.put(chars.getBytes());
	}

	public static String getStr(IoBuffer buff) {
		int length = buff.getShort();
		byte[] bytes = new byte[length];
		buff.get(bytes);
		return new String(bytes);
	}

	public static void putPlayer(Player p, IoBuffer buff) {
		putStr(p.getTank().getKey(), buff);
		buff.putShort(p.getDead());
		buff.putShort(p.getKill());
		buff.put(p.getLife());
		buff.put(p.getState());
		buff.put(p.getTeam());
		buff.putFloat(p.getTank().getBodyAngle());
		buff.put(p.getTank().getCurrentBulletAmmo());
		buff.put(p.getTank().getCurrentRocketAmmo());
		buff.putFloat(p.getTank().getTurretAngle());
		buff.putFloat(p.getTank().getX());
		buff.putFloat(p.getTank().getY());
		putBool(p.isRemotePlayer(), buff);
		buff.put(p.getId());
		putStr(p.getName(), buff);
	}

	public static void putPlayerList(List<Player> players, IoBuffer buff) {
		buff.put((byte) players.size());
		for (Player p : players) {
			putPlayer(p, buff);
		}
	}

	public static Player getPlayer(IoBuffer buff) {
		String tank = getStr(buff);
		short dead = buff.getShort();
		short kill = buff.getShort();
		byte life = buff.get();
		byte state = buff.get();
		byte team = buff.get();
		float bodyAng = buff.getFloat();
		byte currBull = buff.get();
		byte currRock = buff.get();
		float turrAng = buff.getFloat();
		float x = buff.getFloat();
		float y = buff.getFloat();
		boolean remote = getBool(buff);
		byte id = buff.get();
		String name = getStr(buff);
		Player p = new Player(kill, dead, life, team, state, id, name,
				ResourceManager.createTank(tank), remote);
		Tank t = p.getTank();
		t.setBodyAngle(bodyAng);
		t.setCurrentBulletAmmo(currBull);
		t.setCurrentRocketAmmo(currRock);
		t.setTurretAngle(turrAng);
		t.setX(x);
		t.setY(y);
		p.setTank(t);

		return p;
	}

	public static List<Player> getPlayerList(IoBuffer buff) {
		List<Player> players = new ArrayList<Player>();
		int size = buff.get();
		for (int i = 0; i < size; i++) {
			players.add(getPlayer(buff));
		}
		return players;
	}

	public static byte getPlayerId() {

		return playerId++;
	}

	public static void resetPlayerIdCounter() {
		playerId = 0;
	}

	public static void putScoreList(Map<Byte, short[]> scores, IoBuffer buff) {
		buff.put((byte) scores.size());
		for (Entry<Byte, short[]> e : scores.entrySet()) {
			putScore(e.getKey(), e.getValue(), buff);
		}
	}

	public static void putScore(byte id, short[] scores, IoBuffer buff) {
		buff.put(id);
		buff.putShort(scores[0]);
		buff.putShort(scores[1]);
	}

	public static short[] getScore(IoBuffer buff) {
		short[] x = new short[3];
		x[0] = buff.get();
		x[1] = buff.getShort();
		x[2] = buff.getShort();

		return x;
	}

	public static Map<Byte, short[]> getScoreList(IoBuffer buff) {
		Map<Byte, short[]> scores = new HashMap<Byte, short[]>();
		int size = buff.get();
		for (int i = 0; i < size; i++) {
			short[] x = getScore(buff);
			scores.put((byte) x[0], new short[] { x[1], x[2] });
		}
		return scores;
	}

	public static Map<Byte, float[]> getTankPositionList(IoBuffer buff) {
		Map<Byte, float[]> pos = new HashMap<Byte, float[]>();
		int size = buff.get();
		for (int i = 0; i < size; i++) {
			float[] x = getTankPosition(buff);
			pos.put((byte) x[0], new float[] { x[1], x[2] });
		}
		return pos;
	}

	public static void putTankPositionList(Map<Byte, float[]> pos, IoBuffer buff) {
		buff.put((byte) pos.size());
		for (Entry<Byte, float[]> e : pos.entrySet()) {
			putTankPosition(e.getKey(), e.getValue(), buff);
		}
	}

	public static void putTankPosition(byte id, float[] pos, IoBuffer buff) {
		buff.put(id);
		buff.putFloat(pos[0]);
		buff.putFloat(pos[1]);
	}

	public static float[] getTankPosition(IoBuffer buff) {
		float[] x = new float[3];
		x[0] = buff.get();
		x[1] = buff.getFloat();
		x[2] = buff.getFloat();

		return x;
	}

	public static Map<Byte, float[]> applyRandomTankPosition() {
		Map<Byte, float[]> pos = new HashMap<Byte, float[]>();
		float[][] genPos = Config.getSelectedGameMap().generateRandomPositions(
				20);
		// set local player first then ...
		PlayerList.getLocalPlayer().getTank().setX(genPos[0][0]);
		PlayerList.getLocalPlayer().getTank().setY(genPos[0][1]);
		PlayerList.getLocalPlayer().getTank().setTurretAngle(
				PlayerList.getLocalPlayer().getTank().getBodyAngle());
		pos.put(PlayerList.getLocalPlayer().getId(), genPos[0]);
		// set remote players
		int idx = 1;
		for (Player p : PlayerList.getRemotePlayers()) {
			p.getTank().setX(genPos[idx][0]);
			p.getTank().setY(genPos[idx][1]);
			p.getTank().setTurretAngle(p.getTank().getBodyAngle());
			pos.put(p.getId(), genPos[idx]);
			idx++;
		}

		return pos;
	}

	public static void updateAllTankPosition(Map<Byte, float[]> pos) {
		for (Entry<Byte, float[]> e : pos.entrySet()) {
			Player p = PlayerList.getPlayer(e.getKey());
			float[] x = e.getValue();
			p.getTank().setX(x[0]);
			p.getTank().setY(x[1]);
			p.getTank().setTurretAngle(p.getTank().getBodyAngle());
		}
	}

	public static String createGameAnnouncementMessage() {
		String address = "localhost";
		try {
			address = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
		}
		return address + "\t" + Config.getSelectedServerName() + "\t"
				+ (PlayerList.getRemotePlayers().size() + 1);
	}

	/**
	 * Parse Game Announcement Message
	 * @param message
	 * @return Server's Info
	 */
	public static String[] parseGameAnnouncementMessage(String message) {
		String[] result = null;
		String matchedText = null;
		Matcher matcher = patterns[0].matcher(message);
		if (matcher.find()) {
			matchedText = matcher.group();
		} else {
			return null;
		}
		String address = "";
		String server = "";
		String number = "";
		matcher = patterns[1].matcher(matchedText);
		if (matcher.find()) {
			address = matcher.group().trim();
		} else {
			return null;
		}
		matcher = patterns[2].matcher(matchedText);
		if (matcher.find()) {
			server = matcher.group().trim();
		} else {
			return null;
		}
		matcher = patterns[3].matcher(matchedText);
		if (matcher.find()) {
			number = matcher.group().trim();
		} else {
			return null;
		}

		result = new String[] { address, server, number };

		return result;
	}
}
