package com.seabass.orangered;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class GameScreen extends ScreenAdapter {

	OrangeRed game;
	OrthographicCamera guiCam;

	int state;
	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_LEVEL_END = 3;
	static final int GAME_OVER = 4;

	private float w, h;

	Vector3 touchPoint;
	World world;
	WorldRenderer renderer;
	ShapeRenderer shapes;

	Stage pauseStage, gameOverStage;

	Table pauseTable, gameOverTable;
	TextButton resumeButton, quitButton, gameOverQuitButton, restartButton, gameOverRestartButton;
	Label scoreLabel, gameOverLabel;

	Button pauseBtn;

	Rectangle pauseScreenBounds, pauseScreenInnerBounds, quitScreenBounds, quitScreenInnerBounds, leftBox, rightBox;

	GestureListener gameListener;
	GestureDetector gameInputProcessor;

	String scoreString;
	float scaleButtonHeight, buttonGap;

	int lastScore;

	private boolean debug = false;
	private boolean[] wasTouched = { false, false, false, false, false };

	public GameScreen(OrangeRed game) {
		this.game = game;

		state = GAME_READY;
		this.w = Gdx.graphics.getWidth();
		this.h = Gdx.graphics.getHeight();
		guiCam = new OrthographicCamera(w, h);
		guiCam.position.set(w / 2, h / 2, 0);
		touchPoint = new Vector3();
		world = new World(this.game);
		shapes = new ShapeRenderer();
		renderer = new WorldRenderer(game.batcher, world, shapes);
		float pauseScale = w / (9 * Assets.pauseSprt.getWidth());

		leftBox = new Rectangle(0, 0, w / 2, World.ZERO_POINT);
		rightBox = new Rectangle(w / 2, 0, w / 2, World.ZERO_POINT);

		setGestureListener();
		gameInputProcessor = new GestureDetector(gameListener);
		Gdx.input.setInputProcessor(gameInputProcessor);

		pauseStage = new Stage();
		pauseTable = new Table();
		resumeButton = new TextButton("Resume", Assets.skin);
		resumeButton.setDisabled(true);
		quitButton = new TextButton("Main Menu", Assets.skin);
		quitButton.setDisabled(true);
		restartButton = new TextButton("Restart", Assets.skin);
		restartButton.setDisabled(true);
		scoreLabel = new Label("\nScore: 0", Assets.skin);
		scoreLabel.setAlignment(Align.center);

		gameOverStage = new Stage();
		gameOverTable = new Table();
		gameOverQuitButton = new TextButton("Main Menu", Assets.skin);
		gameOverQuitButton.setDisabled(true);
		gameOverRestartButton = new TextButton("Restart", Assets.skin);
		gameOverRestartButton.setDisabled(true);
		gameOverLabel = new Label("\nScore: 0", Assets.skin);
		gameOverLabel.setAlignment(Align.center);

		scaleButtonHeight = h / 10;
		buttonGap = h / 40;

		initiatePauseTable();
		pauseStage.addActor(pauseTable);
		initiategameOverTable();
		gameOverStage.addActor(gameOverTable);

		float pauseBoundary = 15 * h / 1280;
		pauseBtn = new Button(Assets.pauseSprt, w - pauseScale * Assets.pauseSprt.getWidth() - pauseBoundary,
				h - pauseScale * Assets.pauseSprt.getHeight() - pauseBoundary,
				pauseScale * Assets.pauseSprt.getWidth());

		lastScore = 0;
		
		Settings.incrementTotGameSessions();
	}

	private void initiatePauseTable() {
		pauseTable.setFillParent(true);
		pauseTable.top().pad(2 * scaleButtonHeight);
		// scoreLabel.setText(String.format("Score: %d", world.score));
		pauseTable.add(scoreLabel).width(w).height(scaleButtonHeight);
		pauseTable.row();
		pauseTable.add().height(buttonGap);
		pauseTable.row();
		pauseTable.add(resumeButton).width(w).height(scaleButtonHeight);
		pauseTable.row();
		pauseTable.add().height(buttonGap);
		pauseTable.row();
		pauseTable.add(restartButton).width(w).height(scaleButtonHeight);
		pauseTable.row();
		pauseTable.add().height(buttonGap);
		pauseTable.row();
		pauseTable.add(quitButton).width(w).height(scaleButtonHeight);

		resumeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Assets.playSound(Assets.clickSound);
				Gdx.input.setInputProcessor(gameInputProcessor);
				state = GAME_RUNNING;
				game.actionResolver.hideBanner();
			};
		});

		restartButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Assets.playSound(Assets.clickSound);
				saveScores();
				game.setScreen(new GameScreen(game));
				game.actionResolver.hideBanner();
			};
		});

		quitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Assets.playSound(Assets.clickSound);
				saveScores();
				game.setScreen(new MainMenuScreen(game));
				game.actionResolver.hideBanner();
			};
		});
	}

	private void initiategameOverTable() {
		gameOverTable.setFillParent(true);
		gameOverTable.top().pad(2 * scaleButtonHeight);
		gameOverLabel.setText(String.format("GAME OVER\nScore: %d\n", world.score));
		gameOverTable.add(gameOverLabel).width(w).height(scaleButtonHeight * 2);
		gameOverTable.row();
		gameOverTable.add().height(buttonGap);
		gameOverTable.row();
		gameOverTable.add(gameOverRestartButton).width(w).height(scaleButtonHeight);
		gameOverTable.row();
		gameOverTable.add().height(buttonGap);
		gameOverTable.row();
		gameOverTable.add(gameOverQuitButton).width(w).height(scaleButtonHeight);

		gameOverQuitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Assets.playSound(Assets.clickSound);
				game.setScreen(new MainMenuScreen(game));
				game.actionResolver.hideBanner();
			};
		});
		gameOverRestartButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Assets.playSound(Assets.clickSound);
				game.setScreen(new GameScreen(game));
				game.actionResolver.hideBanner();
			};
		});
	}

	public void update(float deltaTime) {

		if (deltaTime > 0.1f)
			deltaTime = 0.1f;
		switch (state) {
		case GAME_READY:
			updateReady();
			break;
		case GAME_RUNNING:
			updateRunning(deltaTime);
			break;
		case GAME_PAUSED:
			updatePaused();
			break;
		case GAME_LEVEL_END:
			updateLevelEnd();
			break;
		case GAME_OVER:
			updateGameOver();
			break;
		}

	}

	private void updateReady() {
		state = GAME_RUNNING;
	}

	private void updateRunning(float deltaTime) {

		// Toggle debug
		if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
			debug = !debug;
		}
		world.update(deltaTime);

		if (world.getState() == World.WORLD_STATE_GAME_OVER) {
			state = GAME_OVER;
			gameOverLabel.setText(String.format("Game Over\nScore: %d", world.score));
			Gdx.input.setInputProcessor(gameOverStage);
			saveScores();
			game.actionResolver.showBanner();
		}

	}

	private void saveScores() {
		game.incrementGamesPlayed();
		Settings.addHighScore(world.score);
		Settings.addCumulativeScore(world.score);
		Settings.save();
		game.actionResolver.analyticsGameOver(world.score, world.level, game.gamesPlayed);
		if (game.actionResolver.getSignedInGPGS())
			game.actionResolver.submitScoreGPGS(world.score);
	}

	private void updatePaused() {
		scoreLabel.setText(String.format("Score: %d", world.score));
		if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
			debug = !debug;
		}
	}

	private void updateLevelEnd() {
		state = GAME_READY;
	}

	private void updateGameOver() {
		gameOverLabel.setText(String.format("Game Over\nScore: %d", world.score));
		if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
			debug = !debug;
		}
	}

	@Override
	public void render(float deltaTime) {
		update(deltaTime);
		draw();
	}

	public void draw() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.render();
		drawOverlay();

		switch (state) {
		case GAME_READY:
			System.out.println("\nLEVEL READY???");
			break;
		case GAME_RUNNING:
			presentRunning();
			break;
		case GAME_PAUSED:
			presentPaused();
			break;
		case GAME_LEVEL_END:
			System.out.println("\nLEVEL END???");
			break;
		case GAME_OVER:
			presentGameOver();
			break;
		}
	}

	private void drawOverlay() {
		game.batcher.begin();
		pauseBtn.draw(game.batcher);

		String scoreString;
		scoreString = String.format("Score: %d\nLevel: %d", world.score, world.level);
		Assets.fontMid.draw(game.batcher, scoreString, 10, h - 10);
		game.batcher.end();
		if (debug) {
			debug();
		}
	}

	private void presentRunning() {
		game.batcher.begin();
		game.batcher.end();
	}

	private void presentPaused() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapes.begin(ShapeType.Filled);
		shapes.setColor(0f, 0f, 0f, 0.6f);
		shapes.rect(0, 0, w, h); // draw translucent layer

		shapes.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		pauseStage.act();
		pauseStage.draw();

		if (debug) {
			debug();
		}
	}

	private void presentGameOver() {

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapes.begin(ShapeType.Filled);
		shapes.setColor(0f, 0f, 0f, 0.6f);
		shapes.rect(0, 0, w, h); // draw translucent layer
		shapes.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		gameOverStage.act();
		gameOverStage.draw();
	}

	@Override
	public void pause() {
		if (state == GAME_RUNNING)
			state = GAME_PAUSED;
	}

	private void debug() {
		game.batcher.begin();
		shapes.begin(ShapeType.Line);
		shapes.line(0, World.ZERO_POINT, w, World.ZERO_POINT, Color.MAGENTA, Color.MAGENTA);
		shapes.line(world.columnPosition[0], 0, world.columnPosition[0], h, Color.BLACK, Color.BLACK);
		shapes.line(world.columnPosition[1], 0, world.columnPosition[1], h, Color.BLACK, Color.BLACK);
		for (Tile tile : world.tiles) {
			shapes.setColor(Color.RED);
			shapes.rect(tile.getScaledPositX(), tile.getScaledPositY(), tile.getScaledWidth(), tile.getScaledHeight());
			shapes.line(0, tile.getScaledPositY(), w, tile.getScaledPositY(), Color.GREEN, Color.GREEN);

		}

		shapes.rect(pauseBtn.getBounds().getX(), pauseBtn.getBounds().getY(), pauseBtn.getBounds().getWidth(),
				pauseBtn.getBounds().getHeight());
		shapes.end();
		game.batcher.end();
	}

	private void setGestureListener() {
		gameListener = new GestureListener() {
			@Override
			public boolean touchDown(float x, float y, int pointer, int button) {
				x+=w/2;
				y-=h/2;
				System.out.println("Init touch: x = " +x +", y = "+y);
				guiCam.unproject(touchPoint.set(x, y, 0));
				System.out.println("Final touch: x = " +touchPoint.x +", y = "+touchPoint.y);
				
				if (leftBox.contains(touchPoint.x,touchPoint.y)) {
					world.switchLeft();
				} else if (rightBox.contains(touchPoint.x, touchPoint.y)) {
					world.switchRight();
				} else if (pauseBtn.getBounds().contains(touchPoint.x, touchPoint.y)) {
					System.out.println("PAUSED");
					state = GAME_PAUSED;
					scoreLabel.setText(String.format("Score: %d", world.score));
					Gdx.input.setInputProcessor(pauseStage);
					game.actionResolver.showBanner();
				}
				return true;
			}

			@Override
			public boolean tap(float x, float y, int count, int button) {
				return false;
			}

			@Override
			public boolean longPress(float x, float y) {
				return false;
			}

			@Override
			public boolean fling(float velocityX, float velocityY, int button) {
				return false;
			}

			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				return false;
			}

			@Override
			public boolean panStop(float x, float y, int pointer, int button) {
				return false;
			}

			@Override
			public boolean zoom(float initialDistance, float distance) {
				return false;
			}

			@Override
			public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
				return false;
			}
		};
	}

	@Override
	public void resume(){
		System.out.println("PAUSED");
		state = GAME_PAUSED;
		scoreLabel.setText(String.format("Score: %d", world.score));
		Gdx.input.setInputProcessor(pauseStage);
	}
	
	@Override
	public void dispose() {
		shapes.dispose();
		pauseStage.dispose();
		gameOverStage.dispose();
	}
}