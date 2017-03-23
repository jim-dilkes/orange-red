package com.seabass.orangered.android;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.AnalyticsEvent;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.InitializationException;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.MobileAnalyticsManager;
import com.amazonaws.regions.Regions;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.seabass.orangered.ActionResolver;
import com.seabass.orangered.GameIds;
import com.seabass.orangered.OrangeRed;
import com.seabass.orangered.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class AndroidLauncher extends AndroidApplication implements GameHelperListener, ActionResolver {

	private static final String AD_UNIT_ID = "ca-app-pub-9757179573303545/3369536712";
	private static final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=com.seabass.orangered.android";
	protected AdView adView;
	protected View gameView;

	private GameHelper gameHelper;
	private static MobileAnalyticsManager analytics;
	private static CognitoCachingCredentialsProvider credentialsProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.hideStatusBar = false;
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useImmersiveMode = true;

		// Do the stuff that initialize() would do for you
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		// Create a gameView and a bannerAd AdView
        View gameView = initializeForView(new OrangeRed(this), config);
        setupAds();
         
        // Define the layout
        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layout.addView(adView, params);
         
        setContentView(layout);
        
		//startAdvertising();
		
		if (gameHelper == null) {
			gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
			gameHelper.enableDebugLog(true);
		}
		gameHelper.setup(this);
		gameHelper.setMaxAutoSignInAttempts(0);
		setIdStrings();

		// Initialize the Amazon Cognito credentials provider
		CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
				getApplicationContext(), "us-east-1:e4a0b6db-e4ea-4dcb-b640-9662ad58ddb2",
				Regions.US_EAST_1 // Region
		);
		try {
			analytics = MobileAnalyticsManager.getOrCreateInstance(this.getApplicationContext(),
					"ab5a14c7225a4fee95048404208787c9", Regions.US_EAST_1,
					credentialsProvider); // Amazon Cognito Identity Pool ID
		} catch (InitializationException ex) {
			Log.e(this.getClass().getName(), "Failed to initialize Amazon Mobile Analytics", ex);
		}
	}

	public void setupAds() {
		adView = new AdView(this);
        adView.setVisibility(View.INVISIBLE);
        adView.setBackgroundColor(0xff000000); // black
        adView.setAdUnitId(AD_UNIT_ID);
        adView.setAdSize(AdSize.SMART_BANNER);
    }
	
	/*public void startAdvertising() {
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}*/

	public void showBanner() {

	    runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	adView.setVisibility(View.VISIBLE);
	            AdRequest.Builder builder = new AdRequest.Builder();
	            AdRequest ad = builder.build();
	            adView.loadAd(ad);
	        }
	    });
	}

	public void hideBanner() {
		runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	adView.setVisibility(View.INVISIBLE);
	        }
	    });
	}

	
	@Override
	public void onStart() {
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (analytics != null) {
			analytics.getSessionClient().pauseSession();
			analytics.getEventClient().submitEvents();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (analytics != null) {
			analytics.getSessionClient().resumeSession();
		}
	}

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
	}

	@Override
	public boolean getSignedInGPGS() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});

		} catch (final Exception ex) {
		}
	}

	@Override
	public void submitScoreGPGS(int score) {
		Games.Leaderboards.submitScore(gameHelper.getApiClient(), GameIds.leaderboardId, score);
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
	}

	@Override
	public void getLeaderboardGPGS() {
		if (gameHelper.isSignedIn()) {
			startActivityForResult(
					Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), GameIds.leaderboardId), 100);
		} else if (!gameHelper.isConnecting()) {
			loginGPGS();
		}
	}

	@Override
	public void getAchievementsGPGS() {
		if (gameHelper.isSignedIn()) {
			startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
		} else if (!gameHelper.isConnecting()) {
			loginGPGS();
		}
	}

	public void setIdStrings() {
		GameIds.appId = getString(R.string.app_id);
		GameIds.leaderboardId = getString(R.string.leaderboard_high_scores);
		GameIds.achLevel2Id = getString(R.string.achievement_level_2);
		GameIds.achLevel4Id = getString(R.string.achievement_level_4);
		GameIds.achLevel6Id = getString(R.string.achievement_level_6);
		GameIds.achLevel8Id = getString(R.string.achievement_level_8);
		GameIds.achLevel10Id = getString(R.string.achievement_level_10);
		GameIds.ach100Id = getString(R.string.achievement_100);
		GameIds.ach500Id = getString(R.string.achievement_500);
		GameIds.ach2000Id = getString(R.string.achievement_2000);
		GameIds.ach10000Id = getString(R.string.achievement_10000);
		GameIds.ach100000Id = getString(R.string.achievement_100000);

	}

	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
	}
	
	public void analyticsGameOver(int score, int level, int gamesPlayed) {

		double scoreDouble=(double)score, levelDouble=(double)level, gamesPlayedDouble=(double)gamesPlayed;
		String levelString=Integer.toString(level), sessionsString=Integer.toString(level);
	    //Create a Level Complete event with some attributes and metrics(measurements)
	    //Attributes and metrics can be added using with statements
	    AnalyticsEvent levelCompleteEvent = analytics.getEventClient().createEvent("LevelComplete")
	            .withMetric("Score", scoreDouble)
	            .withMetric("Level", levelDouble)
	            .withMetric("Total Game Sessions", Settings.getTotGameSessionsDouble())
	            .withMetric("Games Played this Load", gamesPlayedDouble)
	            .withAttribute("Level String", levelString)
	            .withAttribute("Total Sessions String", sessionsString);
	    
	    //Record the Level Complete event
	    analytics.getEventClient().recordEvent(levelCompleteEvent);
	}
}