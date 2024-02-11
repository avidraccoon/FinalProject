package org.example;

import java.util.ArrayList;

public class Player {
    public int x = 0;
    public int y = 0;
    private ArrayList<String> keysPressed = new ArrayList<String>();
    private ArrayList<String> keysReleased = new ArrayList<String>();

    public Player(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean canMove(int cX, int cY, World world){
        int wX = world.toWorldCord(x);
        int wY = world.toWorldCord(y);
        int tX = world.toTileCord(x);
        int tY = world.toTileCord(y);
        int nX = x+cX;
        int nY = y+cY;
        int nWX = world.toWorldCord(nX);
        int nWY = world.toWorldCord(nY);
        int nTX = world.toTileCord(nX);
        int nTY = world.toTileCord(nY);
        int cur = world.getTile(wX, wY, tX, tY);
        int o = world.getTile(nWX, nWY, nTX, nTY);
        if (cur == o) return true;
        return Tiles.movable(x, y, nX, nY, cur, o, world);
    }

    public void pressKey(String key){
        if (keysReleased.contains(key)) keysReleased.remove(key);
        if (!keysPressed.contains(key)) keysPressed.add(key);
    }

    public void releaseKey(String key){
        if (!keysReleased.contains(key)) keysReleased.add(key);
    }

    public void applyReleased(){
        for (String key : keysReleased) {
            if (keysPressed.contains(key)) keysPressed.remove(key);
        }
    }

    public boolean isPressed(String key){
        return keysPressed.contains(key);
    }
}
