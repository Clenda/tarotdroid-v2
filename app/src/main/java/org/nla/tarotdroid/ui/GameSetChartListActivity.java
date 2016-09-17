package org.nla.tarotdroid.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.nla.tarotdroid.BuildConfig;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.computers.GameSetStatisticsComputerFactory;
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.ui.charts.BetsStatsChart;
import org.nla.tarotdroid.ui.charts.CalledPlayersStatsChart;
import org.nla.tarotdroid.ui.charts.FullBetsStatsChart;
import org.nla.tarotdroid.ui.charts.GameScoresEvolutionChart;
import org.nla.tarotdroid.ui.charts.IStatsChart;
import org.nla.tarotdroid.ui.charts.KingsStatsChart;
import org.nla.tarotdroid.ui.charts.LeadingPlayersStatsChart;
import org.nla.tarotdroid.ui.charts.SuccessesStatsChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnItemClick;

public class GameSetChartListActivity extends BaseActivity {

	@BindView(R.id.listView) protected ListView listView;
	private String[] menuTexts;
	private String[] menuSummaries;
	private IStatsChart[] statCharts;

	@Override
	protected int getTitleResId() {
		return R.string.lblMainStatActivityTitle;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			// create charts objects
			IGameSetStatisticsComputer gameSetStatisticsComputer = GameSetStatisticsComputerFactory.GetGameSetStatisticsComputer(
					getGameSet(),
					"guava");
			IStatsChart[] statChartsTarot4 = new IStatsChart[] {
					new GameScoresEvolutionChart(gameSetStatisticsComputer, this),
					new BetsStatsChart(gameSetStatisticsComputer, this),
					new FullBetsStatsChart(gameSetStatisticsComputer, this),
					new SuccessesStatsChart(gameSetStatisticsComputer, this),
					new LeadingPlayersStatsChart(gameSetStatisticsComputer, this),
			};
			IStatsChart[] statChartsTarot5 = new IStatsChart[]  {
					new GameScoresEvolutionChart(gameSetStatisticsComputer, this),
					new KingsStatsChart(gameSetStatisticsComputer, this),
					new BetsStatsChart(gameSetStatisticsComputer, this),
					new FullBetsStatsChart(gameSetStatisticsComputer, this),
					new SuccessesStatsChart(gameSetStatisticsComputer, this),
					new LeadingPlayersStatsChart(gameSetStatisticsComputer, this),
					new CalledPlayersStatsChart(gameSetStatisticsComputer, this),
			};			
			
			// create data structures backing the list adapter
			statCharts = getGameSet().getGameStyleType() == GameStyleType.Tarot5
					? statChartsTarot5
                    : statChartsTarot4;
			menuTexts = new String[statCharts.length];
			menuSummaries = new String[statCharts.length];
			for (int i = 0; i < statCharts.length; ++i) {
				menuTexts[i] = statCharts[i].getName();
				menuSummaries[i] = statCharts[i].getDescription();
			}

			// create the adapter backing the list
			SimpleAdapter adapter = new SimpleAdapter(
					this,
					getListValues(),
					android.R.layout.simple_list_item_2,
					new String[] {IStatsChart.NAME, IStatsChart.DESC },
					new int[] {android.R.id.text1, android.R.id.text2 }
			);
			listView.setAdapter(adapter);
		} catch (Exception e) {
			auditHelper.auditError(ErrorTypes.gameSetStatisticsActivityError, e, this);
		}
	}

	@Override
	protected void inject() {
		TarotDroidApp.get(this).getComponent().inject(this);
	}

	private GameSet getGameSet() {
		return TabGameSetActivity.getInstance().gameSet;
	}

	@Override
	protected void auditEvent() {
		auditHelper.auditEvent(AuditHelper.EventTypes.displayGameSetStatisticsPage);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_gameset_chart_list;
	}

	private List<Map<String, String>> getListValues() {
		List<Map<String, String>> values = new ArrayList<Map<String, String>>();
		int length = menuTexts.length;
		for (int i = 0; i < length; i++) {
			Map<String, String> v = new HashMap<String, String>();
			v.put(IStatsChart.NAME, menuTexts[i]);
			v.put(IStatsChart.DESC, menuSummaries[i]);
			values.add(v);
		}
		return values;
	}

	@OnItemClick(R.id.listView)
	protected void onListItemClick(AdapterView<?> parent, int position) {
		try {
			IStatsChart chart = statCharts[position];
			auditHelper.auditSession(this);
			auditHelper.auditEvent(chart.getAuditEventType());
			startActivity(chart.execute(this, uiHelper));
		}
		catch (Exception e) {
			Log.v(BuildConfig.APP_LOG_TAG,
				  "TarotDroid Exception in " + getClass().toString(),
				  e);
			Toast.makeText(
					this,
					getString(R.string.lblErrorStatisticsComputation, e.getMessage()),
					Toast.LENGTH_LONG
			).show();
		}
	}
	
	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event)  {
	    // back button
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
}
