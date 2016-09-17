
package org.nla.tarotdroid.ui.charts;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.ui.constants.FragmentParameters;

import java.util.Map;

public class FullBetsStatsChartFragment extends ChartFragment {

    public static FullBetsStatsChartFragment newInstance(Context context) {
        FullBetsStatsChartFragment fragment = new FullBetsStatsChartFragment();
        Bundle arguments = new Bundle();
        arguments.putString(FragmentParameters.CHART_TITLE,
                            context.getResources().getString(R.string.statNameFullBetsFrequency));
        fragment.setArguments(arguments);
        return fragment;
    }

    protected CategorySeries buildCategoryDataset(final Map<String, Integer> stringValues) {
        CategorySeries series = new CategorySeries(getContext().getResources()
                                                               .getString(R.string.statNameFullBetsFrequency));
        for (String description : stringValues.keySet()) {
            series.add(description + " (" + stringValues.get(description) + ")",
                       stringValues.get(description));
        }
        return series;
    }

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState
    ) {

        return ChartFactory.getPieChartView(
                getActivity(),
                buildCategoryDataset(this.statisticsComputer.getFullBetCount()),
                buildCategoryRenderer(this.statisticsComputer.getFullBetCountColors())
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // HACK found on http://code.google.com/p/android/issues/detail?id=19917 to prevent error "Unable to pause activity" (described on web site)
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
}