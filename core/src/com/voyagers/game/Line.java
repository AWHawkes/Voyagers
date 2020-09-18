package com.voyagers.game;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Array.get;
import static java.lang.reflect.Array.set;

public class Line {
    public int identifier;
    public int x;
    public int y;
    public String contents = "";
    public Texture image;
    public Player player;
    public int orientation = 0;

    /* line orientation:
    #:    means:
    0       horizontal line
    1       vertical line
    2       slant left
    3       slant right

     */

    // contains data on what lines, vertices, and hexes are connected.
    // Also contains data on how they are oriented relatively
   // List lines = new ArrayList();
    //List lines = new ArrayList<Line>();
   // List vertices = new ArrayList<Vertex>();
   // List hexes = new ArrayList<Hex>();

    // position 0 is on the left/top, position 1 is on the right/bottom
    Vertex vertices[] = new Vertex[2];
    // position 0, means that the hex is on top/left, position 1 means on bottom/right
    Hex hexes[] = new Hex[2];


    public void create() {
        // loop that adds the empty elements to the array lists
        // actually will have to do this outside of this class
        /*
        for (int i = 1; i <= 2; i++) {
            //lines.add(i,"");
            vertices.add(i,"");
            hexes.add(i,"");
        }*/
    }
/*
    public void setLine(Line line, int position){
        lines.set(position, line);
    }*/

    public void setVertex(Vertex vertex, int position){
        set(vertices,position, vertex);

    }

    public void setHex(Hex hex, int position){
        set(hexes,position, hex);
    }

    /*  we don't need this anymore since we switched to using arrays
    // these return the identifier for what line/vertex/hex is attached to what position

    public Object getVertex(int position){
        Object temp = get(vertices,position);
        return temp;
    }

    public Object getHex(int position){
        Object temp = get(hexes,position);
        return temp;
    } */
}
