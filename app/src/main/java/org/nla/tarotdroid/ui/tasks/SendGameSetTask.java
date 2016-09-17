package org.nla.tarotdroid.ui.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import org.nla.tarotdroid.BuildConfig;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.helpers.UIHelper;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.UUID;

public class SendGameSetTask extends BaseAsyncTask<Void, String, Integer, Object> {

    private final Activity activity;
    private final BluetoothAdapter bluetoothAdapter;
    private final BluetoothDevice bluetoothDevice;
    private final GameSet gameSet;
    private final OutputStream outStream;
    private final BluetoothSocket socket;
    private ProgressDialog dialog;

    public SendGameSetTask(
            final Activity activity,
            final ProgressDialog dialog,
            final GameSet gameSet,
            final BluetoothDevice bluetoothDevice,
            final BluetoothAdapter bluetoothAdapter
    )
            throws IOException {

        // TODO Still useful to check arguments ?
//        checkArgument(activity != null, "activity is null");
//        checkArgument(gameSet != null, "gameSet is null");
//        checkArgument(bluetoothDevice != null, "bluetoothDevice is null");
//        checkArgument(bluetoothAdapter != null, "bluetoothAdapter is null");

        this.activity = activity;
        this.dialog = dialog;
        this.gameSet = gameSet;
        this.bluetoothAdapter = bluetoothAdapter;
        this.bluetoothDevice = bluetoothDevice;
        this.socket = this.bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(
                BuildConfig.BLUETOOTH_UUID));
        this.outStream = this.socket.getOutputStream();

        if (this.dialog == null) {
            this.dialog = new ProgressDialog(this.activity);
        }
    }

    @Override
    protected Integer doInBackground(final Void... params) {
        this.bluetoothAdapter.cancelDiscovery();
        int nbGameSetDownloaded = 0;
        try {
            // Connect the device through the socket. This will block until it
            // succeeds or throws an exception
            this.socket.connect();

            // indicate the game set is being received
            this.publishProgress(this.activity.getResources()
                                              .getString(R.string.msgSendingToReceviver));

            // serialize game set
            ObjectOutputStream oos = new ObjectOutputStream(this.outStream);
            oos.writeObject(this.gameSet);
            oos.flush();
            ++nbGameSetDownloaded;
        } catch (final IOException ioe) {
            Log.v(BuildConfig.APP_LOG_TAG, getClass().toString(), ioe);
            this.backgroundException = ioe;
            try {
                this.socket.close();
            } catch (IOException closeException) {
            }
            nbGameSetDownloaded = 0;
        } catch (final Exception e) {
            Log.v(BuildConfig.APP_LOG_TAG, getClass().toString(), e);
            this.backgroundException = e;
            nbGameSetDownloaded = 0;
        }
        return Integer.valueOf(nbGameSetDownloaded);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        try {
            this.socket.close();
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
            // TODO auditError still useful ?
//            auditHelper.auditError(AuditHelper.ErrorTypes.bluetoothSendError,
//                                   this.backgroundException);
        }

        // display toast if everything's okay
        else {
            Toast.makeText(this.activity,
                           this.activity.getResources().getString(R.string.msgSendSuccededSolo),
                           Toast.LENGTH_LONG).show();

            // executes the potential callback
            if (this.callback != null) {
                this.callback.execute(null, null);
            }
        }
    }

    @Override
    protected final void onPreExecute() {
        this.dialog.setMessage(this.activity.getResources()
                                            .getString(R.string.msgWaitingForReceiver,
                                                       this.bluetoothDevice.getName()));
        this.dialog.show();
    }

    @Override
    protected void onProgressUpdate(final String... messages) {
        if (messages.length > 0) {
            this.dialog.setMessage(messages[0]);
        }
    }
}