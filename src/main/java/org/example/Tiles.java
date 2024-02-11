package org.example;

public class Tiles{
    private static final Tiles[] tileTypes = {

    };

    private boolean top = false;
    private boolean bottom = false;
    private boolean right = false;
    private boolean left = false;

    private Tiles(){

    }
    private Tiles(boolean t, boolean b, boolean r, boolean l){
        top = t;
        bottom = b;
        right = r;
        left = l;
    }

    public static boolean movable(int x, int y, int nX, int nY, int cur, int o, World world){
        int wX = world.toWorldCord(x);
        int wY = world.toWorldCord(y);
        int tX = world.toTileCord(x);
        int tY = world.toTileCord(y);
        int nWX = world.toWorldCord(nX);
        int nWY = world.toWorldCord(nY);
        int nTX = world.toTileCord(nX);
        int nTY = world.toTileCord(nY);
        int xD = calcDir(wX, tX, nWX, nTX);
        int yD = calcDir(wY, tY, nWY, nTY);
        /* Direction
         * 0 - up
         * 1 - down
         * 2 - left
         * 3 - right
         * 4 - up left
         * 5 - up right
         * 6 - down left
         * 7 - down right
        */
        int d = -1;
        if (xD<0 && yD<0) d = 4;
        else if (xD>0 && yD<0) d = 5;
        else if (xD<0 && yD>0) d = 6;
        else if (xD>0 && yD>0) d = 7;
        else if (yD<0) d = 0;
        else if (yD>0) d = 1;
        else if (xD<0) d = 2;
        else if (xD>0) d = 3;
        switch (d) {
            case 0:
                return tileTypes[o].getUp();
            case 1:
                return tileTypes[o].getDown();
            case 2:
                return tileTypes[o].getLeft();
            case 3:
                return tileTypes[o].getRight();
            case 4:
                //TODO implment
                return false;
            case 5:
                //TODO implment
                return false;
            case 6:
                //TODO implment
                return false;
            case 7:
                //TODO implment
                return false;
        }
        return false;
    }

    public static int calcDir(int w, int t, int nW, int nT){
        if (w>nW) return -1;
        if (nW>w) return 1;
        if (t>nT) return -1;
        if (nT>t) return 1;
        return 0;
    }

    public boolean getUp(){
        return top;
    }

    public boolean getDown(){
        return bottom;
    }

    public boolean getLeft(){
        return left;
    }

    public boolean getRight(){
        return right;
    }
}