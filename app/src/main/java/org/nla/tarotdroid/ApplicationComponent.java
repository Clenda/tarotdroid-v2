package org.nla.tarotdroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.ui.MainDashboardActivity;
import org.nla.tarotdroid.ui.PlayerSelectorActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {ApplicationModule.class}
)
public interface ApplicationComponent {

    void inject(BaseApp baseApp);

    void inject(MainDashboardActivity mainDashboardActivity);

    void inject(PlayerSelectorActivity playerSelectorActivity);

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