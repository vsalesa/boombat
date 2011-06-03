package net.java.dev.boombat.multiplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.entity.layer.ChatList;
import net.java.dev.boombat.game.entity.layer.PlayerList;
import net.java.dev.boombat.game.entity.layer.SystemMessageList;
import net.java.dev.boombat.game.ui.ext.FlashMessageBox;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.multiplayer.core.Client;
import net.java.dev.boombat.multiplayer.core.Finder;
import net.java.dev.boombat.multiplayer.core.GameSession;
import net.java.dev.boombat.multiplayer.core.Host;
import net.java.dev.boombat.multiplayer.core.SecurityFilter;
import net.java.dev.boombat.multiplayer.core.Util;
import net.java.dev.boombat.multiplayer.message.AddNewPlayerMsg;
import net.java.dev.boombat.multiplayer.message.ChangePlayerNameMsg;
import net.java.dev.boombat.multiplayer.message.ChangeTeamMsg;
import net.java.dev.boombat.multiplayer.message.ChatMsg;
import net.java.dev.boombat.multiplayer.message.FireBulletMsg;
import net.java.dev.boombat.multiplayer.message.FireRocketMsg;
import net.java.dev.boombat.multiplayer.message.InvalidLoginMsg;
import net.java.dev.boombat.multiplayer.message.KeepAliveMsg;
import net.java.dev.boombat.multiplayer.message.LeaveGameReqMsg;
import net.java.dev.boombat.multiplayer.message.LoginReqMsg;
import net.java.dev.boombat.multiplayer.message.Message;
import net.java.dev.boombat.multiplayer.message.RemovePlayerMsg;
import net.java.dev.boombat.multiplayer.message.SessionFlagMsg;
import net.java.dev.boombat.multiplayer.message.SwitchMapMsg;
import net.java.dev.boombat.multiplayer.message.TankPositionMsg;
import net.java.dev.boombat.multiplayer.message.ValidLoginMsg;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.newdawn.slick.util.Log;

/**
 * Multiplayer Game Manager. <br/> Manager handles all multiplayer game logics.
 * 
 * @author Eriq Adams <a href mailto="eriq.adams@gmail.com">
 *         (eriq.adams@gmail.com)</a>
 */
public class Manager {

	private static boolean client = true;
	private static boolean wait = true;

	/**
	 * TODO Find Active Game Servers.
	 * 
	 * @return Server Info
	 */
	public static List<String[]> findServers() {
		return Finder.findServers();
	}

	/**
	 * Create Game Session. (method for Server only)
	 * 
	 * @param name
	 *            player's name
	 * @param pass
	 *            game session password
	 * @param maxp
	 *            max allowed player number
	 * @param map
	 *            map key
	 * @param team
	 *            player's team
	 * @param tank
	 *            tank key
	 * @return success or not
	 */
	public static boolean createGame(String name, String pass, int maxp,
			String map, byte team, String tank) {

		// clear player list
		PlayerList.clear();

		// reset player id counter
		Util.resetPlayerIdCounter();

		// set game server properties
		Config.setPassword(pass);
		Config.setMaxPlayer(maxp);

		// start game server
		boolean ok = Host.start();

		if (ok) {
			// set manager to server
			Manager.setClient(false);
			// set player radar
			Config.setPlayerRadar(Config.getOption().getBoolean("playerRadar"));
			// set server name
			Config.setSelectedServerName(Config.getOption().getString(
					"serverName"));
			// create new local player
			Player p = new Player(Util.getPlayerId(), name, ResourceManager
					.createTank(tank));
			p.setTeam(team);
			PlayerList.addPlayer(p);
			// create map
			Config.setSelectedGameMap(map);
			// start session
			GameSession.startCreatorSession();
			// generate random pos
			Util.applyRandomTankPosition();
			// new round
			GameSession.newRound();
			// add system message
			SystemMessageList.add(name + " join " + p.getTeamName());

			return true;
		} else {

			return false;
		}
	}

