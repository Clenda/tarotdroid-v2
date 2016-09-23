package org.nla.tarotdroid.gameset;

import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.core.BaseFragment;

public abstract class BaseGameSetFragment extends BaseFragment {

    // TODO Repasser en protected
    public GameSet getGameSet() {
        return gameSetWrapper.getGameSet();
    }

}
