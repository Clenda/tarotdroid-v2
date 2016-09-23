package org.nla.tarotdroid.gameset.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.core.AppParams;
import org.nla.tarotdroid.core.GameSetWrapper;
import org.nla.tarotdroid.core.helpers.LocalizationHelper;

import javax.inject.Inject;

public abstract class BaseRow extends LinearLayout {

    protected @Inject GameSetWrapper gameSetWrapper;
    protected @Inject LocalizationHelper localizationHelper;
    protected @Inject AppParams appParams;

    protected BaseRow(
            final Context context,
            final AttributeSet attrs,
            final float weight
    ) {
        super(context, attrs);
        inject();
        setOrientation(HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                         LayoutParams.MATCH_PARENT,
                                         weight));
        setWeightSum(getGameSet().getPlayers().size() + 1);
    }

    protected BaseRow(
            final Context context,
            final AttributeSet attrs
    ) {
        super(context, attrs);
        inject();
        setOrientation(HORIZONTAL);
    }

    protected void inject() {
        TarotDroidApp.get(getContext()).getComponent().inject(this);
    }

    protected GameSet getGameSet() {
        return gameSetWrapper.getGameSet();
    }
}
