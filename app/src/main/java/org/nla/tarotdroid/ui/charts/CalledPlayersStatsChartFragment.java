package org.nla.tarotdroid.ui.charts;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.ui.constants.FragmentParameters;

import java.util.Map;

public class CalledPlayersStatsChartFragment extends ChartFragment {

    public static CalledPlayersStatsChartFragment newInstance(Context context) {
        CalledPlayersStatsChartFragment fragment = new CalledPlayersStatsChartFragment();
        Bundle arguments = new Bundle();
        arguments.putString(FragmentParameters.CHART_TITLE,
                            context.getResources()
                                   .getString(R.string.statNameCalledPlayersFrequency));
        fragment.setArguments(arguments);
        return fragment;
    }

    protected CategorySeries buildCategoryDataset(final Map<Player, Integer> mapPlayerValues) {
        // TODO Localize
        CategorySeries series = new CategorySeries("Called players 1");
        for (Player player : mapPlayerValues.keySet()) {
            series.add(player.getName() + " (" + mapPlayerValues.get(player) + ")",
                       mapPlayerValues.get(player));
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
                this.getActivity(),
                this.buildCategoryDataset(this.statisticsComputer.getCalledCount()),
                this.buildCategoryRenderer(this.statisticsComputer.getCalledCountColors())
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // HACK found on http://code.google.com/p/android/issues/detail?id=19917 to prevent error "Unable to pause activity" (described on web site)
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
}
