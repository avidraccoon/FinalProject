package org.example;

import io.javalin.websocket.WsContext;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    private static final Server server = new Server();
    private static final ArrayList<Lobby> lobbies = new ArrayList<Lobby>();
    private static ArrayList<String> lobbyTokens = new ArrayList<String>();
    public static void main(String[] args) {
        boolean running = true;
        double ns = 1000000000.0 / 60.0;
        double delta = 0;

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                server.sendUpdate();
                for (Lobby lobby : lobbies) {
                    lobby.tick();
                }
                delta--;
            }
        }
    }

    public static Lobby getPlayersLobby(WsContext ctx){
        for (Lobby lobby : lobbies) {
            if (lobby.contains(ctx)){
                return lobby;
            }
        }
        return null;
    }

    public static String createNewLobby() {
        String rand = randomString();
        while (lobbyTokens.contains(rand)){
            rand = randomString();
        }
        Lobby newLobby = new Lobby(""+rand);
        String token = newLobby.getToken();
        lobbies.add(newLobby);
        return token;
    }

    public static Lobby getLobby(String lobbyToken) {
        for (Lobby lobby : lobbies) {
            if (lobby.tokenIs(lobbyToken)){
                return lobby;
            }
        }
        return null;
    }

    public static String randomString(){
      String string = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

      StringBuilder strbuilder = new StringBuilder();
      Random rand = new Random();
      int length = 5;

      for (int i = 0; i < length; i++) {
         int index = rand.nextInt(string.length());
         char randomChar = string.charAt(index);
         strbuilder.append(randomChar);
      }

      String randomString = strbuilder.toString();
      return randomString;
    }
}