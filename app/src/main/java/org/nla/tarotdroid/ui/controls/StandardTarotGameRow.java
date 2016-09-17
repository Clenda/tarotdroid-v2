package org.nla.tarotdroid.ui.controls;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.AttributeSet;

import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.helpers.LocalizationHelper;

import javax.inject.Inject;

public class StandardTarotGameRow extends BaseGameRow {

    @Inject LocalizationHelper localizationHelper;

    protected StandardTarotGameRow(
            final Context context,
            final ProgressDialog dialog,
            final AttributeSet attrs,
            final StandardBaseGame game,
            final float weight,
            final GameSet gameSet
    ) {
        super(context, dialog, attrs, weight, gameSet);
        this.game = game;
        TarotDroidApp.get(context).getComponent().inject(this);
        initializeViews();
        setOnLongClickListener(this);
    }

    public StandardTarotGameRow(
            final Context context,
            final ProgressDialog dialog,
            final StandardBaseGame game,
            final float weight,
            final GameSet gameSet
    ) {
        this(context, dialog, null, game, weight, gameSet);
        TarotDroidApp.get(context).getComponent().inject(this);
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