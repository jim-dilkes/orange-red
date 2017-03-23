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

public class HelpScreen extends ScreenAdapter {

	OrangeRed game;
	OrthographicCamera guiCam;
	ShapeRenderer shapes;
	Rectangle backBounds;
	Vector3 touchPoint;
	Table table;
	Stage stage;

	Label titleLabel, helpLabel1, helpLabel2, helpLabel3;
	TextButton backButton;
	Image orangeDiamond, redDiamond;

	boolean debug;
	private final int h = Gdx.graphics.getHeight(), w = Gdx.graphics.getWidth();

	public HelpScreen(OrangeRed game) {
		this.game = game;

		shapes = new ShapeRenderer();
		guiCam = new OrthographicCamera(w, h);
		guiCam.position.set(w / 2, h / 2, 0);
		touchPoint = new Vector3();
		debug = false;

		stage = new Stage();
		table = new Table(Assets.skin);

		titleLabel = new Label("Help", Assets.skin);
		titleLabel.setAlignment(Align.center);
		helpLabel1 = new Label(
				"Tap the boxes at the bottom of the screen to alternate between red and orange.",
				Assets.skin, "midFont");
		helpLabel1.setAlignment(Align.center);
		helpLabel1.setWrap(true);
		helpLabel2 = new Label("The colour of each box must always match the colour of a diamond that touches it.",
				Assets.skin, "midFont");
		helpLabel2.setAlignment(Align.center);
		helpLabel2.setWrap(true);
		helpLabel3 = new Label(
				"Match the box to the colour of the next diamond.",
				Assets.skin, "midFont");
		helpLabel3.setAlignment(Align.center);
		helpLabel3.setWrap(true);
		backButton = new TextButton("Back", Assets.skin);
		backButton.setDisabled(true);

		orangeDiamond = new Image(Assets.orangeSprt);
		orangeDiamond.setColor(Assets.jimOrange);
		redDiamond = new Image(Assets.redSprt);
		redDiamond.setColor(Assets.jimRed);

		table.setFillParent(true);
		Gdx.input.setInputProcessor(stage);// Make the stage consume events
		stage.addActor(table);

		float diamondSize = h / 7;
		float rowGap = 20 * h / 1280;
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
		table.add().height(rowGap);
		table.row();
		table.add(helpLabel1).width(w - Assets.boundaryWidth * 2).colspan(5).height(diamondSize);
		table.row();
		table.add(helpLabel2).width(w - Assets.boundaryWidth * 2).colspan(5).height(diamondSize);
		table.row();
		table.add(helpLabel3).width(w - Assets.boundaryWidth * 2).colspan(5).height(diamondSize);

		table.row();
		table.add().height(rowGap);
		table.row();
		table.add(backButton).width(w - Assets.boundaryWidth * 2).colspan(5);

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
	public void dispose(){
		shapes.dispose();
		stage.dispose();
	}

}
