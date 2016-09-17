package org.nla.tarotdroid.gameset.charts;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.constants.FragmentParameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameScoresEvolutionChartFragment extends ChartFragment {

    private static final Map<Integer, PointStyle[]> MAP_PLAYERCOUNT_POINTSTYLES = new HashMap<Integer, PointStyle[]>();

    static {
        GameScoresEvolutionChartFragment.MAP_PLAYERCOUNT_POINTSTYLES.put(3,
                                                                         new PointStyle[]{
                                                                                 PointStyle.POINT,
                                                                                 PointStyle.POINT,
                                                                                 PointStyle.POINT
                                                                         });
        GameScoresEvolutionChartFragment.MAP_PLAYERCOUNT_POINTSTYLES.put(4,
                                                                         new PointStyle[]{
                                                                                 PointStyle.POINT,
                                                                                 PointStyle.POINT,
                                                                                 PointStyle.POINT,
                                                                                 PointStyle.POINT
                                                                         });
        GameScoresEvolutionChartFragment.MAP_PLAYERCOUNT_POINTSTYLES.put(5,
                                                                         new PointStyle[]{
                                                                                 PointStyle.POINT,
                                                                                 PointStyle.POINT,
                                                                                 PointStyle.POINT,
                                                                                 PointStyle.POINT,
                                                                                 PointStyle.POINT
                                                                         });
        GameScoresEvolutionChartFragment.MAP_PLAYERCOUNT_POINTSTYLES.put(6,
                                                                         new PointStyle[]{
                                                                                 PointStyle.POINT,
                                                                                 PointStyle.POINT,
                                                                                 PointStyle.POINT,
                                                                                 PointStyle.POINT,
                                                                                 PointStyle.POINT,
                                                                                 PointStyle.POINT
                                                                         });
    }

    public static GameScoresEvolutionChartFragment newInstance(Context context) {
        GameScoresEvolutionChartFragment fragment = new GameScoresEvolutionChartFragment();
        Bundle arguments = new Bundle();
        arguments.putString(FragmentParameters.CHART_TITLE,
                            context.getResources().getString(R.string.statNameScoreEvolution));
        fragment.setArguments(arguments);
        return fragment;
    }

    protected void setChartSettings(
            final XYMultipleSeriesRenderer renderer,
            final String title,
            final String xTitle,
            final String yTitle,
            final double xMin,
            final double xMax,
            final double yMin,
            final double yMax,
            final int axesColor,
            final int labelsColor
    ) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
        renderer.setPanLimits(new double[]{xMin, xMax, yMin, yMax});
        renderer.setZoomLimits(new double[]{xMin, xMax, yMin, yMax});
    }

    protected XYMultipleSeriesRenderer buildRenderer(
            final int[] colors,
            final PointStyle[] styles
    ) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setPointSize(5f);
        renderer.setMargins(new int[]{20, 30, 15, 20});
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            r.setLineWidth(5f);
            renderer.addSeriesRenderer(r);
        }
        renderer.setXLabels(5);
        renderer.setYLabels(10);
        renderer.setDisplayChartValues(true);
        return renderer;
    }

    protected XYMultipleSeriesDataset buildDataset(
            final String[] playerNames,
            final List<double[]> playerGameScoresArrays
    ) {
        XYMultipleSeriesDataset gameSetDataSet = new XYMultipleSeriesDataset();
        for (int playerIndex = 0; playerIndex < playerNames.length; ++playerIndex) {
            XYSeries playerSeries = new XYSeries(playerNames[playerIndex]);
            double[] playerGameScoresArray = playerGameScoresArrays.get(playerIndex);
            for (int gameIndex = 0; gameIndex < playerGameScoresArray.length; ++gameIndex) {
                playerSeries.add(gameIndex, playerGameScoresArray[gameIndex]);
            }
            gameSetDataSet.addSeries(playerSeries);
        }
        return gameSetDataSet;
    }

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState
    ) {
        List<double[]> scores = statisticsComputer.getScores();
        XYMultipleSeriesRenderer renderer = buildRenderer(statisticsComputer.getScoresColors(),
                                                          getScoresPointStyles());
        setChartSettings(
                renderer,
                "",
                getContext().getResources().getString(R.string.lblScoreEvolutionGames),
                getContext().getResources().getString(R.string.lblScoreEvolutionPoints),
                0,
                statisticsComputer.getGameCount() + 1,
                statisticsComputer.getMaxAbsoluteScore() * -1 - 100,
                statisticsComputer.getMaxAbsoluteScore() + 100,
                Color.GRAY,
                Color.LTGRAY
        );

        return ChartFactory.getLineChartView(
                getActivity(),
                buildDataset(statisticsComputer.getPlayerNames(), scores),
                renderer
        );
    }

    private PointStyle[] getScoresPointStyles() {
        int playerCount = statisticsComputer.getPlayerCount();
        if (!GameScoresEvolutionChartFragment.MAP_PLAYERCOUNT_POINTSTYLES.containsKey(playerCount)) {
            throw new IllegalStateException("missing PointStyle array or illegal player count:" + playerCount);
        }

        return GameScoresEvolutionChartFragment.MAP_PLAYERCOUNT_POINTSTYLES.get(playerCount);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // HACK found on http://code.google.com/p/android/issues/detail?id=19917 to prevent error "Unable to pause activity" (described on web site)
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
}
