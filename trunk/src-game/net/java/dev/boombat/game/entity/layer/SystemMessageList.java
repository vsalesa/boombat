package net.java.dev.boombat.game.entity.layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SystemMessageList {

	private static List<String> messages = Collections
			.synchronizedList(new ArrayList<String>());
	private static int maxSize = 4;

	public static void add(String sysMsg) {
		synchronized (messages) {
			if (maxSize == messages.size()) {
				messages.remove(messages.size() - 1);
			}
			messages.add(0, sysMsg);
		}
	}

	public static void clear() {
		synchronized (messages) {
			messages.clear();
		}
	}

	public static List<String> getMessages() {

		return messages;
	}

	public static int getMaxSize() {
		return maxSize;
	}

	public static void setMaxSize(int maxSize) {
		SystemMessageList.maxSize = maxSize;
	}
}
