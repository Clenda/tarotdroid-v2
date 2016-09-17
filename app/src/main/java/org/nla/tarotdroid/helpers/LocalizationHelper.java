package org.nla.tarotdroid.helpers;

import android.content.Context;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.biz.enums.KingType;

import java.text.DateFormat;

public final class LocalizationHelper {

    private final Context context;

    public LocalizationHelper(Context context) {
        this.context = context;
    }

    public String buildGameSetHistoryTitle(final GameSet gameSet) {
        String formatedDate = DateFormat.getInstance().format(gameSet.getCreationTs());

        return String.format(
                context.getResources().getString(R.string.lblGameSetTitle),
                formatedDate
        );
    }

    public String buildGameSetHistoryDescription(final GameSet gameSet) {
        String playersIdsAsString = "";
        for (Player player : gameSet.getPlayers()) {
            playersIdsAsString += player.getName() + ", ";
        }
        String gameCountAsString = Integer.toString(gameSet.getGameCount());

        String toFormat = "0".equals(gameCountAsString)
                ? context.getResources().getString(R.string.lblGameSetDescriptionSingle)
                : context.getResources().getString(R.string.lblGameSetDescriptionPlural);

        return String.format(
                toFormat,
                gameCountAsString,
                playersIdsAsString.substring(0, playersIdsAsString.length() - 2)
        );
    }

    public String getKingTranslation(final KingType king) {
        switch (king) {
            case Clubs:
                return context.getResources().getString(R.string.lblClubsColor);
            case Diamonds:
                return context.getResources().getString(R.string.lblDiamondsColor);
            case Hearts:
                return context.getResources().getString(R.string.lblHeartsColor);
            case Spades:
                return context.getResources().getString(R.string.lblSpadesColor);
            default:
                return "unknow Colors";
        }
    }

    public String getBetTranslation(final BetType bet) {
        switch (bet) {
            case Prise:
                return context.getResources().getString(R.string.priseDescription);
            case Petite:
                return context.getResources().getString(R.string.petiteDescription);
            case Garde:
                return context.getResources().getString(R.string.gardeDescription);
            case GardeSans:
                return context.getResources().getString(R.string.gardeSansDescription);
            case GardeContre:
                return context.getResources().getString(R.string.gardeContreDescription);
            case Belge:
                return context.getResources().getString(R.string.belgeDescription);
            default:
                return "unknow BetType";
        }
    }

    public String getShortBetTranslation(final BetType bet) {
        switch (bet) {
            case Petite:
                return TarotDroidApp.get()
                                    .getResources()
                                    .getString(R.string.shortPetiteDescription);
            case Prise:
                return TarotDroidApp.get().getResources().getString(R.string.shortPriseDescription);
            case Garde:
                return TarotDroidApp.get().getResources().getString(R.string.shortGardeDescription);
            case GardeSans:
                return context.getResources().getString(R.string.shortGardeSansDescription);
            case GardeContre:
                return context.getResources().getString(R.string.shortGardeContreDescription);
            case Belge:
                return context.getResources().getString(R.string.shortBelgeDescription);
            default:
                return "unknow BetType";
        }
    }
}