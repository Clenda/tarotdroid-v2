package org.nla.tarotdroid.ui.charts;

import android.content.Context;
import android.content.Intent;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.LocalizationHelper;

import java.util.Map;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class LeadingPlayersStatsChart extends BaseStatsChart {

	/**
	 * Creates a LeadingPlayersStatsChart.
	 */
	public LeadingPlayersStatsChart(
			final IGameSetStatisticsComputer gameSetStatisticsComputer,
			Context context
	) {
		super(
				context.getResources().getString(R.string.statNameLeadingPlayerFrequency),
				context.getResources().getString(R.string.statDescLeadingPlayerFrequency),
				gameSetStatisticsComputer,
				AuditHelper.EventTypes.displayGameSetStatisticsLeadingPlayerRepartition
		);
	}
	
	/**
	 * Builds a category series using the provided values.
	 * @return the category series
	 */
	protected CategorySeries buildCategoryDataset(final Map<Player, Integer> mapPlayerValues) {
		// TODO Localize
		CategorySeries series = new CategorySeries("Called players 1");
		for (Player player : mapPlayerValues.keySet()) {
			series.add(player.getName() + " (" + mapPlayerValues.get(player) + ")", mapPlayerValues.get(player));
		}
		return series;
	}

	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.ui.controls.IStatsChart#execute(android.content.Context)
	 */
	@Override
	public Intent execute(final Context context, final LocalizationHelper localizationHelper) {
		return ChartFactory.getPieChartIntent(
				context,
				this.buildCategoryDataset(this.statisticsComputer.getLeadingCount()),
				this.buildCategoryRenderer(this.statisticsComputer.getLeadingCountColors()),
				context.getResources().getString(R.string.statNameLeadingPlayerFrequency)
		);
	}
}
