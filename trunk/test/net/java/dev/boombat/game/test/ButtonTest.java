/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.test;

import net.java.dev.boombat.game.Config;
import net.java.dev.boombat.game.entity.layer.ParticleList;
import net.java.dev.boombat.game.ui.core.Button;
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
public class ButtonTest extends BasicGame {

        private Button testButton;
            
	public static void main(String... args) throws SlickException {
		AppGameContainer game = new AppGameContainer(new ButtonTest(),800,600,false);
                game.start();
	}

	public ButtonTest() {
		super("Button Test");
	}

	@Override
	public void init(GameContainer c) throws SlickException {
		ResourceManager.init();
                testButton = new Button(c, ResourceManager
				.getImage("createGameNormalImage"), 300, 200);
                testButton.setMouseOverImage(ResourceManager
				.getImage("createGameOverImage"));

	}

	@Override
	public void update(GameContainer c, int delta) throws SlickException {
		Input input = c.getInput();

		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			AppGameContainer ac = (AppGameContainer) c;
			Config.getOption().save();
			ac.exit();
                }
	}

	public void render(GameContainer c, Graphics g) throws SlickException {
                testButton.render(c, g);
	}
}
