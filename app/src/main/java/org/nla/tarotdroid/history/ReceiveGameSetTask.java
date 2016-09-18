package org.nla.tarotdroid.history;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import org.nla.tarotdroid.BuildConfig;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.core.BaseAsyncTask;
import org.nla.tarotdroid.core.dal.IDalService;
import org.nla.tarotdroid.core.helpers.UIHelper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.UUID;

public class ReceiveGameSetTask extends BaseAsyncTask<Void, String, Integer, Object> {

    private final Activity activity;
    private final BluetoothServerSocket serverSocket;
    private ProgressDialog dialog;
    private IDalService dalService;

    public ReceiveGameSetTask(
            final Activity activity,
            final ProgressDialog dialog,
            final BluetoothAdapter bluetoothAdapter,
            final IDalService dalService
    ) throws IOException {
        // TODO check whether checkArgument still useful
//        checkArgument(activity != null, "activity is null");
//        checkArgument(bluetoothAdapter != null, "bluetoothAdapter is null");
        this.activity = activity;
        this.dialog = dialog;
        this.dalService = dalService;
        // TODO Use context
        this.serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(BuildConfig.BLUETOOTH_SERVICE,
                                                                                UUID.fromString(
                                                                                        BuildConfig.BLUETOOTH_UUID));

        if (this.dialog == null) {
            this.dialog = new ProgressDialog(this.activity);
        }
    }

    @Override
    protected Integer doInBackground(final Void... params) {
        int nbGSDownloaded = 0;
        try {
            // wait for a connection
            BluetoothSocket socket = null;
            while (true) {
                socket = this.serverSocket.accept();
                if (socket != null) {
                    this.serverSocket.close();
                    break;
                }
            }

            // indicate the game set is being received
            this.publishProgress(this.activity.getResources()
                                              .getString(R.string.msgDownloadingFromSender));

            // deserialize the game set
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            GameSet gameSet = (GameSet) ois.readObject();
            ois.close();
            socket.close();

            // store the game set and its children
            this.publishProgress(this.activity.getResources()
                                              .getString(R.string.msgStoringDownloadedGameSet));
            // TODO Use context
            dalService.saveGameSet(gameSet);

            // increment game set count
            nbGSDownloaded++;
        } catch (Exception e) {
            Log.v(getClass().toString(), "Logged Exception", e);
            this.backgroundException = e;
        }
        return nbGSDownloaded;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        try {
            this.serverSocket.close();
        } catch (IOException e) {
        }
    }

    @Override
    protected void onPostExecute(final Integer nbGameSets) {
        // hide busy idicator
        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }

        // display toast if error happened
        if (this.backgroundException != null) {
            UIHelper.showSimpleRichTextDialog(this.activity,
                                              this.activity.getString(R.string.msgBluetoothTransferProblem,
                                                                      BuildConfig.APP_VERSION,
                                                                      this.backgroundException.getMessage()),
                                              this.activity.getString(R.string.titleBluetoothTransferProblem));
            // TODO Keep auditing ?
            //auditHelper.auditError(ErrorTypes.bluetoothReceiveError, this.backgroundException);
        }

        // display toast if everything's okay
        else {
            Toast.makeText(this.activity,
                           this.activity.getResources().getString(R.string.msgDownloadSuccededSolo),
                           Toast.LENGTH_LONG).show();

            // executes the potential callback
            if (this.callback != null) {
                this.callback.execute(null, this.backgroundException);
            }
        }
    }

    @Override
    protected final void onPreExecute() {
        this.dialog.setMessage(this.activity.getResources()
                                            .getString(R.string.msgWaitingForSender));
        this.dialog.show();
    }

    @Override
    protected void onProgressUpdate(final String... messages) {
        if (messages.length > 0) {
            this.dialog.setMessage(messages[0]);
        }
    }
}