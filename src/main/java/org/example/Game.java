package org.example;

import io.javalin.websocket.WsContext;

import java.io.ObjectOutputStream.PutField;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Game {
    public final Map<String, Player> players = new HashMap<String, Player>();
    private World world;

    public void renamePlayer(String put, String name) {
        players.put(name, players.get(put));
        players.remove(put);
    }

    public String toJSON(){
        return toJSONNode().toString();
    }

    public ObjectNode toJSONNode(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        Object
        for (String playerName : players.keySet()) {
            Player player = players.get(playerName);
        }

        return root;
    }
}
