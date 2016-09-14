package org.nla.tarotdroid.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.helpers.AuditHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Nullable @BindView(R.id.toolbar) protected Toolbar toolbar;

    private boolean restarting;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);
        setupToolbar();
        getWindow().setTitle(getString(R.string.app_name));
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

    protected abstract void inject();

    @LayoutRes
    protected abstract int getLayoutResId();

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
