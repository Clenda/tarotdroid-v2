package org.nla.tarotdroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.LocalizationHelper;
import org.nla.tarotdroid.helpers.UIHelper;
import org.nla.tarotdroid.ui.BaseGameActivity;
import org.nla.tarotdroid.ui.DisplayAndRemoveGameDialogActivity;
import org.nla.tarotdroid.ui.GameReadViewPagerActivity;
import org.nla.tarotdroid.ui.GameSetChartListActivity;
import org.nla.tarotdroid.ui.GameSetChartViewPagerActivity;
import org.nla.tarotdroid.ui.GameSetGamesFragment;
import org.nla.tarotdroid.ui.GameSetHistoryActivity;
import org.nla.tarotdroid.ui.GameSetSynthesisFragment;
import org.nla.tarotdroid.ui.MainDashboardActivity;
import org.nla.tarotdroid.ui.MainPreferencesActivity;
import org.nla.tarotdroid.ui.NewGameSetDashboardActivity;
import org.nla.tarotdroid.ui.PlayerListActivity;
import org.nla.tarotdroid.ui.PlayerSelectorActivity;
import org.nla.tarotdroid.ui.PlayerStatisticsActivity;
import org.nla.tarotdroid.ui.StandardGameActivity;
import org.nla.tarotdroid.ui.TabGameSetActivity;
import org.nla.tarotdroid.ui.TabGameSetPreferencesActivity;
import org.nla.tarotdroid.ui.charts.BetsStatsChartFragment;
import org.nla.tarotdroid.ui.charts.KingsStatsChartFragment;
import org.nla.tarotdroid.ui.charts.SuccessesStatsChartFragment;
import org.nla.tarotdroid.ui.controls.BelgianTarotGameRow;
import org.nla.tarotdroid.ui.controls.PenaltyGameRow;
import org.nla.tarotdroid.ui.controls.StandardTarot5GameRow;
import org.nla.tarotdroid.ui.controls.StandardTarotGameRow;

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

    void inject(GameSetChartListActivity gameSetChartListActivity);

    void inject(KingsStatsChartFragment kingsStatsChartFragment);

    void inject(BetsStatsChartFragment betsStatsChartFragment);

    Application application();

    Context context();

    SharedPreferences sharedPreferences();

    GameSetParameters gameSetParameters();

    AppParams appParams();

    AuditHelper auditHelper();

    UIHelper uIHelper();

    LocalizationHelper localizationHelper();

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