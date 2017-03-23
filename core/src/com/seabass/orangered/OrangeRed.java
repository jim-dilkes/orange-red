package com.seabass.orangered;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class OrangeRed extends Game{
	
	public SpriteBatch batcher;
	ActionResolver actionResolver;
	int gamesPlayed=0;
	boolean firstTimeCreate = true;
	
	public OrangeRed(ActionResolver actionResolver){
		this.actionResolver=actionResolver;
	}
	
	@Override
	public void create () {
		batcher = new SpriteBatch();
		Settings.load();
		Assets.load();
		setScreen(new MainMenuScreen(this));
	}
	
	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();

		getScreen().dispose();
	}
	
	public void incrementGamesPlayed(){
		gamesPlayed++;
	}
}
