package net.java.dev.boombat.game.test;

import org.newdawn.slick.Input;

public class PrintInputTest {
	public static void main(String[] args) {
		for (int i = 0; i < 255; i++) {
			String in = Input.getKeyName(i);
			if (in != null) {
				System.out.println(in + ":" + i);
			}
		}

	}
}
