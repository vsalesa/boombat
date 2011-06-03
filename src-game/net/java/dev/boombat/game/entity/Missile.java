package net.java.dev.boombat.game.entity;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.layer.ParticleList;
import net.java.dev.boombat.game.entity.layer.PlayerList;
import net.java.dev.boombat.game.entity.layer.SystemMessageList;
import net.java.dev.boombat.game.util.CollisionUtil;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.game.util.SoundFilter;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.util.FastTrig;

/**
 * 
 * @author objectworks
 */
public abstract class Missile {

	protected Image image;
	protected float speed;
	protected int hitScore;
	protected float x;
	protected float y;
	protected float angle;
	protected boolean completed;
	protected float vecX;
	protected float vecY;
	protected float distance;
	protected BaseParticle explosionParticle;
	protected byte missileOwnerId;
	protected int particleLifeTime;
	protected String emitterName;
	protected String explosionSoundName;
	protected float startX;
	protected float startY;
	public static final int INFINITE_RANGE = -1;
	protected int range;
	protected float expX;
	protected float expY;
	protected float centerXAdd;
	protected float centerYAdd;

	public Missile(Image image, float speed, int hitScore,
			int particleLifeTime, String emitterName, String soundName,
			byte missileOwnerId, float x, float y, float distance, float angle) {
		this(image, speed, hitScore, particleLifeTime, emitterName, soundName,
				missileOwnerId, x, y, distance, angle, INFINITE_RANGE);
	}

	public Missile(Image image, float speed, int hitScore,
			int particleLifeTime, String emitterName, String soundName,
			byte missileOwnerId, float x, float y, float distance, float angle,
			int range) {
		this.image = image;
		this.speed = speed;
		this.hitScore = hitScore;
		this.x = x;
		this.y = y;
		this.startX = x;
		this.startY = y;
		this.angle = angle;
		this.distance = distance;
		this.missileOwnerId = missileOwnerId;
		this.particleLifeTime = particleLifeTime;
		this.emitterName = emitterName;
		this.explosionSoundName = soundName;
		this.range = range;

	}

	public void update(int delta) {
		updateVector();
		float newDelta = delta * speed;
		float newX = vecX * newDelta;
		float newY = vecY * newDelta;

		if (range != INFINITE_RANGE) {
			float dx = (startX - x);
			float dy = (startY - y);
			float dist = (float) Math.sqrt(dx * dx + dy * dy);
			if (dist > range) {
				completed = true;
				return;
			}
		}

		if (CollisionUtil.isCollideWithEdgeScreen(x + newX, y + newY)) {
			completed = true;
			return;
		}

		Player player = PlayerList.isCollide(missileOwnerId, x + newX,
				y + newY, 2);
		if (player != null) {
			expX = x + newX;
			expY = y + newY;
			updateIfCollideWithPlayers(delta, player);
			return;
		}

		GameMap selectedGameMap = Config.getSelectedGameMap();
		if (selectedGameMap.isCollide(x, y, newX, newY)) {
			expX = x + newX;
			expY = y + newY;
			updateIfCollideWithTileMap(delta);
			return;
		}

		updateIfNotCollide(delta, newX, newY);

	}

	protected void updateIfCollideWithTileMap(int delta) {
		// create new explosion particle and update it
		createParticle(delta);
		explosionParticle.addPosToDrawCenter(0, -16);
		ParticleList.add(explosionParticle);
		completed = true;
	}

	protected void updateIfCollideWithPlayers(int delta, Player hittedPlayer) {
		updateScore(hittedPlayer);

		// create new explosion particle and update it
		createParticle(delta);
		explosionParticle.addPosToDrawCenter(0, -16);
		ParticleList.add(explosionParticle);
		completed = true;
	}

	protected void updateScore(Player hittedPlayer) {
		if (Player.STATE_LIFE != hittedPlayer.getState()) {
			return;
		}

		Player missileOwner = PlayerList.getPlayer(missileOwnerId);
		boolean friendlyFire = Config.getOption().getBoolean("friendlyFire");
		if (!friendlyFire && hittedPlayer.getTeam() == missileOwner.getTeam()) {
			return;
		}

		byte hittedPlayerScore = hittedPlayer.getLife();
		hittedPlayerScore -= hitScore;
		hittedPlayer.setLife(hittedPlayerScore);

		if (hittedPlayer.getLife() == 0) {
			short missileOwnerKill = missileOwner.getKill();
			if (hittedPlayer.getTeam() == missileOwner.getTeam()) {
				missileOwnerKill--;
			} else {
				missileOwnerKill++;
			}
			missileOwner.setKill(missileOwnerKill);
			SystemMessageList.add(missileOwner.getName() + " kill "
					+ hittedPlayer.getName());
		}
	}

	private void createParticle(int delta) {
		explosionParticle = new BaseParticle(ResourceManager
				.loadConfigurableEmitter(emitterName), particleLifeTime);
		Sound sound = ResourceManager.getSound(explosionSoundName);
		Tank tankSource = PlayerList.getLocalPlayer().getTank();
		SoundFilter.filter3D(tankSource.getX(), tankSource.getY(), expX, expY,
				sound, Config.MAX_SOUND_DISTANCE_IN_TILES);
		explosionParticle.setPosition(expX, expY, angle);
	}

	protected void updateIfNotCollide(int delta, float newX, float newY) {
		x += newX;
		y += newY;
	}

	public void render(Graphics g) {
		drawImage(g);
	}

	protected void drawImage(Graphics g) {
		int xTile = Math.round(x * GameMap.TILE_SIZE);
		int yTile = Math.round(y * GameMap.TILE_SIZE);
		g.rotate(xTile, yTile, getAngle());
		drawImagePosition(xTile, yTile);
		g.rotate(xTile, yTile, -getAngle());
	}

	protected void drawImagePosition(float x, float y) {
		image.draw(x + centerXAdd, y + centerYAdd);
	}

	protected void updateVector() {
		vecX = (float) FastTrig.sin(Math.toRadians(angle));
		vecY = (float) -FastTrig.cos(Math.toRadians(angle));
	}

	public boolean isCompleted() {
		return completed;
	}

	/**
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * @param angle
	 *            the angle to set
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void addPosToDrawCenter(float xadd, float yadd) {
		centerXAdd = xadd;
		centerYAdd = yadd;
	}
}
