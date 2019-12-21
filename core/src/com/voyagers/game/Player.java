package com.voyagers.game;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.*;



public class Player {
    public int number;
    public String color;
    public String name;
    public int turnNumber;
    public int victoryPoints;
    // array lists of resources, buildings, cards, etc
    List roads = new ArrayList();
    List settlements = new ArrayList();
    List Cities = new ArrayList();
    List resources = new ArrayList();
    List developmentCards = new ArrayList();

    // player info:
    Rectangle pointer;


    public void create() {
        pointer = new Rectangle();
        pointer.width = 1;
        pointer.height = 1;
        pointer.x = 0;
        pointer.y = 0;
    }

    public void setPointer(int x, int y){
        pointer.x = x;
        pointer.y = y;
    }

    public Rectangle getPointer(){
        return pointer;
    }



}
