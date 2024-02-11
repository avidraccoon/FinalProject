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
                ctx.enableAutomaticPings();
                System.out.println("Connected");
                String username = "User" + nextUserNumber++;
                userUsernameMap.put(ctx, username);
                ctx.send(generateUserJSON(username));
            });
            ws.onMessage(ctx -> {
                parseMessage(ctx);
            });
            ws.onBinaryMessage(ctx -> System.out.println("Message"));
            ws.onClose(ctx -> {
                System.out.println("Closed");
                Lobby lobby = Main.getPlayersLobby(ctx);
                if (lobby != null){
                    Game game = lobby.getGame();
                    lobby.removePlayer(ctx, userUsernameMap.remove(ctx));
                }else{
                    userUsernameMap.remove(ctx);
                }

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
        if (userUsernameMap.get(ctx).equals((String) map.get("username"))){
            Lobby lobby = Main.getPlayersLobby(ctx);
            Player player = null;
            if (lobby != null){
                Game game = lobby.getGame();
                player = game.players.get(map.get("username"));
            }
            //System.out.println(map);
            switch ((String) map.get("packetType")) {
                case "keyPressed":
                    player.pressKey((String) map.get("key"));
                    System.out.println(map.get("key"));
                    break;
                case "keyReleased":
                    player.releaseKey((String) map.get("key"));
                    System.out.println(map.get("key"));
                    break;
                case "usernameChange":
                    if (userUsernameMap.containsValue(map.get("newUsername"))){
                        sendUsernameChangeDenial(ctx);
                    }else{
                        changeUserName((String) map.get("newUsername"), ctx);
                        sendUsernameChangeApproval(ctx);
                    }
                    break;
                case "createLobby":
                    createLobby(map, ctx);
                case "joinLobby":
                    joinLobby(map, ctx, (String) map.get("lobby"));
            }
        }
    }

    private void createLobby(HashMap map, WsContext ctx) {
        String lobbyToken = Main.createNewLobby();
        joinLobby(map, ctx, lobbyToken);
    }

    private void joinLobby(HashMap map, WsContext ctx, String lobbyToken){
        Lobby lobby = Main.getLobby(lobbyToken);
        if (lobby == null){
            sendLobbyJoinDenial(map, ctx);
        }else{
            lobby.addPlayer(ctx, userUsernameMap.get(ctx));
            sendLobbyJoinApproval(map, ctx);
        }
    }

    private void sendLobbyJoinApproval(HashMap map, WsContext ctx) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("packetType", "lobbyJoinResponse");
        root.put("accepted", true);
        String json = root.toString();
        ctx.send(json);
    }

    private void sendLobbyJoinDenial(HashMap map, WsContext ctx) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("packetType", "lobbyJoinResponse");
        root.put("accepted", false);
        String json = root.toString();
        ctx.send(json);
    }

    private void sendUsernameChangeApproval(WsMessageContext ctx) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("packetType", "usernameResponse");
        root.put("accepted", true);
        String json = root.toString();
        ctx.send(json);
    }

    private void changeUserName(String name, WsMessageContext ctx) {
        userUsernameMap.put(ctx, name);
        Lobby lobby = Main.getPlayersLobby(ctx);
        if (lobby != null){
            lobby.renamePlayer(ctx, name);
        }
        ctx.send(generateUserJSON(name));
    }

    private void sendUsernameChangeDenial(WsMessageContext ctx) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("packetType", "usernameResponse");
        root.put("accepted", false);
        String json = root.toString();
        ctx.send(json);
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
        try {
            userUsernameMap.keySet().stream().filter(ctx -> ctx.session.isOpen()).forEach(session -> {
                session.send(message);
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String generatePosistionJSON(WsContext ctx){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("packetType", "update");
        ObjectNode playerNode = mapper.createObjectNode();
        Lobby lobby = Main.getPlayersLobby(ctx);
        Game game = lobby.getGame();
        try {
            game.players.keySet().forEach(str -> {
                Player player = game.players.get(str);
                try {
                    playerNode.put(str, mapper.writeValueAsString(player));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
        root.put("players", playerNode);
        root.put("lobby", lobby.getToken());
        root.put("started", lobby.isStarted());
        root.put("timeToStart", (int) (lobby.timeToStart()/60));
        String json = root.toString();
        //System.out.println(json);
        return json;
    }

    public void sendUpdate() {
        for (WsContext ctx : userUsernameMap.keySet()) {
            Lobby lobby = Main.getPlayersLobby(ctx);
            if (lobby != null){
                try {
                    ctx.send(generatePosistionJSON(ctx));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
}
