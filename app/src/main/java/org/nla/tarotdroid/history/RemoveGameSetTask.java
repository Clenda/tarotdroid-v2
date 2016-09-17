/*
	This file is part of the Android application TarotDroid.
 	
	TarotDroid is free software: you can redistribute it and/or modify
 	it under the terms of the GNU General Public License as published by
 	the Free Software Foundation, either version 3 of the License, or
 	(at your option) any later version.
 	
 	TarotDroid is distributed in the hope that it will be useful,
 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 	GNU General Public License for more details.
 	
 	You should have received a copy of the GNU General Public License
 	along with TarotDroid. If not, see <http://www.gnu.org/licenses/>.
 */
package org.nla.tarotdroid.history;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import org.nla.tarotdroid.BuildConfig;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.core.BaseAsyncTask;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * An AsyncTask aimed to remove the last game.
 */
public class RemoveGameSetTask extends BaseAsyncTask<Void, Void, Void, Object> {

	/**
	 * The context.
	 */
	private final Activity activity;
	/**
	 * GameSet to remove.
	 */
	private final GameSet gameSet;
	/**
	 * A progress dialog shown during the game creation and storage.
	 */
	private ProgressDialog dialog;

	/**
	 * Constructor.
	 * 
	 * @param activity
	 * @param dialog
	 * @param gameSet
	 */
	public RemoveGameSetTask(final Activity activity, final ProgressDialog dialog, final GameSet gameSet) {
		checkArgument(activity != null, "activity is null");
		checkArgument(gameSet != null, "gameSet is null");

		this.activity = activity;
		this.dialog = dialog;
		this.gameSet = gameSet;

		if (this.dialog == null) {
			this.dialog = new ProgressDialog(this.activity);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected final Void doInBackground(final Void... voids) {
		try {
			// TODO Use context
			TarotDroidApp.get().getDalService().deleteGameSet(this.gameSet);
		} catch (Exception e) {
			this.backgroundException = e;
			Log.v(BuildConfig.APP_LOG_TAG,
				  "TarotDroid Exception in " + this.getClass().toString(),
				  e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected final void onPostExecute(final Void unused) {
		// hide busy idicator
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}

		// display toast if error happened
		if (this.backgroundException != null) {
			Toast.makeText(this.activity, "DAL Error: " + this.backgroundException, Toast.LENGTH_LONG).show();
		}

		// display toast if everything's okay
		else {
			Toast.makeText(this.activity, this.activity.getResources().getString(R.string.msgGameSetDeleted), Toast.LENGTH_LONG).show();

			// executes the potential callback
			if (this.callback != null) {
				this.callback.execute(null, null);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected final void onPreExecute() {
		this.dialog.setMessage(this.activity.getResources().getString(R.string.msgGameSetDeletion));
		this.dialog.show();
	}
}
