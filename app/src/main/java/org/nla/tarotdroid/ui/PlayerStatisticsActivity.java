package org.nla.tarotdroid.ui;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.common.base.Throwables;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.app.AppContext;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.computers.IPlayerStatisticsComputer;
import org.nla.tarotdroid.biz.computers.PlayerStatisticsComputerFactory;
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.biz.enums.ResultType;
import org.nla.tarotdroid.dal.DalException;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.helpers.UIHelper;
import org.nla.tarotdroid.ui.constants.ResultCodes;
import org.nla.tarotdroid.ui.controls.IconContextMenu;
import org.nla.tarotdroid.ui.controls.TextItem;
import org.nla.tarotdroid.ui.controls.ThumbnailItem;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PlayerStatisticsActivity extends AppCompatActivity {
	
	private static final List<String> READ_PERMISSIONS = Arrays.asList("email");
	private ActionTypes actionType;
	private Player player;
	private IconContextMenu pictureContextMenu;
	private PlayerStatisticsAdapter adapter;
	private boolean pictureChanged;
	private ListView listView;

	@Override
    public void onCreate(final Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_player_statistics);
			listView = (ListView)findViewById(R.id.listView);

			this.auditEvent();
			this.pictureChanged = false;

			listView.setCacheColorHint(0);
            listView.setBackgroundResource(R.drawable.img_excuse);

			// make sure player name is provided
			if (!this.getIntent().getExtras().containsKey("player")) {
				throw new IllegalArgumentException("player must be provided");
			}
			// retrieves the player
			this.player = AppContext.getApplication().getDalService().getPlayerByName((this.getIntent().getExtras().getString("player")));
			//this.player = (Player)this.getIntent().getExtras().get("player");
			this.setTitle(String.format(getResources().getString(R.string.lblPlayerStatisticsActivityTitle), this.player.getName()));

            this.refresh();
        }
		catch (final Exception e) {
			AuditHelper.auditError(ErrorTypes.playerStatisticsActivityError, e, this);
		}
    }
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		AuditHelper.auditSession(this);
	}
	
	private void auditEvent() {
		AuditHelper.auditEvent(AuditHelper.EventTypes.displayPlayerStatisticsPage);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == R.id.select_picture_from_contact_action_code && resultCode == RESULT_OK) {
			try {
				this.player.setPictureUri(data.getData().toString());

				AppContext.getApplication().getDalService().updatePlayer(this.player);
				this.pictureChanged = true;
				this.refresh();
			}
			catch (Exception e) {
				UIHelper.showSimpleRichTextDialog(
						this,
						Throwables.getStackTraceAsString(e),
						this.getString( R.string.lblUnexpectedError, e.getMessage())
				);
				AuditHelper.auditError(ErrorTypes.unexpectedError, e);
			}
		}
	}

    @Override
	protected Dialog onCreateDialog(int id) {
	    if (id == R.id.select_picture_context_menu) {
	        return this.pictureContextMenu.createMenu(this.getString(R.string.lblSelectPictureMenuTitle));
	    }
	    return super.onCreateDialog(id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

        SubMenu menuPicture = menu.addSubMenu(this.getString(R.string.lblPictureItem));
		MenuItem miPicture = menuPicture.getItem();
		miPicture.setIcon(R.drawable.icon_add_picture);
		miPicture.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT|MenuItem.SHOW_AS_ACTION_ALWAYS);


        MenuItem miFromContacts = menuPicture.add(this.getString(R.string.lblPictureFromContactsItem));
		miFromContacts.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		miFromContacts.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @TargetApi(Build.VERSION_CODES.ECLAIR)
			@Override
			public boolean onMenuItemClick(MenuItem item) {
            	Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                // TODO Fix crash http://stackoverflow.com/questions/25529865/java-lang-illegalargumentexception-can-only-use-lower-16-bits-for-requestcode
                PlayerStatisticsActivity.this.startActivityForResult(intent, R.id.select_picture_from_contact_action_code);
				return false;
			}
		});

        MenuItem miNoPicture = menuPicture.add(this.getString(R.string.lblPictureNoPictureItem));
		miNoPicture.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		miNoPicture.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
			public boolean onMenuItemClick(MenuItem item) {
				try {
					PlayerStatisticsActivity.this.player.setPictureUri(null);

                    AppContext.getApplication().getDalService().updatePlayer(PlayerStatisticsActivity.this.player);
					PlayerStatisticsActivity.this.pictureChanged = true;
					PlayerStatisticsActivity.this.refresh();
				}
				catch (DalException e) {
					UIHelper.showSimpleRichTextDialog(
							PlayerStatisticsActivity.this,
							Throwables.getStackTraceAsString(e),
							PlayerStatisticsActivity.this.getString( R.string.lblUnexpectedError, e.getMessage())
					);
					AuditHelper.auditError(ErrorTypes.unexpectedError, e);
				}

                return false;
			}
		});

		return true;
	}
	
	private void refresh() {
		adapter = new PlayerStatisticsAdapter(this);
		listView.setAdapter(this.adapter);
	}
	
	@Override
	public void onBackPressed() {
		if (this.pictureChanged) {
			Intent resultIntent = new Intent();
			resultIntent.putExtra(ResultCodes.PlayerNameCode, this.player.getName());
			resultIntent.putExtra(ResultCodes.PlayerPictureUriCode, this.player.getPictureUri());
			this.setResult(ResultCodes.PlayerPictureChanged, resultIntent);
		}
		else {
			this.setResult(ResultCodes.Irrelevant);
		}
		this.finish();
	}

    private enum ActionTypes {
        setMyImageAsPlayerPicture,
        setFriendImageAsPlayerPicture
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
			
			IPlayerStatisticsComputer playerStatisticsComputer = PlayerStatisticsComputerFactory.GetPlayerStatisticsComputer(AppContext.getApplication().getDalService().getAllGameSets(), player, "guava");
			
			// gameset statistics
			int gameSetCountForPlayer = playerStatisticsComputer.getGameSetCountForPlayer();
			int wonGameSetCountForPlayer = playerStatisticsComputer.getWonAndLostGameSetsForPlayer().get(
					ResultType.Success);
			Map<GameStyleType, Integer> gameSetCountForPlayerByGameStyleType = playerStatisticsComputer.getGameSetCountForPlayerByGameStyleType();
			int gameSetTarot3CountForPlayer = gameSetCountForPlayerByGameStyleType.get(GameStyleType.Tarot3);
			int gameSetTarot4CountForPlayer = gameSetCountForPlayerByGameStyleType.get(GameStyleType.Tarot4);
			int gameSetTarot5CountForPlayer = gameSetCountForPlayerByGameStyleType.get(GameStyleType.Tarot5);
			Map<GameStyleType, Map<ResultType, Integer>> wonAndLostGameSetsForPlayerByGameStyleType = playerStatisticsComputer.getWonAndLostGameSetsForPlayerByGameStyleType();
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
			Map<BetType, Map<ResultType, Integer>> wonAndLostGamesForPlayerByBetType = playerStatisticsComputer.getWonAndLostGamesForPlayerByBetType();
			int gamePriseSuccessesCountForPlayer = wonAndLostGamesForPlayerByBetType.get(BetType.Prise).get(
					ResultType.Success);
			int gameGardeSuccessesCountForPlayer = wonAndLostGamesForPlayerByBetType.get(BetType.Garde).get(
					ResultType.Success);
			int gameGardeSansSuccessesCountForPlayer = wonAndLostGamesForPlayerByBetType.get(BetType.GardeSans).get(
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
			this.setPlayerPicture();
			
			this.playedGameSetCountItem = new TextItem(
					context,
					context.getString(R.string.lblPlayerStatsGameSets), 
					context.getString(R.string.lblPlayerStatsGameSetsDetails, gameSetCountForPlayer, wonGameSetCountForPlayer)
			);
			
			this.playedGameSetCountByGameStyleTypeItem = new TextItem(
					context,
					context.getString(R.string.lblPlayerStatsGameSetsRepartition),
					context.getString(R.string.lblGameSetCountByGameStyleType, gameSetTarot3CountForPlayer, gameSetTarot3SuccessesCountForPlayer, gameSetTarot4CountForPlayer, gameSetTarot4SuccessesCountForPlayer, gameSetTarot5CountForPlayer, gameSetTarot5SuccessesCountForPlayer)
			);		
			
			this.playedGameCountItem = new TextItem(
					context,
					context.getString(R.string.lblPlayerStatsGames),
					context.getString(R.string.lblPlayerStatsGamesDetails, gameCountForPlayer, wonGameCountForPlayer)
			);
			
			this.playedGameCountByBetTypeItem = new TextItem(
					context,
					context.getString(R.string.lblPlayerStatsGamesRepartition),
					context.getString(R.string.lblGameCountByBetTypeItem, gamePriseCountForPlayer, gamePriseSuccessesCountForPlayer, gameGardeCountForPlayer, gameGardeSuccessesCountForPlayer, gameGardeSansCountForPlayer, gameGardeSansSuccessesCountForPlayer, gameGardeContreCountForPlayer, gameGardeContreSuccessesCountForPlayer)
			);
		
			this.leaderInGameCountItem = new TextItem(
					context,
					context.getString(R.string.lblPlayerStatsLeadingPlayer),
					context.getString(R.string.lblPlayerStatsLeadingPlayerDetails, gameCountAsLeadingPlayer)
			);
			
			this.calledInGameCountItem = new TextItem(
					context,
					context.getString(R.string.lblPlayerStatsCalledPlayer), 
					context.getString(R.string.lblPlayerStatsCalledPlayerDetails, gameCountAsCalledPlayer)
			);
		}

		public void setPlayerPicture() {

            if (player.getPictureUri() != null) {
                this.playerItem = new ThumbnailItem(
						context,
						Uri.parse(player.getPictureUri()),
						R.drawable.icon_android,
						player.getName(),
						context.getString(R.string.lblPlayerStatsCreateOn, player.getCreationTs().toLocaleString())
				);			
			}
			
			// no pic
			else {
				this.playerItem = new ThumbnailItem(
					context,
					R.drawable.icon_android,
					player.getName(),
					context.getString(R.string.lblPlayerStatsCreateOn, player.getCreationTs().toLocaleString())
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
			switch(position) {
				case 0:
					return this.playerItem;
				case 1:
					return this.playedGameSetCountItem;
				case 2:
					return this.playedGameSetCountByGameStyleTypeItem;
				case 3:
					return this.playedGameCountItem;
				case 4:
					return this.playedGameCountByBetTypeItem;
				case 5:
					return this.leaderInGameCountItem;
				case 6:
					return this.calledInGameCountItem;
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
			switch(position) {
				case 0:
					return this.playerItem;
				case 1:
					return this.playedGameSetCountItem;
				case 2:
					return this.playedGameSetCountByGameStyleTypeItem;
				case 3:
					return this.playedGameCountItem;
				case 4:
					return this.playedGameCountByBetTypeItem;
				case 5:
					return this.leaderInGameCountItem;
				case 6:
					return this.calledInGameCountItem;
				default:
					return null;
			}
		}
	}
}