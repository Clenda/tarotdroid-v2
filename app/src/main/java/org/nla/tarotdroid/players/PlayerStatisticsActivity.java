package org.nla.tarotdroid.players;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.common.base.Throwables;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.computers.IPlayerStatisticsComputer;
import org.nla.tarotdroid.biz.computers.PlayerStatisticsComputerFactory;
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.biz.enums.ResultType;
import org.nla.tarotdroid.constants.ResultCodes;
import org.nla.tarotdroid.core.BaseActivity;
import org.nla.tarotdroid.core.ThumbnailItem;
import org.nla.tarotdroid.core.dal.DalException;
import org.nla.tarotdroid.core.dal.IDalService;
import org.nla.tarotdroid.core.helpers.AuditHelper;
import org.nla.tarotdroid.core.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.core.helpers.UIHelper;
import org.nla.tarotdroid.history.TextItem;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

public class PlayerStatisticsActivity extends BaseActivity {

    @BindView(R.id.listView) protected ListView listView;
    @Inject IDalService dalService;
    private Player player;
    private PlayerStatisticsAdapter adapter;
    private IconContextMenu pictureContextMenu;
    private boolean pictureChanged;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            pictureChanged = false;

            listView.setCacheColorHint(0);
            listView.setBackgroundResource(R.drawable.img_excuse);

            // make sure player name is provided
            if (!getIntent().getExtras().containsKey("player")) {
                throw new IllegalArgumentException("player must be provided");
            }
            // retrieves the player
            player = dalService.getPlayerByName((getIntent().getExtras().getString("player")));
            //player = (Player)getIntent().getExtras().get("player");
            setTitle(String.format(getResources().getString(R.string.lblPlayerStatisticsActivityTitle),
                                   player.getName()));

