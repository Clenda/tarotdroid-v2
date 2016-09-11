package org.nla.tarotdroid.ui.tasks;

import android.app.Activity;
import android.app.ProgressDialog;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.reflect.TypeToken;

import org.apache.http.params.HttpParams;
import org.nla.tarotdroid.app.AppContext;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.clientmodel.RestAccount;
import org.nla.tarotdroid.clientmodel.RestGameSet;
import org.nla.tarotdroid.clientmodel.RestPlayer;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.ui.cloud.GameSetConverter;
import org.nla.tarotdroid.ui.cloud.PlayerConverter;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import twitter4j.internal.http.HttpResponse;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * An AsyncTask aimed to store a game set in the cloud.
 */
public class UpSyncGameSetTask extends BaseAsyncTask<GameSet, String, Void, Object> {

	/**
     * Indicates whether task was canceled;
     */
    private final boolean isCanceled;
    /**
     * The context.
     */
    private Activity activity;
    /**
     * Progress dialog to display messages.
	 */
	private ProgressDialog progressDialog;

	/**
	 * Constructor.
	 * 
	 * @param activity
	 * @param progressDialog
	 */
	public UpSyncGameSetTask(final Activity activity, final ProgressDialog progressDialog) {
		checkArgument(activity != null, "activity is null");
		this.activity = activity;
		this.progressDialog = progressDialog;
		this.isCanceled = false;

		if (this.httpClient == null) {
            this.httpClient = new OkHttpClient();
        }
        if (this.progressDialog == null) {
			this.progressDialog = new ProgressDialog(this.activity);
			this.progressDialog.setCanceledOnTouchOutside(false);
		}
	}

	/**
	 * TODO Probably to remove.
	 * 
	 * @param activity
	 */
	public void attach(Activity activity) {
		this.activity = activity;
	}

