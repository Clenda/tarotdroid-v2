package org.nla.tarotdroid.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;

import org.nla.tarotdroid.R;

public class TabGameSetPreferencesFragment extends PreferenceFragmentCompat {

    public static final String FRAGMENT_TAG = "tab_gameset_preference_fragment";

    public TabGameSetPreferencesFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the default white background in the view so as to avoid transparency
        view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.background_material_light));
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.tab_gameset_preferences, rootKey);
    }
}
