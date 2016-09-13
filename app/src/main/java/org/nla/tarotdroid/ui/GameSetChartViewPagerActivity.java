package org.nla.tarotdroid.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.app.AppContext;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.computers.GameSetStatisticsComputerFactory;
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;
import org.nla.tarotdroid.biz.enums.GameStyleType;
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
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private PagerAdapter pagerAdapter;

	private List<ChartFragment> chartFragments;

	public static IGameSetStatisticsComputer getGameSetStatisticsComputer() {
		return gameSetStatisticsComputer;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			this.setContentView(R.layout.simple_titles);

            auditEvent();
            setTitle(this.getString(R.string.lblMainStatActivityTitle));

			UIHelper.setKeepScreenOn(this, AppContext.getApplication().getAppParams().isKeepScreenOn());

            initialisePaging();

			ActionBar mActionBar = getSupportActionBar();
			mActionBar.setHomeButtonEnabled(true);
			mActionBar.setDisplayShowHomeEnabled(true);
		}
		catch (Exception e) {
			AuditHelper.auditError(AuditHelper.ErrorTypes.tabGameSetActivityError, e, this);
		}
	}
	
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
        viewPager = (ViewPager) super.findViewById(R.id.pager);
        setupViewPager();
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        // instantiate statistics computer
        gameSetStatisticsComputer = GameSetStatisticsComputerFactory.GetGameSetStatisticsComputer(
                this.getGameSet(),
                "guava");

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
        this.pagerAdapter = new ChartViewPagerAdapter(super.getSupportFragmentManager(),
                                                      this.chartFragments);
        this.viewPager.setAdapter(this.pagerAdapter);
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