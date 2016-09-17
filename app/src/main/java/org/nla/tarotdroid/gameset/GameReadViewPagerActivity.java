package org.nla.tarotdroid.gameset;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.constants.ActivityParams;
import org.nla.tarotdroid.core.BaseActivity;
import org.nla.tarotdroid.core.helpers.AuditHelper;

import java.util.List;

import butterknife.BindView;

import static com.google.common.collect.Lists.newArrayList;

public class GameReadViewPagerActivity extends BaseActivity {

    @BindView(R.id.pager) protected ViewPager viewPager;
    @BindView(R.id.tabs) protected TabLayout tabLayout;
    private PagerAdapter pagerAdapter;
    private int currentGameIndex;

    private DialogInterface.OnClickListener removeGameDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    new RemoveGameTask(GameReadViewPagerActivity.this,
                                       GameReadViewPagerActivity.this,
                                       getGameSet()).execute();
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
            currentGameIndex = getIntent().getExtras().getInt(ActivityParams.PARAM_GAME_INDEX);
            initialisePaging();
            ActionBar mActionBar = getSupportActionBar();
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
            setTitle(R.string.lblViewGameActivityTitle);
        } catch (Exception e) {
            auditHelper.auditError(AuditHelper.ErrorTypes.gameReadViewPagerActivityError, e, this);
        }
    }

    @Override
    protected void inject() {
        TarotDroidApp.get(this).getComponent().inject(this);
    }

    // TODO Implement
    @Override
    protected void auditEvent() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.simple_titles;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentGameIndex == getGameSet().getGameCount()) {
            MenuItem miTrash = menu.add(getString(R.string.lblDeleteGameItem));
            miTrash.setIcon(R.drawable.gd_action_bar_trashcan);
            miTrash.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
            miTrash.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameReadViewPagerActivity.this);
                    String dialogTitle = String.format(
                            getString(R.string.titleRemoveGameYesNo),
                            currentGameIndex
                    );
                    String dialogMessage = getString(R.string.msgRemoveGameYesNo);
                    builder.setTitle(dialogTitle);
                    builder.setMessage(dialogMessage);
                    builder.setPositiveButton(getString(R.string.btnOk),
                                              removeGameDialogClickListener);
                    builder.setNegativeButton(getString(R.string.btnCancel),
                                              removeGameDialogClickListener);
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
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentGameIndex = tab.getPosition() + 1;
                invalidateOptionsMenu();
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
        for (BaseGame game : getGameSet().getGames()) {
            fragments.add(GameReadFragment.newInstance(game.getIndex(), getGameSet()));
        }
        pagerAdapter = new GameReadPagerAdapter(super.getSupportFragmentManager(),
                                                fragments,
                                                getGameSet());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentGameIndex - 1);
    }
}