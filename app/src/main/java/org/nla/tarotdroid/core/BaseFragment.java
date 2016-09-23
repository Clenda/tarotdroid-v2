package org.nla.tarotdroid.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.core.dal.IDalService;
import org.nla.tarotdroid.core.helpers.AuditHelper;
import org.nla.tarotdroid.core.helpers.LocalizationHelper;

import javax.inject.Inject;

public abstract class BaseFragment extends Fragment {

    public @Inject AppParams appParams;
    public @Inject AuditHelper auditHelper;
    public @Inject LocalizationHelper localizationHelper;
    public @Inject IDalService dalService;
    protected @Inject GameSetWrapper gameSetWrapper;

    protected void inject() {
        TarotDroidApp.get(getContext()).getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        inject();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
