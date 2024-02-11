package org.example;

public class WorldTile {
    private final WorldTile BLANK_TILE = new WorldTile();
    private Tile[][] tiles;
    public WorldTile getBlankTile(){
        return BLANK_TILE;
    }
}
