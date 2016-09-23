package org.nla.tarotdroid.gameset.controls;

import android.content.Context;
import android.util.AttributeSet;

import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.StandardBaseGame;

public class StandardTarotGameRow extends BaseGameRow {

    protected StandardTarotGameRow(
            final Context context,
            final AttributeSet attrs,
            final StandardBaseGame game,
            final float weight
    ) {
        super(context, attrs, weight);
        this.game = game;
        initializeViews();
        setOnLongClickListener(this);
    }

    public StandardTarotGameRow(
            final Context context,
            final StandardBaseGame game,
            final float weight
    ) {
        this(context, null, game, weight);
    }

    public StandardBaseGame getGame() {
        return (StandardBaseGame) game;
    }

    protected void initializeViews() {
        // game bet label
        addView(ScoreCellFactory.buildStandardGameDescription(
                getContext(),
                getGame().getBet().getBetType(),
                (int) getGame().getDifferentialPoints(),
                game.getIndex(),
                localizationHelper
        ));

        // each individual player score
        for (Player player : getGameSet().getPlayers()) {
            int playerScore = 0;

            // dead player
            if (!game.getPlayers().contains(player)) {
                addView(ScoreCellFactory.buildDeadPlayerCell(getContext()));
                continue;
            } else {
                // get score to display, depending on the user settings
                if (appParams.isDisplayGlobalScoresForEachGame()) {
                    playerScore = getGameSet().getGameSetScores()
                                              .getIndividualResultsAtGameOfIndex(game.getIndex(),
                                                                                 player);
                } else {
                    playerScore = game.getGameScores().getIndividualResult(player);
                }

                // leading player
                if (player == getGame().getLeadingPlayer()) {
                    addView(ScoreCellFactory.buildLeaderPlayerCell(
                            getContext(),
                            playerScore)
                    );
                    continue;
                }

                // defense player
                else {
                    addView(ScoreCellFactory.buildDefensePlayerCell(
                            getContext(),
                            playerScore)
                    );
                    continue;
                }
            }
        }
    }
}