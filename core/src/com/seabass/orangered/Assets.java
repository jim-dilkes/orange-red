package com.seabass.orangered;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Assets {

	public static Texture whiteDiamondTex, pauseTex;
	public static Sprite redSprt, orangeSprt, pauseSprt;

	public static Skin skin;

	public static Sound matchSound1, menuSound2;
	public static BitmapFont fontSmall, fontMid, fontLarge, station12;
	public static Color jimRed, jimOrange, jimForeground, jimBackground, jimAlt;

	public static float tileScaleFactor, tileAdjustScaleFactor, playerScaleFactor, playerAdjustScaleFactor;

	// UI element sizes
	public static float boundaryWidth;
	public static float w, h;

	public static void load() {

		h = Gdx.graphics.getHeight();
		w = Gdx.graphics.getWidth();

		jimRed = new255Color(235, 95, 87, 255);
		jimOrange = new255Color(254, 157, 80, 255);
		jimForeground = new255Color(69, 66, 90, 255);
		jimBackground = new255Color(242, 248, 244, 255);
		jimAlt = new255Color(167, 190, 211, 255);

		pauseTex = new Texture(Gdx.files.internal("pause.png"));
		whiteDiamondTex = new Texture(Gdx.files.internal("whiteDiamond.png"));

		redSprt = new Sprite(whiteDiamondTex);
		redSprt.setColor(jimRed);
		orangeSprt = new Sprite(whiteDiamondTex);
		orangeSprt.setColor(jimOrange);

		pauseSprt = new Sprite(pauseTex);
		pauseSprt.setColor(jimForeground);

		tileScaleFactor = (h / 10) / redSprt.getHeight();
		tileAdjustScaleFactor = (1 - tileScaleFactor) / 2;

		redSprt.setScale(tileScaleFactor);
		orangeSprt.setScale(tileScaleFactor);
		playerAdjustScaleFactor = (1 - playerScaleFactor) / 2;

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Regular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();

		// Set large size font
		parameter.size = Math.round(40 * h / 1280);
		fontSmall = new BitmapFont();
		fontSmall = generator.generateFont(parameter); // font
		fontSmall.setColor(jimForeground);

		// Set mid size font
		parameter.size = Math.round(55 * h / 1280);
		fontMid = new BitmapFont();
		fontMid = generator.generateFont(parameter); // font
		fontMid.setColor(jimForeground);

		// Set large size font
		parameter.size = Math.round(70 * h / 1280);
		fontLarge = new BitmapFont();
		fontLarge = generator.generateFont(parameter); // font
		fontLarge.setColor(jimForeground);
		generator.dispose();

		matchSound1 = Gdx.audio.newSound(Gdx.files.internal("sounds/match1.wav"));

		createBasicSkin();

		// Set UI element sizes
		boundaryWidth = 25 * h / 1280;

	}

	private static void createBasicSkin() {
		// Create a font
		skin = new Skin();
		skin.add("default", fontLarge);

		// Create a texture
		Pixmap pixmap = new Pixmap(Math.round(w / 10), Math.round(h / 11), Pixmap.Format.RGB888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("background", new Texture(pixmap));

		// Create a button style
		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("background", jimOrange);
		textButtonStyle.down = skin.newDrawable("background", jimRed);
		textButtonStyle.checked = skin.newDrawable("background", jimRed);
		textButtonStyle.over = skin.newDrawable("background", jimRed);
		textButtonStyle.font = skin.getFont("default");
		textButtonStyle.fontColor = jimBackground;
		skin.add("default", textButtonStyle);

		// Create label style
		Label.LabelStyle labelStyleSmall = new Label.LabelStyle(fontSmall, jimBackground);
		labelStyleSmall.background = skin.newDrawable("background", jimForeground);
		skin.add("midFont", labelStyleSmall);
		
		// Create a Large label style
		Label.LabelStyle labelStyleLarge = new Label.LabelStyle(fontLarge, jimBackground);
		labelStyleLarge.background = skin.newDrawable("background", jimForeground);
		skin.add("default", labelStyleLarge);

	}

	private static Color new255Color(int r, int g, int b, int a) {
		return new Color((r / 255f), (g / 255f), (b / 255f), (a / 255f));
	}

	public static void playSound(Sound sound) {
		// sound.play();
	}

}
