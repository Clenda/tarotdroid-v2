package org.nla.tarotdroid.gameset.controls;

import android.content.Context;
import android.view.View;

import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.BelgianTarot3Game;
import org.nla.tarotdroid.biz.BelgianTarot4Game;
import org.nla.tarotdroid.biz.BelgianTarot5Game;
import org.nla.tarotdroid.biz.PassedGame;
import org.nla.tarotdroid.biz.PenaltyGame;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.biz.StandardTarot3Game;
import org.nla.tarotdroid.biz.StandardTarot4Game;
import org.nla.tarotdroid.biz.StandardTarot5Game;

import static com.google.common.base.Preconditions.checkArgument;

public final class RowFactory {

    private RowFactory() {
    }

    public static View buildGameRow(
            final Context context,
            final BaseGame game,
            final float weight
    ) {
        checkArgument(game != null, "game is null");

        // the row to return
        BaseGameRow gameRow = null;

        // 3 or 4 standard player style
        if (game instanceof StandardTarot3Game || game instanceof StandardTarot4Game) {
            gameRow = new StandardTarotGameRow(context, (StandardBaseGame) game,
                                               weight);
        }
        // 5 standard player style
        else if (game instanceof StandardTarot5Game) {
            gameRow = new StandardTarot5GameRow(context,
                                                (StandardBaseGame) game,
                                                weight);
        }
        // 3, 4 or 5 belgian player style
        else if (game instanceof BelgianTarot3Game || game instanceof BelgianTarot4Game || game instanceof BelgianTarot5Game) {
            gameRow = new BelgianTarotGameRow(context, game, weight);
        }
        // penalty game
        else if (game instanceof PenaltyGame) {
            gameRow = new PenaltyGameRow(context, game, weight);
        }
        // passed game
        else if (game instanceof PassedGame) {
            gameRow = new PassedGameRow(context, game, weight);
        } else {
            throw new IllegalArgumentException("game is of unknown type:" + game);
        }

        return gameRow;
    }
}