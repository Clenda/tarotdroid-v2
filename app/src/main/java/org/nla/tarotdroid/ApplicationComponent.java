package org.nla.tarotdroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.core.AppParams;
import org.nla.tarotdroid.core.dal.IDalService;
import org.nla.tarotdroid.core.helpers.AuditHelper;
import org.nla.tarotdroid.core.helpers.BluetoothHelper;
import org.nla.tarotdroid.core.helpers.LocalizationHelper;
import org.nla.tarotdroid.dashboard.MainDashboardActivity;
import org.nla.tarotdroid.dashboard.MainPreferencesActivity;
import org.nla.tarotdroid.dashboard.NewGameSetDashboardActivity;
import org.nla.tarotdroid.dashboard.PlayerSelectorActivity;
import org.nla.tarotdroid.gameset.BaseGameActivity;
import org.nla.tarotdroid.gameset.DisplayAndRemoveGameDialogActivity;
import org.nla.tarotdroid.gameset.GameReadViewPagerActivity;
import org.nla.tarotdroid.gameset.GameSetChartViewPagerActivity;
import org.nla.tarotdroid.gameset.GameSetGamesFragment;
import org.nla.tarotdroid.gameset.GameSetSynthesisFragment;
import org.nla.tarotdroid.gameset.StandardGameActivity;
import org.nla.tarotdroid.gameset.TabGameSetActivity;
import org.nla.tarotdroid.gameset.TabGameSetPreferencesActivity;
import org.nla.tarotdroid.gameset.charts.BetsStatsChartFragment;
import org.nla.tarotdroid.gameset.charts.KingsStatsChartFragment;
import org.nla.tarotdroid.gameset.charts.SuccessesStatsChartFragment;
import org.nla.tarotdroid.gameset.controls.BelgianTarotGameRow;
import org.nla.tarotdroid.gameset.controls.PenaltyGameRow;
import org.nla.tarotdroid.gameset.controls.StandardTarot5GameRow;
import org.nla.tarotdroid.gameset.controls.StandardTarotGameRow;
import org.nla.tarotdroid.history.GameSetHistoryActivity;
import org.nla.tarotdroid.players.PlayerListActivity;
import org.nla.tarotdroid.players.PlayerStatisticsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {ApplicationModule.class}
)
public interface ApplicationComponent {

    void inject(TarotDroidApp tarotDroidApp);

    void inject(MainDashboardActivity mainDashboardActivity);

    void inject(GameSetHistoryActivity gameSetHistoryActivity);

    void inject(TabGameSetActivity tabGameSetActivity);

    void inject(PlayerSelectorActivity playerSelectorActivity);

    void inject(DisplayAndRemoveGameDialogActivity displayAndRemoveGameDialogActivity);

    void inject(GameReadViewPagerActivity gameReadViewPagerActivity);

    void inject(GameSetChartViewPagerActivity gameSetChartViewPagerActivity);

    void inject(GameSetGamesFragment gameSetGamesFragment);

    void inject(GameSetSynthesisFragment gameSetSynthesisFragment);

    void inject(StandardTarotGameRow standardTarotGameRow);

    void inject(StandardTarot5GameRow standardTarot5GameRow);

    void inject(BelgianTarotGameRow belgianTarotGameRow);

    void inject(PenaltyGameRow penaltyGameRow);

    void inject(StandardGameActivity standardGameActivity);

    void inject(BaseGameActivity baseGameActivity);

    void inject(MainPreferencesActivity mainPreferencesActivity);

    void inject(TabGameSetPreferencesActivity tabGameSetPreferencesActivity);

    void inject(PlayerListActivity playerListActivity);

    void inject(PlayerStatisticsActivity playerStatisticsActivity);

    void inject(SuccessesStatsChartFragment successesStatsChartFragment);

    void inject(NewGameSetDashboardActivity newGameSetDashboardActivity);

    void inject(KingsStatsChartFragment kingsStatsChartFragment);

    void inject(BetsStatsChartFragment betsStatsChartFragment);

    Application application();

    Context context();

    SharedPreferences sharedPreferences();

    GameSetParameters gameSetParameters();

    AppParams appParams();

    AuditHelper auditHelper();

    LocalizationHelper localizationHelper();

    BluetoothHelper bluetoothHelper();

    IDalService dalService();

    final class Initializer {

        private Initializer() {
            // static class
        }

        static ApplicationComponent init(final TarotDroidApp app) {
            return DaggerApplicationComponent
                    .builder()
                    .applicationModule(new ApplicationModule(app))
                    .build();
        }
    }
}