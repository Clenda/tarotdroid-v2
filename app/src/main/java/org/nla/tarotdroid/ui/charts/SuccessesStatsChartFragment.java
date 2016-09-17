package org.nla.tarotdroid.ui.charts;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.Result;
import org.nla.tarotdroid.biz.enums.ResultType;
import org.nla.tarotdroid.ui.constants.FragmentParameters;

import java.util.Map;

public class SuccessesStatsChartFragment extends ChartFragment {

	public static SuccessesStatsChartFragment newInstance(Context context) {
		SuccessesStatsChartFragment fragment = new SuccessesStatsChartFragment();
		
		Bundle arguments = new Bundle();
		arguments.putString(FragmentParameters.CHART_TITLE,
							context.getResources().getString(R.string.statNameSuccessesFrequency));
		fragment.setArguments(arguments);

		return fragment;
	}
	
	protected CategorySeries buildCategoryDataset(final Map<ResultType, Integer> mapResultValues) {
		CategorySeries series = new CategorySeries("Successes/Failures 1");
		
		for (ResultType result : mapResultValues.keySet()) {
			series.add(Result.valueOf(result).getLabel() + " (" + mapResultValues.get(result) + ") ", mapResultValues.get(result));
		}
		return series;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
	    return ChartFactory.getPieChartView(
				getActivity(),
				buildCategoryDataset(statisticsComputer.getResultsCount()),
				buildCategoryRenderer(statisticsComputer.getResultsColors())
		);
	}

	@Override
    public void onSaveInstanceState(Bundle outState) {
    	// HACK found on http://code.google.com/p/android/issues/detail?id=19917 to prevent error "Unable to pause activity" (described on web site) 
    	outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
    	super.onSaveInstanceState(outState);
    }
}
