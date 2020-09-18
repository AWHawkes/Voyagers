package com.voyagers.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class Island {

    Texture testImg;
    Texture lineImage;
    Texture vertexImage;

    public Vertex vDefault;

    public String mapName;
    public Array<Hex> hexes;
    public int hexNumber = 1;
    public Array<Vertex> vertices;
    public int vertexNumber = 0;
    private Array<Line> lines;
    private int lineNumber = 0;
    private int hexWidth = 100;


    boolean[][] mapMat;
    int[][] mapTrack;

    public void create(){
        hexes = new Array<Hex>();
        lines = new Array<Line>();
        vertices = new Array<Vertex>();

        testImg = new Texture("testHex.jpg");
    }

    public void changeMapName(String newMapName){
        mapName = newMapName;
    }



    public Hex makeHex(int x, int y, int mapHeight, int mapWidth){
        Hex newHex = new Hex();
        newHex.x = x;
        newHex.y = y;
        newHex.mapHeight = mapHeight;
        newHex.mapWidth = mapWidth;
        newHex.image = testImg;

        newHex.identifier = hexNumber;
        hexNumber++;

        return newHex;
    }

    public Line makeLine(int x, int y, int orientation){
        Line newLine = new Line();
        newLine.x = x;
        newLine.y = y;
        newLine.image = lineImage;
        newLine.orientation = orientation;

        newLine.identifier = lineNumber;
        lineNumber++;

        // filling up the vertex array
        newLine.vertices[0] = vDefault;
        newLine.vertices[1] = vDefault;

        return newLine;
    }

    public Vertex makeVertex(int x, int y){
        Vertex newVertex = new Vertex();
        newVertex.x = x;
        newVertex.y = y;
        newVertex.image = vertexImage;

        newVertex.identifier = vertexNumber;
        vertexNumber++;

        return newVertex;
    }

    public void generateMap(int x, int y){

        loadMap("default");

        for(int height = 0; height < mapMat.length; height++){

            for(int width = 0; width < mapMat[0].length; width++){
                Hex hex = makeHex(x,y,height,width);
                positionHexAccordingToMap(hex);
                if(!mapMat[height][width])
                    hex.type = "water";

                hexes.add(hex);

            }
        }

    }

    public void assignLinesToHex(Hex hex){
        // first check to see if hex is in top row, or leftmost column, so we can just automatically make lines for them

        //
    }

    public void positionHexAccordingToMap(Hex hex){
        if(hex.mapHeight % 2 == 0)
            hex.x = hex.x + (hex.mapWidth * hexWidth);
        else
            hex.x = hex.x + (hex.mapWidth * hexWidth) - (hexWidth/2);

        hex.y = hex.y - (hex.mapHeight * hexWidth);
    }

    public void loadMap(String mapName){
        if(mapName == "default"){
            // where tiles should be
            mapMat = new boolean[][]{
                    {false,false,false,false,false,false,false},
                    {false,false,true ,true ,true ,false,false},
                    {false,true ,true ,true,true ,false,false},
                    {false,true ,true ,true ,true ,true ,false},
                    {false,true ,true ,true ,true ,false,false},
                    {false,false,true ,true ,true ,false,false},
                    {false,false,false,false,false,false,false}
            };

            mapTrack = new int[][]{
                    {0,0,0,0,0,0,0,},
                    {0,0,0,0,0,0,0,},
                    {0,0,0,0,0,0,0,},
                    {0,0,0,0,0,0,0,},
                    {0,0,0,0,0,0,0,},
                    {0,0,0,0,0,0,0,},
                    {0,0,0,0,0,0,0,}
            };
        } else if(mapName == "Default Six Player"){
            mapMat = new boolean[][]{
                    {false,false,false,false,false,false,false,false},
                    {false,false,true ,true ,true ,false,false,false},
                    {false,true ,true ,true,true ,false,false,false},
                    {false,true ,true ,true ,true ,true ,false,false},
                    {false,true ,true ,true ,true ,true ,true ,false},
                    {false,true ,true ,true ,true ,true ,false,false},
                    {false,true ,true ,true ,true ,false,false,false},
                    {false,false,true ,true ,true ,false,false,false},
                    {false,false,false,false,false,false,false,false}
            };

            mapTrack = new int[][]{
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0}
            };

        } else {
            mapMat = new boolean[][]{
                    {false,false,true,true,true,false,false}
            };
            mapTrack = new int[][]{
                    {0,0,0,0,0,0,0,}
            };
        }

    }

}
