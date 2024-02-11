package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class WorldTile {
    private static final int defaultWidth = 8;
    private static final int defaultHeight = 8;
    private int[][] tiles;
    private int height;
    private int width;

    public WorldTile(){
        this(defaultWidth, defaultHeight);
    }

    public WorldTile(int w, int h){
        width = w;
        height = h;
        tiles = new int[h][w];
        for (int hI = 0; hI<h; hI++){
            for (int wI = 0; wI<w; wI++){
                tiles[hI][wI] = 0;
            }
        }
    }

    public int getTile(int x, int y){
        return tiles[y][x];
    }

    public void setTile(int x, int y, int v){
        tiles[y][x] = v;
    }

    public int[][] getTiles(){
        return tiles;
    }

    public String toJSON(){
        return toJSONNode().toString();
    }

    public ObjectNode toJSONNode(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        ArrayNode jsonTiles = mapper.createArrayNode();

        for (int[] arr : tiles){
            ArrayNode collum = mapper.createArrayNode();
            for (int val : arr){
                collum.add(val);
            }
            jsonTiles.add(jsonTiles);
        }

        root.put("tiles", jsonTiles);
        root.put("width", width);
        root.put("height", height);
        

        return root;
    }
}
