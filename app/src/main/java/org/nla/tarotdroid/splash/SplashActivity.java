package org.nla.tarotdroid.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.core.dal.IDalService;
import org.nla.tarotdroid.dashboard.MainDashboardActivity;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity {

    @Inject IDalService dalService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dalService.initialize();
                Intent i = new Intent(SplashActivity.this, MainDashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void inject() {
        TarotDroidApp.get(this).getComponent().inject(this);
    }
}