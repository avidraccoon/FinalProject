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
