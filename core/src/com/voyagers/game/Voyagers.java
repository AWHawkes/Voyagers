package com.voyagers.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import jdk.internal.net.http.common.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Voyagers extends ApplicationAdapter {

	SpriteBatch batch;
	BitmapFont font;
	BitmapFont fontHighlighted;

	Texture img;
	Texture testImg;
	Texture lineImage;
	Texture vertexImage;
	Texture buildGuideImage;
	Texture townImage;
	Texture cityImage;
	Texture robberImage;

	Texture mainMenuButtonImage;
	Texture gameScreenButtonImage;
	Texture menuButtonImage;
	Texture settingsButtonImage;
	Texture quitButtonImage;
	Texture offlineGameButtonImage;
	Texture onlineGameButtonImage;
	Texture finishRound;
	Texture backgroundGrey;

	Texture mouseImage;

	Texture oreImage;
	Texture wheatImage;
	Texture brickImage;
	Texture woodImage;
	Texture sheepImage;
	Texture forestImage;

	Rectangle mouse;

	Rectangle gameScreenButton;
	Rectangle menuButton;
	Rectangle settingsButton;
	Rectangle quitButton;
	Rectangle backgroundGreyPanel;
	Rectangle finishRoundButton;

	Rectangle roadCreator;
	Rectangle cityCreator;
	Rectangle townCreator;


	//lobby stuff
	public int maxPlayers = 4;

	Rectangle addPlayerButton;
	Rectangle changeMapButton;


	// map generation stuff
	public int currentMap;
	public String[] mapFileNames = {"default", "Default Six Player", "Other"};
	boolean[][] mapMat;
	int[][] mapTrack;
	public int mapXCameraOffset = 0;
	public int mapYCameraOffset = 0;
	public float mapZoom = 1.0f;

	/*
	public int xOffset = 0;
	public int yOffset = 0;
	public float scaling */


	private OrthographicCamera camera;

	private Array<Player> players;
	public int playerNumber = 0;
	private Array<Hex> hexes;
	public int hexNumber = 1;
	private Array<Vertex> vertices;
	public int vertexNumber = 0;
	private Array<Line> lines;
	private int lineNumber = 0;
	private Array<Rectangle> menuGui;
	private Array<Integer> resourceNumbers;
	private Array<Pair> resourceTiles;


	Pair<String,Boolean> menuFlags;
	private float timeSinceLastFrame;
	private Array<Integer> turnOrder;
	private int turnNumber = 0;
	private int roundNumber = 0;
	private int hexWidth = 100;

	 private boolean mainMenu = true;
	 private boolean gameScreen = false;
	 private boolean settingsScreen = false;
	 private boolean lobbyScreen = false;

	private int screenFlag = 0;

	public String dragObject = "";

	public Vertex vDefault;

	public Player localPlayer;
	public Player cpuOne;
	public Player cpuTwo;

	//public Island island;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.BLACK);



		//island = new Island();
		//island.changeMapName("default");

		currentMap = 0;

		fontHighlighted = new BitmapFont();
		fontHighlighted.setColor(Color.BLUE);

		// textures
		img = new Texture("badlogic.jpg");
		testImg = new Texture("testHex.jpg");
		sheepImage = new Texture("testHex.jpg");
		forestImage = new Texture("testHex.jpg");
		lineImage = new Texture("line.png");
		vertexImage = new Texture("vertex.png");
		townImage = new Texture("town.png");
		cityImage = new Texture("city.png");

        gameScreenButtonImage = new Texture("gameButton.png");

        menuButtonImage = new Texture("menuButton.png");
        settingsButtonImage = new Texture("settings button.png");
        quitButtonImage = new Texture("quitButton.png");
        backgroundGrey = new Texture("backgroundGrey.png");
        finishRound = new Texture("end turn.png");

        mouseImage = new Texture("mouseCursorImage.png");


        // arrays
        hexes = new Array<Hex>();
        lines = new Array<Line>();
        vertices = new Array<Vertex>();
        players = new Array<Player>();

        resourceNumbers = new Array<Integer>();
        resourceTiles = new Array<Pair>();

		localPlayer = playerCreation(0,"local","Red");
		localPlayer.road = vertexImage;
		players.add(localPlayer);

		cpuOne = playerCreation(1,"CPU1","Blue");
		players.add(cpuOne);

		cpuTwo = playerCreation(2,"CPU2","White");
		players.add(cpuTwo);

		try {
			initialResourceNumberCreator();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		initialTileCreator();

		mapGenerator(0,500);


		createPlayerTown(localPlayer,vertices.get(5));
		createPlayerTown(localPlayer,vertices.get(9));
		createPlayerRoad(localPlayer,lines.get(9));
		claimVertexForPlayer(localPlayer,vertices.get(18));
		localPlayer.victoryPoints = calculatePlayerPoints(localPlayer);


		initialization();
		initialCoordinateAssigner();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0f, 0.72f, 1.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		if(gameScreen)
			drawGameScreen();

		if(lobbyScreen)
			drawLobbyScreen();

		if(mainMenu)
			drawMainMenu();

		if(settingsScreen) {
			drawSettingsScreen();
			interactionSettingsScreen();
		}

		batch.draw(mouseImage,mouse.x,mouse.y);

		batch.end();

		mouse.x = Gdx.input.getX()+1;
		mouse.y = -Gdx.input.getY()+ 479;


	}

	public void drawGameScreen(){
		drawMap(mapXCameraOffset,mapYCameraOffset,mapZoom);
		playerInfoGUI();
		drawMenuButton();
		drawBuilderGuide();
		if(players.get(turnNumber) == localPlayer)
			drawButton(finishRoundButton,"End Turn");
		else
			drawButton(finishRoundButton, players.get(turnNumber).name + "'s Turn");
		interactionGameScreen();
	}

	public void drawBuilderGuide(){
		batch.draw(lineImage,roadCreator.x,roadCreator.y,roadCreator.width,roadCreator.height);
		batch.draw(townImage,townCreator.x,townCreator.y,townCreator.width,townCreator.height);
		batch.draw(cityImage,cityCreator.x,cityCreator.y,cityCreator.width,cityCreator.height);
	}

	public void drawMap(int xOffset, int yOffset, float scaling){
		drawHexes(xOffset, yOffset, scaling);

		drawLines(xOffset, yOffset, scaling);

		drawVertices(xOffset, yOffset, scaling);
	}

	public void drawHexes(int xOffset, int yOffset, float scaling){
		float length = hexWidth * scaling;
		for (int i = 0; i < hexes.size; i++)
			batch.draw(hexes.get(i).image, hexes.get(i).x * scaling + xOffset, hexes.get(i).y * scaling + yOffset, length, length);
	}

	public void drawLines(int xOffset, int yOffset, float scaling){
		float width = 70 * scaling;
		float height = 6 * scaling;
		for (int i = 0; i < lines.size; i++)
			if(lines.get(i).contents != "")
				batch.draw(lines.get(i).player.road, lines.get(i).x * scaling + xOffset, lines.get(i).y * scaling + yOffset, width, height);
			else if(dragObject == "road" && !checkIfLineClaimed(lines.get(i)))
				batch.draw(vertexImage, lines.get(i).x * scaling + xOffset, lines.get(i).y * scaling + yOffset, width, height);
	}

	public void drawVertices(int xOffset, int yOffset, float scaling){
		int verticeWidth = 30;
		float length = verticeWidth * scaling;
		for (int i = 0; i < vertices.size; i++)
			if(vertices.get(i).contents != "")
				if(dragObject == "city" && vertices.get(i).player == players.get(turnNumber))
					batch.draw(vertexImage, vertices.get(i).x * scaling + xOffset, vertices.get(i).y * scaling + yOffset, length, length);
				else
					batch.draw(vertices.get(i).image, vertices.get(i).x * scaling + xOffset, vertices.get(i).y * scaling + yOffset, length, length);
			else if(dragObject == "town" && checkIfVertexCanHaveTown(vertices.get(i)))
				batch.draw(vertexImage, vertices.get(i).x * scaling + xOffset, vertices.get(i).y * scaling + yOffset, length, length);
	}

	// discontinued temporarily while I think on how to make this function not a fustercluck.  FFS it doesn't even make it easier to call than the batch.draw system does
	public void drawVertex(Texture texture,Vertex vertex,float scaling,int xOffset, int yOffset, int width, int height){
		batch.draw(texture, vertex.x * scaling + xOffset, vertex.y * scaling + yOffset, width, height);
	}

	public void drawLobbyScreen(){
		int startingHeight = 380;
		drawMenuButton();
		drawPlayButton();
		drawText(fontHighlighted,"Lobby",200,450);


		drawLobbyPlayers(startingHeight);
		drawLobbyMap(startingHeight);

		interactionLobbyScreen();
	}

	public void drawLobbyPlayers(int startingHeight){
		drawText(fontHighlighted, "Players " + players.size + "/" + maxPlayers,10,startingHeight);
		for(int i = 0; i < players.size; i++)
			drawPlayerInfo(i,startingHeight-40,30);
		drawButton(addPlayerButton,"+");
	}

	public void drawLobbyMap(int startingHeight){
		drawText(fontHighlighted, "Map: " + mapFileNames[currentMap], 400,startingHeight);
		drawButton(changeMapButton,"Change Map");

		int miniMapHeight = mapMat.length * 45;
		drawMap(300,startingHeight - miniMapHeight,0.5f);
	}

	public void drawMainMenu(){
	    if(!lobbyScreen && !gameScreen && !settingsScreen)
		    fontHighlighted.draw(batch,"Voyagers",200,400);

		drawPlayButton();
		drawButton(settingsButton,"Settings");
		drawButton(quitButton,"Quit");
		interactionMainMenu();
	}

	public void drawSettingsScreen(){
		drawMenuButton();
		font.draw(batch,"Settings:",200,300);
		font.draw(batch,"sound on/off", 200, 200);
	}

	public void drawMenuButton(){
		drawButton(menuButton,"Menu");
	}

	public void drawPlayButton(){ drawButton(gameScreenButton,"Play"); }

	public void drawButton(Rectangle detectionBox, String text){
		if(mouse.overlaps(detectionBox))
			drawText(fontHighlighted,text,detectionBox.x,detectionBox.y + detectionBox.height/2);
		else
			drawText(font,text,detectionBox.x,detectionBox.y + detectionBox.height/2);
	}

	public void drawText(BitmapFont fontType, String text, float xCoord, float yCoord){
		fontType.draw(batch,text,xCoord,yCoord);
	}

	public void interactionGameScreen(){
		int cameraMove = 5;

		if(clicksRectangle(menuButton))
			mainMenu = true;
		if(clicksRectangle(finishRoundButton))
			nextTurn();
		// builder guide stuff  // should I use a switch statement here?
		// //Definitely need to make it a separate function, maybe "builderGuideInteraction" followed by "xDragInteraction"?
		if(clicksRectangle(roadCreator) && dragObject == ""){
			centerRectangleToMouse(roadCreator);
			dragObject = "road";
		}
		if(dragObject == "road") {
			if (isLeftButtonPressed())
				centerRectangleToMouse(roadCreator);
			else {
				for(int p = 0; p < lines.size; p++){
					if(roadCreator.contains(lines.get(p).x * mapZoom + mapXCameraOffset,lines.get(p).y * mapZoom + mapYCameraOffset) && !checkIfLineClaimed(lines.get(p))){
						createPlayerRoad(players.get(turnNumber),lines.get(p));
					}
				}
				dragObject = "";
				roadCreator.x = 600;  // need way to store "default values" for rectangles
				roadCreator.y = 120;
			}
		}
		if(clicksRectangle(townCreator) && dragObject ==""){
			centerRectangleToMouse(townCreator);
			dragObject = "town";
		}
		if(dragObject == "town") {
			if (isLeftButtonPressed())
				centerRectangleToMouse(townCreator);
			else {
				for(int p = 0; p < vertices.size; p++){
					if(townCreator.contains(vertices.get(p).x * mapZoom + mapXCameraOffset,vertices.get(p).y * mapZoom + mapYCameraOffset) && checkIfVertexCanHaveTown(vertices.get(p))){
						createPlayerTown(players.get(turnNumber),vertices.get(p));
					}
				}
				dragObject = "";
				townCreator.x = 600;  // need way to store "default values" for rectangles
				townCreator.y = 70;
			}
		}
		if(clicksRectangle(cityCreator) && dragObject == ""){
			centerRectangleToMouse(cityCreator);
			dragObject = "city";
		}
		if(dragObject == "city") {
			if (isLeftButtonPressed())
				centerRectangleToMouse(cityCreator);
			else {
				for(int p = 0; p < vertices.size; p++){
					if(cityCreator.contains(vertices.get(p).x * mapZoom + mapXCameraOffset,vertices.get(p).y * mapZoom + mapYCameraOffset)){
						createPlayerCity(vertices.get(p));
					}
				}
				dragObject = "";
				cityCreator.x = 600;  // need way to store "default values" for rectangles
				cityCreator.y = 20;
			}
		}


		if(Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE)) {
			mapXCameraOffset = 0;
			mapYCameraOffset = 0;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))
			mapYCameraOffset += cameraMove;
		if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))
			mapYCameraOffset -= cameraMove;
		if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
			mapXCameraOffset += cameraMove;
		if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			mapXCameraOffset -= cameraMove;

	}

	public void nextTurn(){
		if(turnNumber != players.size - 1)
			turnNumber++;
		else {
			turnNumber = 0;
			roundNumber++;
		}
	}

	public boolean isLeftButtonPressed(){
		return Gdx.input.isButtonPressed(Input.Buttons.LEFT);
	}

	public void interactionLobbyScreen(){
		if(clicksRectangle(menuButton))
			mainMenu = true;
		if(clicksRectangle(gameScreenButton)) {
            gameScreen = true;
            lobbyScreen = false;
        }
		if(clicksRectangle(changeMapButton))
			lobbyChangeMap();
	}

	public void lobbyChangeMap(){
		if(currentMap == mapFileNames.length-1)
			currentMap = 0;
		else
			currentMap += 1;
	}

	public void interactionMainMenu(){
		if(clicksRectangle(gameScreenButton)){
			mainMenu = false;
			if(!gameScreen)
			    lobbyScreen = true;
		}

		if(clicksRectangle(quitButton)){
			if(gameScreen)
				gameScreen = false;
			else if(lobbyScreen)
				lobbyScreen = false;
			else
				quit();
		}

		if(clicksRectangle(settingsButton)){
			mainMenu = false;
			settingsScreen = true;
		}
	}

	public void interactionSettingsScreen(){
		if(clicksRectangle(menuButton)){
			mainMenu = true;
			settingsScreen = false;
		}
	}

	public boolean clicksRectangle(Rectangle rectangle){
		return mouse.overlaps(rectangle) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
	}

	public void centerRectangleToMouse(Rectangle rectangle){
		rectangle.x = mouse.x - rectangle.width/2;
		rectangle.y = mouse.y - rectangle.height/2;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		sheepImage.dispose();
		forestImage.dispose();
	}

	public void initialTileCreator(){
		Pair<String, Integer> tempPair = new Pair<>("fields", 4);
		resourceTiles.add(tempPair);
		tempPair = new Pair<>("forest", 4);
		resourceTiles.add(tempPair);
		tempPair = new Pair<>("pasture", 4);
		resourceTiles.add(tempPair);
		tempPair = new Pair<>("mountains", 3);
		resourceTiles.add(tempPair);
		tempPair = new Pair<>("hills", 3);
		resourceTiles.add(tempPair);
		tempPair = new Pair<>("desert", 1);
		resourceTiles.add(tempPair);
	}

	public void initialResourceNumberCreator() throws FileNotFoundException {
		String temp;
		Scanner reader = readFile("resourceNumbers.txt");
		while(reader.hasNextLine()){
			temp = reader.nextLine();
			temp = temp.substring(temp.length() - 1);
			resourceNumbers.add(Integer.parseInt(temp));
		}
	}

	public Scanner readFile(String fileName) throws FileNotFoundException {

		File file = new File("resourceNumbers.txt");
		return new Scanner(file);

	}

	public void initialization(){
		gameScreenButton = new Rectangle();
		settingsButton = new Rectangle();
		quitButton = new Rectangle();
		menuButton = new Rectangle();
		backgroundGreyPanel = new Rectangle();

		mouse = new Rectangle();

		addPlayerButton = new Rectangle();
		changeMapButton = new Rectangle();

		finishRoundButton = new Rectangle();

		roadCreator = new Rectangle();
		cityCreator = new Rectangle();
		townCreator = new Rectangle();

	}

	public void initialCoordinateAssigner(){
		coordinateAssignerRectangle(200,300,150,50,gameScreenButton);
		coordinateAssignerRectangle(200,220,150,50,settingsButton);
		coordinateAssignerRectangle(200,150,115,50,quitButton);
		coordinateAssignerRectangle(0,430,100,30,menuButton);
		coordinateAssignerRectangle(0,0,100,500,backgroundGreyPanel);

		coordinateAssignerRectangle(0,0,2,2,mouse);

		coordinateAssignerRectangle(92,362,34,35,addPlayerButton);
		coordinateAssignerRectangle(396,340,80,35,changeMapButton);

		coordinateAssignerRectangle(0,0,90,30,finishRoundButton);

		coordinateAssignerRectangle(600,120,30,30,roadCreator);
		coordinateAssignerRectangle(600,70,30,30,townCreator);
		coordinateAssignerRectangle(600,20,30,30,cityCreator);

	}

	public void coordinateAssignerRectangle(int x, int y, int width, int height, Rectangle rectangle){
		rectangle.x = x;
		rectangle.y = y;
		rectangle.width = width;
		rectangle.height = height;
	}



	public Player playerCreation(int playerNumber, String playerName, String playerColor){
		Player player = new Player();
		player.create(playerNumber,playerColor,playerName);
		return player;
	}

	public void playerInfoGUI(){
		int spacing = 50;
		int startingHeight = 350;

		batch.draw(backgroundGrey,backgroundGreyPanel.x,backgroundGreyPanel.y,backgroundGreyPanel.width,backgroundGreyPanel.height);
		drawRoundInfo(startingHeight);

		for(int i = 0; i < players.size; i++)
			drawPlayerInfo(i,startingHeight-spacing,spacing);
	}

	public void drawRoundInfo(int startingHeight){
		font.draw(batch, players.get(turnNumber).name + "'s turn",0,startingHeight);
		font.draw(batch, "Round: " + roundNumber,0,startingHeight-20);
	}

	public void drawPlayerInfo(int playerNumber, int startingHeight, int spacing){
		String playerName = players.get(playerNumber).name + " (" + players.get(playerNumber).color + ")";
		// So I don't think we should perhaps give an easy way to see points
		String playerPoints = "Points: " + players.get(playerNumber).victoryPoints;
		int yHeight = startingHeight - (playerNumber * spacing);

		font.draw(batch,playerName,10,yHeight);
		font.draw(batch,playerPoints,0,yHeight - 20);
	}



	public void mapGenerator(int x, int y){

		loadMap(mapFileNames[currentMap]);


		for(int height = 0; height < mapMat.length; height++){

			for(int width = 0; width < mapMat[0].length; width++){

				if(mapMat[height][width]){
					Hex hex3 = makeHex(x,y);

					if(height % 2 == 0)
						hex3.x = x + (width * hexWidth);
					else
						hex3.x = x + (width * hexWidth) - (hexWidth/2);

					hex3.y = y - (height * hexWidth);
					hexes.add(hex3);



					Line lineRight = makeLine(hex3.x + hexWidth * 5/8, hex3.y + hexWidth/2 - 5,1);
					lines.add(lineRight);
					hex3.lines[1] = lineRight;
					lineRight.hexes[0] = hex3;

					Line lineBotRight = makeLine(hex3.x + hexWidth * 3/8, hex3.y + 5,2);
					lines.add(lineBotRight);
					hex3.lines[2] = lineBotRight;
					lineBotRight.hexes[0] = hex3;

					Line lineBotLeft = makeLine(hex3.x - hexWidth /8, hex3.y + 10,3);
					lines.add(lineBotLeft);
					hex3.lines[3] = lineBotLeft;
					lineBotLeft.hexes[0] = hex3;


					// filling up hex3's vertice array with default vertex
					for(int vertexNum = 0; vertexNum < 6; vertexNum++){
						hex3.vertices[vertexNum] = vDefault;
					}

					Vertex vertBottom = makeVertex(hex3.x + hexWidth/2, hex3.y - 1);
					vertices.add(vertBottom);
					hex3.vertices[3] = vertBottom;


					vertBottom.lines[5] = hex3.lines[3];
					hex3.lines[3].vertices[1] = vertBottom;
					vertBottom.lines[1] = hex3.lines[2];
					hex3.lines[2].vertices[0] = vertBottom;


					Vertex vertBotRight = makeVertex(hex3.x + hexWidth - 1, hex3.y + hexWidth/5);
					vertices.add(vertBotRight);
					hex3.vertices[2] = vertBotRight;

					vertBotRight.lines[4] = hex3.lines[2];
					hex3.lines[2].vertices[1] = vertBotRight;
					vertBotRight.lines[0] = hex3.lines[1];
					hex3.lines[1].vertices[1] = vertBotRight;


					//checking LEFT line:
					if(width == 0){
						// make left line
						Line line = makeLine(hex3.x - hexWidth * 5/8, hex3.y + hexWidth/2,1);
						lines.add(line);
						hex3.lines[4] = line;
						line.hexes[1] = hex3;

					} else {
						if(mapTrack[height][width - 1] != 0) {
							// find line and attach
							Line lineTemp = hexes.get(mapTrack[height][width - 1]).lines[1];
							hex3.lines[4] = lineTemp;
							lineTemp.hexes[1] = hex3;

						} else {
							// make left line
							Line line = makeLine(hex3.x - hexWidth * 5/8, hex3.y + hexWidth/2,1);
							lines.add(line);
							hex3.lines[4] = line;
							line.hexes[1] = hex3;
						}
					}

					// checking TOP-LEFT lines
					if(width == 0 && height % 2 != 0){
							// if this is triggered, it is an odd row at the far left end of the map
							// make top-right line
							Line line = makeLine(hex3.x - hexWidth * 2/8, hex3.y + hexWidth * 6/8,2);
							lines.add(line);
							hex3.lines[5] = line;
							line.hexes[1] = hex3;

					} else if(height == 0){
						// make top-left line
						Line line = makeLine(hex3.x - hexWidth * 3/8, hex3.y + hexWidth * 6/8,2);
						lines.add(line);
						hex3.lines[5] = line;
						line.hexes[1] = hex3;

					} else {
						// this is for even rows
						if(mapTrack[height - 1][width] != 0 && height % 2 == 0) {
							// finds bottom-left line of the hex to the top-right, and attaches
							Line lineTemp = hexes.get(mapTrack[height - 1][width]).lines[2];
							hex3.lines[5] = lineTemp;
							lineTemp.hexes[1] = hex3;

							// this is for odd rows
						} else if(mapTrack[height - 1][width - 1] != 0 && height % 2 != 0) {
							// finds bottom-right line of the hex to the top-right, and attaches
							Line lineTemp = hexes.get(mapTrack[height - 1][width - 1]).lines[2];
							hex3.lines[5] = lineTemp;
							lineTemp.hexes[1] = hex3;

						} else {
							// make top-right line
							Line line = makeLine(hex3.x - hexWidth * 2/8, hex3.y + hexWidth * 6/8,2);
							lines.add(line);
							hex3.lines[5] = line;
							line.hexes[1] = hex3;
						}
					}


					// checking top-right
					if(width == 6 && height % 2 == 0){
							// make top-right line
							Line line = makeLine(hex3.x + hexWidth * 2/8, hex3.y + hexWidth * 6/8 - 15,3);
							lines.add(line);
							hex3.lines[0] = line;
							line.hexes[1] = hex3;


					} else if(height == 0){
						// make top-right line
						Line line = makeLine(hex3.x + hexWidth * 3/8, hex3.y + hexWidth * 6/8 - 15,3);
						lines.add(line);
						hex3.lines[0] = line;
						line.hexes[1] = hex3;

					} else {
						// this is for even rows
						if(mapTrack[height - 1][width + 1] != 0 && height % 2 == 0) {
							// finds bottom-left line of the hex to the top-right, and attaches
							Line lineTemp = hexes.get(mapTrack[height - 1][width + 1]).lines[3];
							hex3.lines[0] = lineTemp;
							lineTemp.hexes[1] = hex3;

							// this is for odd rows
						} else if(mapTrack[height - 1][width] != 0 && height % 2 != 0) {
							// finds bottom-right line of the hex to the top-right, and attaches
							Line lineTemp = hexes.get(mapTrack[height - 1][width]).lines[3];
							hex3.lines[0] = lineTemp;
							lineTemp.hexes[1] = hex3;

						} else {
							// make top-right line
							Line line = makeLine(hex3.x + hexWidth * 2/8, hex3.y + hexWidth * 6/8 - 15,3);
							lines.add(line);
							hex3.lines[0] = line;
							line.hexes[1] = hex3;
						}
					}


					// Vertex #0
					if(hex3.vertices[0] == vDefault){ // top left vertex
						//check line #5 of hex3 to see if it has a vertex at 1
						if(hex3.lines[5].vertices[1] != vDefault){
							// this means that we have an existing vertex we can add to the hex
							hex3.vertices[0] = hex3.lines[5].vertices[1];

							// we now add the vertex to line 0, and vice versa
							if(hex3.lines[0].vertices[0] != hex3.vertices[0]){
								hex3.lines[0].vertices[0] = hex3.vertices[0]; // line 0
								hex3.vertices[0].lines[2] = hex3.lines[0];
							}


						} else if(hex3.lines[0].vertices[0] != vDefault) {
							// this means that we have an existing vertex we can add to the hex
							hex3.vertices[0] = hex3.lines[0].vertices[0];

							// we now add the vertex to line 5, and vice versa
							if(hex3.lines[5].vertices[1] != hex3.vertices[0]){
								hex3.lines[5].vertices[1] = hex3.vertices[0]; // line 5
								hex3.vertices[0].lines[4] = hex3.lines[5];
							}

						}else { // if no vertex exists at all that could be put here
							Vertex vertTop = makeVertex(hex3.x + hexWidth/2 -5, hex3.y + hexWidth - 5);
							vertices.add(vertTop);

							hex3.vertices[0] = vertTop;

							hex3.lines[5].vertices[1] = vertTop; // line 5
							vertTop.lines[4] = hex3.lines[5];
							hex3.lines[0].vertices[0] = vertTop; // line 0
							vertTop.lines[2] = hex3.lines[0];

						}
					}
					// Vertex #1
					if(hex3.vertices[1] == vDefault){ // top left vertex

						if(hex3.lines[0].vertices[1] != vDefault){

							hex3.vertices[1] = hex3.lines[0].vertices[1];

							if(hex3.lines[1].vertices[0] != hex3.vertices[1]){
								hex3.lines[1].vertices[0] = hex3.vertices[1]; // line 1
								hex3.vertices[1].lines[3] = hex3.lines[1];
							}


						} else if(hex3.lines[1].vertices[0] != vDefault) { // checking line 1, vertex 0

							hex3.vertices[1] = hex3.lines[1].vertices[0];

							// we now add the vertex to line 0, and vice versa
							if(hex3.lines[0].vertices[1] != hex3.vertices[1]){
								hex3.lines[0].vertices[1] = hex3.vertices[1]; // line 0
								hex3.vertices[1].lines[5] = hex3.lines[0];
							}

						}else {
							Vertex vertTopRight = makeVertex(hex3.x + hexWidth * 4/5, hex3.y + hexWidth * 4/5 - 5);
							vertices.add(vertTopRight);
							// adding to hex
							hex3.vertices[1] = vertTopRight;

							hex3.lines[0].vertices[1] = vertTopRight; // line 0
							vertTopRight.lines[5] = hex3.lines[0];
							hex3.lines[1].vertices[0] = vertTopRight; // line 1
							vertTopRight.lines[3] = hex3.lines[1];

						}
					}
					// Vertex #4
					if(hex3.vertices[4] == vDefault){ // Bottom left Vertex
						//check line #3 of hex3 to see if it has a vertex at 0
						if(hex3.lines[3].vertices[0] != vDefault){
							// this means that we have an existing vertex we can add to the hex
							hex3.vertices[4] = hex3.lines[3].vertices[0];

							// we now add the vertex to line 4, and vice versa
							if(hex3.lines[4].vertices[1] != hex3.vertices[4]){
								hex3.lines[4].vertices[1] = hex3.vertices[4]; // line 4
								hex3.vertices[4].lines[0] = hex3.lines[4];
							}


						} else if(hex3.lines[4].vertices[1] != vDefault) { // checking line 4, vertex 1
							// this means that we have an existing vertex we can add to the hex
							hex3.vertices[4] = hex3.lines[4].vertices[1];

							// we now add the vertex to line 3, and vice versa
							if(hex3.lines[3].vertices[0] != hex3.vertices[4]){
								hex3.lines[3].vertices[0] = hex3.vertices[4]; // line 3
								hex3.vertices[4].lines[0] = hex3.lines[3];
							}

						}else { // if no vertex exists at all that could be put here
							Vertex vertBotLeft = makeVertex(hex3.x + hexWidth/5, hex3.y + hexWidth/5 - 5);
							vertices.add(vertBotLeft);
							// adding to hex
							hex3.vertices[4] = vertBotLeft;

							hex3.lines[3].vertices[0] = vertBotLeft; // line 3
							vertBotLeft.lines[2] = hex3.lines[3];
							hex3.lines[4].vertices[1] = vertBotLeft; // line 4
							vertBotLeft.lines[0] = hex3.lines[4];

						}
					}
					// Vertex #5
					if(hex3.vertices[5] == vDefault){ // top left vertex

						if(hex3.lines[5].vertices[0] != vDefault){

							hex3.vertices[5] = hex3.lines[5].vertices[0];

							if(hex3.lines[4].vertices[0] != hex3.vertices[5]){
								hex3.lines[4].vertices[0] = hex3.vertices[5];
								hex3.vertices[5].lines[3] = hex3.lines[4];
							}


						} else if(hex3.lines[4].vertices[0] != vDefault) {

							hex3.vertices[5] = hex3.lines[4].vertices[0];

							if(hex3.lines[5].vertices[0] != hex3.vertices[5]){
								hex3.lines[5].vertices[0] = hex3.vertices[5];
								hex3.vertices[5].lines[1] = hex3.lines[4];
							}


						}else {
							Vertex vertTopLeft = makeVertex(hex3.x + hexWidth/5, hex3.y + hexWidth * 4/5 - 5);
							vertices.add(vertTopLeft);

							hex3.vertices[3] = vertTopLeft;

							hex3.lines[5].vertices[0] = vertTopLeft;
							vertTopLeft.lines[1] = hex3.lines[5];
							hex3.lines[4].vertices[0] = vertTopLeft;
							vertTopLeft.lines[1] = hex3.lines[5];

						}
					}

					mapTrack[height][width] = hex3.identifier;

				}
			}
		}

	}



	public boolean checkIfVertexClaimed(Vertex vertex){
		if(vertex.player == null)
			return false;
		else
			return true;
	}

	public boolean checkIfLineClaimed(Line line){
		if(line.player == null)
			return false;
		else
			return true;
	}

	public Vertex getOtherVertexOnLine(Vertex vertex, Line line){
		if(line.vertices[0] == vertex)
			return line.vertices[0];
		else
			return line.vertices[1];
	}

	public boolean checkIfVertexCanHaveTown(Vertex vertex){
		if(checkIfVertexClaimed(vertex)){
			return false;
		} else {
			for(int i = 0; i < 6; i++){
				if(vertex.lines[i] == null){
				}else if(checkIfVertexClaimed(getOtherVertexOnLine(vertex,vertex.lines[i]))){
					return false;
				}
			}
		}
		return true;
	}


	public Hex makeHex(int x, int y){
		Hex newHex = new Hex();
		newHex.x = x;
		newHex.y = y;
		newHex.image = testImg;

        newHex.identifier = hexNumber;
        hexNumber++;

		return newHex;
	}

	public Player makePlayer(){
		Player newPlayer = new Player();

		return newPlayer;
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



	public void createPlayerRoad(Player player, Line line){
		putRoadOnLine(line);
		claimLineForPlayer(player,line);
	}

	public void createPlayerTown(Player player, Vertex vertex){
		putTownOnVertex(vertex);
		claimVertexForPlayer(player, vertex);
		player.victoryPoints += 1;
	}

	public void createPlayerCity(Vertex vertex){
		putCityOnVertex(vertex);
		vertex.player.victoryPoints += 1;
	}

	public void putRoadOnLine(Line line){
		line.contents = "road";
		line.image = lineImage;
	}

	public void putTownOnVertex(Vertex vertex){
		vertex.contents = "town";
		vertex.image = townImage;
	}

	public void putCityOnVertex(Vertex vertex){
		vertex.contents = "city";
		vertex.image = cityImage;
	}
	public void claimLineForPlayer(Player player, Line line){
		player.playerOwnedLines.add(line);
		line.player = player;
	}

	public void claimVertexForPlayer(Player player, Vertex vertex){
		player.playerOwnedVertices.add(vertex);
		vertex.player = player;
	}

	public int calculatePlayerPoints(Player player){
		int playerPoints = 0;

		for(int i = 0; i < player.playerOwnedVertices.size(); i++){
			switch(player.playerOwnedVertices.get(i).contents) {
				case "town" :
					playerPoints += 1;
					break;
				case "city" :
					playerPoints += 2;
					break;
			}
		}
		return playerPoints;
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

	public void quit(){
		System.exit(1);
	}

}
