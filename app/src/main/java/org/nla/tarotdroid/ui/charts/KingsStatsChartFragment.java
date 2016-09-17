package org.nla.tarotdroid.ui.charts;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.enums.KingType;
import org.nla.tarotdroid.helpers.LocalizationHelper;
import org.nla.tarotdroid.ui.constants.FragmentParameters;

import java.util.Map;

import javax.inject.Inject;

public class KingsStatsChartFragment extends ChartFragment {

	@Inject LocalizationHelper localizationHelper;

	public static KingsStatsChartFragment newInstance(Context context) {
		KingsStatsChartFragment fragment = new KingsStatsChartFragment();
		Bundle arguments = new Bundle();
		arguments.putString(FragmentParameters.CHART_TITLE,
							context.getResources().getString(R.string.statNameCalledKingFrequency));
		fragment.setArguments(arguments);
		return fragment;
	}
	
	protected CategorySeries buildCategoryDataset(final Map<KingType, Integer> mapKingValues) {
		CategorySeries series = new CategorySeries(getContext().getResources()
															   .getString(R.string.statNameCalledKingFrequency));
		for (KingType king : mapKingValues.keySet()) {
			if (mapKingValues.get(king) != 0) {
				series.add(localizationHelper.getKingTranslation(king) + " (" + mapKingValues.get(
						king) + ")",
						   mapKingValues.get(king));
			}
		}
		return series;
	}
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		TarotDroidApp.get(getContext()).getComponent().inject(this);
		return ChartFactory.getPieChartView(
				getActivity(),
				buildCategoryDataset(statisticsComputer.getKingCount()),
				buildCategoryRenderer(statisticsComputer.getKingCountColors())
		);
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
    	// HACK found on http://code.google.com/p/android/issues/detail?id=19917 to prevent error "Unable to pause activity" (described on web site) 
    	outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
    	super.onSaveInstanceState(outState);
    }
}
