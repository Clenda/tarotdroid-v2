package org.nla.tarotdroid.gameset.charts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;
import org.nla.tarotdroid.constants.FragmentParameters;
import org.nla.tarotdroid.gameset.GameSetChartViewPagerActivity;

public abstract class ChartFragment extends Fragment
{
    protected IGameSetStatisticsComputer statisticsComputer;

    private FrameLayout frameLayout;

	protected ChartFragment() {
		this.statisticsComputer = GameSetChartViewPagerActivity.getGameSetStatisticsComputer();
	}
		
	protected DefaultRenderer buildCategoryRenderer(final int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setLabelsTextSize(20);
		renderer.setLegendTextSize(20);
		renderer.setMargins(new int[] {20, 30, 15, 20});
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
			renderer.setShowLabels(true);
			renderer.setShowLegend(true);
		}
		return renderer;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        frameLayout = (FrameLayout) inflater.inflate(R.layout.belgian_game_read, container, false);
        return frameLayout;
    }
	
	public String getChartTitle() {
		if (this.getArguments() != null && this.getArguments().getString(FragmentParameters.CHART_TITLE) != null) {
			return this.getArguments().getString(FragmentParameters.CHART_TITLE);
		}
		return "UNDEFINED_TITLE";
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
    	// HACK found on http://code.google.com/p/android/issues/detail?id=19917 to prevent error "Unable to pause activity" (described on web site) 
    	outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
    	super.onSaveInstanceState(outState);
    }
}