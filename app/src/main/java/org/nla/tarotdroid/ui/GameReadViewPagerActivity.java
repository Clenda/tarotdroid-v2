package org.nla.tarotdroid.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.nla.tarotdroid.AppContext;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.UIHelper;
import org.nla.tarotdroid.ui.constants.ActivityParams;
import org.nla.tarotdroid.ui.tasks.RemoveGameTask;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class GameReadViewPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private int currentGameIndex;

    private DialogInterface.OnClickListener removeGameDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    new RemoveGameTask(GameReadViewPagerActivity.this,
                                       GameReadViewPagerActivity.this,
                                       GameReadViewPagerActivity.this.getGameSet()).execute();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            super.setContentView(R.layout.simple_titles);
            currentGameIndex = this.getIntent().getExtras().getInt(ActivityParams.PARAM_GAME_INDEX);
            UIHelper.setKeepScreenOn(this,
                                     AppContext.getApplication().getAppParams().isKeepScreenOn());

            initialisePaging();

            ActionBar mActionBar = getSupportActionBar();
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            this.setTitle(R.string.lblViewGameActivityTitle);
        } catch (Exception e) {
            AuditHelper.auditError(AuditHelper.ErrorTypes.gameReadViewPagerActivityError, e, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.currentGameIndex == this.getGameSet().getGameCount()) {
            MenuItem miTrash = menu.add(this.getString(R.string.lblDeleteGameItem));
            miTrash.setIcon(R.drawable.gd_action_bar_trashcan);
            miTrash.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
            miTrash.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameReadViewPagerActivity.this);
                    String dialogTitle = String.format(
                            GameReadViewPagerActivity.this.getString(R.string.titleRemoveGameYesNo),
                            GameReadViewPagerActivity.this.currentGameIndex
                    );
                    String dialogMessage = GameReadViewPagerActivity.this.getString(R.string.msgRemoveGameYesNo);
                    builder.setTitle(dialogTitle);
                    builder.setMessage(dialogMessage);
                    builder.setPositiveButton(GameReadViewPagerActivity.this.getString(R.string.btnOk),
                                              GameReadViewPagerActivity.this.removeGameDialogClickListener);
                    builder.setNegativeButton(GameReadViewPagerActivity.this.getString(R.string.btnCancel),
                                              GameReadViewPagerActivity.this.removeGameDialogClickListener);
                    builder.show();
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    return true;
                }
            });
        }
        return true;
    }

    private GameSet getGameSet() {
        return TabGameSetActivity.getInstance().gameSet;
    }

    private void initialisePaging() {
        viewPager = (ViewPager) super.findViewById(R.id.pager);
        setupViewPager();
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                GameReadViewPagerActivity.this.currentGameIndex = tab.getPosition() + 1;
                GameReadViewPagerActivity.this.invalidateOptionsMenu();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // TODO Re add visual indicators (dots at the bottom of the screen)
    }

    private void setupViewPager() {
        List<Fragment> fragments = newArrayList();
        for (BaseGame game : this.getGameSet().getGames()) {
            fragments.add(GameReadFragment.newInstance(game.getIndex(), this.getGameSet()));
        }
        pagerAdapter = new GameReadPagerAdapter(super.getSupportFragmentManager(),
                                                fragments,
                                                this.getGameSet());
        viewPager.setAdapter(this.pagerAdapter);
        viewPager.setCurrentItem(this.currentGameIndex - 1);
    }
}