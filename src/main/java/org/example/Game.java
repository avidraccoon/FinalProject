package org.example;

import io.javalin.websocket.WsContext;

import java.io.ObjectOutputStream.PutField;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    public final Map<String, Player> players = new HashMap<String, Player>();

    public void renamePlayer(String put, String name) {
        players.put(name, players.get(put));
        players.remove(put);
    }
}
