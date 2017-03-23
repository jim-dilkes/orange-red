package com.seabass.orangered;

import com.badlogic.gdx.math.Vector2;

public class Tile {

	public static final int TILE_STATE_NORMAL = 0;
	public static final int TILE_STATE_COUNTED = 1;

	public float velocity;

	private int colour, column;
	public int state;
	private Vector2 posit;
	private float width, height;

	public Tile(float initX, float initY, int column, int colour, float velocity) {
		this.colour = colour;
		this.column = column;

		this.posit = new Vector2(initX, initY);
		this.state = TILE_STATE_NORMAL;
		this.velocity=velocity;
		
		this.width=Assets.redSprt.getWidth();
		this.height=Assets.redSprt.getHeight();
	}

	public void update(float deltaTime) {
			posit.y += velocity * deltaTime;
	}
	
	public void addVelocity(float addVelocity){
		this.velocity+=addVelocity;
	}

	public void setVelocity(float newVelocity) {
		this.velocity = newVelocity;
	}

	public float getPositX() {
		return posit.x;
	}

	public void setPositX(float positX) {
		this.posit.x = positX;
	}
	
	public float getScaledPositX(){
		return posit.x+Assets.tileAdjustScaleFactor*Assets.redSprt.getWidth();
	}
	
	public float getScaledPositY(){
		return posit.y+Assets.tileAdjustScaleFactor*Assets.redSprt.getHeight();
	}

	public float getPositY() {
		return posit.y;
	}

	public void setPositY(float positY) {
		this.posit.y = positY;
	}
	
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	public float getScaledWidth() {
		return width*Assets.tileScaleFactor;
	}

	public float getScaledHeight() {
		return height*Assets.tileScaleFactor;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}

	public int getColour() {
		return colour;
	}

	public int getColumn() {
		return column;
	}

	public int getState() {
		return state;
	}

}
