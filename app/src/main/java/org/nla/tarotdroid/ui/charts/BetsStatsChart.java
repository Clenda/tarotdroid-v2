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
package org.nla.tarotdroid.ui.charts;

import android.content.Context;
import android.content.Intent;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.LocalizationHelper;

import java.util.Map;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class BetsStatsChart extends BaseStatsChart {

	/**
	 * Creates a KingsStatsChart.
	 */
	public BetsStatsChart(
			final IGameSetStatisticsComputer gameSetStatisticsComputer,
			Context context
	) {
		super(
				context.getResources().getString(R.string.statNameBetsFrequency),
				context.getResources().getString(R.string.statDescBetsFrequency),
				gameSetStatisticsComputer,
				AuditHelper.EventTypes.displayGameSetStatisticsBetsDistribution
		);
	}
	
	/**
	 * Builds a category series using the provided values.
	 * @return the category series
	 */
	protected CategorySeries buildCategoryDataset(
			final Map<BetType, Integer> mapBetsValues,
			Context context,
			LocalizationHelper localizationHelper
	) {
		CategorySeries series = new CategorySeries(context.getResources()
														  .getString(R.string.statNameBetsFrequency));
		for (BetType bet : mapBetsValues.keySet()) {
			series.add(localizationHelper.getBetTranslation(bet) + " (" + mapBetsValues.get(bet) + ")",
					   mapBetsValues.get(bet));
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
				this.buildCategoryDataset(this.statisticsComputer.getBetCount(),
										  context,
										  localizationHelper),
				this.buildCategoryRenderer(this.statisticsComputer.getBetCountColors()),
				context.getResources().getString(R.string.statNameBetsFrequency)
		);
	}
}
