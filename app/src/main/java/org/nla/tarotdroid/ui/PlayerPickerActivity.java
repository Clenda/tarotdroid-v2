package org.nla.tarotdroid.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.facebook.FacebookException;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.app.AppContext;

public class PlayerPickerActivity extends AppCompatActivity {

	public static final Uri FRIEND_PICKER = Uri.parse("picker://friend");
	
	private FriendPickerFragment friendPickerFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.pickers);

	    Bundle args = getIntent().getExtras();
	    FragmentManager manager = getSupportFragmentManager();
	    Fragment fragmentToShow = null;

        if (savedInstanceState == null) {
            this.friendPickerFragment = new FriendPickerFragment(args);
        }
        else {
        	this.friendPickerFragment = (FriendPickerFragment) manager.findFragmentById(R.id.picker_fragment);
        }
        
        // Set the listener to handle errors
        this.friendPickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener() {

			@Override
			public void onError(PickerFragment<?> fragment, FacebookException error) {
				
			}
        });
        
        // Set the listener to handle button clicks
        this.friendPickerFragment.setOnDoneButtonClickedListener(
                new PickerFragment.OnDoneButtonClickedListener() {
		            @Override
		            public void onDoneButtonClicked(PickerFragment<?> fragment) {
		                finishActivity();
		            }
        });
        fragmentToShow = this.friendPickerFragment;

	    manager.beginTransaction()
	           .replace(R.id.picker_fragment, fragmentToShow)
	           .commit();
	}

	private void onError(Exception error) {
	    this.onError(error.getLocalizedMessage(), false);
	}

	private void onError(String error, final boolean finishActivity) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.error_dialog_title).setMessage(error);
	    builder.setPositiveButton(R.string.error_dialog_button_text,  new DialogInterface.OnClickListener() {
            	@Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (finishActivity) {
                        finishActivity();
                    }
                }
            }
	    );
	    builder.show();
	}
	
	@Override
	protected void onStart() {
	    super.onStart();
        try {
            this.friendPickerFragment.loadData(false);
        }
        catch (Exception ex) {
            onError(ex);
        }
	}

	private void finishActivity() {
	    if (this.friendPickerFragment != null && this.friendPickerFragment.getSelection().size() > 0) {
	    	AppContext.getApplication().setSelectedUser(friendPickerFragment.getSelection().get(0));
	    	AppContext.getApplication().setSelectedUsers(friendPickerFragment.getSelection());
	    }   
	    setResult(RESULT_OK, null);
	    this.finish();
	}
}