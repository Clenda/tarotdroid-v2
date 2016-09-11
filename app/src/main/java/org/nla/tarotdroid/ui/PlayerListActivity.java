package org.nla.tarotdroid.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.app.AppContext;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.ui.constants.RequestCodes;
import org.nla.tarotdroid.ui.constants.ResultCodes;
import org.nla.tarotdroid.ui.controls.FacebookThumbnailItem;
import org.nla.tarotdroid.ui.controls.ThumbnailItem;
import org.nla.tarotdroid.ui.tasks.IAsyncCallback;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlayerListActivity extends AppCompatActivity {

	private ListView listView;

	private class PlayerAdapter extends ArrayAdapter<Player> {

		public PlayerAdapter(Context context, List<Player> players) {
			super(context, R.layout.thumbnail_item, players);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Player player = this.getItem(position);

			View item = null;

			// TODO Improve using convertView

			// facebook pic was set
			if (player.getFacebookId() != null) {
				item = new FacebookThumbnailItem(this.getContext(), player.getFacebookId(), player.getName(), this.getContext().getString(R.string.lblPlayerStatsCreateOn,
						player.getCreationTs().toLocaleString()));
			}

			// contact pic was set
			else if (player.getPictureUri() != null) {
				item = new ThumbnailItem(this.getContext(), Uri.parse(player.getPictureUri()), R.drawable.icon_android, player.getName(), this.getContext().getString(R.string.lblPlayerStatsCreateOn,
																																									  player.getCreationTs().toLocaleString()));
			}

			// no pic
			else {
				item = new ThumbnailItem(this.getContext(), R.drawable.icon_android, player.getName(), this.getContext().getString(R.string.lblPlayerStatsCreateOn,
						player.getCreationTs().toLocaleString()));
			}
			//
			//
			//
			// if (player.getFacebookId() != null) {
			// item = new FacebookThumbnailItem(
			// this.getContext(),
			// player.getFacebookId(),
			// player.getName(),
			// this.getContext().getString(R.string.lblPlayerStatsCreateOn,
			// player.getCreationTs().toLocaleString())
			// );
			//
			// }
			// else {
			// item = new ThumbnailItem(
			// this.getContext(),
			// R.drawable.icon_android,
			// player.getName(),
			// this.getContext().getString(R.string.lblPlayerStatsCreateOn,
			// player.getCreationTs().toLocaleString())
			// );
			// }

			return item;
		}
	}

	private final Comparator<Player> playerNameComparator = new Comparator<Player>() {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(final Player player1, final Player player2) {
			if (player1 == null) {
				return -1;
			}
			if (player2 == null) {
				return 1;
			}

			return player1.getName().toLowerCase().compareTo(player2.getName().toLowerCase());
		}
	};

	private void auditEvent() {
		AuditHelper.auditEvent(AuditHelper.EventTypes.displayPlayerListPage);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RequestCodes.DISPLAY_PLAYER) {
			if (resultCode == ResultCodes.PlayerPictureChanged) {
				this.refresh();
			}
		}
	}

	@Override
	public void onCreate(final Bundle icicle) {
		try {
			super.onCreate(icicle);
			this.auditEvent();
			setContentView(R.layout.activity_player_list);
			listView = (ListView)findViewById(R.id.listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onListItemClick(view, position, id);
                }
            });

			// set excuse as background image
			listView.setCacheColorHint(0);
			listView.setBackgroundResource(R.drawable.img_excuse);

			// set action bar properties
			this.setTitle(this.getResources().getString(R.string.lblPlayerListActivityTitle));

			// wait for the dal to be initiated to refresh the player list
			if (AppContext.getApplication().getLoadDalTask().getStatus() == AsyncTask.Status.RUNNING) {
				AppContext.getApplication().getLoadDalTask().showDialogOnActivity(this, this.getResources().getString(R.string.msgGameSetsRetrieval));
				AppContext.getApplication().getLoadDalTask().setCallback(new IAsyncCallback<String>() {

					@Override
					public void execute(String isNull, Exception e) {
						// Check exception
						PlayerListActivity.this.refresh();
					}
				});
			}
			// refresh the player list
			else {
				this.refresh();
			}
		} catch (Exception e) {
			AuditHelper.auditError(AuditHelper.ErrorTypes.playerListActivityError, e, this);
		}
	}

	protected void onListItemClick(View v, int position, long id) {
		Intent intent = new Intent(this, PlayerStatisticsActivity.class);
		intent.putExtra("player", ((Player) listView.getAdapter().getItem(position)).getName());
		this.startActivityForResult(intent, RequestCodes.DISPLAY_PLAYER);
	}

	@Override
	protected void onStart() {
		super.onStart();
		AuditHelper.auditSession(this);
	}

	private void refresh() {
		try {

			List<Player> players = AppContext.getApplication().getDalService().getAllPlayers();
			Collections.sort(players, playerNameComparator);
			listView.setAdapter(new PlayerAdapter(this, players));
		} catch (final Exception e) {
			Log.v(AppContext.getApplication().getAppLogTag(), this.getClass().toString(), e);
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			AuditHelper.auditError(AuditHelper.ErrorTypes.unexpectedError, e);

		}
	}
}