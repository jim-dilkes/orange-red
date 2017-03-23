package com.seabass.orangered;

public class Achievements {
	//Check if level achievements accomplished
	public static void checkLevel(int level, OrangeRed game) {
		switch (level) {
		case 2:
			if (game.actionResolver.getSignedInGPGS())
				game.actionResolver.unlockAchievementGPGS(GameIds.achLevel2Id);
			break;
		case 4:
			if (game.actionResolver.getSignedInGPGS())
				game.actionResolver.unlockAchievementGPGS(GameIds.achLevel4Id);
			break;
		case 6:
			if (game.actionResolver.getSignedInGPGS())
				game.actionResolver.unlockAchievementGPGS(GameIds.achLevel6Id);
			break;
		case 8:
			if (game.actionResolver.getSignedInGPGS())
				game.actionResolver.unlockAchievementGPGS(GameIds.achLevel8Id);
			break;
		case 10:
			if (game.actionResolver.getSignedInGPGS())
				game.actionResolver.unlockAchievementGPGS(GameIds.achLevel10Id);
			break;
		}
	}
	//Check if cumulative score achievements accomplished
	public static void checkScore(OrangeRed game) {
		if (game.actionResolver.getSignedInGPGS()){
			int cumulativeScore = Settings.cumulativeScore;
			if(cumulativeScore>=10)
				game.actionResolver.unlockAchievementGPGS(GameIds.ach100Id);
			if(cumulativeScore>=500)
				game.actionResolver.unlockAchievementGPGS(GameIds.ach500Id);
			if(cumulativeScore>=2000)
				game.actionResolver.unlockAchievementGPGS(GameIds.ach2000Id);
			if(cumulativeScore>=10000)
				game.actionResolver.unlockAchievementGPGS(GameIds.ach10000Id);
			if(cumulativeScore>=100000)
				game.actionResolver.unlockAchievementGPGS(GameIds.ach100000Id);
		}
	}
}
