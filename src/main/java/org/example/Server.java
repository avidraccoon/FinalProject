package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsMessageContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private final Map<WsContext, String> userUsernameMap = new ConcurrentHashMap<>();
    private int nextUserNumber = 1;
    private Javalin app;
    public Server() {
        app = Javalin.create(config -> {
            config.staticFiles.add("src/main/java/org/example/public", Location.EXTERNAL);
        }).start(7070);
        app.ws("/websocket", ws -> {
            ws.onConnect(ctx -> {
                System.out.println("Connected");
                String username = "User" + nextUserNumber++;
                userUsernameMap.put(ctx, username);
                Main.getGame().players.put(username, new Player(0,0));
                ctx.send(generateUserJSON(username));
            });
            ws.onMessage(ctx -> {
                parseMessage(ctx);
            });
            ws.onBinaryMessage(ctx -> System.out.println("Message"));
            ws.onClose(ctx -> {
                System.out.println("Closed");
                String username = userUsernameMap.get(ctx);
                Main.getGame().players.remove(userUsernameMap.remove(ctx));

            });
            ws.onError(ctx -> System.out.println("Errored"));
        });
    }

    private void parseMessage(WsMessageContext ctx) {
        HashMap map;
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(ctx.message(), HashMap.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Game game = Main.getGame();
        Player player = game.players.get(map.get("username"));
        //System.out.println(map);
        switch ((String) map.get("packetType")) {
            case "keyPressed":
                if (map.get("key").equals("w")) {player.up=true;}
                if (map.get("key").equals("a")) {player.left=true;}
                if (map.get("key").equals("s")) {player.down=true;}
                if (map.get("key").equals("d")) {player.right=true;}
                System.out.println(map.get("key"));
                break;
            case "keyReleased":
                if (map.get("key").equals("w")) {player.up=false;}
                if (map.get("key").equals("a")) {player.left=false;}
                if (map.get("key").equals("s")) {player.down=false;}
                if (map.get("key").equals("d")) {player.right=false;}
        }
    }

    private String generateUserJSON(String username) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("packetType", "username");
        root.put("username", username);
        String json = root.toString();
        return json;
    }

    public void broadcastMessage(String message) {
        userUsernameMap.keySet().stream().filter(ctx -> ctx.session.isOpen()).forEach(session -> {
            session.send(message);
        });
    }

    public static String generatePosistionJSON(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("packetType", "position");
        ObjectNode playerNode = mapper.createObjectNode();
        Main.getGame().players.keySet().forEach(str -> {
            Player player = Main.getGame().players.get(str);
            try {
                playerNode.put(str, mapper.writeValueAsString(player));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        root.put("players", playerNode);
        String json = root.toString();
        return json;
    }
}
