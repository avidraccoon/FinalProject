package org.example;

import io.javalin.websocket.WsContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    public final Map<String, Player> players = new HashMap<String, Player>();
}
