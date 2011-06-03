package net.java.dev.boombat.game.entity;

/**
 * 
 * @author objectworks
 */
public class Player {

	protected short kill;
	protected short dead;
	protected byte id;
	protected String name;
	protected Tank tank;
	protected byte life;
	public static final byte NO_TEAM = 0;
	public static final byte TEAM_TERRORIST = 33;
	public static final byte TEAM_COUNTER_TERRORIST = 99;
	public static final byte STATE_SPECTATOR = 3;
	public static final byte STATE_LIFE = 7;
	public static final byte STATE_DEAD = 9;
	protected boolean remotePlayer;
	protected byte state;
	protected byte team;

	public Player(byte id, String name, Tank tank) {
		this((short) 0, (short) 0, (byte) 100, NO_TEAM, STATE_SPECTATOR, id,
				name, tank, false);
	}

	public Player(short score, short death, byte life, byte team, byte state,
			byte id, String name, Tank tank, boolean remotePlayer) {
		this.kill = score;
		this.dead = death;
		this.life = life;
		this.id = id;
		this.name = name;
		this.state = state;
		this.team = team;
		setTank(tank);
		this.remotePlayer = remotePlayer;
	}

	/**
	 * @return the score
	 */
	public short getKill() {
		return kill;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setKill(short kill) {
		this.kill = kill;
	}

	/**
	 * @return the id
	 */
	public byte getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the tank
	 */
	public Tank getTank() {
		return tank;
	}

	/**
	 * @param tank
	 *            the tank to set
	 */
	public void setTank(Tank tank) {
		if (tank != null) {
			this.tank = tank;
			this.tank.setTankOwnerId(id);
		} else {
			state = STATE_SPECTATOR;
		}
	}

	/**
	 * @return the life
	 */
	public byte getLife() {
		return life;
	}

	/**
	 * @param life
	 *            the life to set
	 */
	public void setLife(byte life) {
		if (life <= 0) {
			life = 0;
			state = STATE_DEAD;
			dead++;
			tank.explode();
		}
		this.life = life;
	}

	/**
	 * @return the remotePlayer
	 */
	public boolean isRemotePlayer() {
		return remotePlayer;
	}

	public byte getState() {
		return state;
	}

	public void setRemotePlayer(boolean remotePlayer) {
		this.remotePlayer = remotePlayer;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public short getDead() {
		return dead;
	}

	public void setDead(short dead) {
		this.dead = dead;
	}

	public byte getTeam() {
		return team;
	}

	public void setTeam(byte team) {
		this.team = team;
	}

	public String getTeamName() {
		if (team == TEAM_COUNTER_TERRORIST) {
			return "Counter T.";
		} else if (team == TEAM_TERRORIST) {
			return "Terrorist";
		} else {
			return "Unknown Team";
		}
	}

	public String getStateName() {
		if (state == STATE_DEAD) {
			return "DEAD";
		} else if (state == STATE_LIFE) {
			return "LIFE";
		} else {
			return "SPECTATING";
		}
	}

}
