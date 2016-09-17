package org.nla.tarotdroid.history;

import android.app.Activity;
import android.app.ProgressDialog;

import com.google.gson.reflect.TypeToken;

import org.nla.tarotdroid.BuildConfig;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.clientmodel.RestAccount;
import org.nla.tarotdroid.clientmodel.RestGameSet;
import org.nla.tarotdroid.clientmodel.RestPlayer;
import org.nla.tarotdroid.cloud.GameSetConverter;
import org.nla.tarotdroid.cloud.PlayerConverter;
import org.nla.tarotdroid.core.BaseAsyncTask;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.google.common.base.Preconditions.checkArgument;

public class UpSyncGameSetTask extends BaseAsyncTask<GameSet, String, Void, Object> {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final boolean isCanceled;
    private Activity activity;
    private ProgressDialog progressDialog;

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

    public void attach(Activity activity) {
        this.activity = activity;
    }

    public void detach() {
        this.activity = null;
    }

    @Override
    protected Void doInBackground(GameSet... params) {
        try {
            // TODO Remplace l'authent' par autre chose...
            String facebookEmail = "facebookEmailProperty.toString();";

            if (!accountExists("facebookEmail", "user")) {
                createAccount("facebookEmail", "user");
            }
            upSyncGameSetPlayers(params[0], "facebookEmail", "user");
            upSyncGameSet(params[0], "facebookEmail", "user");

        } catch (Exception e) {
            this.backgroundException = e;
        }
        return null;
    }

    private boolean accountExists(String facebookEmail, String user) throws IOException {
        Request request = new Request.Builder()
                .url(BuildConfig.BACKEND_BASE_URL + "/rest/accounts")
                .addHeader("Accept-Language", "fr-FR")
                .addHeader("Cookie",
                           MessageFormat.format("EMAIL={0}; EXTID={1}; EXTSYS={2}; TOKEN={3}",
                                                facebookEmail,
                                                user,
                                                "facebook",
                                                "Session.getActiveSession().getAccessToken()"))
                .get()
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.isSuccessful();
    }

    private void createAccount(String facebookEmail, String user) throws Exception {
        RestAccount newRestAccount = new RestAccount();
        newRestAccount.setName(facebookEmail);

        RequestBody createAccountRequestBody = RequestBody.create(JSON,
                                                                  gson.toJson(newRestAccount));
        Request createAccountRequest = new Request.Builder()
                .url(BuildConfig.BACKEND_BASE_URL + "/rest/accounts")
                .addHeader("Accept-Language", "fr-FR")
                .addHeader("Cookie",
                           MessageFormat.format("EMAIL={0}; EXTID={1}; EXTSYS={2}; TOKEN={3}",
                                                facebookEmail,
                                                user,
                                                "facebook",
                                                "Session.getActiveSession().getAccessToken()"))
                .post(createAccountRequestBody)
                .build();
        Response createAccountResponse = httpClient.newCall(createAccountRequest).execute();
        if (!createAccountResponse.isSuccessful()) {
            String content = createAccountResponse.body().string();
            throw new Exception(MessageFormat.format("Problem when creating account: {0}",
                                                     content));
        }
    }

    private void upSyncGameSetPlayers(
            GameSet gameSetToUpload,
            String facebookEmail,
            String user
    ) throws Exception {
        List<RestPlayer> playersToUploadRest = PlayerConverter.convertToRest(gameSetToUpload.getPlayers()
                                                                                            .getPlayers());

        if (playersToUploadRest != null && playersToUploadRest.size() > 0) {

            RequestBody requestBody = RequestBody.create(JSON, gson.toJson(playersToUploadRest));
            Request request = new Request.Builder()
                    .url(BuildConfig.BACKEND_BASE_URL + "/rest/players")
                    .addHeader("Accept-Language", "fr-FR")
                    .addHeader("Cookie",
                               MessageFormat.format("EMAIL={0}; EXTID={1}; EXTSYS={2}; TOKEN={3}",
                                                    facebookEmail,
                                                    "user",
                                                    "facebook",
                                                    "Session.getActiveSession().getAccessToken()"))
                    .post(requestBody)
                    .build();
            Response response = httpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                // retrieve the potential new uuids for players
                String responseContent = response.body().string();
                final Type collectionType = new TypeToken<Map<String, String>>() {
                }.getType();
                Map<String, String> restFormerAndNewsUuids = gson.fromJson(responseContent,
                                                                           collectionType);

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
                    TarotDroidApp.get()
                                 .getDalService()
                                 .updatePlayerAfterSync(playerToStore, newPlayerUuid);

                    // update actual player
                    if (newPlayerUuid != null) {
                        playerToStore.setUuid(newPlayerUuid);
                    }
                }
            } else {
                String content = response.body().string();
                throw new Exception(MessageFormat.format(
                        "Problème lors du l''upload des nouveaux joueurs: Code = {0}, Message = {1}",
                        response.code(),
                        content));
            }
        }

    }

    private void upSyncGameSet(
            GameSet gameSetToUpload,
            String facebookEmail,
            String user
    ) throws Exception {
        List<RestGameSet> gameSetsToUploadRest = GameSetConverter.convertToRest(Arrays.asList(
                gameSetToUpload));

        RequestBody requestBody = RequestBody.create(JSON, gson.toJson(gameSetsToUploadRest));
        Request request = new Request.Builder()
                .url(BuildConfig.BACKEND_BASE_URL + "/rest/gamesets")
                .addHeader("Accept-Language", "fr-FR")
                .addHeader("Cookie",
                           MessageFormat.format("EMAIL={0}; EXTID={1}; EXTSYS={2}; TOKEN={3}",
                                                facebookEmail,
                                                user,
                                                "facebook",
                                                "Session.getActiveSession().getAccessToken()"))
                .post(requestBody)
                .build();

        Response response = httpClient.newCall(request).execute();

        if (response.isSuccessful()) {
            // update the game set with its cloud info
            gameSetToUpload.setSyncTimestamp(new Date());
            gameSetToUpload.setSyncAccount(facebookEmail);
            TarotDroidApp.get().getDalService().updateGameSetAfterSync(gameSetToUpload);
        } else {
            String content = response.body().string();
            throw new Exception(MessageFormat.format(
                    "Problème lors de l''upload de la partie: Code = {0}, Message = {1}",
                    response.code(),
                    content));
        }
    }

    @Override
    protected void onPostExecute(final Void param) {
        this.progressDialog.setOnCancelListener(null);
        if (this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }

        if (this.backgroundException != null) {
            // TODO Keep audit error ?
            // auditHelper.auditError(AuditHelper.ErrorTypes.upSyncGameSetTaskError, this.backgroundException, this.activity);
            return;
        }

        if (!this.isCanceled && this.callback != null) {
            this.callback.execute(null, this.backgroundException);
        }
    }
}