	/**
	 * Join Game Session. (method for Client only)
	 * 
	 * @param name
	 *            server's IP
	 * @param name
	 *            player's name
	 * @param pass
	 *            game session password
	 * @param team
	 *            team
	 * @param tank
	 *            tank key
	 * @return success or not
	 */
	public static boolean joinGame(String address, String name, String pass,
			byte team, String tank) {
		int waitTimeout = 2000;
		long startTime = System.currentTimeMillis();
		// connect to server
		boolean ok = Client.connect(address);

		// clear player list
		PlayerList.clear();

		if (ok) {
			wait = true;
			// send authentication request message
			LoginReqMsg msg = new LoginReqMsg();
			msg.name = name;
			msg.password = pass;
			msg.tank = tank;
			msg.team = team;
			Client.send(msg.toBuffer());

			while (System.currentTimeMillis() - startTime < waitTimeout) {

				if (!wait) {
					if (Client.isValidConnected()) {
						// set manager to client
						Manager.setClient(true);
						// create map
						Config.setSelectedGameMap(Config.getMapKeyName());
						return true;
					} else {

						return false;
					}
				}
			}
		}

		return false;

	}

	/**
	 * send Keep Alive msg. Prevent from disconnecting. (method for Client
	 * only).
	 */
	public static void keepAlive() {
		if (client) {
			Client.send(new KeepAliveMsg().toBuffer());
		}
	}

	/**
	 * Send Game State
	 * 
	 * @param flags
	 */
	public static int sendGameState(byte flags) {
		if (SessionFlagMsg.NEW_ROUND == flags) {
			Map<Byte, float[]> pos = Util.applyRandomTankPosition();
			SessionFlagMsg msg = new SessionFlagMsg();
			msg.flags = SessionFlagMsg.NEW_ROUND;
			msg.pos = pos;
			Host.broadcast(msg.toBuffer());
			return 2;
		} else if (SessionFlagMsg.RESTART_ROUND == flags) {
			Map<Byte, float[]> pos = Util.applyRandomTankPosition();
			SessionFlagMsg msg = new SessionFlagMsg();
			msg.flags = SessionFlagMsg.RESTART_ROUND;
			msg.pos = pos;
			Host.broadcast(msg.toBuffer());
			return 1;
		} else if (SessionFlagMsg.END_SESSION == flags) {
			SessionFlagMsg msg = new SessionFlagMsg();
			msg.flags = SessionFlagMsg.END_SESSION;
			Set<WriteFuture> futures = Host.broadcast(msg.toBuffer());
			for (WriteFuture f : futures) {
				f.awaitUninterruptibly(1000);
			}
			Host.destroy();
			return 3;
		}
		return 0;
	}

	/**
	 * Make a Chat. (method for both client and server)
	 * 
	 * @param content
	 *            chat content
	 */
	public static void sendChat(String content) {
		ChatList.add(content);
		ChatMsg msg = new ChatMsg();
		msg.content = content;
		if (client) {
			Client.send(msg.toBuffer());
		} else {
			Host.broadcast(msg.toBuffer());
		}
	}

	/**
	 * Send Tank Position.(method for both client and server)
	 * 
	 * @param p
	 *            player
	 */
	public static boolean sendTankPosition(Player p) {
		TankPositionMsg msg = new TankPositionMsg();
		msg.playerId = p.getId();
		msg.x = p.getTank().getX();
		msg.y = p.getTank().getY();
		msg.bodyAng = p.getTank().getBodyAngle();
		msg.turretAng = p.getTank().getTurretAngle();
		if (isClient()) {
			Client.send(msg.toBuffer());
			return true;
		} else {
			Host.broadcast(msg.toBuffer());
			return false;
		}
	}

	/**
	 * Send Fire State
	 * 
	 * @param playerId
	 * @param msgId
	 */
	public static int sendFireState(byte playerId, byte msgId) {
		if (FireBulletMsg.ID == msgId) {
			FireBulletMsg fireBulletMsg = new FireBulletMsg();
			fireBulletMsg.playerId = playerId;
			if (isClient()) {
				Client.send(fireBulletMsg.toBuffer());
				return 1;
			} else {
				Host.broadcast(fireBulletMsg.toBuffer());
				return 2;
			}
		} else if (FireRocketMsg.ID == msgId) {
			FireRocketMsg fireRocketMsg = new FireRocketMsg();
			fireRocketMsg.playerId = playerId;
			if (isClient()) {
				Client.send(fireRocketMsg.toBuffer());
				return 3;
			} else {
				Host.broadcast(fireRocketMsg.toBuffer());
				return 4;
			}
		}
		return 0;
	}

