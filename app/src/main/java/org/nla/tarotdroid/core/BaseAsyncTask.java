package org.nla.tarotdroid.core;

import android.os.AsyncTask;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;

public abstract class BaseAsyncTask<Params, Progress, Result, CallbackResult> extends AsyncTask<Params, Progress, Result> {

	protected Exception backgroundException;
	protected IAsyncCallback<CallbackResult> callback;
	protected Gson gson;
    protected OkHttpClient httpClient;

	public BaseAsyncTask() {
		this.gson = new Gson();

		if (httpClient == null) {
			httpClient = new OkHttpClient();

		}
	}

	public void setCallback(IAsyncCallback<CallbackResult> callback) {
		this.callback = callback;
	}
}
