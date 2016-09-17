package org.nla.tarotdroid.ui;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.computers.GameSetStatisticsComputerFactory;
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.ui.charts.BetsStatsChartFragment;
import org.nla.tarotdroid.ui.charts.CalledPlayersStatsChartFragment;
import org.nla.tarotdroid.ui.charts.ChartFragment;
import org.nla.tarotdroid.ui.charts.FullBetsStatsChartFragment;
import org.nla.tarotdroid.ui.charts.GameScoresEvolutionChartFragment;
import org.nla.tarotdroid.ui.charts.KingsStatsChartFragment;
import org.nla.tarotdroid.ui.charts.LeadingPlayersStatsChartFragment;
import org.nla.tarotdroid.ui.charts.SuccessesStatsChartFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GameSetChartViewPagerActivity extends BaseActivity {

    private static IGameSetStatisticsComputer gameSetStatisticsComputer;

    @BindView(R.id.pager) protected ViewPager viewPager;
    @BindView(R.id.tabs) protected TabLayout tabLayout;
    private PagerAdapter pagerAdapter;
    private List<ChartFragment> chartFragments;

	public static IGameSetStatisticsComputer getGameSetStatisticsComputer() {
		return gameSetStatisticsComputer;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
            super.onCreate(savedInstanceState);
            initialisePaging();
			ActionBar mActionBar = getSupportActionBar();
			mActionBar.setHomeButtonEnabled(true);
			mActionBar.setDisplayShowHomeEnabled(true);
		}
		catch (Exception e) {
            auditHelper.auditError(AuditHelper.ErrorTypes.tabGameSetActivityError, e, this);
        }
	}

	@Override
    protected void inject() {
        TarotDroidApp.get(this).getComponent().inject(this);
    }

    @Override
    protected void auditEvent() {
        auditHelper.auditEvent(AuditHelper.EventTypes.displayCharts);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.simple_titles;
    }

    @StringRes
    protected int getTitleResId() {
        return R.string.lblMainStatActivityTitle;
    }


	private GameSet getGameSet() {
		return TabGameSetActivity.getInstance().gameSet;
	}

	private void initialisePaging() {
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        // instantiate statistics computer
        gameSetStatisticsComputer = GameSetStatisticsComputerFactory.GetGameSetStatisticsComputer(
                getGameSet(),
                "guava");

        // instantiate fragments
        chartFragments = new ArrayList<>();
        chartFragments.add(GameScoresEvolutionChartFragment.newInstance(this));
        chartFragments.add(LeadingPlayersStatsChartFragment.newInstance(this));
        chartFragments.add(BetsStatsChartFragment.newInstance(this));
        chartFragments.add(FullBetsStatsChartFragment.newInstance(this));
        chartFragments.add(SuccessesStatsChartFragment.newInstance(this));
        if (getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
            chartFragments.add(CalledPlayersStatsChartFragment.newInstance(this));
            chartFragments.add(KingsStatsChartFragment.newInstance(this));
        }

        // populate adapter and pager
        pagerAdapter = new ChartViewPagerAdapter(super.getSupportFragmentManager(),
                                                 chartFragments);
        viewPager.setAdapter(pagerAdapter);
    }

    protected class ChartViewPagerAdapter extends FragmentPagerAdapter {

		private final List<ChartFragment> fragments;

		public ChartViewPagerAdapter(FragmentManager fm, List<ChartFragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
            return fragments.get(position);
        }

		@Override
		public int getCount() {
            return fragments.size();
        }
		
	    @Override
	    public CharSequence getPageTitle(int position) {
            return fragments.get(position).getChartTitle();
        }
	}
}