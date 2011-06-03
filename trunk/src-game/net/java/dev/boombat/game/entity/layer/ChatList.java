package net.java.dev.boombat.game.entity.layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatList {

	private static List<String> chats = Collections
			.synchronizedList(new ArrayList<String>());
	private static int maxSize = 5;

	public static void add(String chat) {
		synchronized (chats) {
			if (maxSize == chats.size()) {
				chats.remove(chats.size() - 1);
			}
			chats.add(0, chat);
		}
	}

	public static void clear() {
		synchronized (chats) {
			chats.clear();
		}
	}

	public static List<String> getChats() {

		return chats;
	}

	public static int getMaxSize() {
		return maxSize;
	}

	public static void setMaxSize(int maxSize) {
		ChatList.maxSize = maxSize;
	}
}
