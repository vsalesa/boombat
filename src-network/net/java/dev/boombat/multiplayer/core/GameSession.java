package net.java.dev.boombat.multiplayer.core;

import java.util.HashMap;
import java.util.Map;

import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.entity.layer.ChatList;
import net.java.dev.boombat.game.entity.layer.MissileList;
import net.java.dev.boombat.game.entity.layer.NodeList;
import net.java.dev.boombat.game.entity.layer.ParticleList;
import net.java.dev.boombat.game.entity.layer.PlayerList;
import net.java.dev.boombat.game.entity.layer.SystemMessageList;
import net.java.dev.boombat.game.ui.ext.FlashMessageBox;

public class GameSession {

	private static Map<Byte, short[]> oldScores = new HashMap<Byte, short[]>();

	private static boolean sessionActive;

	public static final int MAX_ROUND_TIME = 360000;

	public static final int MAX_ROUND = 20;

	private static int currentRemainingTime;

	private static byte roundCount = 0;

	private static byte counterTerroristWinCount;

	private static byte terroristWinCount;

	public static final byte NO_WINNER = -1;

	public static final byte TERRORIST_WIN = 1;

	public static final byte COUNTER_TERRORIST_WIN = 2;

	public static final byte DRAW = 0;

	private static byte currentWinner = NO_WINNER;

	
	public static void newRound() {
		updateOldScores();
		resetStates();
	}

	public static void resetStates() {
		// reset all player states to Player.STATE_LIFE and life to 100%
		Player localPlayer = PlayerList.getLocalPlayer();
		if (localPlayer != null) {
			localPlayer.setState(Player.STATE_LIFE);
			localPlayer.setLife((byte) 100);
			localPlayer.getTank().resetAmmo();
		}

		int idx = 1;
		for (Player p : PlayerList.getRemotePlayers()) {
			p.setState(Player.STATE_LIFE);
			p.setLife((byte) 100);
			p.getTank().resetAmmo();
			idx++;
		}

		// reset currentRemainingTime to MAX_TIME
		currentRemainingTime = MAX_ROUND_TIME;

		// update roundCount +1
		roundCount++;

		// reset winner
		currentWinner = NO_WINNER;

		// clear NodeList
		NodeList.clear();
		// clear ParticleList
		ParticleList.clear();
		// clear MissileList
		MissileList.clear();
		// clear chat list
		ChatList.clear();
		// clear system message list
		SystemMessageList.clear();
	}

	public static void roundNotify() {
		byte winner = currentWinner;
		if (COUNTER_TERRORIST_WIN == winner) {
			FlashMessageBox.instance().showFlashMessage(2000,
					"Counter Terrorist Win.");
		} else if (TERRORIST_WIN == winner) {
			FlashMessageBox.instance().showFlashMessage(2000, "Terrorist Win.");
		} else if (DRAW == winner) {
			FlashMessageBox.instance().showFlashMessage(2000, "Round Draw");
		} else if (NO_WINNER == winner) {
			FlashMessageBox.instance().showFlashMessage(2000, "New Round");
		}
	}

	public static void restartRound() {
		// update PlayerList scores to oldScore, (do rollback)
		rollbackScores();
		resetStates();
	}

	private static void updateOldScores() {
		if (PlayerList.getCounterTerroristPlayers().size() == 0
				|| PlayerList.getTerroristPlayers().size() == 0) {
			return;
		}
		// save current scores to oldscores before start round
		Player localPlayer = PlayerList.getLocalPlayer();
		if (localPlayer != null) {
			oldScores.put(localPlayer.getId(), new short[] {
					localPlayer.getKill(), localPlayer.getDead() });
		}

		for (Player p : PlayerList.getRemotePlayers()) {
			oldScores.put(p.getId(), new short[] { p.getKill(), p.getDead() });
		}
	}

	public static void calculateScore() {
		if (PlayerList.getCounterTerroristPlayers().size() == 0
				|| PlayerList.getTerroristPlayers().size() == 0) {
			return;
		}

		if (PlayerList.getRemotePlayers().size() == 0) {
			currentWinner = NO_WINNER;
			return;
		}

		Player localPlayer = PlayerList.getLocalPlayer();
		int terroristDeaths = 0;
		int counterTerroristDeaths = 0;

		if (localPlayer != null) {
			if (Player.STATE_DEAD == localPlayer.getState()) {
				if (Player.TEAM_COUNTER_TERRORIST == localPlayer.getTeam()) {
					counterTerroristDeaths++;
				} else if (Player.TEAM_TERRORIST == localPlayer.getTeam()) {
					terroristDeaths++;
				}
			}
		}

		for (Player p : PlayerList.getRemotePlayers()) {
			if (Player.STATE_DEAD == p.getState()) {
				if (Player.TEAM_COUNTER_TERRORIST == p.getTeam()) {
					counterTerroristDeaths++;
				} else if (Player.TEAM_TERRORIST == p.getTeam()) {
					terroristDeaths++;
				}
			}
		}

		if (counterTerroristDeaths < terroristDeaths) {
			counterTerroristWinCount++;
			currentWinner = COUNTER_TERRORIST_WIN;
		} else if (counterTerroristDeaths > terroristDeaths) {
			terroristWinCount++;
			currentWinner = TERRORIST_WIN;
		} else {
			currentWinner = DRAW;
		}
	}

	private static void rollbackScores() {
		Player localPlayer = PlayerList.getLocalPlayer();
		if (localPlayer != null) {
			short[] score = oldScores.get(localPlayer.getId());
			if (score == null) {
				score = new short[] { 0, 0 };
			}
			localPlayer.setKill(score[0]);
			localPlayer.setDead(score[1]);
		}

		for (Player p : PlayerList.getRemotePlayers()) {
			short[] score = oldScores.get(p.getId());
			if (score == null) {
				score = new short[] { 0, 0 };
			}
			p.setKill(score[0]);
			p.setDead(score[1]);
		}
	}

	public static boolean update(int delta) {
		if (currentRemainingTime > 0) {
			currentRemainingTime -= delta;
			return true;
		}

		return false;
	}

	public static byte getCounterTerroristWinCount() {
		return counterTerroristWinCount;
	}

	public static byte getTerroristWinCount() {
		return terroristWinCount;
	}

	public static byte getRoundCount() {
		return roundCount;
	}

	public static boolean isSessionActive() {
		return sessionActive;
	}

	public static void startCreatorSession() {
		oldScores.clear();
		counterTerroristWinCount = 0;
		terroristWinCount = 0;
		sessionActive = true;
		FlashMessageBox.instance().showFlashMessage(2000, "Session started.");
	}

	public static void startJoinerSession(Map<Byte, short[]> scores,
			int currRemainingTime, byte rndCount, byte CTWinCount,
			byte TWinCount, byte currWinner) {
		oldScores = scores;
		currentRemainingTime = currRemainingTime;
		roundCount = rndCount;
		counterTerroristWinCount = CTWinCount;
		terroristWinCount = TWinCount;
		currentWinner = currWinner;
		sessionActive = true;
		FlashMessageBox.instance().showFlashMessage(2000, "Session started.");
	}

	public static void endSession() {
		resetStates();
		sessionActive = false;
	}

	public static int getCurrentRemainingTime() {
		return currentRemainingTime;
	}

	public static Map<Byte, short[]> getOldScores() {
		return oldScores;
	}

	public static byte getCurrentWinner() {
		return currentWinner;
	}

}
