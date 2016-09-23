package org.nla.tarotdroid.gameset;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.nla.tarotdroid.BuildConfig;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.constants.ResultCodes;
import org.nla.tarotdroid.core.dal.IDalService;

import java.util.List;

public class RemoveGameTask extends AsyncTask<BaseGame, Void, Void> {

    private final ProgressDialog dialog;
    private Activity activity;
    private boolean backroundErrorHappened;
    private Exception backgroundException;
    private Activity activityToFinish;
    private GameSet gameSet;
    private IDalService dalService;

    public RemoveGameTask(
            final Activity activity,
            final Activity activityToFinish,
            final GameSet gameSet,
            final IDalService dalService
    ) {
        this.activity = activity;
        this.gameSet = gameSet;
        this.dialog = new ProgressDialog(this.activity);
        this.backroundErrorHappened = false;
        this.activityToFinish = activityToFinish;
        this.dalService = dalService;
    }

    @Override
    protected final void onPreExecute() {
        this.dialog.setMessage(this.activity.getResources().getString(R.string.msgGameDeletion));
        this.dialog.show();
    }

    @Override
    protected final Void doInBackground(final BaseGame... games) {
        try {

            // remove game of given index
            if (games != null && games.length == 1) {
                // remove all games starting and after selected game
                List<BaseGame> removedGames = this.gameSet.removeGameAndAllSubsequentGames(games[0]);

                // delete from dal only if gameset is persisted
                if (this.gameSet.isPersisted()) {
                    // TODO optimize...
                    for (BaseGame removedGame : removedGames) {
                        // TODO Use context
                        dalService.deleteGame(removedGame, this.gameSet);
                    }
                }
            } else {
                // remove last game from game set, and get a reference to it before it's removed
                BaseGame removedGame = this.gameSet.removeLastGame();

                // delete from dal only if gameset is persisted
                if (this.gameSet.isPersisted()) {
                    // TODO Use context
                    dalService.deleteGame(removedGame, this.gameSet);
                }
            }
        } catch (Exception e) {
            this.backroundErrorHappened = true;
            this.backgroundException = e;
            Log.v(BuildConfig.APP_LOG_TAG, this.getClass().toString(), e);
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
        if (this.backroundErrorHappened) {
            Toast.makeText(
                    this.activity,
                    this.activity.getResources()
                                 .getText(R.string.msgDeleteGameFromDalException)
                                 .toString() + this.backgroundException,
                    Toast.LENGTH_LONG
            ).show();
        }

        // display toast if everything's okay
        else {
            Toast.makeText(
                    this.activity,
                    this.activity.getResources().getString(R.string.msgGameDeleted),
                    Toast.LENGTH_LONG
            ).show();
        }

        // finish the activity if set
        if (this.activityToFinish != null) {

            // go back to main previous activity
            this.activityToFinish.setResult(ResultCodes.RemovedGame_Ok);
            this.activityToFinish.finish();
        }
    }
}