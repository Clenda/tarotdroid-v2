package org.nla.tarotdroid.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;

import org.nla.tarotdroid.BuildConfig;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.core.AppParams;
import org.nla.tarotdroid.core.BaseActivity;
import org.nla.tarotdroid.core.IAsyncCallback;
import org.nla.tarotdroid.core.ThumbnailItem;
import org.nla.tarotdroid.core.dal.IDalService;
import org.nla.tarotdroid.core.helpers.AuditHelper;
import org.nla.tarotdroid.core.helpers.UIHelper;
import org.nla.tarotdroid.history.GameSetHistoryActivity;
import org.nla.tarotdroid.players.PlayerListActivity;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.common.collect.Lists.newArrayList;

public class MainDashboardActivity extends BaseActivity {

    private static final int FILE_SELECT_CODE = 0;
    private final IAsyncCallback<String[]> exportDatabaseCallback = new IAsyncCallback<String[]>() {

        @Override
        public void execute(String[] databaseContent, Exception e) {
            onDatabaseExported(databaseContent);
        }
    };
    private final IAsyncCallback<String> importDatabaseCallback = new IAsyncCallback<String>() {

        @Override
        public void execute(String object, Exception e) {
            onDatabaseImported();
        }
    };
    @BindView(R.id.listOptions) protected ListView listOptions;
    @Inject protected AppParams appParams;
    @Inject IDalService dalService;
    private InsertMockGameSetsTask insertMockGameSetsTask;

