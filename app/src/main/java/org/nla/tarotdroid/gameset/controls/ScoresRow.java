package org.nla.tarotdroid.gameset.controls;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.MapPlayersScores;
import org.nla.tarotdroid.biz.PersistableBusinessObject;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.constants.UIConstants;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public class ScoresRow extends BaseRow {

    private Map<Player, TextView> mapPlayersScoresTextViews;

    public ScoresRow(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                                               LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 8);
        setLayoutParams(params);
        mapPlayersScoresTextViews = new HashMap<Player, TextView>();
        initializeViews();
    }

    public ScoresRow(final Context context) {
        this(context, null);
    }

    protected void initializeViews() {
        // "Scores" label
        TextView lblScores = new TextView(getContext());
        lblScores.setText(R.string.lblScores);
        lblScores.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        lblScores.setLayoutParams(UIConstants.PLAYERS_LAYOUT_PARAMS);
        lblScores.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
        lblScores.setBackgroundColor(Color.TRANSPARENT);
        lblScores.setTextColor(Color.WHITE);
        lblScores.setSingleLine();
        lblScores.setEllipsize(TruncateAt.END);
        lblScores.setTypeface(null, Typeface.BOLD);
        addView(lblScores);

        // Individual scores text views
        for (Player player : getGameSet().getPlayers()) {
            // player name
            TextView txtPlayerScore = new TextView(getContext());
            txtPlayerScore.setText("0");
            txtPlayerScore.setGravity(Gravity.CENTER);
            txtPlayerScore.setLayoutParams(UIConstants.PLAYERS_LAYOUT_PARAMS);
            txtPlayerScore.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
            txtPlayerScore.setBackgroundColor(Color.GRAY);
            txtPlayerScore.setTextColor(Color.BLACK);
            txtPlayerScore.setSingleLine();
            txtPlayerScore.setEllipsize(TruncateAt.END);
            txtPlayerScore.setTypeface(null, Typeface.BOLD);
            addView(txtPlayerScore);
            mapPlayersScoresTextViews.put(player, txtPlayerScore);
        }
    }

    public void updatePlayerScore(final MapPlayersScores mapPlayersScores) {
        checkArgument(mapPlayersScores != null, "mapPlayersScores is null");

        // player names and scores
        for (PersistableBusinessObject player : getGameSet().getPlayers()) {
            // map the player score to his score text view
            int rankColor = Color.GRAY;
            switch (mapPlayersScores.getPlayerRank(player)) {
                case 1:
                    rankColor = getResources().getColor(R.color.FirstScore);
                    break;
                case 2:
                    rankColor = getResources().getColor(R.color.SecondScore);
                    break;
                case 3:
                    rankColor = getResources().getColor(R.color.ThirdScore);
                    break;
                case 4:
                    rankColor = getResources().getColor(R.color.FourthScore);
                    break;
                case 5:
                    rankColor = getResources().getColor(R.color.FifthScore);
                    break;
                case 6:
                    rankColor = getResources().getColor(R.color.SixthScore);
                    break;
                default:
                    rankColor = Color.WHITE;
            }
            mapPlayersScoresTextViews.get(player).setBackgroundColor(rankColor);
            mapPlayersScoresTextViews.get(player)
                                     .setText(Integer.toString(mapPlayersScores.get(player)));
        }
    }

    public void resetAllPlayerScores() {
        for (PersistableBusinessObject player : getGameSet().getPlayers()) {
            int rankColor = Color.GRAY;
            mapPlayersScoresTextViews.get(player).setBackgroundColor(rankColor);
            mapPlayersScoresTextViews.get(player).setText("0");
        }
    }
}