package com.seabass.orangered;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Settings {
	public static boolean soundEnabled = true;
	public static int[] highscores = new int[] { 0, 0, 0, 0, 0 };
	public static int cumulativeScore = 0;
	public static int totGameSessions = 0;
	public final static String file = "settings.txt";

	public static void load() {
		try {
			FileHandle filehandle = Gdx.files.local(file);

			boolean isLocalDirAvailable = Gdx.files.isLocalStorageAvailable();
			String localDir = Gdx.files.getLocalStoragePath();

			String[] strings = filehandle.readString().split("\n");

			soundEnabled = Boolean.parseBoolean(strings[0]);
			
			for (int i = 0; i < 5; i++) {
				highscores[i] = Integer.parseInt(strings[i + 1].trim());
			}

			cumulativeScore = Integer.parseInt(strings[6]);
			totGameSessions=Integer.parseInt(strings[7]);
			
		} catch (Throwable e) {
			System.out.println("Settings read error.");
		}

	}

	public static void save() {
		try {
			FileHandle filehandle = Gdx.files.local(file);
			
			filehandle.writeString(Boolean.toString(soundEnabled) + "\n", false);
			
			for (int i = 0; i < 5; i++) {
				filehandle.writeString(Integer.toString(highscores[i]) + "\n", true);
			}
			filehandle.writeString(Integer.toString(cumulativeScore)+"\n", true);
			filehandle.writeString(Integer.toString(totGameSessions), true);
			
		} catch (Throwable e) {
			System.out.println(e);
		}
	}

	public static void addHighScore(int score) {
		for (int i = 0; i < 5; i++) {
			if (highscores[i] < score) {
				for (int j = 4; j > i; j--)
					highscores[j] = highscores[j - 1];
				highscores[i] = score;
				break;
			}
		}
	}
	
	public static int getHighScore(){
		return highscores[0];
	}
	
	public static void addCumulativeScore(int score){
		cumulativeScore+=score;
	}
	
	public static void incrementTotGameSessions(){
		totGameSessions++;
	}
	
	public static double getTotGameSessionsDouble(){
		return (double)totGameSessions;
	}
}