	/**
	 * Switch Map. (method for server only)
	 * 
	 * @param map
	 *            map key
	 */
	public static void switchMap(String map) {
		if (map.equals(Config.getMapKeyName())) {
			return;
		}
		SwitchMapMsg msg = new SwitchMapMsg();
		msg.map = map;
		Host.broadcast(msg.toBuffer());
		FlashMessageBox.instance().showFlashMessage(
				2000,
				"Game Map changed to "
						+ ResourceManager.getMapDescription(msg.map));
		Config.setSelectedGameMap(map);
	}

	public static void endGame() {
		GameSession.endSession();
		Manager.sendGameState(SessionFlagMsg.END_SESSION);
	}

	/**
	 * Leave Game. (method for client only)
	 * 
	 */
	public static void leaveGame() {
		byte playerId = PlayerList.getLocalPlayer().getId();
		LeaveGameReqMsg msg = new LeaveGameReqMsg();
		msg.playerId = playerId;
		Client.send(msg.toBuffer()).awaitUninterruptibly(1000);
		GameSession.endSession();
		Client.destroy();
	}

	/**
	 * Change Local Player Team and Tank. (method for both client and server)
	 * 
	 * @param team
	 *            player's team
	 * @param tank
	 *            player's tank key
	 */
	public static void changeLocalPlayerTeam(byte team, String tank) {
		if (team == PlayerList.getLocalPlayer().getTeam()
				&& tank.equals(PlayerList.getLocalPlayer().getTank().getKey())) {
			return;
		}
		PlayerList.getLocalPlayer().setTeam(team);
		PlayerList.getLocalPlayer().setTank(ResourceManager.createTank(tank));
		PlayerList.getLocalPlayer().setState(Player.STATE_SPECTATOR);
		ChangeTeamMsg msg = new ChangeTeamMsg();
		msg.playerId = PlayerList.getLocalPlayer().getId();
		msg.tank = tank;
		msg.team = team;
		if (client) {
			Client.send(msg.toBuffer());
		} else {
			Host.broadcast(msg.toBuffer());
		}
		SystemMessageList.add(PlayerList.getLocalPlayer().getName()
				+ " change team and tank");

	}

	/**
	 * Change local player name. (method for both client and server).
	 * 
	 * @param newName
	 *            player's name
	 */
	public static void changeLocalPlayerName(String newName) {
		String oldName = PlayerList.getLocalPlayer().getName();
		if (oldName.equals(newName)) {
			return;
		}
		PlayerList.getLocalPlayer().setName(newName);
		ChangePlayerNameMsg msg = new ChangePlayerNameMsg();
		msg.name = newName;
		if (client) {
			Client.send(msg.toBuffer());
		} else {
			Host.broadcast(msg.toBuffer());
		}
		SystemMessageList.add(oldName + " change name to " + newName);
	}

	/**
	 * Add new remote player.(method for client only)
	 * 
	 * @param message
	 *            {@link AddNewPlayerMsg}
	 */
	public static void addNewPlayer(Message message) {
		AddNewPlayerMsg addMsg = (AddNewPlayerMsg) message;
		PlayerList.addPlayer(addMsg.player);
		// add system message
		SystemMessageList.add(addMsg.player.getName() + " join "
				+ addMsg.player.getTeamName());
	}

	/**
	 * Process valid login (method for client only)
	 * 
	 * @param message
	 *            {@link ValidLoginMsg}
	 */
	public static void processValidLogin(Message message) {

		ValidLoginMsg msg = (ValidLoginMsg) message;
		// set player radar
		Config.setPlayerRadar(msg.playerRadar);
		// set player list
		for (Player p : msg.players) {
			PlayerList.addPlayer(p);
		}
		// set map
		Config.setMapKeyName(msg.map);
		// set server name
		Config.setSelectedServerName(msg.serverName);
		// set client status validConnected to true
		Client.setValidConnected(true);
		// hey manager don't wait
		Manager.dontWaitLogin();
		// start session
		GameSession.startJoinerSession(msg.scores, msg.currRemainingTime,
				msg.rndCount, msg.CTWinCount, msg.TWinCount, msg.currWinner);
		// add system message
		SystemMessageList.add(PlayerList.getLocalPlayer().getName() + " join "
				+ PlayerList.getLocalPlayer().getTeamName());
	}

