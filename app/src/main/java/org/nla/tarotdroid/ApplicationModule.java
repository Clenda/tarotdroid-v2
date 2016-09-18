package org.nla.tarotdroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.cloud.PlayerConverter;
import org.nla.tarotdroid.constants.PreferenceConstants;
import org.nla.tarotdroid.core.AppParams;
import org.nla.tarotdroid.core.dal.IDalService;
import org.nla.tarotdroid.core.dal.sql.SqliteDalService;
import org.nla.tarotdroid.core.helpers.AuditHelper;
import org.nla.tarotdroid.core.helpers.BluetoothHelper;
import org.nla.tarotdroid.core.helpers.LocalizationHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final TarotDroidApp app;

    ApplicationModule(final TarotDroidApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return app;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    SharedPreferences providesPreferences(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences;
    }

    @Provides
    @Singleton
    AppParams providesAppParams(SharedPreferences preferences) {
        AppParams appParams = new AppParams();
        // main preferences
        appParams.setBelgianGamesAllowed(preferences.getBoolean(PreferenceConstants.PrefAreBelgianGamesAllowed,
                                                                false));
        appParams.setPenaltyGamesAllowed(preferences.getBoolean(PreferenceConstants.PrefArePenaltyGamesAllowed,
                                                                false));
        appParams.setPassedGamesAllowed(preferences.getBoolean(PreferenceConstants.PrefArePassedGamesAllowed,
                                                               true));
        appParams.setDisplayNextDealer(preferences.getBoolean(PreferenceConstants.PrefDisplayNextDealer,
                                                              true));
        appParams.setPriseAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsPriseAuthorized,
                                                            true));
        appParams.setPetiteAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsPetiteAuthorized,
                                                             false));
        appParams.setDeadPlayerAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsOneDeadPlayerAuthorized,
                                                                 false));
        appParams.setDeadPlayerAutomaticallySelected(preferences.getBoolean(PreferenceConstants.PrefIsDeadPlayerAutomaticallySelected,
                                                                            true));
        appParams.setMiseryAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsMiseryAuthorized,
                                                             false));
        // display preferences
        appParams.setDisplayGamesInReverseOrder(preferences.getBoolean(PreferenceConstants.PrefDisplayGamesInReverseOrder,
                                                                       true));
        appParams.setDisplayGlobalScoresForEachGame(preferences.getBoolean(PreferenceConstants.PrefDisplayGlobalScoresForEachGame,
                                                                           false));
        appParams.setKeepScreenOn(preferences.getBoolean(PreferenceConstants.PrefKeepScreenOn,
                                                         true));
        // dev preferences
        appParams.setDevSimulationMode(preferences.getBoolean(PreferenceConstants.PrefIsSimulationMode,
                                                              false));
        appParams.setDevGameSetCount(preferences.getInt(PreferenceConstants.PrefDevGameSetCount,
                                                        5));
        appParams.setDevMaxGameCount(preferences.getInt(PreferenceConstants.PrefDevMaxGameCount,
                                                        15));
        return appParams;
    }

    @Provides
    @Singleton
    GameSetParameters providesGameSetParameters(SharedPreferences preferences) {
        GameSetParameters gameSetParameters = new GameSetParameters();
        gameSetParameters.setPetiteBasePoints(preferences.getInt(PreferenceConstants.PrefPetitePoints,
                                                                 10));
        gameSetParameters.setPetiteRate(preferences.getInt(PreferenceConstants.PrefPetiteRate, 1));
        gameSetParameters.setPriseBasePoints(preferences.getInt(PreferenceConstants.PrefPrisePoints,
                                                                25));
        gameSetParameters.setPriseRate(preferences.getInt(PreferenceConstants.PrefPriseRate, 1));
        gameSetParameters.setGardeBasePoints(preferences.getInt(PreferenceConstants.PrefGardePoints,
                                                                50));
        gameSetParameters.setGardeRate(preferences.getInt(PreferenceConstants.PrefGardeRate, 2));
        gameSetParameters.setGardeSansBasePoints(preferences.getInt(PreferenceConstants.PrefGardeSansPoints,
                                                                    100));
        gameSetParameters.setGardeSansRate(preferences.getInt(PreferenceConstants.PrefGardeSansRate,
                                                              4));
        gameSetParameters.setGardeContreBasePoints(preferences.getInt(PreferenceConstants.PrefGardeContrePoints,
                                                                      150));
        gameSetParameters.setGardeContreRate(preferences.getInt(PreferenceConstants.PrefGardeContreRate,
                                                                6));
        gameSetParameters.setPoigneePoints(preferences.getInt(PreferenceConstants.PrefPoigneePoints,
                                                              20));
        gameSetParameters.setDoublePoigneePoints(preferences.getInt(PreferenceConstants.PrefDoublePoigneePoints,
                                                                    30));
        gameSetParameters.setTriplePoigneePoints(preferences.getInt(PreferenceConstants.PrefTriplePoigneePoints,
                                                                    40));
        gameSetParameters.setMiseryPoints(preferences.getInt(PreferenceConstants.PrefMiseryPoints,
                                                             10));
        gameSetParameters.setKidAtTheEndPoints(preferences.getInt(PreferenceConstants.PrefKidAtTheEndPoints,
                                                                  10));
        gameSetParameters.setAnnouncedAndSucceededChelemPoints(preferences.getInt(
                PreferenceConstants.PrefAnnouncedAndSucceededChelemPoints,
                400));
        gameSetParameters.setAnnouncedAndFailedChelemPoints(preferences.getInt(PreferenceConstants.PrefAnnouncedAndFailedChelemPoints,
                                                                               -200));
        gameSetParameters.setNotAnnouncedButSucceededChelemPoints(preferences.getInt(
                PreferenceConstants.PrefNotAnnouncedButSucceededChelemPoints,
                200));
        gameSetParameters.setBelgianBaseStepPoints(preferences.getInt(PreferenceConstants.PrefBelgianStepPoints,
                                                                      100));
        return gameSetParameters;
    }

    @Provides
    @Singleton
    AuditHelper providesAuditHelper() {
        return new AuditHelper();
    }

    @Provides
    @Singleton
    LocalizationHelper providesLocalizationHelper(final Context context) {
        return new LocalizationHelper(context);
    }

    @Provides
    @Singleton
    BluetoothHelper providesBluetoothHelper(final Context context) {
        return new BluetoothHelper(context);
    }

    @Provides
    @Singleton
    IDalService providesDalService(final Context context) {
        IDalService dalService = new SqliteDalService(context);

        // TODO Very ugly code. TO BE REFACTORED !!!
        PlayerConverter.DAL_SERVICE = dalService;
        // TODO

        return dalService;
    }

}
