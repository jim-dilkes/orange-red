package com.seabass.orangered;

import com.badlogic.gdx.math.Vector2;

public class Player {

	private int colour, column;
	private Vector2 posit;
	private float width, height;

	public Player(int colour, int column, float initX, float initY) {
		this.colour = colour;
		this.column = column;
		this.posit = new Vector2(initX,initY);
	}

	public void switchColour() {
			if (colour == World.RED)
				colour = World.ORANGE;
			else
				colour = World.RED;
	}

	public float getPositX() {
		return posit.x;
	}

	public float getPositY() {
		return posit.y;
	}


	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	public float getScaledWidth() {
		return width*Assets.playerScaleFactor;
	}

	public float getScaledHeight() {
		return height*Assets.playerScaleFactor;
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
}
