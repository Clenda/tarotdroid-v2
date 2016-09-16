package org.nla.tarotdroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;

import org.nla.tarotdroid.biz.Bet;
import org.nla.tarotdroid.biz.Chelem;
import org.nla.tarotdroid.biz.King;
import org.nla.tarotdroid.biz.Result;
import org.nla.tarotdroid.biz.Team;
import org.nla.tarotdroid.dal.IDalService;
import org.nla.tarotdroid.dal.sql.SqliteDalService;
import org.nla.tarotdroid.helpers.BluetoothHelper;
import org.nla.tarotdroid.model.TarotDroidUser;
import org.nla.tarotdroid.ui.constants.PreferenceConstants;
import org.nla.tarotdroid.ui.tasks.LoadDalTask;

import java.util.Map;
import java.util.UUID;

import static com.google.common.collect.Maps.newHashMap;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public abstract class BaseApp extends MultiDexApplication implements ITarotDroidApp {
	
    private IDalService dalService;
    private AppParams appParams;
    private BluetoothHelper bluetoothHelper;
    private boolean appInDebugMode;
    private String versionName;
    private String packageName;
    private LoadDalTask loadDalTask;
    private long lastLaunchTimestamp;
    private TarotDroidUser tarotDroidUser;
    private Map<String, Integer> notificationIds;
    private ApplicationComponent component;

    public static BaseApp get(final Context context) {
        return (BaseApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.setApplication(this);
        this.initializeDalService();
        this.retrieveInfosFromManifest();
        this.initializeBiznessStrings();
        this.initializeUser();
        this.setLastLaunchTimestamp();
        this.notificationIds = newHashMap();

        component = ApplicationComponent.Initializer.init(this);
        component.inject(this);
    }

    public ApplicationComponent getComponent() {
        return component;
    }
	
	@Override
	public UUID getUuid() {
		return UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	}
	
	@Override
	public String getServiceName() {
		return "TarotDroidService";
	}
	
	public String getCloudDns() {
        if (BuildConfig.IS_IN_DEBUG_MODE) {
            return this.getString(R.string.dnsTarotDroidDevCloud);
		}
		else {
			return this.getString(R.string.dnsTarotDroidCloud);
		}
	}

    public String getFacebookCloudUrl() {
        if (BuildConfig.IS_IN_DEBUG_MODE) {
            return this.getString(R.string.urlTarotDroidFacebookDevCloud);
		}
		else {
			return this.getString(R.string.urlTarotDroidFacebookCloud);
		}
	}

//	public String getFacebookAppUrl() {
//        if (BuildConfig.IS_IN_DEBUG_MODE) {
//            return this.getString(R.string.urlTarotDroidFacebookDevApp);
//		}
//		else {
//			return this.getString(R.string.urlTarotDroidFacebookApp);
//		}
//	}
	
    private void setLastLaunchTimestamp() {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	this.lastLaunchTimestamp = preferences.getLong(PreferenceConstants.PrefDateLastLaunch, 0);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PreferenceConstants.PrefDateLastLaunch, System.currentTimeMillis());
        editor.commit();
    }
    
    @Override
    public long getLastLaunchTimestamp() {
        return this.lastLaunchTimestamp;
    }
    
    private void retrieveInfosFromManifest() {
    	try {
			PackageManager manager = this.getPackageManager();
			PackageInfo packageInfo = manager.getPackageInfo(this.getPackageName(), 0);
			this.versionName = packageInfo.versionName;
			this.packageName = packageInfo.packageName;
			ApplicationInfo appInfo = packageInfo.applicationInfo;
			this.appInDebugMode = (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE;

		}
    	catch (NameNotFoundException e) {
    		this.appInDebugMode = false;
		}
	}
    
    private void initializeBiznessStrings() {
    	Bet.PETITE.setLabel(this.getString(R.string.petiteDescription));
		Bet.PRISE.setLabel(this.getString(R.string.priseDescription));
		Bet.GARDE.setLabel(this.getString(R.string.gardeDescription));
		Bet.GARDESANS.setLabel(this.getString(R.string.gardeSansDescription));
		Bet.GARDECONTRE.setLabel(this.getString(R.string.gardeContreDescription));

		King.HEART.setLabel(this.getString(R.string.lblHeartsColor));
		King.DIAMOND.setLabel(this.getString(R.string.lblDiamondsColor));
		King.SPADE.setLabel(this.getString(R.string.lblSpadesColor));
		King.CLUB.setLabel(this.getString(R.string.lblClubsColor));

		Chelem.CHELEM_ANOUNCED_AND_SUCCEEDED.setLabel(this.getString(R.string.lblAnnouncedAndSucceededChelem));
		Chelem.CHELEM_ANOUNCED_AND_FAILED.setLabel(this.getString(R.string.lblAnnouncedAndFailedChelem));
		Chelem.CHELEM_NOT_ANOUNCED_BUT_SUCCEEDED.setLabel(this.getString(R.string.lblNotAnnouncedButSucceededChelem));

		Team.DEFENSE_TEAM.setLabel(this.getString(R.string.lblDefenseTeam));
		Team.LEADING_TEAM.setLabel(this.getResources().getString(R.string.lblLeadingTeam));
		Team.BOTH_TEAMS.setLabel(this.getResources().getString(R.string.lblBothTeams));

		Result.SUCCESS.setLabel(this.getString(R.string.lblSuccesses));
		Result.FAILURE.setLabel(this.getString(R.string.lblFailures));
    }

    private void initializeUser() {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	String prefTarotDroidUser = preferences.getString(PreferenceConstants.PrefTarotDroidUser, null);
    	if (prefTarotDroidUser != null) {
    		this.tarotDroidUser = new Gson().fromJson(prefTarotDroidUser, TarotDroidUser.class);
    	}
    }

    @Override
    public String getAppVersion() {
    	return this.versionName;
    }
    
    @Override
    public String getAppPackage() {
    	return this.packageName;
    }
    
    @Override
    public IDalService getDalService() {
    	return this.dalService;
    }
    
    @Override
    public void setDalService(final IDalService dalService) {
    	this.dalService = dalService;
    }

    @Override
    public LoadDalTask getLoadDalTask() {
    	return this.loadDalTask;
    }

    @Override
    public BluetoothHelper getBluetoothHelper() {
    	if (this.bluetoothHelper == null) {
    		this.bluetoothHelper = new BluetoothHelper(this);
    	}
    	return this.bluetoothHelper;
    }
    
    private void initializeDalService() {
		this.loadDalTask = new LoadDalTask(this);
		this.loadDalTask.execute();
    }

//    @Override
//	public GameSetParameters initializeGameSetParameters() {
//		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//		// initiailizes game set params
//		GameSetParameters gameSetParameters = new GameSetParameters();
//		gameSetParameters.setPetiteBasePoints(preferences.getInt(PreferenceConstants.PrefPetitePoints, 10));
//		gameSetParameters.setPetiteRate(preferences.getInt(PreferenceConstants.PrefPetiteRate, 1));
//		gameSetParameters.setPriseBasePoints(preferences.getInt(PreferenceConstants.PrefPrisePoints, 25));
//		gameSetParameters.setPriseRate(preferences.getInt(PreferenceConstants.PrefPriseRate, 1));
//		gameSetParameters.setGardeBasePoints(preferences.getInt(PreferenceConstants.PrefGardePoints, 50));
//		gameSetParameters.setGardeRate(preferences.getInt(PreferenceConstants.PrefGardeRate, 2));
//		gameSetParameters.setGardeSansBasePoints(preferences.getInt(PreferenceConstants.PrefGardeSansPoints, 100));
//		gameSetParameters.setGardeSansRate(preferences.getInt(PreferenceConstants.PrefGardeSansRate, 4));
//		gameSetParameters.setGardeContreBasePoints(preferences.getInt(PreferenceConstants.PrefGardeContrePoints, 150));
//		gameSetParameters.setGardeContreRate(preferences.getInt(PreferenceConstants.PrefGardeContreRate, 6));
//		gameSetParameters.setPoigneePoints(preferences.getInt(PreferenceConstants.PrefPoigneePoints, 20));
//		gameSetParameters.setDoublePoigneePoints(preferences.getInt(PreferenceConstants.PrefDoublePoigneePoints, 30));
//		gameSetParameters.setTriplePoigneePoints(preferences.getInt(PreferenceConstants.PrefTriplePoigneePoints, 40));
//		gameSetParameters.setMiseryPoints(preferences.getInt(PreferenceConstants.PrefMiseryPoints, 10));
//		gameSetParameters.setKidAtTheEndPoints(preferences.getInt(PreferenceConstants.PrefKidAtTheEndPoints, 10));
//		gameSetParameters.setAnnouncedAndSucceededChelemPoints(preferences.getInt(PreferenceConstants.PrefAnnouncedAndSucceededChelemPoints, 400));
//		gameSetParameters.setAnnouncedAndFailedChelemPoints(preferences.getInt(PreferenceConstants.PrefAnnouncedAndFailedChelemPoints, -200));
//		gameSetParameters.setNotAnnouncedButSucceededChelemPoints(preferences.getInt(PreferenceConstants.PrefNotAnnouncedButSucceededChelemPoints, 200));
//		gameSetParameters.setBelgianBaseStepPoints(preferences.getInt(PreferenceConstants.PrefBelgianStepPoints, 100));
//
//		// initializes app params
//		this.initializeAppParams();
//
//		return gameSetParameters;
//	}
	
    private void initializeAppParams() {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	this.appParams = new AppParams();

    	this.appParams.setBelgianGamesAllowed(preferences.getBoolean(PreferenceConstants.PrefAreBelgianGamesAllowed, false));
    	this.appParams.setPenaltyGamesAllowed(preferences.getBoolean(PreferenceConstants.PrefArePenaltyGamesAllowed, false));
    	this.appParams.setPassedGamesAllowed(preferences.getBoolean(PreferenceConstants.PrefArePassedGamesAllowed, true));
    	this.appParams.setDisplayNextDealer(preferences.getBoolean(PreferenceConstants.PrefDisplayNextDealer, true));
		this.appParams.setPriseAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsPriseAuthorized, true));
		this.appParams.setPetiteAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsPetiteAuthorized, false));
		this.appParams.setDeadPlayerAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsOneDeadPlayerAuthorized, false));
		this.appParams.setDeadPlayerAutomaticallySelected(preferences.getBoolean(PreferenceConstants.PrefIsDeadPlayerAutomaticallySelected, true));
		this.appParams.setMiseryAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsMiseryAuthorized, false));

		// display preferences
		this.appParams.setDisplayGamesInReverseOrder(preferences.getBoolean(PreferenceConstants.PrefDisplayGamesInReverseOrder, true));
		this.appParams.setDisplayGlobalScoresForEachGame(preferences.getBoolean(PreferenceConstants.PrefDisplayGlobalScoresForEachGame, false));
		this.appParams.setKeepScreenOn(preferences.getBoolean(PreferenceConstants.PrefKeepScreenOn, true));

		// dev preferences
		this.appParams.setDevSimulationMode(preferences.getBoolean(PreferenceConstants.PrefIsSimulationMode, false));
		this.appParams.setDevGameSetCount(preferences.getInt(PreferenceConstants.PrefDevGameSetCount, 5));
		this.appParams.setDevMaxGameCount(preferences.getInt(PreferenceConstants.PrefDevMaxGameCount, 15));
    }
    
	public TarotDroidUser getTarotDroidUser() {
		return this.tarotDroidUser;
	}

	public void setTarotDroidUser(TarotDroidUser tarotDroidUser) {
		this.tarotDroidUser = tarotDroidUser;
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = preferences.edit();
		if (tarotDroidUser == null) {
			editor.putString(PreferenceConstants.PrefTarotDroidUser, null);
		}
		else {
			editor.putString(PreferenceConstants.PrefTarotDroidUser, new Gson().toJson(tarotDroidUser));
		}
		editor.commit();
	}

	@Override
	public Map<String, Integer> getNotificationIds() {
		return this.notificationIds;
	}

	@Override
	public SQLiteDatabase getSQLiteDatabase() {
		return SqliteDalService.getSqliteDatabase(this);
	}
}