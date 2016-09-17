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

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
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

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

@ReportsCrashes(
        mailTo = "tarotdroid@gmail.com",
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.BRAND,
                ReportField.PHONE_MODEL,
                ReportField.ANDROID_VERSION,
                ReportField.SHARED_PREFERENCES,
                ReportField.CUSTOM_DATA,
                ReportField.STACK_TRACE,
                ReportField.LOGCAT
        },
        mode = ReportingInteractionMode.TOAST
//        resToastText = R.string.crash_toast_text,
//        resDialogText = R.string.crash_dialog_text,
//        resDialogIcon = android.R.drawable.ic_dialog_info,
//        resDialogTitle = R.string.crash_dialog_title,
//        resDialogOkToast = R.string.crash_dialog_ok_toast
)
public class TarotDroidApp extends MultiDexApplication {

    private static TarotDroidApp app;
    private IDalService dalService;
    private BluetoothHelper bluetoothHelper;
    private String versionName;
    private String packageName;
    private LoadDalTask loadDalTask;
    private long lastLaunchTimestamp;
    private TarotDroidUser tarotDroidUser;
    private Map<String, Integer> notificationIds;
    private ApplicationComponent component;

    public static TarotDroidApp get(final Context context) {
        return (TarotDroidApp) context.getApplicationContext();
    }

    // TODO See if we can remove this method
    public static TarotDroidApp get() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Fabric.with(this, new Crashlytics());
        initializeDalService();
        retrieveInfosFromManifest();
        initializeBiznessStrings();
        initializeUser();
        setLastLaunchTimestamp();
        notificationIds = new HashMap<>();

        component = ApplicationComponent.Initializer.init(this);
        component.inject(this);
    }

    public ApplicationComponent getComponent() {
        return component;
    }

//    public UUID getUuid() {
//        return UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//    }
//
//    public String getServiceName() {
//        return "TarotDroidService";
//    }

    public String getCloudDns() {
        if (BuildConfig.IS_IN_DEV_MODE) {
            return getString(R.string.dnsTarotDroidDevCloud);
        } else {
            return getString(R.string.dnsTarotDroidCloud);
        }
    }

    public String getFacebookCloudUrl() {
        if (BuildConfig.IS_IN_DEV_MODE) {
            return getString(R.string.urlTarotDroidFacebookDevCloud);
        } else {
            return getString(R.string.urlTarotDroidFacebookCloud);
        }
    }

    private void setLastLaunchTimestamp() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        lastLaunchTimestamp = preferences.getLong(PreferenceConstants.PrefDateLastLaunch, 0);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PreferenceConstants.PrefDateLastLaunch, System.currentTimeMillis());
        editor.commit();
    }

    public long getLastLaunchTimestamp() {
        return lastLaunchTimestamp;
    }

    private void retrieveInfosFromManifest() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            packageName = packageInfo.packageName;
            ApplicationInfo appInfo = packageInfo.applicationInfo;
        } catch (NameNotFoundException e) {

        }
    }

    private void initializeBiznessStrings() {
        Bet.PETITE.setLabel(getString(R.string.petiteDescription));
        Bet.PRISE.setLabel(getString(R.string.priseDescription));
        Bet.GARDE.setLabel(getString(R.string.gardeDescription));
        Bet.GARDESANS.setLabel(getString(R.string.gardeSansDescription));
        Bet.GARDECONTRE.setLabel(getString(R.string.gardeContreDescription));

        King.HEART.setLabel(getString(R.string.lblHeartsColor));
        King.DIAMOND.setLabel(getString(R.string.lblDiamondsColor));
        King.SPADE.setLabel(getString(R.string.lblSpadesColor));
        King.CLUB.setLabel(getString(R.string.lblClubsColor));

        Chelem.CHELEM_ANOUNCED_AND_SUCCEEDED.setLabel(getString(R.string.lblAnnouncedAndSucceededChelem));
        Chelem.CHELEM_ANOUNCED_AND_FAILED.setLabel(getString(R.string.lblAnnouncedAndFailedChelem));
        Chelem.CHELEM_NOT_ANOUNCED_BUT_SUCCEEDED.setLabel(getString(R.string.lblNotAnnouncedButSucceededChelem));

        Team.DEFENSE_TEAM.setLabel(getString(R.string.lblDefenseTeam));
        Team.LEADING_TEAM.setLabel(getResources().getString(R.string.lblLeadingTeam));
        Team.BOTH_TEAMS.setLabel(getResources().getString(R.string.lblBothTeams));

        Result.SUCCESS.setLabel(getString(R.string.lblSuccesses));
        Result.FAILURE.setLabel(getString(R.string.lblFailures));
    }

    private void initializeUser() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String prefTarotDroidUser = preferences.getString(PreferenceConstants.PrefTarotDroidUser,
                                                          null);
        if (prefTarotDroidUser != null) {
            tarotDroidUser = new Gson().fromJson(prefTarotDroidUser, TarotDroidUser.class);
        }
    }

