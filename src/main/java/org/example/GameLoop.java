package org.example;

public class GameLoop extends Thread{
    private boolean running;

    public GameLoop(){

    }

    public void run(){
        running = true;
        double ns = 1000000000.0 / 60.0;
        double delta = 0;

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                tick();
                delta--;
            }
        }
    }

    private void tick() {
        Game game = Main.getGame();
        game.players.keySet().forEach(str -> {
            Player player = game.players.get(str);
            if (player.up){player.y--;}
            if (player.down){player.y++;}
            if (player.left){player.x--;}
            if (player.right){player.x++;}
        });
    }
}