    private void buildMenuForNewAndroidDevices(Menu menu) {
        MenuItem miExportDB = menu.add(R.string.lblDbExportItem);
        miExportDB.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        miExportDB.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ExportDatabaseTask exportDatabaseTask = new ExportDatabaseTask(MainDashboardActivity.this,
                                                                               dalService);
                exportDatabaseTask.setCallback(exportDatabaseCallback);
                exportDatabaseTask.execute();
                return true;
            }
        });

        MenuItem miImportDB = menu.add(R.string.lblDbImportItem);
        miImportDB.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        miImportDB.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showFileChooser();
                return true;
            }
        });

        MenuItem miContact = menu.add(R.string.lblContactItem);
        miContact.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        miContact.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL,
                                new String[]{MainDashboardActivity.this.getString(R.string.urlEmail)});
                intent.putExtra(Intent.EXTRA_TEXT, buildMessageBody().toString());
                startActivity(Intent.createChooser(intent, getString(R.string.lblContactUs)));
                return true;
            }
        });

        if (BuildConfig.IS_IN_DEV_MODE) {
            MenuItem miMockData = menu.add(R.string.lblMockItem);
            miMockData.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            miMockData.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    MainDashboardActivity.this.insertMockGameSetsTask = new InsertMockGameSetsTask(
                            MainDashboardActivity.this,
                            appParams.getDevGameSetCount(),
                            appParams.getDevMaxGameCount(), dalService);
                    MainDashboardActivity.this.insertMockGameSetsTask.execute();
                    return true;
                }
            });
        }
    }

    private void buildMenuForOldAndroidDevices(Menu menu) {
        SubMenu subMenuMore = menu.addSubMenu("+");
        MenuItem subMenuMoreItem = subMenuMore.getItem();
        subMenuMoreItem.setIcon(R.drawable.ic_menu_moreoverflow_normal_holo_light);
        subMenuMoreItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem miExportDB = subMenuMore.add(R.string.lblDbExportItem);
        miExportDB.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ExportDatabaseTask exportDatabaseTask = new ExportDatabaseTask(MainDashboardActivity.this,
                                                                               dalService);
                exportDatabaseTask.setCallback(exportDatabaseCallback);
                exportDatabaseTask.execute();
                return true;
            }
        });

        MenuItem miImportDB = subMenuMore.add(R.string.lblDbImportItem);
        miImportDB.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        miImportDB.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showFileChooser();
                return true;
            }
        });

        MenuItem miContact = subMenuMore.add(R.string.lblContactItem);
        miContact.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        miContact.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL,
                                new String[]{MainDashboardActivity.this.getString(R.string.urlEmail)});
                intent.putExtra(Intent.EXTRA_TEXT, buildMessageBody().toString());
                startActivity(Intent.createChooser(intent, getString(R.string.lblContactUs)));
                return true;
            }
        });

        if (BuildConfig.IS_IN_DEV_MODE) {
            MenuItem miMockData = subMenuMore.add(R.string.lblMockItem);
            miMockData.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            miMockData.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    MainDashboardActivity.this.insertMockGameSetsTask = new InsertMockGameSetsTask(
                            MainDashboardActivity.this,
                            appParams.getDevGameSetCount(),
                            appParams.getDevMaxGameCount(), dalService);
                    MainDashboardActivity.this.insertMockGameSetsTask.execute();
                    return true;
                }
            });
        }
    }

    private StringBuilder buildMessageBody() {
        StringBuilder contentText = new StringBuilder();
        contentText.append("TarotDroid version: " + BuildConfig.APP_ID + "[" + BuildConfig.APP_VERSION + "]");
        contentText.append("\n");
        contentText.append("Android version: " + android.os.Build.VERSION.SDK_INT);
        contentText.append("\n");
        contentText.append("Device: " + android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL + "[" + android.os.Build.DEVICE + "]");
        contentText.append("\n");
        return contentText;
    }

    private void initializeViews(final Bundle savedInstanceState) {
        DashboardOption newGameOption = new DashboardOption(R.drawable.icon_3cards_released,
                                                            R.string.lblNewGameSet,
                                                            R.string.lblNewGameSetDetails,
                                                            R.id.new_gameset_item);
        DashboardOption historyOption = new DashboardOption(R.drawable.icon_folder_released,
                                                            R.string.lblGameSetHistory,
                                                            R.string.lblGameSetHistoryDetails,
                                                            R.id.history_item);
        DashboardOption playerStatisticsOption = new DashboardOption(R.drawable.icon_community,
                                                                     R.string.lblPlayers,
                                                                     R.string.lblPlayersDetails,
                                                                     R.id.player_statistics_item);
        DashboardOption marketOption = new DashboardOption(R.drawable.icon_market,
                                                           R.string.lblGooglePlay,
                                                           R.string.lblGooglePlayDetails,
                                                           R.id.google_play_item);
        DashboardOption goFullOption = new DashboardOption(R.drawable.icon,
                                                           R.string.lblFullVersion,
                                                           R.string.lblFullVersionDetails,
                                                           R.id.full_version_item);

        List<DashboardOption> options = newArrayList();
        options.add(newGameOption);
        options.add(historyOption);
        options.add(playerStatisticsOption);
        options.add(marketOption);

        if (!BuildConfig.IS_FULL) {
            options.add(goFullOption);
        }

        listOptions.setAdapter(new DashboardOptionAdapter(this, options));
        listOptions.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                onListItemClick(position);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();

                    String path = null;
                    try {
                        path = FileUtils.getPath(this, uri);
                    } catch (URISyntaxException e) {
                        path = null;
                    }

                    if (path != null) {
                        onImportFileSelected(path);
                    } else {

                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (insertMockGameSetsTask != null && insertMockGameSetsTask.getStatus() == Status.RUNNING) {
            insertMockGameSetsTask.cancel(true);
        }
        super.onBackPressed();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            initializeViews(savedInstanceState);
        } catch (Exception e) {
            auditHelper.auditError(AuditHelper.ErrorTypes.mainDashBoardActivityError, e, this);
        }
    }

    @OnClick(R.id.imgLikeUsOnFacebook)
    public void onClickOnLikeUsOnFacebook() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(MainDashboardActivity.this.getString(R.string.urlFaceBook)));
        startActivity(intent);
    }

    @Override
    protected void inject() {
        TarotDroidApp.get(this).getComponent().inject(this);
    }

    @Override
    protected void auditEvent() {
        UIHelper.trackAppLaunched(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.main_dashboard;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem miPrefs = menu.add(R.string.lblPrefsItem)
                               .setIcon(R.drawable.perm_group_system_tools);
        miPrefs.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        miPrefs.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainDashboardActivity.this,
                                           MainPreferencesActivity.class);
                MainDashboardActivity.this.startActivity(intent);
                return true;
            }
        });

        MenuItem miAbout = menu.add(R.string.lblAboutItem).setIcon(R.drawable.gd_action_bar_info);
        miAbout.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        miAbout.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int idMsgAbout = !BuildConfig.IS_FULL
                        ? R.string.msgAboutLimited
                        : R.string.msgAbout;
                UIHelper.showSimpleRichTextDialog(MainDashboardActivity.this,
                                                  MainDashboardActivity.this.getText(idMsgAbout)
                                                                            .toString(),
                                                  MainDashboardActivity.this.getString(R.string.titleAbout,
                                                                                       BuildConfig.APP_VERSION));
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

    private void onDatabaseExported(String[] databaseContent) {

        // should never happen
        if (databaseContent == null || databaseContent[0] == null) {
            UIHelper.showSimpleRichTextDialog(this,
                                              this.getString(R.string.msgDbExportFailed),
                                              this.getString(R.string.titleDbExportFailed));
        } else {
            try {
                StringBuilder contentText = buildMessageBody();
                // if (AppContext.getApplication().isAppInDebugMode()) {
                // contentText.append("Facebook hash key: [" +
                // UIHelper.getFacebookHashKey(this) + "]");
                // contentText.append("\n");
                // }
                contentText.append("Export (" + databaseContent[0].length() + " characters) \n" + databaseContent[0]);
                contentText.append("\n");

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL,
                                new String[]{this.getString(R.string.urlEmail)});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Export log");
                intent.putExtra(Intent.EXTRA_TEXT, contentText.toString());

                if (databaseContent[1] != null) {
                    Uri uri = Uri.fromFile(new File(databaseContent[1]));
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                }

                this.startActivity(Intent.createChooser(intent, "Envoyer l'export..."));
            } catch (Exception e) {
                auditHelper.auditError(AuditHelper.ErrorTypes.exportDatabaseError, e, this);
            }
        }
    }

    private void onDatabaseImported() {
        Toast.makeText(this, this.getString(R.string.lblDbHelperImportDone), Toast.LENGTH_SHORT)
             .show();
    }

    private void onImportFileSelected(String filePath) {
        ImportDatabaseTask importDatabaseTask = new ImportDatabaseTask(MainDashboardActivity.this,
                                                                       filePath, dalService);
        importDatabaseTask.setCallback(importDatabaseCallback);
        importDatabaseTask.execute();
    }

    protected void onListItemClick(int position) {
        DashboardOption option = (DashboardOption) this.listOptions.getAdapter().getItem(position);
        Intent intent;
        int tagValue = ((Integer) option.getTag()).intValue();

        if (tagValue == R.id.new_gameset_item) {
            intent = new Intent(this, NewGameSetDashboardActivity.class);
            this.startActivity(intent);
        } else if (tagValue == R.id.history_item) {
            intent = new Intent(this, GameSetHistoryActivity.class);
            this.startActivity(intent);
        } else if (tagValue == R.id.player_statistics_item) {
            intent = new Intent(this, PlayerListActivity.class);
            this.startActivity(intent);
        } else if (tagValue == R.id.google_play_item) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(BuildConfig.PLAYSTORE_APP_URL));
            this.startActivity(intent);
        } else if (tagValue == R.id.gmail_item) {
            intent = new Intent(Intent.ACTION_SEND);
            intent.setClassName("com.google.android.gm",
                                "com.google.android.gm.ComposeActivityGmail");
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{this.getString(R.string.urlEmail)});
            // intent.setData(Uri.parse(this.getString(R.string.urlEmail)));
            this.startActivity(Intent.createChooser(intent, null));
        } else if (tagValue == R.id.full_version_item) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(this.getString(R.string.urlGooglePlayFullApp)));
            this.startActivity(intent);
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent,
                                                        this.getString(R.string.lblDbHelperPickXMLFile)),
                                   FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            UIHelper.showSimpleRichTextDialog(this,
                                              this.getString(R.string.msgDbHelperYouNeedAFileExplorer),
                                              this.getString(R.string.titleDbHelperYouNeedAFileExplorer));
        }
    }

    private class DashboardOptionAdapter extends ArrayAdapter<DashboardOption> {

        public DashboardOptionAdapter(Context context, List<DashboardOption> objects) {
            super(context, R.layout.thumbnail_item, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DashboardOption option = this.getItem(position);
            return new ThumbnailItem(this.getContext(),
                                     option.getDrawableId(),
                                     option.getTitleResourceId(),
                                     option.getContentResourceId());
        }
    }
}