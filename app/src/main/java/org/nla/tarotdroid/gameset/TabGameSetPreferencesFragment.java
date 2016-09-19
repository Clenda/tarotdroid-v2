package org.nla.tarotdroid.gameset;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;

import com.takisoft.fix.support.v7.preference.EditTextPreference;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.constants.PreferenceConstants;
import org.nla.tarotdroid.core.AppParams;
import org.nla.tarotdroid.core.dal.IDalService;

import javax.inject.Inject;

public class TabGameSetPreferencesFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String FRAGMENT_TAG = "tab_gameset_preference_fragment";

    @Inject protected AppParams appParams;
    @Inject protected IDalService dalService;
    @Inject protected GameSetParameters gameSetParameters;

    public TabGameSetPreferencesFragment() {
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
        setPreferencesFromResource(R.xml.tab_gameset_preferences, rootKey);
        // remove "misery allowed" pref if game is 5 player style
        GameSet gameSet = TabGameSetActivity.getInstance().gameSet;
        if (gameSet.getGameStyleType() == GameStyleType.Tarot5) {
            CheckBoxPreference prefMiseryAllowedAt3Or5 = (CheckBoxPreference) this.findPreference(
                    PreferenceConstants.PrefIsMiseryAuthorized);
            prefMiseryAllowedAt3Or5.setChecked(false);
            prefMiseryAllowedAt3Or5.setEnabled(false);
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

        // get preferences
        EditTextPreference prefPetitePoints = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefPetitePoints);
        EditTextPreference prefPetiteRate = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefPetiteRate);
        EditTextPreference prefPrisePoints = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefPrisePoints);
        EditTextPreference prefPriseRate = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefPriseRate);
        EditTextPreference prefGardePoints = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefGardePoints);
        EditTextPreference prefGardeRate = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefGardeRate);
        EditTextPreference prefGardeSansPoints = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefGardeSansPoints);
        EditTextPreference prefGardeSansRate = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefGardeSansRate);
        EditTextPreference prefGardeContrePoints = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefGardeContrePoints);
        EditTextPreference prefGardeContreRate = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefGardeContreRate);
        EditTextPreference prefPoigneePoints = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefPoigneePoints);
        EditTextPreference prefDoublePoigneePoints = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefDoublePoigneePoints);
        EditTextPreference prefTriplePoigneePoints = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefTriplePoigneePoints);
        EditTextPreference prefMiseryPoints = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefMiseryPoints);
        EditTextPreference prefKidAtTheEndPoints = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefKidAtTheEndPoints);
        EditTextPreference prefAnnouncedAndSucceededChelemPoints = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefAnnouncedAndSucceededChelemPoints);
        EditTextPreference prefAnnouncedAndFailedChelemPoints = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefAnnouncedAndFailedChelemPoints);
        EditTextPreference prefNotAnnouncedButSucceededChelemPoints = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefNotAnnouncedButSucceededChelemPoints);
        EditTextPreference prefBelgianStepPoints = (EditTextPreference) this.findPreference(
                PreferenceConstants.PrefBelgianStepPoints);

        // set preference values with current game set parameters
        prefPetitePoints.setSummary("" + gameSetParameters.getPetiteBasePoints());
        prefPetiteRate.setSummary("" + gameSetParameters.getPetiteRate());
        prefPrisePoints.setSummary("" + gameSetParameters.getPriseBasePoints());
        prefPriseRate.setSummary("" + gameSetParameters.getPriseRate());
        prefGardePoints.setSummary("" + gameSetParameters.getGardeBasePoints());
        prefGardeRate.setSummary("" + gameSetParameters.getGardeRate());
        prefGardeSansPoints.setSummary("" + gameSetParameters.getGardeSansBasePoints());
        prefGardeSansRate.setSummary("" + gameSetParameters.getGardeSansRate());
        prefGardeContrePoints.setSummary("" + gameSetParameters.getGardeContreBasePoints());
        prefGardeContreRate.setSummary("" + gameSetParameters.getGardeContreRate());
        prefPoigneePoints.setSummary("" + gameSetParameters.getPoigneePoints());
        prefDoublePoigneePoints.setSummary("" + gameSetParameters.getDoublePoigneePoints());
        prefTriplePoigneePoints.setSummary("" + gameSetParameters.getTriplePoigneePoints());
        prefMiseryPoints.setSummary("" + gameSetParameters.getMiseryPoints());
        prefKidAtTheEndPoints.setSummary("" + gameSetParameters.getKidAtTheEndPoints());
        prefAnnouncedAndSucceededChelemPoints.setSummary("" + gameSetParameters.getAnnouncedAndSucceededChelemPoints());
        prefAnnouncedAndFailedChelemPoints.setSummary("" + gameSetParameters.getAnnouncedAndFailedChelemPoints());
        prefNotAnnouncedButSucceededChelemPoints.setSummary("" + gameSetParameters.getNotAnnouncedButSucceededChelemPoints());
        prefBelgianStepPoints.setSummary("" + gameSetParameters.getBelgianBaseStepPoints());

        // set all these preferences read only
        prefPetitePoints.setEnabled(false);
        prefPetiteRate.setEnabled(false);
        prefPrisePoints.setEnabled(false);
        prefPriseRate.setEnabled(false);
        prefGardePoints.setEnabled(false);
        prefGardeRate.setEnabled(false);
        prefGardeSansPoints.setEnabled(false);
        prefGardeSansRate.setEnabled(false);
        prefGardeContrePoints.setEnabled(false);
        prefGardeContreRate.setEnabled(false);
        prefPoigneePoints.setEnabled(false);
        prefDoublePoigneePoints.setEnabled(false);
        prefTriplePoigneePoints.setEnabled(false);
        prefMiseryPoints.setEnabled(false);
        prefKidAtTheEndPoints.setEnabled(false);
        prefAnnouncedAndSucceededChelemPoints.setEnabled(false);
        prefAnnouncedAndFailedChelemPoints.setEnabled(false);
        prefNotAnnouncedButSucceededChelemPoints.setEnabled(false);
        prefBelgianStepPoints.setEnabled(false);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferenceConstants.PrefDisplayGamesInReverseOrder)) {
            appParams.setDisplayGamesInReverseOrder(sharedPreferences.getBoolean(PreferenceConstants.PrefDisplayGamesInReverseOrder,
                                                                                 true));
        } else if (key.equals(PreferenceConstants.PrefDisplayGlobalScoresForEachGame)) {
            appParams.setDisplayGlobalScoresForEachGame(sharedPreferences.getBoolean(
                    PreferenceConstants.PrefDisplayGlobalScoresForEachGame,
                    true));
        } else if (key.equals(PreferenceConstants.PrefKeepScreenOn)) {
            appParams.setKeepScreenOn(sharedPreferences.getBoolean(PreferenceConstants.PrefKeepScreenOn,
                                                                   true));
        } else if (key.equals(PreferenceConstants.PrefIsPetiteAuthorized)) {
            appParams.setPetiteAuthorized(sharedPreferences.getBoolean(PreferenceConstants.PrefIsPetiteAuthorized,
                                                                       true));
        } else if (key.equals(PreferenceConstants.PrefIsPriseAuthorized)) {
            appParams.setPriseAuthorized(sharedPreferences.getBoolean(PreferenceConstants.PrefIsPriseAuthorized,
                                                                      true));
        } else if (key.equals(PreferenceConstants.PrefAreBelgianGamesAllowed)) {
            appParams.setBelgianGamesAllowed(sharedPreferences.getBoolean(PreferenceConstants.PrefAreBelgianGamesAllowed,
                                                                          true));
        } else if (key.equals(PreferenceConstants.PrefArePenaltyGamesAllowed)) {
            appParams.setPenaltyGamesAllowed(sharedPreferences.getBoolean(PreferenceConstants.PrefArePenaltyGamesAllowed,
                                                                          true));
        } else if (key.equals(PreferenceConstants.PrefArePassedGamesAllowed)) {
            appParams.setPassedGamesAllowed(sharedPreferences.getBoolean(PreferenceConstants.PrefArePassedGamesAllowed,
                                                                         true));
        } else if (key.equals(PreferenceConstants.PrefIsMiseryAuthorized)) {
            appParams.setMiseryAuthorized(sharedPreferences.getBoolean(PreferenceConstants.PrefIsMiseryAuthorized,
                                                                       false));
        } else if (key.equals(PreferenceConstants.PrefDisplayNextDealer)) {
            appParams.setDisplayNextDealer(sharedPreferences.getBoolean(PreferenceConstants.PrefDisplayNextDealer,
                                                                        true));
        }
    }
}
