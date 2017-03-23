package com.seabass.orangered;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class WorldRenderer {

	static final float FRUSTUM_WIDTH = 10;
	static final float FRUSTUM_HEIGHT = 15;
	World world;
	OrthographicCamera cam;
	ShapeRenderer shapes;
	SpriteBatch batch;
	private float screenWidth, screenHeight;

	public WorldRenderer(SpriteBatch batch, World world, ShapeRenderer shapes) {
		this.world = world;
		this.cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.cam.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
		this.batch = batch;
		this.shapes = shapes;
		this.screenWidth = Gdx.graphics.getWidth();
		this.screenHeight = Gdx.graphics.getHeight();
	}

	public void render() {
		renderBackground();
		renderTiles();
		renderMessages();
		renderForeground();
		renderPlayers();
	}

	private void renderBackground() {
		batch.begin();
		shapes.begin(ShapeType.Filled);
		shapes.setColor(Assets.jimBackground);
		shapes.rect(0, 0, screenWidth, screenHeight);
		shapes.end();
		batch.end();
	}

	private void renderForeground() {
		batch.begin();
		shapes.begin(ShapeType.Filled);
		shapes.setColor(Assets.jimForeground);
		shapes.rect(0, 0, screenWidth, World.ZERO_POINT);
		shapes.end();

		shapes.begin(ShapeType.Filled);
		int lineWidth = 10 * Gdx.graphics.getWidth() / 720;
		shapes.setColor(Assets.jimBackground);
		shapes.rect(screenWidth / 2 - lineWidth / 2, 0, lineWidth, World.ZERO_POINT);
		shapes.end();
		batch.end();
	}

	private void renderMessages() {

		batch.begin();

		int len = world.messages.size();

		for (int i = 0; i < len; i++) {
			Message message = world.messages.get(i);
			Assets.fontMid.draw(batch, message.getText(), message.getPositX(), message.getPositY(),message.getWidth(), 1, true);
		}

		batch.end();
	}

	private void renderTiles() {
		batch.begin();
		int len = world.tiles.size();
		for (int i = 0; i < len; i++) {
			Tile tile = world.tiles.get(i);
			switch (tile.getColour()) {
			case 1:
				Assets.redSprt.setPosition(tile.getPositX(), tile.getPositY());
				Assets.redSprt.draw(batch);
				break;
			case 2:
				Assets.orangeSprt.setPosition(tile.getPositX(), tile.getPositY());
				Assets.orangeSprt.draw(batch);
				break;
			}
		}
		batch.end();
	}

	private void renderPlayers() {

		float playerBoundary = 3 * Assets.boundaryWidth;

		batch.begin();
		for (Player player : world.players) {
			shapes.begin(ShapeType.Filled);
			switch (player.getColour()) {
			case 1:
				shapes.setColor(Assets.jimRed);
				break;
			case 2:
				shapes.setColor(Assets.jimOrange);
				break;
			}
			shapes.rect(player.getPositX(), 0, Gdx.graphics.getWidth() / 2 - playerBoundary * 2, World.ZERO_POINT);
			shapes.end();
		}
		batch.end();
	}
}