	/**
	 * Process invalid login. (method for client only)
	 * 
	 * @param message
	 *            {@link InvalidLoginMsg}
	 */
	public static void processInvalidLogin() {
		// set client status validConnected to false
		Client.setValidConnected(false);
		// hey manager don't wait
		Manager.dontWaitLogin();
	}

	/**
	 * Process Leave Game Request. (method for server only)
	 * 
	 * @param session
	 *            requester's session
	 * @param message
	 *            {@link LeaveGameReqMsg}
	 */
	public static void processLeaveGameRequest(IoSession session,
			Message message) {
		LeaveGameReqMsg leaveMsg = (LeaveGameReqMsg) message;
		Player delp = PlayerList.getPlayer(leaveMsg.playerId);
		SystemMessageList.add(delp.getName() + " leave game ");
		PlayerList.getRemotePlayers().remove(delp);
		session.close();
	}

	/**
	 * Validate login. (method for server only)
	 * 
	 * @param session
	 *            requester's session
	 * @param message
	 *            {@link LoginReqMsg}
	 */
	public static boolean validateLogin(IoSession session, Message message) {
		LoginReqMsg msg = (LoginReqMsg) message;

		if (msg.password.equals(Config.getPassword())) {

			// create new player
			Player newp = new Player(Util.getPlayerId(), msg.name,
					ResourceManager.createTank(msg.tank));
			newp.setTeam(msg.team);
			SystemMessageList.add(newp.getName() + " join "
					+ newp.getTeamName());

			// tell new player to set his player list and map
			List<Player> players = new ArrayList<Player>();
			players.add(PlayerList.getLocalPlayer());
			players.get(0).setRemotePlayer(true);
			players.add(newp);
			players.addAll(PlayerList.getRemotePlayers());
			ValidLoginMsg loginResMsg = new ValidLoginMsg();
			loginResMsg.map = Config.getMapKeyName();
			loginResMsg.players = players;
			loginResMsg.CTWinCount = GameSession.getCounterTerroristWinCount();
			loginResMsg.currRemainingTime = GameSession
					.getCurrentRemainingTime();
			loginResMsg.currWinner = GameSession.getCurrentWinner();
			loginResMsg.rndCount = GameSession.getRoundCount();
			loginResMsg.scores = GameSession.getOldScores();
			loginResMsg.TWinCount = GameSession.getTerroristWinCount();
			loginResMsg.serverName = Config.getSelectedServerName();
			loginResMsg.playerRadar = Config.isPlayerRadar();
			Host.reply(loginResMsg.toBuffer(), session);

			// tell other player to add new player
			AddNewPlayerMsg addMsg = new AddNewPlayerMsg();
			newp.setRemotePlayer(true);
			addMsg.player = newp;
			Host.relay(addMsg.toBuffer(), session);

			// add new player as remote player to player list
			PlayerList.addPlayer(newp);

			// make session's client valid
			session.setAttribute(SecurityFilter.VALID_KEY, true);
			return true;
		} else {
			InvalidLoginMsg invalidLoginMsg = new InvalidLoginMsg();
			Host.reply(invalidLoginMsg.toBuffer(), session);
			return false;
		}
	}

	/**
	 * Put chat into list. (method for both client and server)
	 * 
	 * @param message
	 *            {@link ChatMsg}
	 */
	public static void receiveChat(Message message) {
		ChatMsg chatMsg = (ChatMsg) message;
		ChatList.add(chatMsg.content);
		Log.info(chatMsg.content);
	}

	/**
	 * Remove existing player from list. (method for client only)
	 * 
	 * @param message
	 *            {@link RemovePlayerMsg}
	 */
	public static void removePlayer(Message message) {
		RemovePlayerMsg remMsg = (RemovePlayerMsg) message;
		Player delp = PlayerList.getPlayer(remMsg.playerId);
		SystemMessageList.add(delp.getName() + " leave game ");
		PlayerList.getRemotePlayers().remove(delp);
	}

