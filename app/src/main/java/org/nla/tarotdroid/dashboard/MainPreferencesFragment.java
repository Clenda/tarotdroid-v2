package org.nla.tarotdroid.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;

import org.nla.tarotdroid.BuildConfig;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.constants.PreferenceConstants;
import org.nla.tarotdroid.core.AppParams;

import javax.inject.Inject;

// TODO Check https://github.com/madlymad/PreferenceApp
public class MainPreferencesFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String FRAGMENT_TAG = "main_preference_fragment";

    @Inject protected AppParams appParams;

    public MainPreferencesFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TarotDroidApp.get(getContext()).getComponent().inject(this);
        // Set the default white background in the view so as to avoid transparency
        view.setBackgroundColor(ContextCompat.getColor(getContext(),
                                                       R.color.background_material_light));
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        if (BuildConfig.IS_IN_DEV_MODE) {
            setPreferencesFromResource(R.xml.main_dashboard_preferences_debug, rootKey);
        } else {
            setPreferencesFromResource(R.xml.main_dashboard_preferences, rootKey);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences()
                              .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences()
                              .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferenceConstants.PrefDevMaxGameCount)) {
            appParams.setDevMaxGameCount(sharedPreferences.getInt(PreferenceConstants.PrefDevMaxGameCount,
                                                                  20));
        } else if (key.equals(PreferenceConstants.PrefDevGameSetCount)) {
            appParams.setDevGameSetCount(sharedPreferences.getInt(PreferenceConstants.PrefDevGameSetCount,
                                                                  20));
        }
    }
}
