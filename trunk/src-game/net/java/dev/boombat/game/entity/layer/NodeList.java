package net.java.dev.boombat.game.entity.layer;

import java.util.ArrayList;
import java.util.List;

import net.java.dev.boombat.game.entity.Node;

import org.newdawn.slick.Graphics;

public class NodeList {
	public static List<Node> nodes = new ArrayList<Node>();

	public static void add(Node node) {
		nodes.add(node);
	}

	public static void renderAll(Graphics g) {
		for (Node n : nodes) {
			n.render(g);
		}
	}

	public static void clear() {
		nodes.clear();
	}
}
