package net.java.dev.boombat.multiplayer.message;

import java.util.List;
import java.util.Map;

import net.java.dev.boombat.game.entity.Player;
import net.java.dev.boombat.multiplayer.core.Util;

import org.apache.mina.core.buffer.IoBuffer;

public class ValidLoginMsg extends Message {
	public final static byte ID = 14;
	public String map;
	public Map<Byte, short[]> scores;
	public int currRemainingTime;
	public byte rndCount;
	public byte CTWinCount;
	public byte TWinCount;
	public byte currWinner;
	public List<Player> players;
	public String serverName;
	public boolean playerRadar;

	@Override
	public Message fill(IoBuffer buff) {
		map = Util.getStr(buff);
		scores = Util.getScoreList(buff);
		currRemainingTime = buff.getInt();
		rndCount = buff.get();
		CTWinCount = buff.get();
		TWinCount = buff.get();
		currWinner = buff.get();
		players = Util.getPlayerList(buff);
		serverName = Util.getStr(buff);
		playerRadar = Util.getBool(buff);
		return this;
	}

	@Override
	public IoBuffer toBuffer() {
		IoBuffer buff = IoBuffer.allocate(1);
		buff.setAutoExpand(true);
		buff.put(ID);
		Util.putStr(map, buff);
		Util.putScoreList(scores, buff);
		buff.putInt(currRemainingTime);
		buff.put(rndCount);
		buff.put(CTWinCount);
		buff.put(TWinCount);
		buff.put(currWinner);
		Util.putPlayerList(players, buff);
		Util.putStr(serverName, buff);
		Util.putBool(playerRadar, buff);
		buff.flip();
		return buff;
	}

	@Override
	public boolean isRelay() {

		return false;
	}
}
