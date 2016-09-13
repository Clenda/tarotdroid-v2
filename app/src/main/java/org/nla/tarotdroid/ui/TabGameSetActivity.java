package org.nla.tarotdroid.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.app.AppContext;
import org.nla.tarotdroid.app.AppParams;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.helpers.AuditHelper.ParameterTypes;
import org.nla.tarotdroid.helpers.UIHelper;
import org.nla.tarotdroid.ui.GameCreationActivity.GameType;
import org.nla.tarotdroid.ui.constants.ActivityParams;
import org.nla.tarotdroid.ui.constants.RequestCodes;
import org.nla.tarotdroid.ui.constants.ResultCodes;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class TabGameSetActivity extends AppCompatActivity {

    private static TabGameSetActivity instance;

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

    protected GameSet gameSet;
    protected ProgressDialog progressDialog;
    private GameSetGamesFragment gameSetGamesFragment;
    private GameSetSynthesisFragment gameSetSynthesisFragment;
	private ViewPager viewPager;
	private TabLayout tabLayout;
	private PagerAdapter pagerAdapter;
	private String shortenedUrl;
    private int startPage;

    public static TabGameSetActivity getInstance() {
        return instance;
    }

	private void auditEvent() {
		if (this.gameSet == null) {
			AuditHelper.auditEvent(AuditHelper.EventTypes.tabGameSetActivity_auditEvent_GameSetIsNull);

			UIHelper.showSimpleRichTextDialog(this, this.getString(R.string.msgUnmanagedErrorGameSetLost), this.getString(R.string.titleUnmanagedErrorGameSetLost));
			this.finish();

		} else if (this.gameSet.getGameCount() == 0) {
			Map<ParameterTypes, Object> parameters = newHashMap();
			parameters.put(ParameterTypes.gameStyleType, this.gameSet.getGameStyleType());
			parameters.put(ParameterTypes.playerCount, this.gameSet.getPlayers().size());
			AuditHelper.auditEvent(AuditHelper.EventTypes.displayTabGameSetPageWithNewGameSetAction, parameters);
		} else {
			AuditHelper.auditEvent(AuditHelper.EventTypes.displayTabGameSetPageWithExistingGameSetAction);
		}
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
                Intent intent = new Intent(TabGameSetActivity.this, TabGameSetPreferencesActivity.class);
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
                Intent intent = new Intent(TabGameSetActivity.this, TabGameSetPreferencesActivity.class);
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

	public GameSet getGameSet() {
        return this.gameSet;
    }

    private void initialisePaging() {
		viewPager = (ViewPager) super.findViewById(R.id.pager);
		setupViewPager();
		tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(viewPager);
	}

	private void setupViewPager() {
		List<Fragment> fragments = newArrayList();
		this.gameSetGamesFragment = GameSetGamesFragment.newInstance();
		this.gameSetSynthesisFragment = GameSetSynthesisFragment.newInstance();
		fragments.add(this.gameSetGamesFragment);
		fragments.add(this.gameSetSynthesisFragment);
		pagerAdapter = new TabGameSetPagerAdapter(super.getSupportFragmentManager(), fragments);
		viewPager.setAdapter(this.pagerAdapter);
		viewPager.setCurrentItem(this.startPage - 1);
	}

	private void navigateTowardsBelgianGameCreationActivity() {
		this.navigateTowardsGameCreationActivity(GameCreationActivity.GameType.Belgian);
	}

	private void navigateTowardsGameCreationActivity(GameType gametype) {
		Intent intent = new Intent(TabGameSetActivity.this, GameCreationActivity.class);
		intent.putExtra(ActivityParams.PARAM_TYPE_OF_GAME, gametype.toString());
		this.startActivityForResult(intent, RequestCodes.ADD_GAME);
	}

	private void navigateTowardsPassedGameCreationActivity() {
		this.navigateTowardsGameCreationActivity(GameCreationActivity.GameType.Pass);
	}

	private void navigateTowardsPenaltyGameCreationActivity() {
		this.navigateTowardsGameCreationActivity(GameCreationActivity.GameType.Penalty);
	}

	private void navigateTowardsStandardGameCreationActivity() {
		this.navigateTowardsGameCreationActivity(GameCreationActivity.GameType.Standard);
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
		String dialogTitle = this.gameSet.isPersisted() ? this.getString(R.string.titleExitGameSetYesNoWithDAL) : this.getString(R.string.titleExitGameSetYesNo);
		String dialogMessage = this.gameSet.isPersisted() ? this.getText(R.string.msgExitGameSetYesNoWithDAL).toString() : this.getText(R.string.msgExitGameSetYesNo).toString();

		builder.setTitle(dialogTitle);
		builder.setMessage(Html.fromHtml(dialogMessage));
		builder.setPositiveButton(this.getString(R.string.btnOk), this.leavingDialogClickListener);
		builder.setNegativeButton(this.getString(R.string.btnCancel), this.leavingDialogClickListener).show();
		builder.setIcon(android.R.drawable.ic_dialog_alert);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			this.setContentView(R.layout.simple_titles);

			// check params
			Bundle args = this.getIntent().getExtras();
			if (args.containsKey(ActivityParams.PARAM_GAMESET_ID)) {
				this.gameSet = AppContext.getApplication().getDalService().getGameSetById(args.getLong(ActivityParams.PARAM_GAMESET_ID));
			} else if (args.containsKey(ActivityParams.PARAM_GAMESET_SERIALIZED)) {
				this.gameSet = (GameSet) args.getSerializable(ActivityParams.PARAM_GAMESET_SERIALIZED);
			} else {
				throw new IllegalArgumentException("Game set id or serialized game set must be provided");
			}

			// instantiate fragments
			this.gameSetGamesFragment = GameSetGamesFragment.newInstance();
			this.gameSetSynthesisFragment = GameSetSynthesisFragment.newInstance();

			this.auditEvent();
			instance = this;

			UIHelper.setKeepScreenOn(this, AppContext.getApplication().getAppParams().isKeepScreenOn());

			this.initialisePaging();

			ActionBar mActionBar = getSupportActionBar();
			mActionBar.setHomeButtonEnabled(true);
			mActionBar.setDisplayShowHomeEnabled(true);
			this.progressDialog = new ProgressDialog(this);
			this.progressDialog.setCancelable(false);
		} catch (Exception e) {
			AuditHelper.auditError(ErrorTypes.tabGameSetActivityError, e, this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		AppParams appParams = AppContext.getApplication().getAppParams();

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

				// running android version >= ICS, show new ui
				if (android.os.Build.VERSION.SDK_INT >= 14) {
					intent = new Intent(TabGameSetActivity.this, GameSetChartViewPagerActivity.class);
				}
				// prevent problem of incorrect pie charts for versions < ICS =>
				// use former activity
				else {
					intent = new Intent(TabGameSetActivity.this, GameSetChartListActivity.class);
				}
				TabGameSetActivity.this.startActivity(intent);

				return true;
			}
		});

		if (android.os.Build.VERSION.SDK_INT > 15) {
			this.buildMenuForNewAndroidDevices(menu);
		} else {
			this.buildMenuForOldAndroidDevices(menu);
		}
		return true;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.containsKey(ActivityParams.PARAM_GAMESET_SERIALIZED)) {
			this.gameSet = (GameSet) savedInstanceState.getSerializable(ActivityParams.PARAM_GAMESET_SERIALIZED);
		}
	}

	@Override
	protected void onResume() {
		try {
			super.onResume();
			this.invalidateOptionsMenu();
		} catch (Exception e) {
			AuditHelper.auditError(ErrorTypes.tabGameSetActivityOnResumeError, e);
			this.finish();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(ActivityParams.PARAM_GAMESET_SERIALIZED, this.gameSet);
	}

	@Override
	protected void onStart() {
		try {
			super.onStart();
			AuditHelper.auditSession(this);
		} catch (Exception e) {
			AuditHelper.auditError(ErrorTypes.tabGameSetActivityOnStartError, e);
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
                    return AppContext.getApplication()
                                     .getResources()
                                     .getString(R.string.lblGamesTitle);
                case 1:
                    return AppContext.getApplication()
                                     .getResources()
                                     .getString(R.string.lblSynthesisTitle);
                default:
                    return "Unknown[" + position + "]";
            }
        }
	}
}