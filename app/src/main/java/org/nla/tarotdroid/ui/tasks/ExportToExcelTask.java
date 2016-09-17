package org.nla.tarotdroid.ui.tasks;

import android.app.Activity;
import android.app.ProgressDialog;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.helpers.ExcelHelper;
import org.nla.tarotdroid.helpers.UIHelper;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * An AsyncTask aimed to post a gameset on Facebook.
 */
public class ExportToExcelTask extends BaseAsyncTask<GameSet, String, String, String> {

	/**
	 * The context.
	 */
	private final Activity activity;
	/**
	 * Indicates whether task was canceled;
	 */
	private final boolean isCanceled;
	/**
	 * Progress dialog to display messages.
	 */
	private final ProgressDialog progressDialog;
	/**
	 * Flag indicating whether an error occured in the background.
	 */
	private boolean backroundErrorHappened;

	private UIHelper uiHelper;
	/**
	 * Constructor.
	 * 
	 * @param activity
	 * @param progressDialog
	 */
	public ExportToExcelTask(Activity activity, ProgressDialog progressDialog, UIHelper uiHelper) {
		checkArgument(activity != null, "activity is null");
		this.activity = activity;
		this.isCanceled = false;
		this.progressDialog = progressDialog;
		this.uiHelper = uiHelper;
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
				fileName = ExcelHelper.exportToExcel(this.activity,
													 TarotDroidApp.get()
																  .getDalService()
																  .getAllGameSets(),
													 uiHelper);
			} else {
				fileName = ExcelHelper.exportToExcel(this.activity, params[0], uiHelper);
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