package org.nla.tarotdroid.dashboard;

import android.app.Activity;
import android.os.Environment;

import org.nla.tarotdroid.core.BaseAsyncTask;
import org.nla.tarotdroid.core.dal.IDalService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;

public class ImportDatabaseTask extends BaseAsyncTask<Void, String, String, String> {

    private final Activity activity;
    private final String importFilePath;
    private final boolean isCanceled;
    private boolean backroundErrorHappened;
    private IDalService dalService;

    public ImportDatabaseTask(
            final Activity activity,
            final String importFilePath,
            final IDalService dalService
    ) {
        // TODO checkArgument ?
//		checkArgument(activity != null, "activity is null");
//		checkArgument(importFilePath != null, "importFileUri is null");
        this.activity = activity;
        this.isCanceled = false;
        this.importFilePath = importFilePath;
        this.dalService = dalService;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected String doInBackground(Void... params) {

        String exportFileUri = null;
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File tarotDroidDir = new File(sdcard.getAbsolutePath(), "TarotDroid");
            if (!tarotDroidDir.exists()) {
                tarotDroidDir.mkdir();
            }
            File exportFile = new File(tarotDroidDir, "export_backup.xml");
            exportFile.createNewFile();

            PrintStream printStream = new PrintStream(exportFile);
            printStream.print(dalService.exportDatabaseContent());
            printStream.close();

            // File importFile = new File(tarotDroidDir, "import.xml");
            File importFile = new File(this.importFilePath);

            StringBuilder xmlContent = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(importFile));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                xmlContent.append(line);
            }
            reader.close();

            dalService.importDatabaseContent(xmlContent.toString());
            // TODO Use context
            dalService.initialize();
            exportFileUri = "imported";
        } catch (Exception e) {
            this.backroundErrorHappened = true;
            this.backgroundException = e;
        }
        return exportFileUri;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(String databaseContent) {

        // display error if exception occured
        if (this.backroundErrorHappened) {

            // TODO audit ?
            //auditHelper.auditError(AuditHelper.ErrorTypes.importDatabaseError, this.backgroundException, this.activity);
            return;
        }

        // else executes potential callback
        else {
            if (!this.isCanceled && this.callback != null) {
                this.callback.execute(databaseContent, backgroundException);
            }
        }
    }
}