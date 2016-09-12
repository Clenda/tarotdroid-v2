package org.nla.tarotdroid.ui.controls;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.PersistableBusinessObject;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.helpers.UIHelper;
import org.nla.tarotdroid.ui.PlayerStatisticsActivity;
import org.nla.tarotdroid.ui.TabGameSetActivity;
import org.nla.tarotdroid.ui.constants.RequestCodes;
import org.nla.tarotdroid.ui.constants.UIConstants;

public class PlayersRow extends LinearLayout {
	
	private PersistableBusinessObject nextDealer;
		
	public PlayersRow(final Context context, final AttributeSet attrs, final PersistableBusinessObject nextDealer) {
		super(context, attrs);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		params.setMargins(0, 4, 0, 4);
		this.setLayoutParams(params);
		this.setOrientation(HORIZONTAL);
		this.setWeightSum(this.getGameSet().getPlayers().size() + 1);
		this.nextDealer = nextDealer;
		this.initializeViews();
	}

	public PlayersRow(final Context context, final PersistableBusinessObject nextDealer) {
		this(context, null, nextDealer);
	}
	
	private GameSet getGameSet() {
		return TabGameSetActivity.getInstance().getGameSet();
	}

	public PlayerList getPlayers() {
		return this.getGameSet().getPlayers();
	}
	
	protected void initializeViews() {
		// "Players" label
		TextView lblPlayers = new TextView(this.getContext());
		lblPlayers.setText(R.string.lblPlayers);
		lblPlayers.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		lblPlayers.setLayoutParams(UIConstants.PLAYERS_LAYOUT_PARAMS);
		lblPlayers.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		lblPlayers.setHeight(UIConstants.PLAYER_VIEW_HEIGHT);
		lblPlayers.setBackgroundColor(Color.TRANSPARENT);
		lblPlayers.setTextColor(Color.WHITE);
		lblPlayers.setTypeface(null, Typeface.BOLD);
		lblPlayers.setSingleLine();
		lblPlayers.setEllipsize(TruncateAt.END);
		this.addView(lblPlayers);
    	
    	// player names and pics
    	for (Player player : this.getGameSet().getPlayers()) {
    		
    		OnClickListener playerClickListener = new PlayerClickListener(player);

    		// contact picture
			if (player.getPictureUri() != null && player.getPictureUri()
														.toString()
														.contains(
																"content://com.android.contacts/contacts")) {
				Bitmap playerImage = null;
				try {
					playerImage = UIHelper.getContactPicture(this.getContext(), Uri.parse(player.getPictureUri()).getLastPathSegment());
				}
				catch (Exception e) {
					playerImage = null;
				}
				
				if (playerImage != null) {
		    		ImageView imgPlayer = new ImageView(this.getContext());
		    		imgPlayer.setImageBitmap(playerImage);
		    		imgPlayer.setLayoutParams(UIConstants.PLAYERS_LAYOUT_PARAMS);
		    		if (this.nextDealer == player) {
		    			imgPlayer.setBackgroundResource(R.drawable.border_next_player);
		    		}
		    		
		    		imgPlayer.setOnClickListener(playerClickListener);
		    		this.addView(imgPlayer);
				}
				else {
		    		
					// TODO Improve duplicate code
					TextView txtPlayer = new TextView(this.getContext());
		    		txtPlayer.setText(player.getName());
		    		txtPlayer.setGravity(Gravity.CENTER);
		    		txtPlayer.setLayoutParams(UIConstants.PLAYERS_LAYOUT_PARAMS);
		    		txtPlayer.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		    		txtPlayer.setHeight(UIConstants.PLAYER_VIEW_HEIGHT);
		    		txtPlayer.setBackgroundColor(Color.TRANSPARENT);
		    		txtPlayer.setTypeface(null, Typeface.BOLD);
		    		txtPlayer.setTextColor(Color.WHITE);
		    		txtPlayer.setSingleLine();
		    		txtPlayer.setEllipsize(TruncateAt.END);
		    		if (this.nextDealer == player) {
		    			txtPlayer.setBackgroundResource(R.drawable.border_next_player);
		    		}
		    		
		    		txtPlayer.setOnClickListener(playerClickListener);
		    		this.addView(txtPlayer);
				}
    		}
    		
    		// no picture, only name
    		else {
	    		TextView txtPlayer = new TextView(this.getContext());
	    		txtPlayer.setText(player.getName());
	    		txtPlayer.setGravity(Gravity.CENTER);
	    		txtPlayer.setLayoutParams(UIConstants.PLAYERS_LAYOUT_PARAMS);
	    		txtPlayer.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
	    		txtPlayer.setHeight(UIConstants.PLAYER_VIEW_HEIGHT);
	    		txtPlayer.setBackgroundColor(Color.TRANSPARENT);
	    		txtPlayer.setTypeface(null, Typeface.BOLD);
	    		txtPlayer.setTextColor(Color.WHITE);
	    		txtPlayer.setSingleLine();
	    		txtPlayer.setEllipsize(TruncateAt.END);
	    		if (this.nextDealer == player) {
	    			txtPlayer.setBackgroundResource(R.drawable.border_next_player);
	    		}
	    		
	    		txtPlayer.setOnClickListener(playerClickListener);
	    		this.addView(txtPlayer);
    		}
    	}
	}
	
	private class PlayerClickListener implements OnClickListener {

		private Player player;
		protected PlayerClickListener(Player player) {
			this.player = player;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(TabGameSetActivity.getInstance(), PlayerStatisticsActivity.class);
			intent.putExtra("player", this.player.getName());
			TabGameSetActivity.getInstance().startActivityForResult(intent, RequestCodes.DISPLAY_PLAYER);
		}
	}
}
