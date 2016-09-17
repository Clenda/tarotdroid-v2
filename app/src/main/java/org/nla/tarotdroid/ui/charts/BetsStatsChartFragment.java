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
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.helpers.LocalizationHelper;
import org.nla.tarotdroid.ui.constants.FragmentParameters;

import java.util.Map;

import javax.inject.Inject;

public class BetsStatsChartFragment extends ChartFragment {

    @Inject LocalizationHelper localizationHelper;

    public static BetsStatsChartFragment newInstance(Context context) {
        BetsStatsChartFragment fragment = new BetsStatsChartFragment();
        Bundle arguments = new Bundle();
        arguments.putString(FragmentParameters.CHART_TITLE,
                            context.getResources().getString(R.string.statNameBetsFrequency));
        fragment.setArguments(arguments);

        return fragment;
    }

    protected CategorySeries buildCategoryDataset(final Map<BetType, Integer> mapBetsValues) {
        CategorySeries series = new CategorySeries(getContext().getResources()
                                                               .getString(R.string.statNameBetsFrequency));
        for (BetType bet : mapBetsValues.keySet()) {
            series.add(localizationHelper.getBetTranslation(bet) + " (" + mapBetsValues.get(bet) + ")",
                       mapBetsValues.get(bet));
        }
        return series;
    }

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState
    ) {
        TarotDroidApp.get(getContext()).getComponent().inject(this);
        return ChartFactory.getPieChartView(
                this.getActivity(),
                this.buildCategoryDataset(this.statisticsComputer.getBetCount()),
                this.buildCategoryRenderer(this.statisticsComputer.getBetCountColors())
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // HACK found on http://code.google.com/p/android/issues/detail?id=19917 to prevent error "Unable to pause activity" (described on web site)
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
}
