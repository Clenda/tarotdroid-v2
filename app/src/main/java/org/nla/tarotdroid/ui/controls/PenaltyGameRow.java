package org.nla.tarotdroid.ui.controls;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.PenaltyGame;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.ui.constants.UIConstants;

public class PenaltyGameRow extends BaseGameRow {

    protected PenaltyGameRow(
            final Context context,
            final ProgressDialog dialog,
            final AttributeSet attrs,
            final BaseGame game,
            final float weight,
            final GameSet gameSet
    ) {
        super(context, dialog, attrs, weight, gameSet);
        if (!(game instanceof PenaltyGame)) {
            throw new IllegalArgumentException("Incorrect game type: " + game.getClass());
        }
        setOrientation(HORIZONTAL);
        this.game = game;
        TarotDroidApp.get(context).getComponent().inject(this);
        initializeViews();
        setOnLongClickListener(this);
    }

    protected PenaltyGameRow(
            final Context context,
            final ProgressDialog dialog,
            final BaseGame game,
            final float weight,
            final GameSet gameSet
    ) {
        this(context, dialog, null, game, weight, gameSet);
    }

    protected PenaltyGame getGame() {
        return (PenaltyGame) game;
    }

    protected void initializeViews() {
        // game bet label
        addView(ScoreCellFactory.buildPenaltyGameDescription(
                getContext(),
                game.getIndex()
        ));

        // each individual player score
        for (Player player : getGameSet().getPlayers()) {
            StringBuffer playerScore = new StringBuffer();
            int color;

            // dead player ?
            if (!game.getPlayers().contains(player)) {
                playerScore.append("#");
                color = Color.GRAY;
            }

            // in game player
            else {

                if (player == getGame().getPenaltedPlayer()) {
                    color = Color.RED;
                } else {
                    color = getContext().getResources().getColor(R.color.LeadingPlayer);
                }

                // get score to display, depending on the user settings
                String scoreToDisplay = "0";
                // => global scores at the end of the game
                if (appParams.isDisplayGlobalScoresForEachGame()) {
                    scoreToDisplay = Integer.toString(getGameSet().getGameSetScores()
                                                                  .getIndividualResultsAtGameOfIndex(
                                                                          game.getIndex(),
                                                                          player));
                }
                // => individual scores of the game
                else {
                    scoreToDisplay = Integer.toString(game.getGameScores()
                                                          .getIndividualResult(player));
                }

                playerScore.append(scoreToDisplay);
            }

            // game score text control creation
            TextView txtIndividualGameScore = new TextView(getContext());
            txtIndividualGameScore.setGravity(Gravity.CENTER);
            txtIndividualGameScore.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
            txtIndividualGameScore.setWidth(UIConstants.PLAYER_VIEW_WIDTH);
            txtIndividualGameScore.setBackgroundColor(color);
            txtIndividualGameScore.setTextColor(Color.BLACK);
            txtIndividualGameScore.setTypeface(null, Typeface.BOLD);
            txtIndividualGameScore.setText(playerScore);
            addView(txtIndividualGameScore);
        }
    }
}