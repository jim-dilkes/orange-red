package com.seabass.orangered;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class MainMenuScreen extends ScreenAdapter {
	OrangeRed game;
	OrthographicCamera guiCam;
	ShapeRenderer shapes;
	Vector3 touchPoint;
	Stage stage;
	TextButton playButton, highScoreButton, quitButton, achievementsButton, helpButton;
	Image orangeDiamond, redDiamond;
	Table table;
	Label label;
	boolean debug;

	private final int h = Gdx.graphics.getHeight(), w = Gdx.graphics.getWidth();

	public MainMenuScreen(OrangeRed game) {
		this.game = game;
		shapes = new ShapeRenderer();
		guiCam = new OrthographicCamera(w, h);
		guiCam.position.set(w / 2, h / 2, 0);
		touchPoint = new Vector3();
		debug = false;

		stage = new Stage();
		table = new Table(Assets.skin);
		
		playButton = new TextButton("Play", Assets.skin); // Use the initialized skin
		playButton.setDisabled(true);
		highScoreButton = new TextButton("Highscores", Assets.skin);
		highScoreButton.setDisabled(true);
		achievementsButton= new TextButton("Achievements", Assets.skin);
		achievementsButton.setDisabled(true);
		helpButton= new TextButton("Help", Assets.skin);
		helpButton.setDisabled(true);
		//quitButton = new TextButton("Quit", Assets.skin);
		//quitButton.setDisabled(true);
		orangeDiamond = new Image(Assets.orangeSprt);
		orangeDiamond.setColor(Assets.jimOrange);
		redDiamond = new Image(Assets.redSprt);
		redDiamond.setColor(Assets.jimRed);

		label = new Label("Orange Red", Assets.skin);
		label.setAlignment(Align.center);

		table.setFillParent(true);
		Gdx.input.setInputProcessor(stage);// Make the stage consume events
		stage.addActor(table);

		float diamondSize = h/7;
		float rowGap = 20*h/1280;
		
		table.top().pad(diamondSize/6+Assets.boundaryWidth);
		table.add().width(2*Assets.boundaryWidth);
		table.add(orangeDiamond).height(diamondSize).width(diamondSize);
		table.add().width(w-2*diamondSize-4*Assets.boundaryWidth);
		table.add(redDiamond).height(diamondSize).width(diamondSize);
		table.add().width(2*Assets.boundaryWidth);
		table.row();
		table.add().height(diamondSize/6).colspan(5);
		table.row();
		
		table.add(label).width(w - Assets.boundaryWidth * 2).colspan(5).height(diamondSize);
		table.row();
		table.add().height(rowGap).colspan(5);
		table.row();
		table.add(playButton).width(w - Assets.boundaryWidth * 2).colspan(5);
		table.row();
		table.add().height(rowGap).colspan(5);
		table.row();
		table.add(highScoreButton).width(w - Assets.boundaryWidth * 2).colspan(5);
		table.row();
		table.add().height(rowGap).colspan(5);
		table.row();
		table.add(achievementsButton).width(w - Assets.boundaryWidth * 2).colspan(5);
		table.row();
		table.add().height(rowGap).colspan(5);
		table.row();
		table.add(helpButton).width(w - Assets.boundaryWidth * 2).colspan(5);
		table.row();
		table.add().height(rowGap).colspan(5);
		//table.row();
		//table.add(quitButton).width(w - Assets.boundaryWidth * 2).colspan(5);
		//table.center().top();

		addButtonListeners();
		
		
	}
	
	@Override
	public void show(){
		Achievements.checkScore(game);
	}
	
	public void addButtonListeners(){
		playButton.addListener( new ClickListener() {              
		    @Override
		    public void clicked(InputEvent event, float x, float y) {
		    	Assets.playSound(Assets.matchSound1);
				game.setScreen(new GameScreen(game));
		    };
		});
		
		highScoreButton.addListener( new ClickListener() {              
		    @Override
		    public void clicked(InputEvent event, float x, float y) {
		    	Assets.playSound(Assets.matchSound1);
				game.setScreen(new HighscoreScreen(game));
		    };
		});
		
		achievementsButton.addListener( new ClickListener() {              
		    @Override
		    public void clicked(InputEvent event, float x, float y) {
		    	Assets.playSound(Assets.matchSound1);
				if (game.actionResolver.getSignedInGPGS()) game.actionResolver.getAchievementsGPGS();
				else game.actionResolver.loginGPGS();
		    };
		});
		
		helpButton.addListener( new ClickListener() {              
		    @Override
		    public void clicked(InputEvent event, float x, float y) {
		    	Assets.playSound(Assets.matchSound1);
				game.setScreen(new HelpScreen(game));
		    };
		});
		
		/*quitButton.addListener( new ClickListener() {              
		    @Override
		    public void clicked(InputEvent event, float x, float y) {
		    	game.dispose();
				Gdx.app.exit();
		    };
		});*/
	}

	public void update() {
		
	}

	public void draw() {
		GL20 gl = Gdx.gl;
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		guiCam.update();

		shapes.begin(ShapeType.Filled);
		shapes.setColor(Assets.jimForeground);
		shapes.rect(0, 0, w, h);
		shapes.setColor(Assets.jimBackground);
		shapes.rect(Assets.boundaryWidth, Assets.boundaryWidth, w - 2 * Assets.boundaryWidth, h - 2 * Assets.boundaryWidth);
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
	public void pause() {
	}
	
	@Override
	public void dispose(){
	}
}