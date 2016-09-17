package org.nla.tarotdroid.ui.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.helpers.RandomHelper;

public class InsertMockGameSetsTask extends AsyncTask<Void, String, Void> {
	
	private Context context;
	private ProgressDialog dialog;
	private boolean backroundErrorHappened;
	private Exception backgroundException;
	private int gameSetCount;
	private int maxGameCount;
	private boolean goOn;
		
	public InsertMockGameSetsTask(final Context context, final int gameSetCount, final int maxGameCount) {
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		}
		
		this.context = context;
		this.backroundErrorHappened = false;
		this.gameSetCount = gameSetCount;
		this.maxGameCount = maxGameCount;
		this.goOn = true;
	}

	@Override
	protected void onPreExecute() {
		this.dialog = new ProgressDialog(this.context);
		this.dialog.setMessage("Statrting game creation...");
		this.dialog.show();
	}

	@Override
	protected Void doInBackground(final Void... params) {
		try {			
			for (int i = 0; i < this.gameSetCount && this.goOn; ++i){
				GameSet gameSet = RandomHelper.createRandomStandardTarotGameSet();

				// TODO Get context
				TarotDroidApp.get().getDalService().saveGameSet(gameSet);
				
				int nbGamesToCreate = RandomHelper.nextInt(this.maxGameCount);
				for (int j = 0; j < nbGamesToCreate && this.goOn; ++j ) {
					this.publishProgress("Creating game set " + i + " on " + this.gameSetCount + " (game " + j + "/" + nbGamesToCreate + ")");
					BaseGame game = RandomHelper.createRandomTarotGame(gameSet);
					gameSet.addGame(game);

					// TODO Get context
					TarotDroidApp.get().getDalService().saveGame(game, gameSet);
				}
			}
		}
		catch (Exception e) {
			this.backroundErrorHappened = true;
			this.backgroundException = e;
		}
		return null;
	}
	
    protected void onProgressUpdate(final String... messages) {
    	if (messages.length > 0) {
    		this.dialog.setMessage(messages[0]);
    	}
    }

	@Override
	protected void onPostExecute(final Void unused) {

		// hide busy idicator
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}

		// display toast if error happened
		if (this.backroundErrorHappened) {
            Toast.makeText(
					this.context,
					"Error :" + this.backgroundException,
					Toast.LENGTH_LONG
            ).show();
		}
		
		// display toast if task was stopped
		if (!this.goOn) {
            Toast.makeText(
					this.context,
					"Games creation stopped...",
					Toast.LENGTH_LONG
            ).show();
		}
	}

	@Override
	protected void onCancelled() {
		this.goOn = false;
		super.onCancelled();
	}
}
