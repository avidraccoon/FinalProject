package org.example;

public class GameLoop extends Thread{
    private boolean running;
    private Game game;
    public GameLoop(Game game){
        this.game = game;
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
        game.players.keySet().forEach(str -> {
            Player player = game.players.get(str);
            if (player.isPressed("w")){player.y--;}
            if (player.isPressed("s")){player.y++;}
            if (player.isPressed("a")){player.x--;}
            if (player.isPressed("d")){player.x++;}
            player.applyReleased();
        });
    }
}
