package org.nla.tarotdroid.gameset.controls;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.BelgianTarot3Game;
import org.nla.tarotdroid.biz.BelgianTarot4Game;
import org.nla.tarotdroid.biz.BelgianTarot5Game;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.constants.UIConstants;

public class BelgianTarotGameRow extends BaseGameRow {

    protected BelgianTarotGameRow(
            final Context context,
            final AttributeSet attrs,
            final BaseGame game,
            final float weight
    ) {
        super(context, attrs, weight);
        if (!(game instanceof BelgianTarot3Game || game instanceof BelgianTarot4Game || game instanceof BelgianTarot5Game)) {
            throw new IllegalArgumentException("Incorrect game type: " + game.getClass());
        }
        this.game = game;
        initializeViews();
        setOnLongClickListener(this);
    }

    protected BelgianTarotGameRow(
            final Context context,
            final BaseGame game,
            final float weight
    ) {
        this(context, null, game, weight);
    }

    protected BaseGame getGame() {
        return game;
    }

    protected void initializeViews() {
        // game bet label
        addView(ScoreCellFactory.buildBelgianGameDescription(
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
                color = getResources().getColor(R.color.DefensePlayer);
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