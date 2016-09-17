package org.nla.tarotdroid.ui.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import org.nla.tarotdroid.BuildConfig;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;

public class RemoveAllGameSetsTask extends BaseAsyncTask<Void, Void, Void, Object> {

	private final Activity activity;
	private ProgressDialog dialog;

	public RemoveAllGameSetsTask(final Activity activity, final ProgressDialog dialog) {

		// TODO checkArgument ?
//        checkArgument(activity != null, "activity is null");
		this.activity = activity;
		this.dialog = dialog;

		if (this.dialog == null) {
			this.dialog = new ProgressDialog(this.activity);
		}
	}

	@Override
	protected final Void doInBackground(final Void... voids) {
		try {
			// TODO Use context
			TarotDroidApp.get().getDalService().reInitDal();
		} catch (Exception e) {
			this.backgroundException = e;
			Log.v(BuildConfig.APP_LOG_TAG,
				  "TarotDroid Exception in " + this.getClass().toString(),
				  e);
		}
		return null;
	}

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
			Toast.makeText(this.activity, this.activity.getResources().getString(R.string.msgGameSetsDeleted), Toast.LENGTH_LONG).show();
		}

		// executes the potential callback
		if (this.callback != null) {
			this.callback.execute(null, null);
		}
	}

	@Override
	protected final void onPreExecute() {
		this.dialog.setMessage(this.activity.getResources().getString(R.string.msgGameSetsDeletion));
		this.dialog.show();
	}
}