	/**
	 * Update Game Map. (method for client only)
	 * 
	 * @param message
	 *            {@link SwitchMapMsg}
	 */
	public static void updateGameMap(Message message) {
		SwitchMapMsg msg = (SwitchMapMsg) message;
		Config.setMapKeyName(msg.map);
		FlashMessageBox.instance().showFlashMessage(
				2000,
				"Game Map changed to "
						+ ResourceManager.getMapDescription(msg.map));
	}

	/**
	 * Update Game Session Flags. (method for client only)
	 * 
	 * @param message
	 *            {@link SessionFlagMsg}
	 */
	public static int updateGameState(Message message) {
		SessionFlagMsg msg = (SessionFlagMsg) message;
		if (SessionFlagMsg.NEW_ROUND == msg.flags) {
			GameSession.calculateScore();
			GameSession.roundNotify();
			GameSession.newRound();
			Util.updateAllTankPosition(msg.pos);
			return 2;
		} else if (SessionFlagMsg.RESTART_ROUND == msg.flags) {
			FlashMessageBox.instance().showFlashMessage(2000,
					"Round Restarted.");
			GameSession.restartRound();
			Util.updateAllTankPosition(msg.pos);
			return 1;
		} else if (SessionFlagMsg.END_SESSION == msg.flags) {
			GameSession.endSession();
			return 3;
		}
		return 0;
	}

	/**
	 * Update Tank Position. (method for both client and server)
	 * 
	 * @param message
	 *            {@link TankPositionMsg}
	 */
	public static boolean updateTankPosition(Message message) {
		TankPositionMsg msg = (TankPositionMsg) message;
		Player p = PlayerList.getPlayer(msg.playerId);
		if (p != null) {
			p.getTank().setX(msg.x);
			p.getTank().setY(msg.y);
			p.getTank().setBodyAngle(msg.bodyAng);
			p.getTank().setTurretAngle(msg.turretAng);
			return true;
		}
		return false;
	}

	/**
	 * Receive Fire State
	 * 
	 * @param message
	 * @param msgId
	 */
	public static boolean receiveFireState(Message message, byte msgId) {
		Player p;
		if (FireBulletMsg.ID == msgId) {
			FireBulletMsg fireBulletMsg = (FireBulletMsg) message;
			p = PlayerList.getPlayer(fireBulletMsg.playerId);
			if (p != null) {
				p.getTank().fireBullet();
				return true;
			}
			return false;
		} else if (FireRocketMsg.ID == msgId) {
			FireRocketMsg fireRocketMsg = (FireRocketMsg) message;
			p = PlayerList.getPlayer(fireRocketMsg.playerId);
			if (p != null) {
				p.getTank().fireRocket();
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Update player's team and tank. (method for both client and server).
	 * 
	 * @param message
	 *            {@link ChangeTeamMsg}
	 */
	public static void updatePlayerTeam(Message message) {
		ChangeTeamMsg msg = (ChangeTeamMsg) message;
		Player p = PlayerList.getPlayer(msg.playerId);
		p.setState(Player.STATE_SPECTATOR);
		p.setTeam(msg.team);
		p.setTank(ResourceManager.createTank(msg.tank));
		SystemMessageList.add(p.getName() + " change team and tank");
	}

	/**
	 * Change player's name. (method for both client and server).
	 * 
	 * @param message
	 *            {@link ChangePlayerNameMsg}
	 */
	public static void updatePlayerName(Message message) {
		ChangePlayerNameMsg msg = (ChangePlayerNameMsg) message;
		Player p = PlayerList.getPlayer(msg.playerId);
		String oldName = p.getName();
		p.setName(msg.name);
		SystemMessageList.add(oldName + " change name to " + p.getName());
	}

	/**
	 * The type of manager.
	 * 
	 * @return client or server ?
	 */
	public static boolean isClient() {
		return client;
	}

	/**
	 * Set the type of manager.
	 * 
	 * @param client
	 *            client or not
	 */
	public static void setClient(boolean client) {
		Manager.client = client;
	}

	/**
	 * Don't wait login. Cause you gotta message from host.
	 */
	public static void dontWaitLogin() {
		Manager.wait = false;
	}
}