            refresh();
        } catch (final Exception e) {
            auditHelper.auditError(ErrorTypes.playerStatisticsActivityError, e, this);
        }
    }

    @Override
    protected void inject() {
        TarotDroidApp.get(this).getComponent().inject(this);
    }

    @Override
    protected void auditEvent() {
        auditHelper.auditEvent(AuditHelper.EventTypes.displayPlayerStatisticsPage);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_player_statistics;
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == R.id.select_picture_from_contact_action_code && resultCode == RESULT_OK) {
            try {
                player.setPictureUri(data.getData().toString());

                dalService.updatePlayer(player);
                pictureChanged = true;
                refresh();
            } catch (Exception e) {
                UIHelper.showSimpleRichTextDialog(
                        this,
                        Throwables.getStackTraceAsString(e),
                        getString(R.string.lblUnexpectedError, e.getMessage())
                );
                auditHelper.auditError(ErrorTypes.unexpectedError, e);
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == R.id.select_picture_context_menu) {
            return pictureContextMenu.createMenu(getString(R.string.lblSelectPictureMenuTitle));
        }
        return super.onCreateDialog(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        SubMenu menuPicture = menu.addSubMenu(getString(R.string.lblPictureItem));
        MenuItem miPicture = menuPicture.getItem();
        miPicture.setIcon(R.drawable.icon_add_picture);
        miPicture.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT | MenuItem.SHOW_AS_ACTION_ALWAYS);


        MenuItem miFromContacts = menuPicture.add(getString(R.string.lblPictureFromContactsItem));
        miFromContacts.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        miFromContacts.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @TargetApi(Build.VERSION_CODES.ECLAIR)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                                           ContactsContract.Contacts.CONTENT_URI);
                // TODO Fix crash http://stackoverflow.com/questions/25529865/java-lang-illegalargumentexception-can-only-use-lower-16-bits-for-requestcode
                PlayerStatisticsActivity.this.startActivityForResult(intent,
                                                                     R.id.select_picture_from_contact_action_code);
                return false;
            }
        });

        MenuItem miNoPicture = menuPicture.add(getString(R.string.lblPictureNoPictureItem));
        miNoPicture.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        miNoPicture.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {
                    player.setPictureUri(null);

                    dalService.updatePlayer(player);
                    pictureChanged = true;
                    refresh();
                } catch (DalException e) {
                    UIHelper.showSimpleRichTextDialog(
                            PlayerStatisticsActivity.this,
                            Throwables.getStackTraceAsString(e),
                            getString(R.string.lblUnexpectedError, e.getMessage())
                    );
                    auditHelper.auditError(ErrorTypes.unexpectedError, e);
                }

                return false;
            }
        });

        return true;
    }

    private void refresh() {
        adapter = new PlayerStatisticsAdapter(this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (pictureChanged) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(ResultCodes.PlayerNameCode, player.getName());
            resultIntent.putExtra(ResultCodes.PlayerPictureUriCode, player.getPictureUri());
            setResult(ResultCodes.PlayerPictureChanged, resultIntent);
        } else {
            setResult(ResultCodes.Irrelevant);
        }
        finish();
    }

    private class PlayerStatisticsAdapter extends BaseAdapter {

        private View playerItem;
        private TextItem playedGameSetCountItem;
        private TextItem playedGameSetCountByGameStyleTypeItem;
        private TextItem playedGameCountItem;
        private TextItem playedGameCountByBetTypeItem;
        private TextItem leaderInGameCountItem;
        private TextItem calledInGameCountItem;
        private Context context;

        public PlayerStatisticsAdapter(Context context) {
            this.context = context;

            IPlayerStatisticsComputer playerStatisticsComputer = PlayerStatisticsComputerFactory.GetPlayerStatisticsComputer(
                    dalService.getAllGameSets(),
                    player,
                    "guava");

            // gameset statistics
            int gameSetCountForPlayer = playerStatisticsComputer.getGameSetCountForPlayer();
            int wonGameSetCountForPlayer = playerStatisticsComputer.getWonAndLostGameSetsForPlayer()
                                                                   .get(
                                                                           ResultType.Success);
            Map<GameStyleType, Integer> gameSetCountForPlayerByGameStyleType = playerStatisticsComputer
                    .getGameSetCountForPlayerByGameStyleType();
            int gameSetTarot3CountForPlayer = gameSetCountForPlayerByGameStyleType.get(GameStyleType.Tarot3);
            int gameSetTarot4CountForPlayer = gameSetCountForPlayerByGameStyleType.get(GameStyleType.Tarot4);
            int gameSetTarot5CountForPlayer = gameSetCountForPlayerByGameStyleType.get(GameStyleType.Tarot5);
            Map<GameStyleType, Map<ResultType, Integer>> wonAndLostGameSetsForPlayerByGameStyleType = playerStatisticsComputer
                    .getWonAndLostGameSetsForPlayerByGameStyleType();
            int gameSetTarot3SuccessesCountForPlayer = wonAndLostGameSetsForPlayerByGameStyleType.get(
                    GameStyleType.Tarot3).get(ResultType.Success);
            int gameSetTarot4SuccessesCountForPlayer = wonAndLostGameSetsForPlayerByGameStyleType.get(
                    GameStyleType.Tarot4).get(ResultType.Success);
            int gameSetTarot5SuccessesCountForPlayer = wonAndLostGameSetsForPlayerByGameStyleType.get(
                    GameStyleType.Tarot5).get(ResultType.Success);

            // game statistics
            int gameCountForPlayer = playerStatisticsComputer.getGameCountForPlayer();
            int wonGameCountForPlayer = playerStatisticsComputer.getWonAndLostGamesForPlayer().get(
                    ResultType.Success);
            Map<BetType, Integer> gameCountForPlayerByBetType = playerStatisticsComputer.getGameCountForPlayerByBetType();
            int gamePriseCountForPlayer = gameCountForPlayerByBetType.get(BetType.Prise);
            int gameGardeCountForPlayer = gameCountForPlayerByBetType.get(BetType.Garde);
            int gameGardeSansCountForPlayer = gameCountForPlayerByBetType.get(BetType.GardeSans);
            int gameGardeContreCountForPlayer = gameCountForPlayerByBetType.get(BetType.GardeContre);
            Map<BetType, Map<ResultType, Integer>> wonAndLostGamesForPlayerByBetType = playerStatisticsComputer
                    .getWonAndLostGamesForPlayerByBetType();
            int gamePriseSuccessesCountForPlayer = wonAndLostGamesForPlayerByBetType.get(BetType.Prise)
                                                                                    .get(
                                                                                            ResultType.Success);
            int gameGardeSuccessesCountForPlayer = wonAndLostGamesForPlayerByBetType.get(BetType.Garde)
                                                                                    .get(
                                                                                            ResultType.Success);
            int gameGardeSansSuccessesCountForPlayer = wonAndLostGamesForPlayerByBetType.get(BetType.GardeSans)
                                                                                        .get(
                                                                                                ResultType.Success);
            int gameGardeContreSuccessesCountForPlayer = wonAndLostGamesForPlayerByBetType.get(
                    BetType.GardeContre).get(ResultType.Success);
            int gameCountAsLeadingPlayer = playerStatisticsComputer.getGameCountAsLeadingPlayer();
            int gameCountAsCalledPlayer = playerStatisticsComputer.getGameCountAsCalledPlayer();

//			if (player.getFacebookId() != null) {
//				this.playerItem = new FacebookThumbnailItem(
//					context,
//					player.getFacebookId(),
//					player.getName(),
//					context.getString(R.string.lblPlayerStatsCreateOn, player.getCreationTs().toLocaleString())
//				);
//			}
//			else {
//				this.playerItem = new ThumbnailItem(
//					context,
//					R.drawable.icon_android,
//					player.getName(),
//					context.getString(R.string.lblPlayerStatsCreateOn, player.getCreationTs().toLocaleString())
//				);
//			}
            setPlayerPicture();

            playedGameSetCountItem = new TextItem(
                    context,
                    context.getString(R.string.lblPlayerStatsGameSets),
                    context.getString(R.string.lblPlayerStatsGameSetsDetails,
                                      gameSetCountForPlayer,
                                      wonGameSetCountForPlayer)
            );

            playedGameSetCountByGameStyleTypeItem = new TextItem(
                    context,
                    context.getString(R.string.lblPlayerStatsGameSetsRepartition),
                    context.getString(R.string.lblGameSetCountByGameStyleType,
                                      gameSetTarot3CountForPlayer,
                                      gameSetTarot3SuccessesCountForPlayer,
                                      gameSetTarot4CountForPlayer,
                                      gameSetTarot4SuccessesCountForPlayer,
                                      gameSetTarot5CountForPlayer,
                                      gameSetTarot5SuccessesCountForPlayer)
            );

            playedGameCountItem = new TextItem(
                    context,
                    context.getString(R.string.lblPlayerStatsGames),
                    context.getString(R.string.lblPlayerStatsGamesDetails,
                                      gameCountForPlayer,
                                      wonGameCountForPlayer)
            );

            playedGameCountByBetTypeItem = new TextItem(
                    context,
                    context.getString(R.string.lblPlayerStatsGamesRepartition),
                    context.getString(R.string.lblGameCountByBetTypeItem,
                                      gamePriseCountForPlayer,
                                      gamePriseSuccessesCountForPlayer,
                                      gameGardeCountForPlayer,
                                      gameGardeSuccessesCountForPlayer,
                                      gameGardeSansCountForPlayer,
                                      gameGardeSansSuccessesCountForPlayer,
                                      gameGardeContreCountForPlayer,
                                      gameGardeContreSuccessesCountForPlayer)
            );

            leaderInGameCountItem = new TextItem(
                    context,
                    context.getString(R.string.lblPlayerStatsLeadingPlayer),
                    context.getString(R.string.lblPlayerStatsLeadingPlayerDetails,
                                      gameCountAsLeadingPlayer)
            );

            calledInGameCountItem = new TextItem(
                    context,
                    context.getString(R.string.lblPlayerStatsCalledPlayer),
                    context.getString(R.string.lblPlayerStatsCalledPlayerDetails,
                                      gameCountAsCalledPlayer)
            );
        }

        public void setPlayerPicture() {

            if (player.getPictureUri() != null) {
                playerItem = new ThumbnailItem(
                        context,
                        Uri.parse(player.getPictureUri()),
                        R.drawable.icon_android,
                        player.getName(),
                        context.getString(R.string.lblPlayerStatsCreateOn,
                                          player.getCreationTs().toLocaleString())
                );
            }

            // no pic
            else {
                playerItem = new ThumbnailItem(
                        context,
                        R.drawable.icon_android,
                        player.getName(),
                        context.getString(R.string.lblPlayerStatsCreateOn,
                                          player.getCreationTs().toLocaleString())
                );
            }
            //this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Object getItem(int position) {
            switch (position) {
                case 0:
                    return playerItem;
                case 1:
                    return playedGameSetCountItem;
                case 2:
                    return playedGameSetCountByGameStyleTypeItem;
                case 3:
                    return playedGameCountItem;
                case 4:
                    return playedGameCountByBetTypeItem;
                case 5:
                    return leaderInGameCountItem;
                case 6:
                    return calledInGameCountItem;
                default:
                    return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (position) {
                case 0:
                    return playerItem;
                case 1:
                    return playedGameSetCountItem;
                case 2:
                    return playedGameSetCountByGameStyleTypeItem;
                case 3:
                    return playedGameCountItem;
                case 4:
                    return playedGameCountByBetTypeItem;
                case 5:
                    return leaderInGameCountItem;
                case 6:
                    return calledInGameCountItem;
                default:
                    return null;
            }
        }
    }
}