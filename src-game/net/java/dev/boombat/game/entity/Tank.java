package net.java.dev.boombat.game.entity;

import java.util.ArrayList;
import java.util.List;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.layer.MissileList;
import net.java.dev.boombat.game.entity.layer.NodeList;
import net.java.dev.boombat.game.entity.layer.ParticleList;
import net.java.dev.boombat.game.entity.layer.PlayerList;
import net.java.dev.boombat.game.util.CollisionUtil;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.game.util.SoundFilter;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.util.FastTrig;

/**
 * 
 * @author objectworks
 */
public class Tank implements Cloneable {

	public static final int SIZE = 32;
	public static final int HALF_SIZE = Math.round(SIZE / 2);
	protected Image bodyImage;
	protected Image turretImage;
	protected float x;
	protected float y;
	protected float bodyAngle;
	protected float turretAngle;
	protected float vecX;
	protected float vecY;
	protected byte maxRocketAmmo = 20;
	protected byte maxBulletAmmo = 20;
	protected byte currentRocketAmmo;
	protected byte currentBulletAmmo;
	protected Sound bulletFireSound;
	protected Sound rocketFireSound;
	protected float moveSpeed;
	protected float rotateSpeed;
	protected float xTurretSpace;
	protected float yTurretSpace;
	protected boolean infiniteBulletAmmo;
	protected boolean infiniteRocketAmmo;
	protected byte numberOfBulletFire = 1;
	protected byte numberOfRocketFire = 1;
	protected byte tankOwnerId;
	protected float turretVecX;
	protected float turretVecY;
	protected int bulletReloadCounter;
	protected int rocketReloadCounter;
	protected int reloadTime = 2000;
	protected float startRocketDistance;
	protected float startBulletDistance;
	protected String fireBulletConfigurableEmitterName;
	protected String fireRocketConfigurableEmitterName;
	protected float rocketX;
	protected float rocketY;
	protected float bulletX;
	protected float bulletY;
	protected String name;
	protected BaseParticle explosionParticle;
	protected String rocketSmokeColorName = "white";
	protected int rocketSmokeColorType = RocketSmokeEmitter.COLOR_TYPE_DARK;
	protected String key;

	/**
	 * All Flags for tank's state
	 */
	// public static final byte FWD_STATE = 1;
	// public static final byte BWD_STATE = 2;
	// public static final byte RBLEFT_STATE = 4;
	// public static final byte RBRIGHT_STATE = 8;
	// public static final byte RTLEFT_STATE = 16;
	// public static final byte RTRIGHT_STATE = 32;
	// public static final byte FIREB_STATE = 64;
	// public static final byte FIRER_STATE = 127;
	// private byte oldflags = 0;
	// private byte recvflags = 0;
	public Tank(String tankName, Image bodyImage, Image turretImage, float x,
			float y, float bodyAngle, float turretAngle, float moveSpeed,
			float rotateSpeed, float xTurretSpace, float yTurretSpace,
			float rocketStartDistance, float bulletStartDistance,
			boolean infiniteBulletAmmo, boolean infiniteRocketAmmo,
			int reloadTime, byte numBullet, byte numRocket, byte maxAmmoBullet,
			byte maxAmmoRocket, String fireBulletConfigurableEmitterName,
			String fireRocketConfigurableEmitterName) {
		this(tankName, bodyImage, turretImage, x, y, bodyAngle, turretAngle,
				moveSpeed, rotateSpeed, xTurretSpace, yTurretSpace,
				ResourceManager.getSound("bulletFireSound"), ResourceManager
						.getSound("rocketFireSound"), rocketStartDistance,
				bulletStartDistance, infiniteBulletAmmo, infiniteRocketAmmo,
				reloadTime, numBullet, numRocket, maxAmmoBullet, maxAmmoRocket,
				fireBulletConfigurableEmitterName,
				fireRocketConfigurableEmitterName);
	}

	public Tank(String tankName, Image bodyImage, Image turretImage, float x,
			float y, float bodyAngle, float turretAngle, float moveSpeed,
			float rotateSpeed, float xTurretSpace, float yTurretSpace,
			float rocketStartDistance, float bulletStartDistance,
			boolean infiniteBulletAmmo, boolean infiniteRocketAmmo,
			int reloadTime, byte numBullet, byte numRocket, byte maxAmmoBullet,
			byte maxAmmoRocket) {

		this(tankName, bodyImage, turretImage, x, y, bodyAngle, turretAngle,
				moveSpeed, rotateSpeed, xTurretSpace, yTurretSpace,
				ResourceManager.getSound("bulletFireSound"), ResourceManager
						.getSound("rocketFireSound"), rocketStartDistance,
				bulletStartDistance, infiniteBulletAmmo, infiniteRocketAmmo,
				reloadTime, numBullet, numRocket, maxAmmoBullet, maxAmmoRocket,
				"fireBulletConfEmitter", "fireRocketConfEmitter");
	}

