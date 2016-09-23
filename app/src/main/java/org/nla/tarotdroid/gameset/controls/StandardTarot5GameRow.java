package org.nla.tarotdroid.gameset.controls;

import android.content.Context;
import android.util.AttributeSet;

import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.biz.StandardTarot5Game;

public class StandardTarot5GameRow extends BaseGameRow {

    protected StandardTarot5GameRow(
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

    protected StandardTarot5GameRow(
            final Context context,
            final StandardBaseGame game,
            final float weight
    ) {
        this(context, null, game, weight);
    }

    public StandardTarot5Game getGame() {
        return (StandardTarot5Game) game;
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
            }

            // in game player
            else {
                // get score to display, depending on the user settings
                if (appParams.isDisplayGlobalScoresForEachGame()) {
                    playerScore = getGameSet().getGameSetScores()
                                              .getIndividualResultsAtGameOfIndex(game.getIndex(),
                                                                                 player);
                } else {
                    playerScore = game.getGameScores().getIndividualResult(player);
                }


                // called player
                if (
                        player == getGame().getCalledPlayer() ||            // called player
                                (
                                        getGame().isLeaderAlone() &&                    // leading player is called player
                                                getGame().getLeadingPlayer() == player
                                )
                        ) {
                    addView(ScoreCellFactory.buildCalledPlayerCell(
                            getContext(),
                            playerScore,
                            getGame().getCalledKing().getKingType())
                    );
                    continue;
                }

                // leading player
                else if (player == getGame().getLeadingPlayer()) {
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