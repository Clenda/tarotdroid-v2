package org.nla.tarotdroid.history;

import android.app.Activity;
import android.app.ProgressDialog;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.core.BaseAsyncTask;
import org.nla.tarotdroid.core.IAsyncCallback;
import org.nla.tarotdroid.core.dal.IDalService;
import org.nla.tarotdroid.core.helpers.ExcelHelper;
import org.nla.tarotdroid.core.helpers.LocalizationHelper;

import static com.google.common.base.Preconditions.checkArgument;

public class ExportToExcelTask extends BaseAsyncTask<GameSet, String, String, String> {

    private final Activity activity;
    private final boolean isCanceled;
    private final ProgressDialog progressDialog;
    private boolean backroundErrorHappened;
    private LocalizationHelper localizationHelper;
    private IDalService dalService;

    public ExportToExcelTask(
            Activity activity,
            ProgressDialog progressDialog,
            LocalizationHelper localizationHelper,
            final IDalService dalService
    ) {
        checkArgument(activity != null, "activity is null");
        this.activity = activity;
        this.isCanceled = false;
        this.progressDialog = progressDialog;
        this.localizationHelper = localizationHelper;
        this.dalService = dalService;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected String doInBackground(GameSet... params) {
        String fileName = null;
        try {
            if (params == null || params.length == 0 || params[0] == null) {
                // TODO Use context
                fileName = ExcelHelper.exportToExcel(this.activity, dalService.getAllGameSets(),
                                                     localizationHelper);
            } else {
                fileName = ExcelHelper.exportToExcel(this.activity, params[0], localizationHelper);
            }
        } catch (Exception e) {
            this.backroundErrorHappened = true;
            this.backgroundException = e;
        }
        return fileName;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(final String fileName) {
        this.progressDialog.setOnCancelListener(null);
        if (this.progressDialog != null && this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }

        if (this.backroundErrorHappened) {
            // TODO audit ?
            // auditHelper.auditError(ErrorTypes.excelFileStorage, this.backgroundException, this.activity);
            return;
        }

        // display toast if everything's okay
        else {
            if (!this.isCanceled && this.callback != null) {
                this.callback.execute(fileName, backgroundException);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected void onPreExecute() {
        progressDialog.setTitle(R.string.lblExportInProgress);
        progressDialog.show();
    }

    /**
     * Sets the callback.
     *
     * @param callback
     */
    @Override
    public void setCallback(final IAsyncCallback<String> callback) {
        this.callback = callback;
    }
}