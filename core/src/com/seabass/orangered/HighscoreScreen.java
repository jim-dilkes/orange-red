package com.seabass.orangered;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class HighscoreScreen extends ScreenAdapter {
	OrangeRed game;
	OrthographicCamera guiCam;
	ShapeRenderer shapes;
	Rectangle backBounds;
	Vector3 touchPoint;
	Table table;
	Label titleLabel, hsLab1, hsLab2, hsLab3, hsLab4, hsLab5;
	TextButton backButton, globalButton;
	Stage stage;
	String[] highScores;
	float xOffset = 0;
	boolean debug;

	Image orangeDiamond, redDiamond;

	private final int h = Gdx.graphics.getHeight(), w = Gdx.graphics.getWidth();

	public HighscoreScreen(OrangeRed game) {
		this.game = game;

		shapes = new ShapeRenderer();
		guiCam = new OrthographicCamera(w, h);
		guiCam.position.set(w / 2, h / 2, 0);
		touchPoint = new Vector3();
		debug = false;

		highScores = new String[5];
		for (int i = 0; i < 5; i++) {
			highScores[i] = "" + Settings.highscores[i];
		}

		stage = new Stage();
		table = new Table(Assets.skin);

		titleLabel = new Label("Highscores", Assets.skin);
		titleLabel.setAlignment(Align.center);
		backButton = new TextButton("Back", Assets.skin);
		backButton.setDisabled(true);
		globalButton = new TextButton("Global", Assets.skin);
		globalButton.setDisabled(true);

		hsLab1 = new Label(highScores[0], Assets.skin);
		hsLab1.setAlignment(Align.center);
		hsLab2 = new Label(highScores[1], Assets.skin);
		hsLab2.setAlignment(Align.center);
		hsLab3 = new Label(highScores[2], Assets.skin);
		hsLab3.setAlignment(Align.center);
		hsLab4 = new Label(highScores[3], Assets.skin);
		hsLab4.setAlignment(Align.center);
		hsLab5 = new Label(highScores[4], Assets.skin);
		hsLab5.setAlignment(Align.center);

		orangeDiamond = new Image(Assets.orangeSprt);
		orangeDiamond.setColor(Assets.jimOrange);
		redDiamond = new Image(Assets.redSprt);
		redDiamond.setColor(Assets.jimRed);

		table.setFillParent(true);
		Gdx.input.setInputProcessor(stage);// Make the stage consume events
		stage.addActor(table);

		float diamondSize = h / 7;
		float rowGap = 2 * h / 1280;
		// table.debug();

		table.top().pad(diamondSize / 6 + Assets.boundaryWidth);
		table.add().width(2 * Assets.boundaryWidth);
		table.add(orangeDiamond).height(diamondSize).width(diamondSize);
		table.add().width(w - 2 * diamondSize - 4 * Assets.boundaryWidth);
		table.add(redDiamond).height(diamondSize).width(diamondSize);
		table.add().width(2 * Assets.boundaryWidth);
		table.row();
		table.add().height(diamondSize / 6).colspan(5);

		table.row();
		table.add(titleLabel).width(w - Assets.boundaryWidth * 2).colspan(5).height(diamondSize);

		table.row();
		table.add().height(8 * h / 1280).colspan(5);
		table.row();
		table.add(hsLab1).width(w - Assets.boundaryWidth * 2).colspan(5);
		table.row();
		table.add().height(rowGap).colspan(5);
		table.row();
		table.add(hsLab2).width(w - Assets.boundaryWidth * 2).colspan(5);
		table.row();
		table.add().height(rowGap).colspan(5);
		table.row();
		table.add(hsLab3).width(w - Assets.boundaryWidth * 2).colspan(5);
		table.row();
		table.add().height(rowGap).colspan(5);
		table.row();
		table.add(hsLab4).width(w - Assets.boundaryWidth * 2).colspan(5);
		table.row();
		table.add().height(rowGap).colspan(5);
		table.row();
		table.add(hsLab5).width(w - Assets.boundaryWidth * 2).colspan(5);

		table.row();
		table.add().height(8 * h / 1280).colspan(5);
		table.row();
		table.add(globalButton).width(w - Assets.boundaryWidth * 2).colspan(5);

		table.row();
		table.add().height(8 * h / 1280).colspan(5);
		table.row();
		table.add(backButton).width(w - Assets.boundaryWidth * 2).colspan(5);

		table.center().top();

		addButtonListeners();

	}

	public void addButtonListeners() {
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Assets.playSound(Assets.matchSound1);
				game.setScreen(new MainMenuScreen(game));
			};
		});

		globalButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Assets.playSound(Assets.matchSound1);
				if (game.actionResolver.getSignedInGPGS())
					game.actionResolver.getLeaderboardGPGS();
				else
					game.actionResolver.loginGPGS();

			};
		});
	}

	public void update() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
			table.debug();
			debug = !debug;
		}
	}

	public void draw() {
		GL20 gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		guiCam.update();

		game.batcher.setProjectionMatrix(guiCam.combined);

		shapes.begin(ShapeType.Filled);
		shapes.setColor(Assets.jimForeground);
		shapes.rect(0, 0, w, h);
		shapes.setColor(Assets.jimBackground);
		shapes.rect(Assets.boundaryWidth, Assets.boundaryWidth, w - 2 * Assets.boundaryWidth,
				h - 2 * Assets.boundaryWidth);
		shapes.end();

		stage.act();
		stage.draw();
	}

	@Override
	public void render(float delta) {
		update();
		draw();
	}

	@Override
	public void dispose() {
		shapes.dispose();
		stage.dispose();
	}
}
