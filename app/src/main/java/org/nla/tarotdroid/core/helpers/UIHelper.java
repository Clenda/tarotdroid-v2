package org.nla.tarotdroid.core.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.constants.PreferenceConstants;

public final class UIHelper {

    private final static int DAYS_UNTIL_PROMPT = 3;
    private final static int LAUNCHES_UNTIL_PROMPT = 7;

	private static DialogInterface.OnClickListener richTextDialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(final DialogInterface dialog, final int which) {
	        switch (which) {
	            case DialogInterface.BUTTON_POSITIVE:
	            case DialogInterface.BUTTON_NEGATIVE:
	            default:
	        	break;
	        }
	    }
	};
	public static void setKeepScreenOn(final Activity activity, final boolean keepScreenOn) {
		if (keepScreenOn) {
			activity.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		} 
		else {
			activity.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}
	
	public static void showSimpleRichTextDialog(final Context context, final String richText, final String title) {
        LayoutInflater factory = LayoutInflater.from(context);
        final View alertDialogView = factory.inflate(R.layout.alert_dialog, null);
        TextView txtDialog = (TextView)alertDialogView.findViewById(R.id.txtDialog);
        txtDialog.setText(Html.fromHtml(richText));
        txtDialog.setMovementMethod(LinkMovementMethod.getInstance());
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setPositiveButton(context.getString(R.string.btnOk), UIHelper.richTextDialogClickListener);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setView(alertDialogView);
		builder.show();
	}
	
	public static void showModifyOrDeleteGameMessage(final Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        
		// display message if first time
		if (!prefs.getBoolean(PreferenceConstants.PrefGameDeletionOrModificationMessageAlreadyShown, false)) { 
			UIHelper.showSimpleRichTextDialog(
				context, 
				context.getString(R.string.msgYouCanModifyAGame),
				context.getString(R.string.titleYouCanModifyAGame)
			);
			
			SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(PreferenceConstants.PrefGameDeletionOrModificationMessageAlreadyShown, true);
            editor.commit();
        }
	}

	/**
	 * Decides whether to show the "please rate my app dialog" 
	 * Code found here http://www.androidsnippets.com/prompt-engaged-users-to-rate-your-app-in-the-android-market-appirater
	 */
    public static void trackAppLaunched(final Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PreferenceConstants.PrefAppRater, 0);
        if (prefs.getBoolean(PreferenceConstants.PrefDontShowAgain, false)) { 
        	return ; 
        }
        
        SharedPreferences.Editor editor = prefs.edit();
        
        // Increment launch counter
        long launch_count = prefs.getLong(PreferenceConstants.PrefLaunchCount, 0) + 1;
        editor.putLong(PreferenceConstants.PrefLaunchCount, launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong(PreferenceConstants.PrefDateFirstLaunch, 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong(PreferenceConstants.PrefDateFirstLaunch, date_firstLaunch);
        }
        
        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(context, editor);
            }
        }
        editor.commit();
    }
    
    private static void showRateDialog(final Context context, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(context);
        dialog.setTitle(context.getString(R.string.lblRateAppTitle));

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        
        TextView tv = new TextView(context);
        tv.setText(context.getString(R.string.lblRateAppContent));
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);
        
        Button b1 = new Button(context);
        b1.setText(context.getString(R.string.lblRateNow));
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=org.nla.tarotdroid")));
                dialog.dismiss();
            }
        });        
        ll.addView(b1);

        Button b2 = new Button(context);
        b2.setText(context.getString(R.string.lblRateLater));
        b2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(context);
        b3.setText(context.getString(R.string.lblRateNever));
        b3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean(PreferenceConstants.PrefDontShowAgain, true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);        
        dialog.show();        
    }
	
	public static Bitmap getContactPicture(final Context context, final String contactId) {
		// the photo to return
		Bitmap photoBitmap = null;

		// get the contact id
		String photoId = null;

		// query for contact name and photo
		Cursor contact = context.getContentResolver().query(
				Contacts.CONTENT_URI,
				new String[]{Contacts.DISPLAY_NAME, Contacts.PHOTO_ID},
				Contacts._ID + "=?",
				new String[]{contactId},
				null
		);

		// get name and photo id
		if (contact.moveToFirst()) {
			photoId = contact.getString(contact.getColumnIndex(Contacts.PHOTO_ID));
		}
		contact.close();

		// if possible, try to get the photo
		if (photoId != null) {

			// query for actual photo
			Cursor photo = context.getContentResolver().query(
					Data.CONTENT_URI,
					new String[]{Photo.PHOTO},
					Data._ID + "=?",
					new String[]{photoId},
					null
			);

			// get the photo
			if (photo.moveToFirst()) {
				byte[] photoBlob = photo.getBlob(photo.getColumnIndex(Photo.PHOTO));
				photoBitmap = BitmapFactory.decodeByteArray(photoBlob, 0, photoBlob.length);
			}
			photo.close();
		}

		return photoBitmap;
	}
}
