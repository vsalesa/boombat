/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.java.dev.boombat.game.test;

import net.java.dev.boombat.game.state.MainMenuState;
import net.java.dev.boombat.game.util.ResourceManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Admin
 */
public class MainMenuStateTest extends StateBasedGame{
    public static void main(String... args) throws SlickException {
	AppGameContainer game = new AppGameContainer(new MainMenuStateTest(
				"Main Menu Test"), 800, 600, false);
	game.start();
    }
    public MainMenuStateTest(String name){
        super(name);
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        ResourceManager.init();
        addState(new MainMenuState());
    }
}
