package org.nla.tarotdroid.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.app.AppContext;

// TODO Check https://github.com/madlymad/PreferenceApp
public class MainPreferencesFragment extends PreferenceFragmentCompat {

    public static final String FRAGMENT_TAG = "main_preference_fragment";

    public MainPreferencesFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the default white background in the view so as to avoid transparency
        view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.background_material_light));
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        if ((AppContext.getApplication().isAppInDebugMode())) {
            setPreferencesFromResource(R.xml.main_dashboard_preferences_debug, rootKey);
        }
        else {
            setPreferencesFromResource(R.xml.main_dashboard_preferences, rootKey);
        }
    }
}