	public Tank(String tankName, Image bodyImage, Image turretImage, float x,
			float y, float bodyAngle, float turretAngle, float moveSpeed,
			float rotateSpeed, float xTurretSpace, float yTurretSpace,
			float rocketStartDistance, float bulletStartDistance,
			boolean infiniteBulletAmmo, boolean infiniteRocketAmmo,
			int reloadTime) {

		this(tankName, bodyImage, turretImage, x, y, bodyAngle, turretAngle,
				moveSpeed, rotateSpeed, xTurretSpace, yTurretSpace,
				ResourceManager.getSound("bulletFireSound"), ResourceManager
						.getSound("rocketFireSound"), rocketStartDistance,
				bulletStartDistance, infiniteBulletAmmo, infiniteRocketAmmo,
				reloadTime, (byte) 1, (byte) 1, (byte) 20, (byte) 20,
				"fireBulletConfEmitter", "fireRocketConfEmitter");
	}

	public Tank(String tankName, Image bodyImage, Image turretImage, float x,
			float y, float bodyAngle, float turretAngle, float moveSpeed,
			float rotateSpeed, float xTurretSpace, float yTurretSpace,
			Sound bulletFireSound, Sound rocketFireSound,
			float rocketStartDistance, float bulletStartDistance,
			boolean infiniteBulletAmmo, boolean infiniteRocketAmmo,
			int reloadTime, byte numBullet, byte numRocket, byte maxAmmoBullet,
			byte maxAmmoRocket, String fireBulletConfigurableEmitterName,
			String fireRocketConfigurableEmitterName) {
		this.name = tankName;
		this.bodyImage = bodyImage;
		this.turretImage = turretImage;
		this.x = x;
		this.y = y;
		this.bodyAngle = bodyAngle;
		this.turretAngle = turretAngle;
		this.bulletFireSound = bulletFireSound;
		this.rocketFireSound = rocketFireSound;
		this.moveSpeed = moveSpeed;
		this.rotateSpeed = rotateSpeed;
		this.xTurretSpace = xTurretSpace;
		this.yTurretSpace = yTurretSpace;
		this.infiniteBulletAmmo = infiniteBulletAmmo;
		this.infiniteRocketAmmo = infiniteRocketAmmo;
		this.reloadTime = reloadTime;
		this.maxBulletAmmo = maxAmmoBullet;
		this.maxRocketAmmo = maxAmmoRocket;
		this.currentBulletAmmo = this.maxBulletAmmo;
		this.currentRocketAmmo = this.maxRocketAmmo;
		this.numberOfBulletFire = numBullet;
		this.numberOfRocketFire = numRocket;
		this.maxBulletAmmo = maxAmmoBullet;
		this.maxRocketAmmo = maxAmmoRocket;
		this.startBulletDistance = bulletStartDistance;
		this.startRocketDistance = rocketStartDistance;
		this.fireBulletConfigurableEmitterName = fireBulletConfigurableEmitterName;
		this.fireRocketConfigurableEmitterName = fireRocketConfigurableEmitterName;
		updateBodyVector();
	}

	public void update(Input input, int delta) {
		// byte flags = 0;

		if (input.isKeyDown(Config.getKey("forwardKey"))) {
			// flags |= FWD_STATE;
			moveForward(delta);
		}

		if (input.isKeyDown(Config.getKey("backwardKey"))) {
			// flags |= BWD_STATE;
			moveBackward(delta);
		}

		if (input.isKeyDown(Config.getKey("rotateCWBodyKey"))) {
			// flags |= RBRIGHT_STATE;
			rotateCWBody(delta);
		}

		if (input.isKeyDown(Config.getKey("rotateCCWBodyKey"))) {
			// flags |= RBLEFT_STATE;
			rotateCCWBody(delta);
		}

		if (input.isKeyDown(Config.getKey("rotateCWTurretKey"))) {
			// flags |= RTRIGHT_STATE;
			rotateCWTurret(delta);
		}

		if (input.isKeyDown(Config.getKey("rotateCCWTurretKey"))) {
			// flags |= RTLEFT_STATE;
			rotateCCWTurret(delta);
		}

		autoReloadRocket(delta);
		autoReloadBullet(delta);

		// if (oldflags == flags) {
		// do not send over network
		// return -1;
		// }

		// oldflags = flags;
		// return flags;

	}

	public void update(int delta) {
		autoReloadRocket(delta);
		autoReloadBullet(delta);
	}

	protected void updateMissilePosition() {
		updateTurretVector();
		rocketX = x + (turretVecX * startRocketDistance);
		rocketY = y + (turretVecY * startRocketDistance);
		bulletX = x + (turretVecX * startBulletDistance);
		bulletY = y + (turretVecY * startBulletDistance);

	}

