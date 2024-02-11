package org.example;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.JsonNode;

import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsContext;

public class Lobby {
    private Game game = new Game();
    private GameLoop gameLoop = new GameLoop(game);
    private final Map<WsContext, String> usersMap = new ConcurrentHashMap<>();
    private String token;
    private int startTime = 120;
    private boolean started = false;

    public Lobby(String tString){
        token = tString;
        //gameLoop.start();
    }

    public boolean isStarted(){
        return started;
    }

    public void tick(){
        if (startTime>0) startTime--;
        else {started = true; if(!gameLoop.isAlive()) gameLoop.start(); }
    }

    public boolean contains(WsContext ctx){
        return usersMap.containsKey(ctx);
    }

    public void renamePlayer(WsContext ctx, String name){
        game.renamePlayer(usersMap.put(ctx, name), name);
    }

    public void addPlayer(WsContext ctx, String name){
        game.players.put(name, new Player((int) (Math.random()*300)+50, (int) (Math.random()*300)+50));
        usersMap.put(ctx, name);
    }

    public Game getGame(){
        return game;
    }

    public String getToken() {
        return token;
    }

    public boolean tokenIs(String lobbyToken) {
        //System.out.println(token.equals(lobbyToken));
        return token.equals(lobbyToken);
    }

    public void removePlayer(WsCloseContext ctx, String remove) {
        usersMap.remove(ctx);
        game.players.remove(remove);
    }

    public int timeToStart() {
        return startTime;
    }
}