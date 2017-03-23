package com.seabass.orangered.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.seabass.orangered.OrangeRed;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Orange Red";
		config.width = 576	;
		config.height = 1024;
		new LwjglApplication(new OrangeRed(new ActionResolverDesktop()), config);
	}
}
