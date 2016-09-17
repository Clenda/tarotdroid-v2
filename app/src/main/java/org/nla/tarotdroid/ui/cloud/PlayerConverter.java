package org.nla.tarotdroid.ui.cloud;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.clientmodel.RestPlayer;
import org.nla.tarotdroid.dal.DalException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayerConverter {

    private static final Function<RestPlayer, Player> restPlayerToModelPlayerUsingCacheFunction = new Function<RestPlayer, Player>() {

        @Override
        public Player apply(RestPlayer restPlayer) {
            Player toReturn = null;

            // try to get player from repo
            try {
                toReturn = TarotDroidApp.get()
                                        .getDalService()
                                        .getPlayerByUuid(restPlayer.getUuid());
            } catch (DalException e) {
                toReturn = null;
            }

            // if not found, create player
            if (toReturn == null) {
                toReturn = new Player();
                toReturn.setUuid(restPlayer.getUuid());
                toReturn.setCreationTs(new Date(restPlayer.getCreationTs()));
                toReturn.setName(restPlayer.getName());
            }

            return toReturn;
        }
    };
    private static final Function<RestPlayer, Player> restPlayerToModelPlayerWithoutCacheFunction = new Function<RestPlayer, Player>() {

        @Override
        public Player apply(RestPlayer restPlayer) {
            Player toReturn = new Player();
            toReturn.setUuid(restPlayer.getUuid());
            toReturn.setCreationTs(new Date(restPlayer.getCreationTs()));
            toReturn.setName(restPlayer.getName());
            //toReturn.setEmail(restPlayer.getEmail());
            //toReturn.setPictureUri(restPlayer.getPictureUri());

            return toReturn;
        }
    };
    private static final Function<Player, RestPlayer> modelPlayerToRestPlayerFunction = new Function<Player, RestPlayer>() {

        @Override
        public RestPlayer apply(Player player) {
            RestPlayer toReturn = new RestPlayer();
            toReturn.setUuid(player.getUuid());
            toReturn.setCreationTs(player.getCreationTs().getTime());
            toReturn.setName(player.getName());
            toReturn.setEmail(player.getEmail());
            toReturn.setPictureUri(player.getPictureUri());
            toReturn.setValid(true);
            return toReturn;
        }
    };
    private static final Function<String, RestPlayer> cloudIdToRestPlayerFunction = new Function<String, RestPlayer>() {

        @Override
        public RestPlayer apply(String playerId) {
            RestPlayer toReturn = new RestPlayer();
            toReturn.setUuid(playerId);
            toReturn.setValid(false);
            return toReturn;
        }
    };

    private PlayerConverter() {
    }

    public static Player convertFromRest(RestPlayer restPlayer, boolean searchInCache) {
        if (restPlayer == null) {
            return null;
        }


        if (searchInCache) {
            return restPlayerToModelPlayerUsingCacheFunction.apply(restPlayer);
        } else {
            return restPlayerToModelPlayerWithoutCacheFunction.apply(restPlayer);
        }

    }

    public static RestPlayer convertToRest(Player player) {
        if (player == null) {
            return null;
        }

        return modelPlayerToRestPlayerFunction.apply(player);
    }

    public static List<Player> convertFromRest(
            List<RestPlayer> restPlayers,
            boolean searchInCache
    ) {
        if (restPlayers == null) {
            return null;
        }

        if (searchInCache) {
            return Lists.transform(restPlayers, restPlayerToModelPlayerUsingCacheFunction);
        } else {
            return Lists.transform(restPlayers, restPlayerToModelPlayerWithoutCacheFunction);
        }
    }

    public static List<RestPlayer> convertToRest(List<Player> players) {
        if (players == null) {
            return null;
        }

        return Lists.transform(players, modelPlayerToRestPlayerFunction);
    }

    public static List<RestPlayer> convertToRestForInvalidation(ArrayList<String> idsOfPlayersToInvalidate) {
        if (idsOfPlayersToInvalidate == null) {
            return null;
        }

        return Lists.transform(idsOfPlayersToInvalidate, cloudIdToRestPlayerFunction);
    }
}
