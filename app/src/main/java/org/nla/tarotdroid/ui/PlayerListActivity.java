package org.nla.tarotdroid.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.nla.tarotdroid.BuildConfig;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.ui.constants.RequestCodes;
import org.nla.tarotdroid.ui.constants.ResultCodes;
import org.nla.tarotdroid.ui.controls.ThumbnailItem;
import org.nla.tarotdroid.ui.tasks.IAsyncCallback;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;

public class PlayerListActivity extends BaseActivity {

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
	@BindView((R.id.listView)) protected ListView listView;

	@Override
	public void auditEvent() {
		auditHelper.auditEvent(AuditHelper.EventTypes.displayPlayerListPage);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_player_list;
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
	protected int getTitleResId() {
		return R.string.lblPlayerListActivityTitle;
	}

	@Override
	public void onCreate(final Bundle icicle) {
		try {
			super.onCreate(icicle);

			// set excuse as background image
			listView.setCacheColorHint(0);
			listView.setBackgroundResource(R.drawable.img_excuse);

			// wait for the dal to be initiated to refresh the player list
			if (TarotDroidApp.get(this).getLoadDalTask().getStatus() == AsyncTask.Status.RUNNING) {
				TarotDroidApp.get(this)
							 .getLoadDalTask()
							 .showDialogOnActivity(this,
												   this.getResources()
													   .getString(R.string.msgGameSetsRetrieval));
				TarotDroidApp.get(this).getLoadDalTask().setCallback(new IAsyncCallback<String>() {

					@Override
					public void execute(String isNull, Exception e) {
						// Check exception
						PlayerListActivity.this.refresh();
					}
				});
			}
			// refresh the player list
			else {
				refresh();
			}
		} catch (Exception e) {
			auditHelper.auditError(AuditHelper.ErrorTypes.playerListActivityError, e, this);
		}
	}

	@Override
	protected void inject() {
		TarotDroidApp.get(this).getComponent().inject(this);
	}

	@OnItemClick(R.id.listView)
	protected void onListItemClick(View v, int position, long id) {
		Intent intent = new Intent(this, PlayerStatisticsActivity.class);
		intent.putExtra("player", ((Player) listView.getAdapter().getItem(position)).getName());
		this.startActivityForResult(intent, RequestCodes.DISPLAY_PLAYER);
	}

	private void refresh() {
		try {

			List<Player> players = TarotDroidApp.get(this).getDalService().getAllPlayers();
			Collections.sort(players, playerNameComparator);
			listView.setAdapter(new PlayerAdapter(this, players));
		} catch (final Exception e) {
			Log.v(BuildConfig.APP_LOG_TAG, this.getClass().toString(), e);
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			auditHelper.auditError(AuditHelper.ErrorTypes.unexpectedError, e);

		}
	}

	private class PlayerAdapter extends ArrayAdapter<Player> {

		public PlayerAdapter(Context context, List<Player> players) {
			super(context, R.layout.thumbnail_item, players);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Player player = this.getItem(position);

			View item = null;

			// TODO Improve using convertView

			if (player.getPictureUri() != null) {
				item = new ThumbnailItem(this.getContext(),
										 Uri.parse(player.getPictureUri()),
										 R.drawable.icon_android,
										 player.getName(),
										 this.getContext()
											 .getString(R.string.lblPlayerStatsCreateOn,
														player.getCreationTs().toLocaleString()));
			}

			// no pic
			else {
				item = new ThumbnailItem(this.getContext(),
										 R.drawable.icon_android,
										 player.getName(),
										 this.getContext()
											 .getString(R.string.lblPlayerStatsCreateOn,
														player.getCreationTs().toLocaleString()));
			}
			return item;
		}
	}
}