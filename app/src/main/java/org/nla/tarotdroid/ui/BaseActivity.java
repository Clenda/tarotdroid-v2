package org.nla.tarotdroid.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.app.AppContext;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.UIHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Nullable @BindView(R.id.toolbar) protected Toolbar toolbar;

    private boolean restarting;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        setupToolbar();
        setKeepScreenOn();
        auditEvent();
        if (shouldDisplayTitle()) {
            getWindow().setTitle(getString(getTitleResId()));
        }
        inject();
    }

    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(shouldDisplayHomeAsUpEnabled());
            getSupportActionBar().setDisplayShowTitleEnabled(shouldDisplayTitle());
        }
    }

    protected boolean shouldDisplayHomeAsUpEnabled() {
        return true;
    }

    protected boolean shouldDisplayTitle() {
        return true;
    }

    protected void setKeepScreenOn() {
        UIHelper.setKeepScreenOn(this, AppContext.getApplication().getAppParams().isKeepScreenOn());
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
