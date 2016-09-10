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
package org.nla.tarotdroid.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.computers.GameSetStatisticsComputerFactory;
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.app.AppContext;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.UIHelper;
import org.nla.tarotdroid.ui.charts.BetsStatsChartFragment;
import org.nla.tarotdroid.ui.charts.CalledPlayersStatsChartFragment;
import org.nla.tarotdroid.ui.charts.ChartFragment;
import org.nla.tarotdroid.ui.charts.FullBetsStatsChartFragment;
import org.nla.tarotdroid.ui.charts.GameScoresEvolutionChartFragment;
import org.nla.tarotdroid.ui.charts.KingsStatsChartFragment;
import org.nla.tarotdroid.ui.charts.LeadingPlayersStatsChartFragment;
import org.nla.tarotdroid.ui.charts.SuccessesStatsChartFragment;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class GameSetChartViewPagerActivity extends AppCompatActivity {

	private static IGameSetStatisticsComputer gameSetStatisticsComputer;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private List<ChartFragment> chartFragments;

	public static IGameSetStatisticsComputer getGameSetStatisticsComputer() {
		return gameSetStatisticsComputer;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			this.setContentView(R.layout.simple_titles);

			this.auditEvent();
			this.setTitle(this.getString(R.string.lblMainStatActivityTitle));

			// set keep screen on
			UIHelper.setKeepScreenOn(this, AppContext.getApplication().getAppParams().isKeepScreenOn());

			// initialize the pager
			this.initialisePaging();

			ActionBar mActionBar = getSupportActionBar();
			mActionBar.setHomeButtonEnabled(true);
			mActionBar.setDisplayShowHomeEnabled(true);
		}
		catch (Exception e) {
			AuditHelper.auditError(AuditHelper.ErrorTypes.tabGameSetActivityError, e, this);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		AuditHelper.auditSession(this);
	}
	
	private void auditEvent() {
		AuditHelper.auditEvent(AuditHelper.EventTypes.displayCharts);
	}
	
	private GameSet getGameSet() {
		return TabGameSetActivity.getInstance().gameSet;
	}

	private void initialisePaging() {
		// instantiate statistics computer
		gameSetStatisticsComputer = GameSetStatisticsComputerFactory.GetGameSetStatisticsComputer(this.getGameSet(), "guava");
		
		// instantiate fragments
		this.chartFragments = newArrayList();
		this.chartFragments.add(GameScoresEvolutionChartFragment.newInstance());
		this.chartFragments.add(LeadingPlayersStatsChartFragment.newInstance());
		this.chartFragments.add(BetsStatsChartFragment.newInstance());
		this.chartFragments.add(FullBetsStatsChartFragment.newInstance());
		this.chartFragments.add(SuccessesStatsChartFragment.newInstance());
		if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
			this.chartFragments.add(CalledPlayersStatsChartFragment.newInstance());
			this.chartFragments.add(KingsStatsChartFragment.newInstance());
		}
		
		// populate adapter and pager
		this.mPagerAdapter = new ChartViewPagerAdapter(super.getSupportFragmentManager(), this.chartFragments);
		this.mPager = (ViewPager) super.findViewById(R.id.pager);
		this.mPager.setAdapter(this.mPagerAdapter);
		
		// show indicator
		TitlePageIndicator indicator = (TitlePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
	}
	
	protected class ChartViewPagerAdapter extends FragmentPagerAdapter {

		private final List<ChartFragment> fragments;

		public ChartViewPagerAdapter(FragmentManager fm, List<ChartFragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
		
	    @Override
	    public CharSequence getPageTitle(int position) {
	    	return this.fragments.get(position).getChartTitle();
	    }
	}
}