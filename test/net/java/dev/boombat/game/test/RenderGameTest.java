/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.dev.boombat.game.test;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.Camera;
import net.java.dev.boombat.game.entity.GameMap;
import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.game.entity.Tank;
import net.java.dev.boombat.game.entity.layer.MissileList;
import net.java.dev.boombat.game.entity.layer.NodeList;
import net.java.dev.boombat.game.entity.layer.ParticleList;
import net.java.dev.boombat.game.entity.layer.PlayerList;
import net.java.dev.boombat.game.ui.ext.ChangeMapBox;
import net.java.dev.boombat.game.ui.ext.ChangeTeamBox;
import net.java.dev.boombat.game.ui.ext.ChatBoard;
import net.java.dev.boombat.game.ui.ext.ChatBox;
import net.java.dev.boombat.game.ui.ext.ConfirmationBox;
import net.java.dev.boombat.game.ui.ext.FlashMessageBox;
import net.java.dev.boombat.game.ui.ext.KeyInfoBox;
import net.java.dev.boombat.game.ui.ext.OptionsBox;
import net.java.dev.boombat.game.ui.ext.PlayerBar;
import net.java.dev.boombat.game.ui.ext.Radar;
import net.java.dev.boombat.game.ui.ext.ScoreList;
import net.java.dev.boombat.game.ui.ext.SystemMessageBoard;
import net.java.dev.boombat.game.ui.ext.TimerBox;
import net.java.dev.boombat.game.util.ResourceManager;
import net.java.dev.boombat.multiplayer.core.GameSession;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Admin
 */
public class RenderGameTest extends BasicGame{

    private KeyInfoBox keyInfoBox;
    private Radar radar;
    private PlayerBar playerBar;
    private ChatBox chatBox;
    private ChangeTeamBox changeTeamBox;
    private ScoreList scoreList;
    private OptionsBox optionsBox;
    private TimerBox timerBox;
    private ChatBoard chatBoard;
    private SystemMessageBoard systemMessageBoard;
    private ConfirmationBox confirmationBox;
    private ChangeMapBox changeMapBox;
    private int elapsedTime = 1000*240;
    private int displayTimeInSeconds;
    
    private float x;
    private float y;        
    private float bodyAngle;        
    private float turretAngle; 
        
    public static void main(String... args) throws SlickException {
        AppGameContainer game = new AppGameContainer(new RenderGameTest(),800,600,false);
        game.start();
    }

    public RenderGameTest() {
	super("Render UI Test");
    }
    @Override
    public void init(GameContainer c) throws SlickException {
        ResourceManager.init();
        
        keyInfoBox = new KeyInfoBox(c, 65, 30);
	radar = new Radar(673, 5);
	radar.setVisible(true);
	playerBar = new PlayerBar(547, 490);
	playerBar.setVisible(true);
	chatBox = new ChatBox(c, 10, 560);
	changeTeamBox = new ChangeTeamBox(c, 150, 80);
	scoreList = new ScoreList(120, 30);
	optionsBox = new OptionsBox(c, 150, 80);
	timerBox = new TimerBox(10, 10);
	timerBox.setVisible(true);
        //Please comment next line to unregister game session
        GameSession.startCreatorSession();
        chatBoard = new ChatBoard(10, 50, 5);
	chatBoard.setVisible(true);

	systemMessageBoard = new SystemMessageBoard(135, 5);
	systemMessageBoard.setVisible(true);

	FlashMessageBox.instance();

	confirmationBox = new ConfirmationBox(c, 190, 140, "End Game ?");
        changeMapBox = new ChangeMapBox(c, 150, 80);
        
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
	//change next line to change player state
        player.setState(Player.STATE_LIFE);
	player.setTeam(Player.TEAM_TERRORIST);
        PlayerList.addPlayer(player);
                
        Camera.instance().setX(x);
	Camera.instance().setY(y);
    }

    @Override
    public void update(GameContainer c, int delta) throws SlickException {
        Player p = PlayerList.getLocalPlayer();
		
	Tank tank = p.getTank();
	Input input = c.getInput();
	if (Player.STATE_LIFE == p.getState()) {
            tank.update(input, delta);
	}else{
            Camera.instance().update(input, delta);
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
        
        if (radar.isVisible()) {
            radar.update(delta);
	}
	playerBar.update(delta);
	chatBox.update(delta);
	changeTeamBox.update(delta);
	changeMapBox.update(delta);
	optionsBox.update(c, delta);
        elapsedTime -= delta;
	displayTimeInSeconds = Math.round(elapsedTime / 1000f);
	timerBox.update(elapsedTime);

	// update score list visible ...
	if (c.getInput().isKeyDown(Config.getKey("showScoreKey"))) {
            scoreList.setVisible(true);
	} else {
            scoreList.setVisible(false);
	}
	FlashMessageBox.instance().update(delta);
    }

    public void render(GameContainer c, Graphics g) throws SlickException {
        if (GameSession.isSessionActive()) {
            renderGameObject(c, null, g);
            renderUI(c, null, g);
        }
    }
    
    private void renderUI(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		playerBar.render(g);
		if (radar.isVisible()) {
			radar.render(g);
		}
		timerBox.render(g);
		chatBoard.render(g);
		keyInfoBox.render(container, g);
		chatBox.render(container, g);
		systemMessageBoard.render(g);
		changeTeamBox.render(container, g);
		changeMapBox.render(container, g);
		optionsBox.render(container, g);
		scoreList.render(g);
		confirmationBox.render(container, g);
		FlashMessageBox.instance().render(g);
    }
    
    private void renderGameObject(GameContainer container,
			StateBasedGame game, Graphics g) throws SlickException {
		Tank refTank = PlayerList.getLocalPlayer().getTank();
                float xpos;
		float ypos;
		int size;
		if (Player.STATE_LIFE != PlayerList.getLocalPlayer().getState()) {
                    xpos = Camera.instance().getX();
                    ypos = Camera.instance().getY();
                    size = 64;
		}else{
                    xpos = refTank.getX();
                    ypos = refTank.getY();
                    size = Tank.SIZE;
                }
		Config.getSelectedGameMap().render(g, xpos, ypos, size);
		PlayerList.renderLocalPlayer(g);
		PlayerList.renderRemotePlayers(g);
		NodeList.renderAll(g);
		MissileList.renderAll(g);
		ParticleList.renderAll(g);
		if (Player.STATE_LIFE != PlayerList.getLocalPlayer().getState()) {
			Camera.instance().render(g);
		}
		g.resetTransform();
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
