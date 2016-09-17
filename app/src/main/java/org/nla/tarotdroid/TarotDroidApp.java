package org.nla.tarotdroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.nla.tarotdroid.biz.Bet;
import org.nla.tarotdroid.biz.Chelem;
import org.nla.tarotdroid.biz.King;
import org.nla.tarotdroid.biz.Result;
import org.nla.tarotdroid.biz.Team;
import org.nla.tarotdroid.constants.PreferenceConstants;
import org.nla.tarotdroid.core.LoadDalTask;
import org.nla.tarotdroid.core.dal.IDalService;
import org.nla.tarotdroid.core.dal.sql.SqliteDalService;
import org.nla.tarotdroid.core.helpers.BluetoothHelper;

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
    private String packageName;
    private long lastLaunchTimestamp;
    private LoadDalTask loadDalTask;
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
        initializeBiznessStrings();
        setLastLaunchTimestamp();
        component = ApplicationComponent.Initializer.init(this);
        component.inject(this);
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    private void setLastLaunchTimestamp() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        lastLaunchTimestamp = preferences.getLong(PreferenceConstants.PrefDateLastLaunch, 0);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PreferenceConstants.PrefDateLastLaunch, System.currentTimeMillis());
        editor.commit();
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

    public IDalService getDalService() {
        return dalService;
    }

    public void setDalService(final IDalService dalService) {
        this.dalService = dalService;
    }

    public LoadDalTask getLoadDalTask() {
        return loadDalTask;
    }

    private void initializeDalService() {
        loadDalTask = new LoadDalTask(this);
        loadDalTask.execute();
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return SqliteDalService.getSqliteDatabase(this);
    }
}