//    public String getAppVersion() {
//        return versionName;
//    }

    public String getAppPackage() {
        return packageName;
    }

    public IDalService getDalService() {
        return dalService;
    }

    public void setDalService(final IDalService dalService) {
        this.dalService = dalService;
    }

    public LoadDalTask getLoadDalTask() {
        return loadDalTask;
    }

    public BluetoothHelper getBluetoothHelper() {
        if (bluetoothHelper == null) {
            bluetoothHelper = new BluetoothHelper(this);
        }
        return bluetoothHelper;
    }

    private void initializeDalService() {
        loadDalTask = new LoadDalTask(this);
        loadDalTask.execute();
    }

//    private void initializeAppParams() {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        appParams = new AppParams();
//
//        appParams.setBelgianGamesAllowed(preferences.getBoolean(PreferenceConstants.PrefAreBelgianGamesAllowed,
//                                                                     false));
//        appParams.setPenaltyGamesAllowed(preferences.getBoolean(PreferenceConstants.PrefArePenaltyGamesAllowed,
//                                                                     false));
//        appParams.setPassedGamesAllowed(preferences.getBoolean(PreferenceConstants.PrefArePassedGamesAllowed,
//                                                                    true));
//        appParams.setDisplayNextDealer(preferences.getBoolean(PreferenceConstants.PrefDisplayNextDealer,
//                                                                   true));
//        appParams.setPriseAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsPriseAuthorized,
//                                                                 true));
//        appParams.setPetiteAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsPetiteAuthorized,
//                                                                  false));
//        appParams.setDeadPlayerAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsOneDeadPlayerAuthorized,
//                                                                      false));
//        appParams.setDeadPlayerAutomaticallySelected(preferences.getBoolean(PreferenceConstants.PrefIsDeadPlayerAutomaticallySelected,
//                                                                                 true));
//        appParams.setMiseryAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsMiseryAuthorized,
//                                                                  false));
//
//        // display preferences
//        appParams.setDisplayGamesInReverseOrder(preferences.getBoolean(PreferenceConstants.PrefDisplayGamesInReverseOrder,
//                                                                            true));
//        appParams.setDisplayGlobalScoresForEachGame(preferences.getBoolean(PreferenceConstants.PrefDisplayGlobalScoresForEachGame,
//                                                                                false));
//        appParams.setKeepScreenOn(preferences.getBoolean(PreferenceConstants.PrefKeepScreenOn,
//                                                              true));
//
//        // dev preferences
//        appParams.setDevSimulationMode(preferences.getBoolean(PreferenceConstants.PrefIsSimulationMode,
//                                                                   false));
//        appParams.setDevGameSetCount(preferences.getInt(PreferenceConstants.PrefDevGameSetCount,
//                                                             5));
//        appParams.setDevMaxGameCount(preferences.getInt(PreferenceConstants.PrefDevMaxGameCount,
//                                                             15));
//    }

    public TarotDroidUser getTarotDroidUser() {
        return tarotDroidUser;
    }

    public void setTarotDroidUser(TarotDroidUser tarotDroidUser) {
        tarotDroidUser = tarotDroidUser;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Editor editor = preferences.edit();
        if (tarotDroidUser == null) {
            editor.putString(PreferenceConstants.PrefTarotDroidUser, null);
        } else {
            editor.putString(PreferenceConstants.PrefTarotDroidUser,
                             new Gson().toJson(tarotDroidUser));
        }
        editor.commit();
    }

    public Map<String, Integer> getNotificationIds() {
        return notificationIds;
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return SqliteDalService.getSqliteDatabase(this);
    }
}