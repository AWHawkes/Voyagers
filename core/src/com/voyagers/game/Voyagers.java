package com.voyagers.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Voyagers extends ApplicationAdapter {
	//batch
	SpriteBatch batch;
	//textures
	Texture img;  // default texture
	Texture testImg;
	Texture forestImage;
	Texture sheepImage;
	// sounds
	/***** shapes and stuff ******/

	//gui?
	Rectangle buildGuide;
		// buncha stuff, follow board

	//camera
	private OrthographicCamera camera;
	// arrays
	private Array<Player> players;
	private Array<Hex> hexes;
	private Array<Vertex> vertices;
	private Array<Line> lines;
	// trackers
	private float timeSinceLastFrame;
	private Array<String> turnOrder;
	private int turnPhase;
	// flags
	private int screenFlag = 0;

	//map components
	  //for loop that makes a bunch of hexes, but how should we know how many lines or vertexes we will have ahead of time?
	    //if we could make these components in a separate function, then pass them up to this base area here, that'd be cool
		  //WAIT, since we have a bunch of arrays of the different components, we can just fucking have the method put shit in there
			// I'm keeping this here so that I don't have to think through  this again tommorrow.
				// tommorrow update:  I'm glad I didn't have to think through this again
					// next day update: also apparently everything is a pointer
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		// textures
		img = new Texture("badlogic.jpg");
		testImg = new Texture("testHex.jpg");
		sheepImage = new Texture("testHex.jpg");
		forestImage = new Texture("testHex.jpg");
	//	forestImage = new Texture("Forest-5-9-roll.svg");

		//shapes

		// arrays
		hexes = new Array<Hex>();
		lines = new Array<Line>();
		vertices = new Array<Vertex>();




		// map generator
		mapGenerator(300,100, 0);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0.9f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// for loop that draws all the hexes, uses the String image to get name of image file
			// also the for loop draws the lines
			// also draws the vertexes, but ONLY if they have something in their contents

		batch.begin();
		batch.draw(img, 0, 0);
		batch.draw(sheepImage,hexes.get(0).x,50);
		//batch.draw(forestImage,150,50);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		sheepImage.dispose();
		forestImage.dispose();
	}

	public void mapGenerator(int x, int y, int hexNumber){
		// checks to see if there is already a hex with the submitted hexnumber
		if(hexes.size <= hexNumber){  // wanna change to check using index later, but weird errors
			// make the hex
			Hex hex = makeHex(x,y);
			hex.identifier = hexNumber;
			hexes.add(hex);
			//hex.x = x;
			//hex.y = y;
		}

		// manual line test


		// for loop that creates and connects lines to hex
		// also attaches the lines to the hex
		// make sure to compare orientation to make sure line orientations are correct
		// add new lines to arraylist
		for(int i = 0; i < 6; i++){

		}

		// have some method of figuring out where the hex should be put
			// maybe attach the hex to the lowest number line that isn't completely full of hexes
			// if the hex is number one, skip attaching, maybe have it do a streamlined version of below


		//does a for loop that makes and adds Vertexes to all empty corners of the hex, attaches to lines
		for(int i = 0; i < 6; i++){
			// the for loop first figures out if there is a pre-existing vertex that should be used
			//if(hex)
			// no fucking clue how to do this one
			// adds new vertex to arraylist
		}




		// for loop that checks the attached lines for attached hexes
			// neighboring hexes are recorded

		// add hex to arraylist
		int size = hexes.size;


		// call the mapGenerator function again.
	}

	public Hex makeHex(int x, int y){
		Hex newHex = new Hex();
		newHex.x = x;
		newHex.y = y;
		newHex.image = "testImg";

		return newHex;
	}

	public Line makeLine(int x, int y){
		Line newLine = new Line();
		newLine.x = x;
		newLine.y = y;
		newLine.image = "testImg";

		return newLine;
	}

	public Vertex makeVertex(int x, int y){
		Vertex newVertex = new Vertex();
		newVertex.x = x;
		newVertex.y = y;
		//newHex.image = "testImg";

		return newVertex;
	}

	/*
	public Hex makeHex(){

	}*/
}
