package org.nla.tarotdroid.ui;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import org.nla.tarotdroid.BaseApp;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.AuditHelper.ErrorTypes;

import javax.inject.Inject;

// TODO Properly implement
public class TabGameSetPreferencesActivity
        extends BaseActivity
        implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    @Inject SharedPreferences preferences;
    private OnSharedPreferenceChangeListener listener;
	
	@Override
    public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Create the fragment only when the activity is created for the first time.
            // ie. not after orientation changes
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                    TabGameSetPreferencesFragment.FRAGMENT_TAG);
            if (fragment == null) {
                fragment = new TabGameSetPreferencesFragment();
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container,
                       fragment,
                       TabGameSetPreferencesFragment.FRAGMENT_TAG);
            ft.commit();
        }


		try {
//			// check params
//			checkArgument(this.getIntent().getExtras().containsKey(ActivityParams.PARAM_GAMESET_ID), "Game set id must be provided");
//			this.gameSet = AppContext.getApplication().getDalService().getGameSetById(this.getIntent().getExtras().getLong(ActivityParams.PARAM_GAMESET_ID));
            
//			// check params
//			Bundle args = this.getIntent().getExtras();
//			if (args.containsKey(ActivityParams.PARAM_GAMESET_ID)) {
//				this.gameSet = AppContext.getApplication().getDalService().getGameSetById(args.getLong(ActivityParams.PARAM_GAMESET_ID));
//			}
//			else if (args.containsKey(ActivityParams.PARAM_GAMESET_SERIALIZED)) {
//				//this.gameSet = UIHelper.deserializeGameSet(args.getString(ActivityParams.PARAM_GAMESET_SERIALIZED));
//				this.gameSet = (GameSet)args.getSerializable(ActivityParams.PARAM_GAMESET_SERIALIZED);
//			}
//			else {
//				throw new IllegalArgumentException("Game set id or serialized game set must be provided");
//			}
//        	this.addPreferencesFromResource(R.layout.tablegameset_preferences);
//
//        	// remove "misery allowed" pref if game is 5 player style
//        	if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
//        		CheckBoxPreference prefMiseryAllowedAt3Or5 = (CheckBoxPreference)this.findPreference(PreferenceConstants.PrefIsMiseryAuthorized);
//        		prefMiseryAllowedAt3Or5.setChecked(false);
//        		prefMiseryAllowedAt3Or5.setEnabled(false);
//        	}
//
//        	this.listener = new OnSharedPreferenceChangeListener() {
//
//        		/* (non-Javadoc)
//        		 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
//        		 */
//        		@Override
//				public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
//					if (key.equals(PreferenceConstants.PrefDisplayGamesInReverseOrder)) {
//						appParams.setDisplayGamesInReverseOrder(sharedPreferences.getBoolean(PreferenceConstants.PrefDisplayGamesInReverseOrder, true));
//					}
//					else if (key.equals(PreferenceConstants.PrefDisplayGlobalScoresForEachGame)) {
//						appParams.setDisplayGlobalScoresForEachGame(sharedPreferences.getBoolean(PreferenceConstants.PrefDisplayGlobalScoresForEachGame, true));
//					}
//					else if (key.equals(PreferenceConstants.PrefKeepScreenOn)) {
//						appParams.setKeepScreenOn(sharedPreferences.getBoolean(PreferenceConstants.PrefKeepScreenOn, true));
//					}
//					else if (key.equals(PreferenceConstants.PrefIsPetiteAuthorized)) {
//						appParams.setPetiteAuthorized(sharedPreferences.getBoolean(PreferenceConstants.PrefIsPetiteAuthorized, true));
//					}
//					else if (key.equals(PreferenceConstants.PrefIsPriseAuthorized)) {
//						appParams.setPriseAuthorized(sharedPreferences.getBoolean(PreferenceConstants.PrefIsPriseAuthorized, true));
//					}
//					else if (key.equals(PreferenceConstants.PrefAreBelgianGamesAllowed)) {
//						appParams.setBelgianGamesAllowed(sharedPreferences.getBoolean(PreferenceConstants.PrefAreBelgianGamesAllowed, true));
//					}
//					else if (key.equals(PreferenceConstants.PrefArePenaltyGamesAllowed)) {
//						appParams.setPenaltyGamesAllowed(sharedPreferences.getBoolean(PreferenceConstants.PrefArePenaltyGamesAllowed, true));
//					}
//					else if (key.equals(PreferenceConstants.PrefArePassedGamesAllowed)) {
//						appParams.setPassedGamesAllowed(sharedPreferences.getBoolean(PreferenceConstants.PrefArePassedGamesAllowed, true));
//					}
//					else if (key.equals(PreferenceConstants.PrefIsMiseryAuthorized)) {
//						appParams.setMiseryAuthorized(sharedPreferences.getBoolean(PreferenceConstants.PrefIsMiseryAuthorized, false));
//					}
//					else if (key.equals(PreferenceConstants.PrefDisplayNextDealer)) {
//						appParams.setDisplayNextDealer(sharedPreferences.getBoolean(PreferenceConstants.PrefDisplayNextDealer, true));
//					}
//				}
//			};
//			this.preferences.registerOnSharedPreferenceChangeListener(this.listener);
		} 
        catch (Exception e) {
        	AuditHelper.auditError(ErrorTypes.tabGameSetPreferencesActivityError, e, this);
        }
    }

	@Override
    protected void inject() {
        BaseApp.get(this).getComponent().inject(this);
    }

    @Override
    protected void auditEvent() {
        AuditHelper.auditEvent(AuditHelper.EventTypes.displayTabGameSetPreferencePage);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main_preferences;
    }

    @Override
    protected void onResume() {
		super.onResume();
//		// get preferences
//		EditTextPreference prefPetitePoints = (EditTextPreference)this.findPreference(PreferenceConstants.PrefPetitePoints);
//		EditTextPreference prefPetiteRate = (EditTextPreference)this.findPreference(PreferenceConstants.PrefPetiteRate);
//		EditTextPreference prefPrisePoints= (EditTextPreference)this.findPreference(PreferenceConstants.PrefPrisePoints);
//		EditTextPreference prefPriseRate = (EditTextPreference)this.findPreference(PreferenceConstants.PrefPriseRate);
//		EditTextPreference prefGardePoints = (EditTextPreference)this.findPreference(PreferenceConstants.PrefGardePoints);
//		EditTextPreference prefGardeRate = (EditTextPreference)this.findPreference(PreferenceConstants.PrefGardeRate);
//		EditTextPreference prefGardeSansPoints = (EditTextPreference)this.findPreference(PreferenceConstants.PrefGardeSansPoints);
//		EditTextPreference prefGardeSansRate = (EditTextPreference)this.findPreference(PreferenceConstants.PrefGardeSansRate);
//		EditTextPreference prefGardeContrePoints = (EditTextPreference)this.findPreference(PreferenceConstants.PrefGardeContrePoints);
//		EditTextPreference prefGardeContreRate = (EditTextPreference)this.findPreference(PreferenceConstants.PrefGardeContreRate);
//		EditTextPreference prefPoigneePoints = (EditTextPreference)this.findPreference(PreferenceConstants.PrefPoigneePoints);
//		EditTextPreference prefDoublePoigneePoints = (EditTextPreference)this.findPreference(PreferenceConstants.PrefDoublePoigneePoints);
//		EditTextPreference prefTriplePoigneePoints = (EditTextPreference)this.findPreference(PreferenceConstants.PrefTriplePoigneePoints);
//		EditTextPreference prefMiseryPoints = (EditTextPreference)this.findPreference(PreferenceConstants.PrefMiseryPoints);
//		EditTextPreference prefKidAtTheEndPoints = (EditTextPreference)this.findPreference(PreferenceConstants.PrefKidAtTheEndPoints);
//		EditTextPreference prefAnnouncedAndSucceededChelemPoints = (EditTextPreference)this.findPreference(PreferenceConstants.PrefAnnouncedAndSucceededChelemPoints);
//		EditTextPreference prefAnnouncedAndFailedChelemPoints = (EditTextPreference)this.findPreference(PreferenceConstants.PrefAnnouncedAndFailedChelemPoints);
//		EditTextPreference prefNotAnnouncedButSucceededChelemPoints = (EditTextPreference)this.findPreference(PreferenceConstants.PrefNotAnnouncedButSucceededChelemPoints);
//		EditTextPreference prefBelgianStepPoints = (EditTextPreference)this.findPreference(PreferenceConstants.PrefBelgianStepPoints);
//
//		// set preference values with current game set parameters
//		GameSetParameters params = this.getGameSet().getGameSetParameters();
//		prefPetitePoints.setSummary(""+params.getPetiteBasePoints());
//		prefPetiteRate.setSummary(""+params.getPetiteRate());
//		prefPrisePoints.setSummary(""+params.getPriseBasePoints());
//		prefPriseRate.setSummary(""+params.getPriseRate());
//		prefGardePoints.setSummary(""+params.getGardeBasePoints());
//		prefGardeRate.setSummary(""+params.getGardeRate());
//		prefGardeSansPoints.setSummary(""+params.getGardeSansBasePoints());
//		prefGardeSansRate.setSummary(""+params.getGardeSansRate());
//		prefGardeContrePoints.setSummary(""+params.getGardeContreBasePoints());
//		prefGardeContreRate.setSummary(""+params.getGardeContreRate());
//		prefPoigneePoints.setSummary(""+params.getPoigneePoints());
//		prefDoublePoigneePoints.setSummary(""+params.getDoublePoigneePoints());
//		prefTriplePoigneePoints.setSummary(""+params.getTriplePoigneePoints());
//		prefMiseryPoints.setSummary(""+params.getMiseryPoints());
//		prefKidAtTheEndPoints.setSummary(""+params.getKidAtTheEndPoints());
//		prefAnnouncedAndSucceededChelemPoints.setSummary(""+params.getAnnouncedAndSucceededChelemPoints());
//		prefAnnouncedAndFailedChelemPoints.setSummary(""+params.getAnnouncedAndFailedChelemPoints());
//		prefNotAnnouncedButSucceededChelemPoints.setSummary(""+params.getNotAnnouncedButSucceededChelemPoints());
//		prefBelgianStepPoints.setSummary(""+params.getBelgianBaseStepPoints());
//
//		// set all these preferences read only
//		prefPetitePoints.setEnabled(false);
//		prefPetiteRate.setEnabled(false);
//		prefPrisePoints.setEnabled(false);
//		prefPriseRate.setEnabled(false);
//		prefGardePoints.setEnabled(false);
//		prefGardeRate.setEnabled(false);
//		prefGardeSansPoints.setEnabled(false);
//		prefGardeSansRate.setEnabled(false);
//		prefGardeContrePoints.setEnabled(false);
//		prefGardeContreRate.setEnabled(false);
//		prefPoigneePoints.setEnabled(false);
//		prefDoublePoigneePoints.setEnabled(false);
//		prefTriplePoigneePoints.setEnabled(false);
//		prefMiseryPoints.setEnabled(false);
//		prefKidAtTheEndPoints.setEnabled(false);
//		prefAnnouncedAndSucceededChelemPoints.setEnabled(false);
//		prefAnnouncedAndFailedChelemPoints.setEnabled(false);
//		prefNotAnnouncedButSucceededChelemPoints.setEnabled(false);
//		prefBelgianStepPoints.setEnabled(false);
	}
	
	private GameSet getGameSet() {
		return TabGameSetActivity.getInstance().gameSet;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		this.preferences.unregisterOnSharedPreferenceChangeListener(this.listener);
	}

    @Override
    public boolean onPreferenceStartScreen(
            PreferenceFragmentCompat preferenceFragmentCompat, PreferenceScreen preferenceScreen
    ) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        TabGameSetPreferencesFragment fragment = new TabGameSetPreferencesFragment();
        Bundle args = new Bundle();
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.getKey());
        fragment.setArguments(args);
        ft.add(R.id.fragment_container, fragment, preferenceScreen.getKey());
        ft.addToBackStack(preferenceScreen.getKey());
        ft.commit();
        return true;
    }
}
