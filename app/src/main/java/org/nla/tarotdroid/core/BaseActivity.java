package org.nla.tarotdroid.core;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.nla.tarotdroid.BuildConfig;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.core.dal.IDalService;
import org.nla.tarotdroid.core.helpers.AuditHelper;
import org.nla.tarotdroid.core.helpers.LocalizationHelper;
import org.nla.tarotdroid.core.helpers.UIHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected @Inject AppParams appParams;
    protected @Inject AuditHelper auditHelper;
    protected @Inject LocalizationHelper localizationHelper;
    protected @Inject IDalService dalService;
    protected @Inject GameSetWrapper gameSetWrapper;
    protected @Nullable @BindView(R.id.toolbar) Toolbar toolbar;
    protected ProgressDialog progressDialog;

    private boolean restarting;

    protected void inject() {
        TarotDroidApp.get(this).getComponent().inject(this);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (shouldSetContentView()) {
            setContentView(getLayoutResId());
        }
        ButterKnife.bind(this);
        inject();
        setupToolbar();
        setupProgressDialog();
        setKeepScreenOn();
        setTitle();
        auditEvent();
    }

    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
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
        auditHelper.auditSession(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auditHelper.stopSession(this);
    }

    protected void showProgressDialogWithText(@StringRes int stringResource) {
        progressDialog.setMessage(getResources().getString(stringResource));
        progressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void onGenericCallbackError(Exception e) {
        dismissProgressDialog();
        Log.v(BuildConfig.APP_LOG_TAG, this.getClass().toString(), e);

//        Toast.makeText(
//                this,
//                "Error: " + e,
//                Toast.LENGTH_LONG
//        ).show();

        UIHelper.showSimpleRichTextDialog(
                this,
                getString(R.string.msgGenericError),
                getString(R.string.titleGenericError));

    }
}
