package com.voyagers.game;

import java.util.ArrayList;
import java.util.List;

public class Line {
    public int identifier;
    public int x;
    public int y;
    public String contents = "";
    public int orientation;

    // contains data on what lines, vertices, and hexes are connected.
    // Also contains data on how they are oriented relatively
   // List lines = new ArrayList();
    //List lines = new ArrayList<Line>();
    List vertices = new ArrayList<Vertex>();
    List hexes = new ArrayList<Hex>();


    public void create() {
        // loop that adds the empty elements to the array lists
        for (int i = 1; i <= 2; i++) {
            //lines.add(i,"");
            vertices.add(i,"");
            hexes.add(i,"");
        }
    }
/*
    public void setLine(Line line, int position){
        lines.set(position, line);
    }*/

    public void setVertex(Vertex vertex, int position){
        vertices.set(position, vertex);
    }

    public void setHex(Hex hex, int position){
        hexes.set(position, hex);
    }

    // these return the identifier for what line/vertex/hex is attached to what position

    public Object getVertex(int position){
        Object temp = vertices.get(position);
        return temp;
    }

    public Object getHex(int position){
        Object temp = hexes.get(position);
        return temp;
    }
}