	public void render(Graphics g) {
		drawBodyImage(g);
		drawTurretImage(g);
	}

	protected void moveForward(int delta) {
		updateMove(delta, 1);
	}

	protected void moveBackward(int delta) {
		updateMove(delta, -1);
	}

	protected void rotateCWBody(int delta) {
		this.bodyAngle = updateRotate(delta, getBodyAngle(), 1);
		rotateCWTurret(delta);
		updateBodyVector();
	}

	protected void rotateCCWBody(int delta) {
		this.bodyAngle = updateRotate(delta, getBodyAngle(), -1);
		rotateCCWTurret(delta);
		updateBodyVector();
	}

	protected void rotateCWTurret(int delta) {
		this.turretAngle = updateRotate(delta, getTurretAngle(), 1);
	}

	protected void rotateCCWTurret(int delta) {
		this.turretAngle = updateRotate(delta, getTurretAngle(), -1);
	}

	protected void drawBodyImage(Graphics g) {
		// draw centered
		drawImage(g, bodyImage, getBodyAngle(), -HALF_SIZE, -HALF_SIZE);
	}

	protected void drawTurretImage(Graphics g) {
		drawImage(g, turretImage, getTurretAngle(), xTurretSpace, yTurretSpace);
	}

	protected void drawImage(Graphics g, Image image, float angle,
			float xSpace, float ySpace) {
		int xTile = Math.round(getX() * Tank.SIZE);
		int yTile = Math.round(getY() * Tank.SIZE);
		g.rotate(xTile, yTile, angle);
		image.draw(xTile + xSpace, yTile + ySpace);
		g.rotate(xTile, yTile, -angle);
	}

	protected void updateTurretVector() {
		float[] vector = updateVector(turretAngle);
		turretVecX = vector[0];
		turretVecY = vector[1];
	}

	protected void updateBodyVector() {
		float[] vector = updateVector(bodyAngle);
		vecX = vector[0];
		vecY = vector[1];
	}

	protected float[] updateVector(float angle) {
		float vectorx = (float) FastTrig.sin(Math.toRadians(angle));
		float vectory = (float) -FastTrig.cos(Math.toRadians(angle));
		return new float[] { vectorx, vectory };
	}

	protected float updateRotate(int delta, float angle, int dir) {
		float newDelta = delta * rotateSpeed * dir;
		angle += newDelta;
		return angle;
	}

	protected void updateMove(int delta, int dir) {
		float newDelta = delta * moveSpeed;
		float newX = vecX * newDelta * dir;
		float newY = vecY * newDelta * dir;
		GameMap gameMap = Config.getSelectedGameMap();
		if (!gameMap.isCollide(x, y, newX, newY)
				&& !CollisionUtil.isCollideWithEdgeScreen(x + newX, y + newY)
				&& PlayerList.isCollide(tankOwnerId, x + newX, y + newY,
						Tank.HALF_SIZE) == null) {
			setX(getX() + newX);
			setY(getY() + newY);
		}
	}

	protected byte doFire(byte currentAmmo, byte numMissileFire,
			boolean infiniteAmmo, Sound sound, boolean isRocket,
			String emitterName, int lifeTime) {
		if (currentAmmo <= 0) {
			return currentAmmo;
		}

		updateMissilePosition();

		List<Missile> m = produceMissile(isRocket);

		BaseParticle p = new BaseParticle(ResourceManager
				.loadConfigurableEmitter(emitterName), lifeTime);
		if (isRocket) {
			p.setPosition(rocketX, rocketY, turretAngle);
		} else {
			p.setPosition(bulletX, bulletY, turretAngle);
		}

		MissileList.add(m);
		ParticleList.add(p);

		Tank tankSource = PlayerList.getLocalPlayer().getTank();
		SoundFilter.filter3D(tankSource.getX(), tankSource.getY(), x, y, sound,
				Config.MAX_SOUND_DISTANCE_IN_TILES);

		if (!infiniteAmmo) {
			currentAmmo -= m.size();
		}

		return currentAmmo;
	}

	/**
	 * If you wanna change tank's rocket hardcoded, so you have to override this
	 * method
	 * 
	 * @return boombat.entity.Missile rocket
	 */
	protected Missile getRocket() {
		updateMissilePosition();
		Rocket rocket = new Rocket(tankOwnerId, rocketX, rocketY,
				startRocketDistance, turretAngle);
		SmokeRocketParticle p = (SmokeRocketParticle) rocket.getSmokeParticle();
		p.setColor(rocketSmokeColorName, rocketSmokeColorType);
		return rocket;
	}

	/**
	 * If you wanna change tank's bullet hardcoded, so you have to override this
	 * method
	 * 
	 * @return boombat.entity.Missile bullet
	 */
	protected Missile getBullet() {
		updateMissilePosition();
		return new Bullet(tankOwnerId, bulletX, bulletY, startBulletDistance,
				turretAngle);
	}

