/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.dev.boombat.game.test;

import static org.junit.Assert.assertEquals;

import net.java.dev.boombat.game.util.PropertiesFile;

import org.junit.Test;


/**
 * 
 * @author objectworks
 */
public class PropertiesUtilTest {

	@Test
	public void load() throws Exception {
		PropertiesFile props = new PropertiesFile("testdata/test.properties");
		String tankImage = props.getString("loadme");
		assertEquals("boombat", tankImage);
	}

	@Test
	public void modifyAndSave() {
		PropertiesFile props = new PropertiesFile("testdata/test.properties");
		props.setString("modifyme", "welcome2");
		props.setBoolean("bool", false);
		props.save();
	}
}
