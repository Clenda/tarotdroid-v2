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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.nla.tarotdroid.AppContext;
import org.nla.tarotdroid.R;
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

public class GameSetChartListActivity extends AppCompatActivity {

	private String[] menuTexts;
	private String[] menuSummaries;
	private IStatsChart[] statCharts;
    private ListView listView;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			this.auditEvent();
			
			// set action bar properties
			this.setTitle(this.getString(R.string.lblMainStatActivityTitle));
			
			// create charts objects
			IGameSetStatisticsComputer gameSetStatisticsComputer = GameSetStatisticsComputerFactory.GetGameSetStatisticsComputer(this.getGameSet(), "guava");
			IStatsChart[] statChartsTarot4 = new IStatsChart[] { 
					new GameScoresEvolutionChart(gameSetStatisticsComputer),
					new BetsStatsChart(gameSetStatisticsComputer),
					new FullBetsStatsChart(gameSetStatisticsComputer),
					new SuccessesStatsChart(gameSetStatisticsComputer),
					new LeadingPlayersStatsChart(gameSetStatisticsComputer),
			};
			IStatsChart[] statChartsTarot5 = new IStatsChart[]  { 
					new GameScoresEvolutionChart(gameSetStatisticsComputer),
					new KingsStatsChart(gameSetStatisticsComputer),
					new BetsStatsChart(gameSetStatisticsComputer),
					new FullBetsStatsChart(gameSetStatisticsComputer),
					new SuccessesStatsChart(gameSetStatisticsComputer),
					new LeadingPlayersStatsChart(gameSetStatisticsComputer),
					new CalledPlayersStatsChart(gameSetStatisticsComputer),
			};			
			
			// create data structures backing the list adapter
            this.setContentView(R.layout.activity_gameset_chart_list);
            this.listView = (ListView) findViewById(R.id.listView);
            this.statCharts = this.getGameSet().getGameStyleType() == GameStyleType.Tarot5
                    ? statChartsTarot5
                    : statChartsTarot4;
            this.menuTexts = new String[this.statCharts.length];
			this.menuSummaries = new String[this.statCharts.length];
			for (int i = 0; i < this.statCharts.length; ++i) {
			    this.menuTexts[i] = this.statCharts[i].getName();
			    this.menuSummaries[i] = this.statCharts[i].getDescription();
			}

			// create the adapter backing the list
			SimpleAdapter adapter = new SimpleAdapter(
					this, 
					this.getListValues(),
					android.R.layout.simple_list_item_2, 
					new String[] {IStatsChart.NAME, IStatsChart.DESC },
					new int[] {android.R.id.text1, android.R.id.text2 }
			);
            this.listView.setAdapter(adapter);
            this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onListItemClick(view, position, id);
                }
            });
        } catch (Exception e) {
			AuditHelper.auditError(ErrorTypes.gameSetStatisticsActivityError, e, this);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		AuditHelper.auditSession(this);
	}
	
	private GameSet getGameSet() {
		return TabGameSetActivity.getInstance().gameSet;
	}
	
	private void auditEvent() {
		AuditHelper.auditEvent(AuditHelper.EventTypes.displayGameSetStatisticsPage);
	}

	private List<Map<String, String>> getListValues() {
		List<Map<String, String>> values = new ArrayList<Map<String, String>>();
		int length = this.menuTexts.length;
		for (int i = 0; i < length; i++) {
			Map<String, String> v = new HashMap<String, String>();
			v.put(IStatsChart.NAME, this.menuTexts[i]);
			v.put(IStatsChart.DESC, this.menuSummaries[i]);
			values.add(v);
		}
		return values;
	}

    protected void onListItemClick(final View v, final int position, final long id) {
        try {
            IStatsChart chart = this.statCharts[position];
			AuditHelper.auditSession(this);
			AuditHelper.auditEvent(chart.getAuditEventType());
		    this.startActivity(chart.execute(this));
		}
		catch (Exception e) {
			Log.v(AppContext.getApplication().getAppLogTag(), "TarotDroid Exception in " + this.getClass().toString(), e);
			Toast.makeText(
					this,
					this.getString(R.string.lblErrorStatisticsComputation, e.getMessage()),
					Toast.LENGTH_LONG
			).show();
		}
	}
	
	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event)  {
	    // back button
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
}
