package org.nla.tarotdroid.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.core.BaseActivity;
import org.nla.tarotdroid.core.helpers.AuditHelper;
import org.nla.tarotdroid.gameset.TabGameSetPreferencesFragment;

public class MainPreferencesActivity
        extends BaseActivity
        implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Create the fragment only when the activity is created for the first time.
            // ie. not after orientation changes
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                    TabGameSetPreferencesFragment.FRAGMENT_TAG);
            if (fragment == null) {
                fragment = new MainPreferencesFragment();
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment, MainPreferencesFragment.FRAGMENT_TAG);
            ft.commit();
        }
    }

    @Override
    protected void inject() {
        TarotDroidApp.get(this).getComponent().inject(this);
    }

    @Override
    protected void auditEvent() {
        auditHelper.auditEvent(AuditHelper.EventTypes.displayMainPreferencePage);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main_preferences;
    }

    @Override
    public boolean onPreferenceStartScreen(
            PreferenceFragmentCompat preferenceFragmentCompat,
            PreferenceScreen preferenceScreen
    ) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MainPreferencesFragment fragment = new MainPreferencesFragment();
        Bundle args = new Bundle();
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.getKey());
        fragment.setArguments(args);
        ft.add(R.id.fragment_container, fragment, preferenceScreen.getKey());
        ft.addToBackStack(preferenceScreen.getKey());
        ft.commit();
        return true;
    }
}
