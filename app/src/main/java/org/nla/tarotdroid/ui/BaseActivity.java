package org.nla.tarotdroid.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.nla.tarotdroid.AppParams;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.UIHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Nullable @BindView(R.id.toolbar) protected Toolbar toolbar;
    @Inject AppParams appParams;

    private boolean restarting;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (shouldSetContentView()) {
            setContentView(getLayoutResId());
        }
        ButterKnife.bind(this);
        inject();
        setupToolbar();
        setKeepScreenOn();
        setTitle();
        auditEvent();
    }

    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(shouldDisplayHomeAsUpEnabled());
            getSupportActionBar().setDisplayShowTitleEnabled(shouldDisplayTitle());
        }
    }

    protected void setTitle() {
        if (shouldDisplayTitle()) {
            getWindow().setTitle(getString(getTitleResId()));
        }
    }

    protected boolean shouldSetContentView() {
        return true;
    }

    protected boolean shouldDisplayHomeAsUpEnabled() {
        return true;
    }

    protected boolean shouldDisplayTitle() {
        return true;
    }

    protected void setKeepScreenOn() {
        UIHelper.setKeepScreenOn(this, appParams.isKeepScreenOn());
    }

    protected abstract void inject();

    protected abstract void auditEvent();

    @LayoutRes
    protected abstract int getLayoutResId();

    @StringRes
    protected int getTitleResId() {
        return R.string.app_name;
    }

    @Override
    protected void onStart() {
        super.onStart();
        AuditHelper.auditSession(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        AuditHelper.stopSession(this);
    }
}
