package org.nla.tarotdroid.ui.tasks;

import android.os.AsyncTask;

import com.google.common.base.Charsets;
import com.google.gson.Gson;

import org.apache.http.params.HttpParams;

import okhttp3.OkHttpClient;

/**
 * A base Async Task aimed to handle all common behavior.
 * 
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract class BaseAsyncTask<Params, Progress, Result, CallbackResult> extends AsyncTask<Params, Progress, Result> {

	/**
	 * The potential exception thrown in the background.
	 */
	protected Exception backgroundException;

	/**
	 * The potential callback to post execute.
	 */
	protected IAsyncCallback<CallbackResult> callback;

	/**
	 * The Gson object for serializing/deserializing objects as a json.
	 */
	protected Gson gson;

	/**
	 * The Http client to aimed handle http communications.
	 */
    protected OkHttpClient httpClient;

	/**
	 * Constructor.
	 * 
	 * @param tarotDroidUser
	 */
	public BaseAsyncTask() {
		this.gson = new Gson();

		if (this.httpClient == null) {
			this.httpClient = new OkHttpClient();

			HttpParams httpParams = new BasicHttpParams();
			HttpProtocolParams.setContentCharset(httpParams, Charsets.UTF_8.toString());
			this.httpClient.setParams(httpParams);
		}
	}

	/**
	 * Sets the callback.
	 * 
	 * @param callback
	 */
	public void setCallback(IAsyncCallback<CallbackResult> callback) {
		this.callback = callback;
	}
}
