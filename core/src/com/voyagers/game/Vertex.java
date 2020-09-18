package com.voyagers.game;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Array.get;
import static java.lang.reflect.Array.set;

public class Vertex {
    public int identifier;
    public int x;
    public int y;
    public String contents = "";
    public Texture image;
    public Player player;

    // contains data on what lines, vertices, and hexes are connected.
    // Also contains data on how they are oriented relatively
    //List lines = new ArrayList<Line>();
    //List vertices = new ArrayList<Vertex>();
    //List hexes = new ArrayList<Hex>();

    /* positions for lines:
    top: 0
    top-right: 1
    bottom-right: 2
    bottom: 3
    bottom-left: 4
    top-left: 5
            */
    Line lines[] = new Line[6];  // maybe I want 3? idk
    //public Hex hex;

    public void create() {
        // loop that adds the empty elements to the array lists
        // actually will have to do this outside of this class
        /*
        for (int i = 1; i <= 6; i++) {
            lines.add(i,"");
            //vertices.add(i,"");
            //hexes.add(i,"");
        }*/
    }

    public void setLine(Line line, int position){
        set(lines,position, line);
    }
    /*
    public void setVertex(Vertex vertex, int position){
        vertices.set(position, vertex);
    }

    public void setHex(Hex hex, int position){
        hexes.set(position, hex);
    }
    */

    /*  we don't need this anymore since we switched to using arrays
    // these return the identifier for what line/vertex/hex is attached to what position
    public Object getLine(int position){
        Object temp = get(lines,position);
        return temp;
    }
*/

}
