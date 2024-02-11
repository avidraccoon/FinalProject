package org.example;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class World {
    private static final int defaultWidth = 5;
    private static final int defaultHeight = 5;
    private WorldTile[][] tiles;
    private int width;
    private int height;


    public World(){
        this(defaultWidth, defaultHeight);
    }

    public World(int w, int h){
        width = w;
        height = h;
        tiles = new WorldTile[h][w];
        for (int hI = 0; hI<h; hI++){
            for (int wI = 0; wI<w; wI++){
                tiles[hI][wI] = new WorldTile();
            }
        }
    }

    public WorldTile getWorldTile(int x, int y){
        return tiles[y][x];
    }

    public void setWorldTile(int x, int y, WorldTile t){
        tiles[y][x] = t;
    }

    public int getTile(int wX, int wY, int tX, tY){
        return tiles[wY][wX].getTile(tx, ty);
    }

    public void setTile(int wX, int wY, int tX, int tY, int v){
        tiles[y][x].setTile(tX, tY, v);
    }

    public static int toWorldCord(int c){
        return c/400;
    }

    public static int toTileCord(int c){
        return c%400/8;
    }

    public String toJSON(){
        return toJSONNode().toString();
    }

    public ObjectNode toJSONNode(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        ArrayNode jsonTiles = mapper.createArrayNode();

        for (WorldTile[] arr : tiles){
            ArrayNode collum = mapper.createArrayNode();
            for (WorldTile val : arr){
                collum.add(val.toJSONNode());
            }
            jsonTiles.add(jsonTiles);
        }
        
        root.put("tiles", jsonTiles);
        root.put("width", width);
        root.put("height", height);
        
        return root;
    }
}
