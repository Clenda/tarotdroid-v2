package org.nla.tarotdroid.core;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.google.common.base.Preconditions.checkArgument;

// TODO Check http://stackoverflow.com/questions/34179922/okhttp-post-body-as-json for okhttp
public class ShortenUrlTask extends BaseAsyncTask<Void, String, String, String> {

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private final boolean isCanceled;
	private final String urlToShorten;
	public ShortenUrlTask(final String urlToShorten) {
		checkArgument(urlToShorten != null, "urlToShorten is null");
		this.urlToShorten = urlToShorten;
		this.isCanceled = false;
	}

	@Override
	protected String doInBackground(Void... params) {

		String shortenedUrl = null;
		try {
			UrlShortenerRequest urlShortenerRequest = new UrlShortenerRequest();
			urlShortenerRequest.setLongUrl(this.urlToShorten);

			RequestBody requestBody = RequestBody.create(JSON, gson.toJson(urlShortenerRequest));
			Request request = new Request.Builder()
					.url("https://www.googleapis.com/urlshortener/v1/url")
					.post(requestBody)
					.build();

			Response response = httpClient.newCall(request).execute();
			if (response.isSuccessful()) {
				String content = response.body().string();

				final Type urlShortenerResponseType = new TypeToken<UrlShortenerResponse>() {
				}.getType();
				UrlShortenerResponse urlShortenerResponse = gson.fromJson(content,
																		  urlShortenerResponseType);

				shortenedUrl = urlShortenerResponse.getId();
			}
		} catch (Exception e) {
			this.backgroundException = e;
		}
		return shortenedUrl;
	}

	@Override
	protected void onPostExecute(String shortenedUrl) {

		if (!this.isCanceled && this.callback != null) {
			this.callback.execute(shortenedUrl, this.backgroundException);
		}
	}

	private class UrlShortenerRequest {

		private String longUrl;

		@SuppressWarnings("unused")
		protected String getLongUrl() {
			return this.longUrl;
		}

		protected void setLongUrl(String longUrl) {
			this.longUrl = longUrl;
		}

		@Override
		public String toString() {
			return gson.toJson(this);
		}
	}

	private class UrlShortenerResponse {

		private String id;
		private String kind;
		private String longUrl;

		protected String getId() {
			return this.id;
		}

		@SuppressWarnings("unused")
		protected void setId(String id) {
			this.id = id;
		}

		@SuppressWarnings("unused")
		protected String getKind() {
			return this.kind;
		}

		@SuppressWarnings("unused")
		protected void setKind(String kind) {
			this.kind = kind;
		}

		@SuppressWarnings("unused")
		protected String getLongUrl() {
			return this.longUrl;
		}

		@SuppressWarnings("unused")
		protected void setLongUrl(String longUrl) {
			this.longUrl = longUrl;
		}

		@Override
		public String toString() {
			return gson.toJson(this);
		}
	}
}