	/**
	 * TODO Probably to remove.
	 */
	public void detach() {
		this.activity = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Void doInBackground(GameSet... params) {
		try {
			GraphUser user = AppContext.getApplication().getLoggedFacebookUser();
			Object facebookEmailProperty = user.getProperty("email");
			String facebookEmail = facebookEmailProperty.toString();

			//
			// Check cloud account exists
			//

			HttpGet request = new HttpGet("http://" + AppContext.getApplication().getCloudDns() + "/rest/accounts");
			request.setHeader("Content-type", "application/json; charset=UTF-8");
			request.setHeader("Accept-Language", "fr-FR");
			request.addHeader("Cookie", MessageFormat.format("EMAIL={0}; EXTID={1}; EXTSYS={2}; TOKEN={3}", facebookEmail, user.getId(), "facebook", Session.getActiveSession().getAccessToken()));

			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {

				//
				// Create cloud account if it doesn't exist
				//
				RestAccount newRestAccount = new RestAccount();
				newRestAccount.setName(facebookEmail);

				HttpPost postRequest = new HttpPost("http://" + AppContext.getApplication().getCloudDns() + "/rest/accounts");
				postRequest.setHeader("Content-type", "application/json; charset=UTF-8");
				postRequest.setHeader("Accept-Language", "fr-FR");
				postRequest.addHeader("Cookie",
									  MessageFormat.format("EMAIL={0}; EXTID={1}; EXTSYS={2}; TOKEN={3}", facebookEmail, user.getId(), "facebook", Session.getActiveSession().getAccessToken()));
				postRequest.setEntity(new StringEntity(gson.toJson(newRestAccount)));

				response = httpClient.execute(postRequest);
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK && response.getStatusLine().getStatusCode() != HttpStatus.SC_ACCEPTED) {
					String content = CharStreams.toString(new InputStreamReader(response.getEntity().getContent()));
					throw new Exception(MessageFormat.format("Problem when creating account: {0}", content));
				}
			}

			// //
			// // STEP 1 : Upsync gameset players
			// //

			GameSet gameSetToUpload = params[0];
			List<RestPlayer> playersToUploadRest = PlayerConverter.convertToRest(gameSetToUpload.getPlayers().getPlayers());

			if (playersToUploadRest != null && playersToUploadRest.size() > 0) {
				HttpPost postRequest = new HttpPost("http://" + AppContext.getApplication().getCloudDns() + "/rest/players");
				postRequest.setHeader("Content-type", "application/json; charset=UTF-8");
				postRequest.setHeader("Accept-Language", "fr-FR");
				postRequest.addHeader("Cookie",
									  MessageFormat.format("EMAIL={0}; EXTID={1}; EXTSYS={2}; TOKEN={3}", facebookEmail, user.getId(), "facebook", Session.getActiveSession().getAccessToken()));
				postRequest.setEntity(new StringEntity(gson.toJson(playersToUploadRest)));

				response = httpClient.execute(postRequest);
				int status = response.getStatusLine().getStatusCode();
				if (status == HttpStatus.SC_OK || status == HttpStatus.SC_ACCEPTED) {

					// retrieve the potential new uuids for players
					String responseContent = CharStreams.toString(new InputStreamReader(response.getEntity().getContent()));
					final Type collectionType = new TypeToken<Map<String, String>>() {
					}.getType();
					Map<String, String> restFormerAndNewsUuids = gson.fromJson(responseContent, collectionType);

					// update the players that were created on the cloud with
					// their cloud info
					for (Player playerToStore : gameSetToUpload.getPlayers().getPlayers()) {

						// update necessary cloud info
						playerToStore.setSyncTimestamp(new Date());
						playerToStore.setSyncAccount(facebookEmail);

						// update player uuid if cloud says so
						String newPlayerUuid = null;
						if (restFormerAndNewsUuids.containsKey(playerToStore.getUuid())) {
							newPlayerUuid = restFormerAndNewsUuids.get(playerToStore.getUuid());
						}

						// update payer in db
						AppContext.getApplication().getDalService().updatePlayerAfterSync(playerToStore, newPlayerUuid);

						// update actual player
						if (newPlayerUuid != null) {
							playerToStore.setUuid(newPlayerUuid);
						}
					}
				} else if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					String content = CharStreams.toString(new InputStreamReader(response.getEntity().getContent()));
					throw new Exception(MessageFormat.format("Problème lors du l''upload des nouveaux joueurs: Code = {0}, Message = {1}", response.getStatusLine().getStatusCode(), content));
				}
			}

			// //
			// // STEP 2 : Upsync gameset
			// //

			List<RestGameSet> gameSetsToUploadRest = GameSetConverter.convertToRest(Arrays.asList(gameSetToUpload));

            RequestBody body = RequestBody.create(JSON,
                                                  new StringEntity(gson.toJson(gameSetsToUploadRest)));
            Request httpRequest = new Request.Builder()
                    .url("http://" + AppContext.getApplication().getCloudDns() + "/rest/gamesets")
                    .header("Content-type", "application/json; charset=UTF-8")
                    .header("Accept-Language", "fr-FR")
                    .header("Cookie",
                            MessageFormat.format("EMAIL={0}; EXTID={1}; EXTSYS={2}; TOKEN={3}",
                                                 facebookEmail,
                                                 user.getId(),
                                                 "facebook",
                                                 Session.getActiveSession().getAccessToken())
                                         .build());
            Response httpResponse = httpClient.newCall(httpRequest).execute();
            String responseBody = httpResponse.body().string();

            if (httpResponse.getStatusCode() == HttpStatus.SC_OK) {
                // update the game set with its cloud info
                gameSetToUpload.setSyncTimestamp(new Date());
				gameSetToUpload.setSyncAccount(facebookEmail);
				AppContext.getApplication().getDalService().updateGameSetAfterSync(gameSetToUpload);
			} else if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				String content = CharStreams.toString(new InputStreamReader(response.getEntity().getContent()));
				throw new Exception(MessageFormat.format("Problème lors de l''upload de la partie: Code = {0}, Message = {1}", response.getStatusLine().getStatusCode(), content));
			}
		} catch (Exception e) {
			this.backgroundException = e;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(final Void param) {
		this.progressDialog.setOnCancelListener(null);
		if (this.progressDialog.isShowing()) {
			this.progressDialog.dismiss();
		}

		if (this.backgroundException != null) {
			AuditHelper.auditError(AuditHelper.ErrorTypes.upSyncGameSetTaskError, this.backgroundException, this.activity);
			return;
		}

		if (!this.isCanceled && this.callback != null) {
			this.callback.execute(null, this.backgroundException);
		}
	}
}
