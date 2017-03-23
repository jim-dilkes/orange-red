package com.seabass.orangered;

public interface ActionResolver {
	  public boolean getSignedInGPGS();
	  public void loginGPGS();
	  public void submitScoreGPGS(int score);
	  public void unlockAchievementGPGS(String achievementId);
	  public void getLeaderboardGPGS();
	  public void getAchievementsGPGS();
	  
	  public void showBanner();
	  public void hideBanner();
	  
	  public void analyticsGameOver(int score, int level, int gamesPlayed);
	  
	}
