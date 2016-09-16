package org.nla.tarotdroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.ui.BaseGameActivity;
import org.nla.tarotdroid.ui.DisplayAndRemoveGameDialogActivity;
import org.nla.tarotdroid.ui.GameReadViewPagerActivity;
import org.nla.tarotdroid.ui.GameSetChartViewPagerActivity;
import org.nla.tarotdroid.ui.GameSetGamesFragment;
import org.nla.tarotdroid.ui.GameSetHistoryActivity;
import org.nla.tarotdroid.ui.MainDashboardActivity;
import org.nla.tarotdroid.ui.MainPreferencesActivity;
import org.nla.tarotdroid.ui.PlayerSelectorActivity;
import org.nla.tarotdroid.ui.StandardGameActivity;
import org.nla.tarotdroid.ui.TabGameSetActivity;
import org.nla.tarotdroid.ui.TabGameSetPreferencesActivity;
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

    void inject(BaseApp baseApp);

    void inject(MainDashboardActivity mainDashboardActivity);

    void inject(GameSetHistoryActivity gameSetHistoryActivity);

    void inject(TabGameSetActivity tabGameSetActivity);

    void inject(PlayerSelectorActivity playerSelectorActivity);

    void inject(DisplayAndRemoveGameDialogActivity displayAndRemoveGameDialogActivity);

    void inject(GameReadViewPagerActivity gameReadViewPagerActivity);

    void inject(GameSetChartViewPagerActivity gameSetChartViewPagerActivity);

    void inject(GameSetGamesFragment gameSetGamesFragment);

    void inject(StandardTarotGameRow standardTarotGameRow);

    void inject(StandardTarot5GameRow standardTarot5GameRow);

    void inject(BelgianTarotGameRow belgianTarotGameRow);

    void inject(PenaltyGameRow penaltyGameRow);

    void inject(StandardGameActivity standardGameActivity);

    void inject(BaseGameActivity baseGameActivity);

    void inject(MainPreferencesActivity mainPreferencesActivity);

    void inject(TabGameSetPreferencesActivity tabGameSetPreferencesActivity);

    Application application();

    Context context();

    SharedPreferences sharedPreferences();

    GameSetParameters gameSetParameters();

    AppParams appParams();

    final class Initializer {

        private Initializer() {
            // static class
        }

        static ApplicationComponent init(final BaseApp app) {
            return DaggerApplicationComponent
                    .builder()
                    .applicationModule(new ApplicationModule(app))
                    .build();
        }
    }
}