	protected List<Missile> produceMissile(boolean isRocket) {
		byte numMissileFire = 0;
		byte currentMissile = 0;
		Missile missile = null;
		List<Missile> returnMissiles = new ArrayList<Missile>();

		if (isRocket) {
			numMissileFire = numberOfRocketFire;
			currentMissile = currentRocketAmmo;
		} else {
			numMissileFire = numberOfBulletFire;
			currentMissile = currentBulletAmmo;
		}

		if (numMissileFire > currentMissile) {
			numMissileFire = currentMissile;
		}

		for (int i = 0; i < numMissileFire; i++) {
			if (isRocket) {
				missile = getRocket();
			} else {
				missile = getBullet();
			}
			missile.setAngle(turretAngle + i);
			returnMissiles.add(missile);
		}

		return returnMissiles;
	}

	public void fireRocket() {
		currentRocketAmmo = doFire(currentRocketAmmo, numberOfRocketFire,
				infiniteRocketAmmo, rocketFireSound, true,
				fireRocketConfigurableEmitterName, 80);
	}

	public void fireBullet() {
		currentBulletAmmo = doFire(currentBulletAmmo, numberOfBulletFire,
				infiniteBulletAmmo, bulletFireSound, false,
				fireBulletConfigurableEmitterName, 50);
	}

	protected void autoReloadRocket(int delta) {
		int[] i = autoReloadMissile(delta, currentRocketAmmo, maxRocketAmmo,
				rocketReloadCounter);
		currentRocketAmmo = (byte) i[0];
		bulletReloadCounter = i[1];
	}

	protected void autoReloadBullet(int delta) {
		int[] i = autoReloadMissile(delta, currentBulletAmmo, maxBulletAmmo,
				bulletReloadCounter);
		currentBulletAmmo = (byte) i[0];
		rocketReloadCounter = i[1];
	}

	protected int[] autoReloadMissile(int delta, byte currentAmmo,
			byte maxAmmo, int reloadCounter) {
		if (currentAmmo < maxAmmo && reloadCounter >= reloadTime) {
			currentAmmo++;
			reloadCounter = 0;
			return new int[] { currentAmmo, reloadCounter };
		}
		reloadCounter += delta;
		return new int[] { currentAmmo, reloadCounter };
	}

	public void explode() {
		ParticleEmitter emitter = ResourceManager
				.loadConfigurableEmitter("tankExplosionConfEmitter");
		explosionParticle = new BaseParticle(emitter, 30);
		explosionParticle.setPosition(x, y, bodyAngle);
		ParticleList.add(explosionParticle);
		Node node = new Node("nodeTankImage", x, y, bodyAngle);
		NodeList.add(node);
		Sound sound = ResourceManager.getSound("rocketExplosionSound");
		Tank tankSource = PlayerList.getLocalPlayer().getTank();
		SoundFilter.filter3D(tankSource.getX(), tankSource.getY(), x, y, sound,
				Config.MAX_SOUND_DISTANCE_IN_TILES);
	}

	public Tank clone() {
		try {
			return (Tank) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return the bodyAngle
	 */
	public float getBodyAngle() {
		return bodyAngle;
	}

	/**
	 * @param bodyAngle
	 *            the bodyAngle to set
	 */
	public void setBodyAngle(float bodyAngle) {
		this.bodyAngle = bodyAngle;
	}

	/**
	 * @return the turretAngle
	 */
	public float getTurretAngle() {
		return turretAngle;
	}

	/**
	 * @param turretAngle
	 *            the turretAngle to set
	 */
	public void setTurretAngle(float turretAngle) {
		this.turretAngle = turretAngle;
	}

	public String getName() {
		return name;
	}

	public void setTankOwnerId(byte tankOwnerId) {
		this.tankOwnerId = tankOwnerId;
	}

	public void setRocketSmokeColor(String name, int type) {
		this.rocketSmokeColorName = name;
		this.rocketSmokeColorType = type;
	}

	public byte getCurrentRocketAmmo() {
		return currentRocketAmmo;
	}

	public void setCurrentRocketAmmo(byte currentRocketAmmo) {
		this.currentRocketAmmo = currentRocketAmmo;
	}

	public byte getCurrentBulletAmmo() {
		return currentBulletAmmo;
	}

	public void setCurrentBulletAmmo(byte currentBulletAmmo) {
		this.currentBulletAmmo = currentBulletAmmo;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void resetAmmo() {
		currentBulletAmmo = maxBulletAmmo;
		currentRocketAmmo = maxRocketAmmo;
	}

	// public void setRecvflags(byte recvflags) {
	// this.recvflags = recvflags;
	// }

	// public byte getRecvflags() {
	// return recvflags;
	// }

}
