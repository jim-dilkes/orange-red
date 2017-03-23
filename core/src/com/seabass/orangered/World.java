package com.seabass.orangered;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class World {

	OrangeRed game;

	// WORLD CONSTANTS
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;

	// GAME CONSTANTS
	public static final int ZERO_POINT = 7 * Gdx.graphics.getHeight() / 24;
	public static final float TILE_TIME_INTERVAL = 0.35f;
	public static final int WHITE = 0;
	public static final int RED = 1;
	public static final int ORANGE = 2;

	public static final int START_SPREAD_RANGE = 40;

	// VARIABLES for pattern scheduler
	private int currentChar = 0;
	private boolean patternSelected = false;
	private FileHandle file;
	private String patternString;
	// Store how many patterns there are of each level
	final static private int[] NUM_LEVEL_PATTERNS = { 5, 4, 3, 3, 2, 2, 2, 2, 2, 7 };
	final static private int NUM_LEVELS = 10;

	// GAME VARIABLES
	public int score;
	public int level;
	public int state;
	public float tileVelocity, tileSuckVelocity;
	private float tileDeltaTime; // Time since last tiles generated

	// NON-FINAL GAME CONSTANTS (To be set)

	public float tileStartPosition;
	public float initialVelocity, initialSuckVelocity;
	private float w, h, tileWidth;
	public float[] columnPosition;

	public final List<Tile> tiles;
	public final Player players[];
	public final List<Message> messages;

	public World(OrangeRed game) {
		this.game = game;
		this.tiles = new ArrayList<Tile>();
		this.messages = new ArrayList<Message>();
		players = new Player[2];
		this.w = Gdx.graphics.getWidth();
		this.h = Gdx.graphics.getHeight();

		columnPosition = new float[2];
		columnPosition[0] = w / 4 + 10;
		columnPosition[1] = 3 * w / 4 - 10;
		tileWidth = Assets.redSprt.getWidth();
		tileDeltaTime = 0;

		initialVelocity = -0.5f * h;
		initialSuckVelocity = -0.85f * h;
		tileVelocity = initialVelocity;
		tileSuckVelocity = initialSuckVelocity;
		tileStartPosition = h;

		generateLevel();
		this.score = 0;
		this.level = 1;
		this.state = WORLD_STATE_RUNNING;
	}

	private void generateLevel() {

		//float messagePositY = h + Assets.redSprt.getHeight() / 2;
		//float messageWidth = 5 * w / 6;
		//float messagePositX = (w - messageWidth) / 2;
		//float messageVelocity = 8 * tileVelocity / 16;
		for (int i = 0; i < 2; i++) {
			/*
			 * Message message1 = new Message(messagePositX, messagePositY,
			 * messageWidth, messageVelocity, "Tap a box to change its colour");
			 * Message message2 = new Message(messagePositX, messagePositY +
			 * Assets.redSprt.getHeight() / 2, messageWidth, messageVelocity,
			 * "Match the boxes to the falling diamonds");
			 * messages.add(message1); messages.add(message2);
			 */

			Tile tile = new Tile(columnPosition[i] - Assets.redSprt.getWidth() / 2, tileStartPosition, i, WHITE,
					tileVelocity);
			tiles.add(tile);

			Player player = new Player(1, i, i * Gdx.graphics.getWidth() / 2 + 3 * Assets.boundaryWidth,
					ZERO_POINT);
			players[i] = player;
		}
	}

	public void update(float deltaTime) {

		updateTiles(deltaTime);
		updateMessages(deltaTime);
		checkTileCollisions(deltaTime);
		// UPDATE level
		setLevel();
	}

	private void updateTiles(float deltaTime) {
		for (Tile tile : tiles) {
			tile.update(deltaTime);
		}
	}


	public void switchLeft() {
		Player player = players[0];
		player.switchColour();
	}

	public void switchRight() {
		Player player = players[1];
		player.switchColour();
	}

	private void updateMessages(float deltaTime) {
		for (Message message : messages) {
			message.update(deltaTime);
		}
	}

	private void checkTileCollisions(float deltaTime) {
		Iterator<Tile> tileIter = tiles.iterator();

		while (tileIter.hasNext()) {
			Tile tempTile = tileIter.next();
			if (tempTile.getScaledPositY() < 0) {
				tileIter.remove();
			} else if (tempTile.getScaledPositY() < ZERO_POINT && tempTile.state == Tile.TILE_STATE_NORMAL) {
				tempTile.state = Tile.TILE_STATE_COUNTED;
				tempTile.setVelocity(tileSuckVelocity);
				if (tempTile.getColour() != WHITE) {
					if (tempTile.getColour() == players[tempTile.getColumn()].getColour()) {
						score++;
						Assets.playSound(Assets.matchSound1);
					} else
						state = WORLD_STATE_GAME_OVER;
				}
			}
		}
		tileDeltaTime += deltaTime;
		if (tileDeltaTime > TILE_TIME_INTERVAL) {
			for (int i = 0; i < 2; i++) {
				Tile tile;
				int spread = MathUtils.random(-START_SPREAD_RANGE / 2, START_SPREAD_RANGE / 2);
				tile = new Tile(columnPosition[i] - tileWidth / 2,
						spread + tileStartPosition + deltaTime * tileVelocity, i, colourScheduler(), tileVelocity);
				tiles.add(tile);
			}
			tileDeltaTime = 0;
		}
	}

	// Methods for updating tile velocities
	private void updateTileVelocities(float newVelocity) {
		for (Tile tile : tiles) {
			if (tile.getState() == Tile.TILE_STATE_NORMAL) {
				tile.setVelocity(newVelocity);
			}
		}
	}

	private void updateTileSuckVelocities(float newVelocity) {
		for (Tile tile : tiles) {
			if (tile.getState() == Tile.TILE_STATE_COUNTED) {
				tile.setVelocity(newVelocity);
			}
		}
	}

	private void setLevel() {
		int levelDif = 15;
		if (level < 10 && score >= (level) * levelDif) {
			level++;
			Achievements.checkLevel(level, game);
		}
	}

	/*
	 * #### PATTERN GENERATOR ### - Reads patterns from text files based on
	 * current game difficulty level - NUM_LEVEL_PATTERNS contains the number of
	 * patterns at each level - Text files are named "XY" where X is the
	 * difficulty level, Y is a unique number for each pattern (starting from 1)
	 * - Each line in the file contains a pair of numbers: the first gives the
	 * colour of the left column; the second gives the right.
	 */
	private int colourScheduler() {
		if (!patternSelected) {
			// select a random pattern of the current level
			String fileName = "patterns/default.txt";
			double rand = Math.random();
			for (int i = 0; i < NUM_LEVEL_PATTERNS[level - 1]; i++) {
				if (rand > ((float) i / (float) NUM_LEVEL_PATTERNS[level - 1])) {
					fileName = String.format("patterns/%d%d.txt", level, i + 1);
				}
			}
			System.out.println("Running pattern: " + fileName);

			// based on selected pattern, read numbers from file
			file = Gdx.files.internal(fileName);
			patternString = file.readString();
			patternSelected = true;
		}
		if (patternString.charAt(currentChar) == '\r' || patternString.charAt(currentChar) == '\n') {
			currentChar += 2; // If next char is a line break, skip it
		}
		String returnString = "" + patternString.charAt(currentChar);
		currentChar++;
		if (currentChar >= patternString.length()) { // Check if that was the
														// last line
			patternSelected = false;
			currentChar = 0;
		}
		return Integer.parseInt(returnString);
	}

	public int getState() {
		return state;
	}

}
