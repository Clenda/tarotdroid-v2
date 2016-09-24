package org.nla.tarotdroid.gameset;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.constants.RequestCodes;
import org.nla.tarotdroid.constants.ResultCodes;
import org.nla.tarotdroid.core.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.core.helpers.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TabGameSetActivity extends BaseGameSetActivity {

    private final DialogInterface.OnClickListener leavingDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    TabGameSetActivity.this.finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
                default:
                    break;
            }
        }
    };

    @BindView(R.id.tabs) protected TabLayout tabLayout;
    @BindView(R.id.pager) protected ViewPager viewPager;

    private GameSetGamesFragment gameSetGamesFragment;
    private GameSetSynthesisFragment gameSetSynthesisFragment;
    private PagerAdapter pagerAdapter;
    private String shortenedUrl;
    private int startPage;

    @Override
    protected void auditEvent() {
        // TODO refactor by passing parameters in the intent...
//        if (this.gameSet == null) {
//            auditHelper.auditEvent(AuditHelper.EventTypes.tabGameSetActivity_auditEvent_GameSetIsNull);
//            UIHelper.showSimpleRichTextDialog(this,
//                                              this.getString(R.string.msgUnmanagedErrorGameSetLost),
//                                              this.getString(R.string.titleUnmanagedErrorGameSetLost));
//            finish();
//
//        } else if (this.gameSet.getGameCount() == 0) {
//            Map<ParameterTypes, Object> parameters = new HashMap<>();
//            parameters.put(ParameterTypes.gameStyleType, this.gameSet.getGameStyleType());
//            parameters.put(ParameterTypes.playerCount, this.gameSet.getPlayers().size());
//            auditHelper.auditEvent(AuditHelper.EventTypes.displayTabGameSetPageWithNewGameSetAction,
//                                   parameters);
//        } else {
//            auditHelper.auditEvent(AuditHelper.EventTypes.displayTabGameSetPageWithExistingGameSetAction);
//        }
    }

    /**
     * Builds the menu for devices with version >= 4.2.
     */
    private void buildMenuForNewAndroidDevices(Menu menu) {
        MenuItem miSettings = menu.add(this.getString(R.string.lblPrefsItem));
        miSettings.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        miSettings.setIcon(R.drawable.perm_group_system_tools);
        miSettings.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(TabGameSetActivity.this,
                                           TabGameSetPreferencesActivity.class);
                TabGameSetActivity.this.startActivity(intent);
                return true;
            }
        });

        MenuItem miHelp = menu.add(this.getString(R.string.lblHelpItem));
        miHelp.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        miHelp.setIcon(R.drawable.gd_action_bar_help);
        miHelp.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                UIHelper.showSimpleRichTextDialog(TabGameSetActivity.this,
                                                  TabGameSetActivity.this.getText(R.string.msgHelp)
                                                                         .toString(),
                                                  TabGameSetActivity.this.getString(R.string.titleHelp));
                return true;
            }
        });
    }

    /**
     * Builds a menu including a submenu for old devices.
     */
    private void buildMenuForOldAndroidDevices(Menu menu) {
        SubMenu subMenuMore = menu.addSubMenu("+");
        MenuItem subMenuMoreItem = subMenuMore.getItem();
        subMenuMoreItem.setIcon(R.drawable.ic_menu_moreoverflow_normal_holo_light);
        subMenuMoreItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem miSettings = subMenuMore.add(this.getString(R.string.lblPrefsItem));
        miSettings.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        miSettings.setIcon(R.drawable.perm_group_system_tools);
        miSettings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(TabGameSetActivity.this,
                                           TabGameSetPreferencesActivity.class);
                TabGameSetActivity.this.startActivity(intent);
                return true;
            }
        });

        MenuItem miHelp = subMenuMore.add(this.getString(R.string.lblHelpItem));
        miHelp.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        miHelp.setIcon(R.drawable.gd_action_bar_help);
        miHelp.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                UIHelper.showSimpleRichTextDialog(TabGameSetActivity.this,
                                                  TabGameSetActivity.this.getText(R.string.msgHelp)
                                                                         .toString(),
                                                  TabGameSetActivity.this.getString(R.string.titleHelp));
                return true;
            }
        });
    }

    private void initialisePaging() {
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        gameSetGamesFragment = GameSetGamesFragment.newInstance();
        gameSetSynthesisFragment = GameSetSynthesisFragment.newInstance();
        fragments.add(this.gameSetGamesFragment);
        fragments.add(this.gameSetSynthesisFragment);
        pagerAdapter = new TabGameSetPagerAdapter(super.getSupportFragmentManager(), fragments);
        viewPager.setAdapter(this.pagerAdapter);
        viewPager.setCurrentItem(this.startPage - 1);
    }

    private void navigateTowardsGameCreationActivity(
            Class<? extends BaseGameActivity> gameCreationActivityClass
    ) {
        Intent intent = new Intent(TabGameSetActivity.this, gameCreationActivityClass);
        startActivityForResult(intent, RequestCodes.ADD_GAME);
    }

    private void navigateTowardsBelgianGameCreationActivity() {
        navigateTowardsGameCreationActivity(BelgianGameActivity.class);
    }

    private void navigateTowardsPassedGameCreationActivity() {
        navigateTowardsGameCreationActivity(PassActivity.class);
    }

    private void navigateTowardsPenaltyGameCreationActivity() {
        navigateTowardsGameCreationActivity(PenaltyActivity.class);
    }

    private void navigateTowardsStandardGameCreationActivity() {
        navigateTowardsGameCreationActivity(StandardGameActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodes.ADD_GAME && resultCode == ResultCodes.AddGame_Ok) {
            UIHelper.showModifyOrDeleteGameMessage(this);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String dialogTitle = getGameSet().isPersisted()
                ? this.getString(R.string.titleExitGameSetYesNoWithDAL)
                : this.getString(R.string.titleExitGameSetYesNo);
        String dialogMessage = getGameSet().isPersisted()
                ? this.getText(R.string.msgExitGameSetYesNoWithDAL).toString()
                : this.getText(R.string.msgExitGameSetYesNo).toString();

        builder.setTitle(dialogTitle);
        builder.setMessage(Html.fromHtml(dialogMessage));
        builder.setPositiveButton(this.getString(R.string.btnOk), this.leavingDialogClickListener);
        builder.setNegativeButton(this.getString(R.string.btnCancel),
                                  this.leavingDialogClickListener).show();
        builder.setIcon(android.R.drawable.ic_dialog_alert);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            initialisePaging();
            ActionBar mActionBar = getSupportActionBar();
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(true);
        } catch (Exception e) {
            auditHelper.auditError(ErrorTypes.tabGameSetActivityError, e, this);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.simple_titles;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (appParams.isBelgianGamesAllowed() || appParams.isPenaltyGamesAllowed() || appParams.isPassedGamesAllowed()) {
            // prepare menu
            SubMenu subMenuAdd = menu.addSubMenu(R.string.lblAddGameItem);
            MenuItem subMenuAddItem = subMenuAdd.getItem();
            subMenuAddItem.setIcon(R.drawable.gd_action_bar_add);
            subMenuAddItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            // Add standard game, always present
            MenuItem miAddStdGame = subMenuAdd.add(R.string.lblAddGameStandardItem);
            miAddStdGame.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    TabGameSetActivity.this.navigateTowardsStandardGameCreationActivity();
                    return true;
                }
            });

            // Add belgian games, optional
            if (appParams.isBelgianGamesAllowed()) {
                MenuItem miAddBelgianGame = subMenuAdd.add(R.string.lblAddGameBelgianItem);
                miAddBelgianGame.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        TabGameSetActivity.this.navigateTowardsBelgianGameCreationActivity();
                        return true;
                    }
                });
            }

            // Add pass, optional
            if (appParams.isPassedGamesAllowed()) {
                MenuItem miAddPassedGame = subMenuAdd.add(R.string.lblAddGamePassedItem);
                miAddPassedGame.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        TabGameSetActivity.this.navigateTowardsPassedGameCreationActivity();
                        return true;
                    }
                });
            }

            // Add penalty, optional
            if (appParams.isPenaltyGamesAllowed()) {
                MenuItem miAddPenaltyGame = subMenuAdd.add(R.string.lblAddGamePenaltyItem);
                miAddPenaltyGame.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        TabGameSetActivity.this.navigateTowardsPenaltyGameCreationActivity();
                        return true;
                    }
                });
            }
        } else {
            MenuItem miAddGame = menu.add(this.getString(R.string.lblAddGameItem));
            miAddGame.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            miAddGame.setIcon(R.drawable.gd_action_bar_add);
            miAddGame.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    TabGameSetActivity.this.navigateTowardsStandardGameCreationActivity();
                    return true;
                }
            });
        }

        MenuItem miStats = menu.add(this.getString(R.string.lblDisplayStatsItem));
        miStats.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        miStats.setIcon(R.drawable.gd_action_bar_pie_chart);
        miStats.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = null;
                intent = new Intent(TabGameSetActivity.this, GameSetChartViewPagerActivity.class);
                startActivity(intent);

                return true;
            }
        });

        if (android.os.Build.VERSION.SDK_INT > 15) {
            buildMenuForNewAndroidDevices(menu);
        } else {
            buildMenuForOldAndroidDevices(menu);
        }
        return true;
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            invalidateOptionsMenu();
        } catch (Exception e) {
            auditHelper.auditError(ErrorTypes.tabGameSetActivityOnResumeError, e);
            this.finish();
        }
    }

    protected class TabGameSetPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragments;

        public TabGameSetPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources()
                            .getString(R.string.lblGamesTitle);
                case 1:
                    return getResources()
                            .getString(R.string.lblSynthesisTitle);
                default:
                    return "Unknown[" + position + "]";
            }
        }
    }
}