package org.nla.tarotdroid.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.app.AppContext;
import org.nla.tarotdroid.helpers.ValidationHelper;
import org.nla.tarotdroid.model.TarotDroidUser;
import org.nla.tarotdroid.ui.tasks.IAsyncCallback;

/**
 * Handles signup process.
 */
public class SigninActivity extends Activity {

	private EditText edtEmail;

	private EditText edtPassword;

	// private LogInCallback onPostSignInCallback = new LogInCallback() {
	//
	// @Override
	// public void done(ParseUser user, ParseException e) {
	// onPostSignin(user, e);
	// }
	// };
	/**
	 * The progress dialog.
	 */
	private ProgressDialog progressDialog;
	/**
	 * Signin callback.
	 */
	private final IAsyncCallback<TarotDroidUser> onPostSigninCallback = new IAsyncCallback<TarotDroidUser>() {

		/**
		 * @param result
		 * @param exception
		 */
		@Override
		public void execute(TarotDroidUser result, Exception exception) {
			onPostSignin(result, exception);
		}
	};

	/**
	 * Handles click on button 'Later'.
	 * 
	 * @param view
	 */
	public void onClickOnBtnLater(View view) {
		// Intent intent = new Intent(this, DashboardGridActivity.class);
		// startActivity(intent);
		finish();
	}

	/**
	 * Handles click on button 'Signin'.
	 * 
	 * @param view
	 */
	public void onClickOnBtnSignin(View view) {

		boolean isFormvalid = ValidationHelper.validateEditTextIsEmail(edtEmail, "You must type in a proper email");
		isFormvalid &= ValidationHelper.validateEditTextNotEmpty(edtPassword, "Password must not be empty");

		if (isFormvalid) {
			progressDialog = ProgressDialog.show(this, "Signing in...", "Signing in...", false);
			progressDialog.setCancelable(false);

//			SigninTask signinTask = new SigninTask(edtEmail.getText().toString(), edtPassword.getText().toString());
//			signinTask.setCallback(onPostSigninCallback);
//			signinTask.execute();
		}
	}

	/**
	 * Handles click on button 'Signin with Facebook'.
	 * 
	 * @param view
	 */
	public void onClickOnBtnSigninWithFacebook(View view) {

	}

	/**
	 * Handles click on button 'Signin with Twitter'.
	 * 
	 * @param view
	 */
	public void onClickOnBtnSigninWithTwitter(View view) {

	}

	/**
	 * Handles click on button 'Signup'.
	 * 
	 * @param view
	 */
	public void onClickOnBtnSignup(View view) {
		Intent intent = new Intent(this, SignupActivity.class);
		startActivity(intent);
		finish();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);

		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtPassword = (EditText) findViewById(R.id.edtPassword);
	}

	private void onPostSignin(TarotDroidUser signInUser, Exception e) {
		String message = "";
		progressDialog.dismiss();
		if (e != null) {
			message = edtPassword.getText().toString() + " could not sign in : " + e.getMessage();
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
		} else {
			message = signInUser.getEmail() + " properly signin up (" + signInUser.getUuid() + ")";
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			AppContext.getApplication().setTarotDroidUser(signInUser);

			Intent intent = new Intent(this, MyAccountActivity.class);
			startActivity(intent);
			finish();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		// ParseAnalytics.trackEvent(TrackConstants.DISPLAY_SCREEN_SIGNIN);
	}
}
