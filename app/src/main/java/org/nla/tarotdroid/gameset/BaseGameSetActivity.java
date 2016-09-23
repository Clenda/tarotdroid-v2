package org.nla.tarotdroid.gameset;

import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.core.BaseActivity;

public abstract class BaseGameSetActivity extends BaseActivity {

    protected GameSet getGameSet() {
        return gameSetWrapper.getGameSet();
    }
}
