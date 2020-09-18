package com.voyagers.game;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.*;



public class Player {
    public int number;
    public String color;
    public String name;
    public int victoryPoints;
    public Texture road = new Texture("line.png");
    // array lists of resources, buildings, cards, etc
    ArrayList<Line> playerOwnedLines = new ArrayList<Line>();
    //ArrayList<Vertex> settlements = new ArrayList<Vertex>();
    ArrayList<Vertex> playerOwnedVertices = new ArrayList<Vertex>();
    ArrayList resources = new ArrayList();
    ArrayList developmentCards = new ArrayList();





    Rectangle playerMouse;


    public void create(int playerNumber, String playerColor, String playerName) {
        playerMouse = new Rectangle();
        playerMouse.width = 1;
        playerMouse.height = 1;
        playerMouse.x = 0;
        playerMouse.y = 0;

        number = playerNumber;
        color = playerColor;
        name = playerName;
        victoryPoints = 0;
    }

    public void setMouse(int x, int y){
        playerMouse.x = x;
        playerMouse.y = y;
    }

    public Rectangle getMouse(){
        return playerMouse;
    }


}
