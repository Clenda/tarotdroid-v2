/*
	This file is part of the Android application TarotDroid.
 	
	TarotDroid is free software: you can redistribute it and/or modify
 	it under the terms of the GNU General Public License as published by
 	the Free Software Foundation, either version 3 of the License, or
 	(at your option) any later version.
 	
 	TarotDroid is distributed in the hope that it will be useful,
 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 	GNU General Public License for more details.
 	
 	You should have received a copy of the GNU General Public License
 	along with TarotDroid. If not, see <http://www.gnu.org/licenses/>.
*/
package org.nla.tarotdroid.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import org.nla.tarotdroid.R;

// TODO Properly implement logic
public class MainPreferencesActivity
		extends AppCompatActivity
		implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_preferences);

		if (savedInstanceState == null) {
			// Create the fragment only when the activity is created for the first time.
			// ie. not after orientation changes
			Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                    TabGameSetPreferencesFragment.FRAGMENT_TAG);
			if (fragment == null) {
				fragment = new MainPreferencesFragment();
			}

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.fragment_container, fragment, TabGameSetPreferencesFragment.FRAGMENT_TAG);
			ft.commit();
		}
	}

	@Override
	public boolean onPreferenceStartScreen(PreferenceFragmentCompat preferenceFragmentCompat,
										   PreferenceScreen preferenceScreen) {
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

//	private OnSharedPreferenceChangeListener listener;
//	private SharedPreferences preferences;
//
//	@SuppressWarnings("deprecation")
//	@Override
//    public void onCreate(final Bundle savedInstanceState) {
//        try {
//            super.onCreate(savedInstanceState);
//            this.auditEvent();
//
//        	if ((AppContext.getApplication().isAppInDebugMode())) {
//        	    MainPreferencesActivity.this.addPreferencesFromResource(R.layout.main_dashboard_preferences_debug);
//        	}
//        	else {
//        	    MainPreferencesActivity.this.addPreferencesFromResource(R.layout.main_dashboard_preferences);
//        	}
//
//        	this.preferences = PreferenceManager.getDefaultSharedPreferences(MainPreferencesActivity.this.getBaseContext());
//        	this.listener = new OnSharedPreferenceChangeListener() {
//
//				/* (non-Javadoc)
//				 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
//				 */
//				@Override
//				public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
//
//					// dev instances to change
//					if (key.equals(PreferenceConstants.PrefDevMaxGameCount)) {
//						AppContext.getApplication().getAppParams().setDevMaxGameCount(sharedPreferences.getInt(PreferenceConstants.PrefDevMaxGameCount, 20));
//					}
//					else if (key.equals(PreferenceConstants.PrefDevGameSetCount)) {
//						AppContext.getApplication().getAppParams().setDevGameSetCount(sharedPreferences.getInt(PreferenceConstants.PrefDevGameSetCount, 20));
//					}
//				}
//			};
//			this.preferences.registerOnSharedPreferenceChangeListener(this.listener);
//        }
//        catch (Exception e) {
//        	AuditHelper.auditError(ErrorTypes.mainPreferencesActivityError, e, this);
//        }
//    }
//
//	@Override
//	protected void onStart() {
//		super.onStart();
//		AuditHelper.auditSession(this);
//	}
//
//	private void auditEvent() {
//		AuditHelper.auditEvent(AuditHelper.EventTypes.displayMainPreferencePage);
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		this.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		this.getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
//
//		for (String key : this.getPreferenceScreen().getSharedPreferences().getAll().keySet()) {
//			if (this.findPreference(key) instanceof EditTextPreference) {
//				EditTextPreference pref = (EditTextPreference)this.findPreference(key);
//				pref.setSummary(pref.getText());
//			}
//		}
//
//	}
//
//	@Override
//	public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
//		// get associated preference
//		if (this.findPreference(key) instanceof EditTextPreference) {
//			EditTextPreference pref = (EditTextPreference)this.findPreference(key);
//			pref.setSummary(pref.getText());
//		}
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		this.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
//	}
}
