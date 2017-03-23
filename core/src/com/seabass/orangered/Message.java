package com.seabass.orangered;

import com.badlogic.gdx.math.Vector2;

public class Message {

	private Vector2 posit;
	private float width, height;
	public float velocity;
	public String text;

	public Message(float initX, float initY, float width, float velocity, String text) {
		this.text = text;
		this.width = width;
		this.posit = new Vector2(initX, initY);
		this.velocity = velocity;
	}

	public void update(float deltaTime) {
		posit.y += velocity * deltaTime;
	}

	public float getWidth() {
		return width;
	}

	public String getText() {
		return text;
	}

	public float getPositX() {
		return posit.x;
	}

	public void setPositX(float positX) {
		this.posit.x = positX;
	}

	public float getPositY() {
		return posit.y;
	}

	public void setPositY(float positY) {
		this.posit.y = positY;
	}
}
