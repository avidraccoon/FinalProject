package org.example;

import java.util.ArrayList;

public class World {
    private static final int defaultWidth = 5;
    private static final int defaultHeight = 5;
    private WorldTile[][] tiles;
    private int width;
    private int height;


    public World(){
        
    }

    public World(int w, int h){
        tiles = new WorldTile[h][w];
        for (int hI = 0; hI<h; hI++){
            for (int wI = 0; wI<w; wI++){
                tiles[hI][wI] = new WorldTile();
            }
        }
    }

    public World getLocalWorld(int x, int y){
        int xV = 0;
        int yV = 0;
        World out = new World(3, 3);
        for (int yI = 0; yI<3; yI++){
            yV = (yI+y)%height;
            for (int xI = 0; xI<3; xI++){
                xV = (xI+y)%width;
                out.setTile(xI, yI, this.getTile(xV, yV));
            }
        }
        return out;
    }

    public WorldTile getTile(int x, int y){
        return tiles[y][x];
    }

    public void setTile(int x, int y, WorldTile tile){
        tiles[y][x] = tile;
    }
}
