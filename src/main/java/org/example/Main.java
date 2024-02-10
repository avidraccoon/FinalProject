package org.example;


import java.util.Scanner;

public class Main {
    private static final Server server = new Server();
    private static GameLoop gameLoop;
    private static Game game;
    public static void main(String[] args) {
        game = new Game();
        Scanner in = new Scanner(System.in);
        //in.nextLine();
        gameLoop = new GameLoop();
        gameLoop.start();
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
                server.broadcastMessage(Server.generatePosistionJSON());
                delta--;
            }
        }
    }

    public static GameLoop getGameLoop(){
        return gameLoop;
    }

    public static Game getGame(){
        return game;
    }
}