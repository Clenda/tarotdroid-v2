package org.nla.tarotdroid.core;

import org.nla.tarotdroid.biz.GameSet;

public class GameSetWrapper {

    private GameSet gameSet;

    public GameSet getGameSet() {
        return gameSet;
    }

    public void setGameSet(final GameSet gameSet) {
        this.gameSet = gameSet;
    }

    public void clearGameSet() {
        setGameSet(null);
    }

    public boolean isGameSetNull() {
        return gameSet == null;
    }
}
