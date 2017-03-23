package com.seabass.orangered;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Button {

	private Rectangle bounds;
	private float scaleX, scaleY;
	private float w, h;
	private float adjustPositX, adjustPositY;
	private float textureWidth, textureHeight;
	private Sprite btnSprite;

	public Button(Sprite tempSprite, float x, float y, float width, float height) { // give position in y and width and height of button as proportion of screen size 
		setConstants();
		this.btnSprite = tempSprite;
		bounds = new Rectangle(x, y, width, height);
		setScales();
	}

	public Button(Sprite tempSprite, float x, float y, float width) { // give button at x,y with natural side length ratios. set based on desired proportional width of screen occupied
		this.btnSprite = tempSprite;
		setConstants();
		float height = (width * textureHeight / textureWidth); // set the proportional height
		bounds = new Rectangle(x, y, width, height);
		setScales();
	}

	private void setConstants() {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		textureWidth = btnSprite.getWidth();
		textureHeight = btnSprite.getHeight();
	}

	private void setScales() {
		scaleX = bounds.width / textureWidth;
		scaleY = bounds.height / textureHeight;
		adjustPositX = bounds.x - ((1 - scaleX) / 2) * textureWidth;
		adjustPositY = bounds.y - ((1 - scaleY) / 2) * textureHeight;
		btnSprite.setScale(scaleX, scaleY);
		btnSprite.setPosition(adjustPositX, adjustPositY);
	}

	public void centre() {
		bounds.setX(w / 2 - bounds.getWidth() / 2);
		btnSprite.setCenterX(w/2);
	}
	
	public void draw(SpriteBatch batch){
		btnSprite.draw(batch);
	}

	public float getAdjustPositX() {
		return adjustPositX;
	}

	public float getAdjustPositY() {
		return adjustPositY;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public float getScaleX() {
		return scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

}
