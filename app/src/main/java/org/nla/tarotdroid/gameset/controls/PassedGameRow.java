package org.nla.tarotdroid.gameset.controls;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.PassedGame;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.constants.UIConstants;

import static com.google.common.base.Preconditions.checkArgument;

public class PassedGameRow extends BaseGameRow {

    protected PassedGameRow(
            final Context context,
            final AttributeSet attrs,
            final BaseGame game,
            final float weight
    ) {
        super(context, attrs, weight);
        checkArgument(game instanceof PassedGame, "Incorrect game type: " + game.getClass());
        this.game = game;
        this.initializeViews();
        this.setOnLongClickListener(this);
    }

    protected PassedGameRow(
            final Context context,
            final BaseGame game,
            final float weight
    ) {
        this(context, null, game, weight);
    }

    protected PassedGame getGame() {
        return (PassedGame) this.game;
    }

    protected void initializeViews() {
        // game bet label
        this.addView(ScoreCellFactory.buildPassedGameDescription(
                this.getContext(),
                this.game.getIndex()
        ));

        // each individual player score
        for (Player player : this.getGameSet().getPlayers()) {
            StringBuffer playerScore = new StringBuffer();
            int color;

            // dead player ?
            if (!game.getPlayers().contains(player)) {
                playerScore.append("#");
                color = Color.GRAY;
            }

            // in game player
            else {
                playerScore.append("-");
                color = Color.GRAY;
            }

            // game score text control creation
            TextView txtIndividualGameScore = new TextView(this.getContext());
            txtIndividualGameScore.setGravity(Gravity.CENTER);
            txtIndividualGameScore.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
            txtIndividualGameScore.setWidth(UIConstants.PLAYER_VIEW_WIDTH);
            txtIndividualGameScore.setBackgroundColor(color);
            txtIndividualGameScore.setTextColor(Color.BLACK);
            txtIndividualGameScore.setTypeface(null, Typeface.BOLD);
            txtIndividualGameScore.setText(playerScore);
            this.addView(txtIndividualGameScore);
        }
    }
}