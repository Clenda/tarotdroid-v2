package org.nla.tarotdroid.gameset.controls;

import android.app.Activity;
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
import android.widget.TextView;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.PersistableBusinessObject;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.constants.RequestCodes;
import org.nla.tarotdroid.constants.UIConstants;
import org.nla.tarotdroid.core.helpers.UIHelper;
import org.nla.tarotdroid.players.PlayerStatisticsActivity;

public class PlayersRow extends BaseRow {

    private PersistableBusinessObject nextDealer;

    public PlayersRow(
            final Context context,
            final AttributeSet attrs,
            final PersistableBusinessObject nextDealer
    ) {
        super(context, attrs);
        this.nextDealer = nextDealer;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                                               LayoutParams.MATCH_PARENT);
        params.setMargins(0, 4, 0, 4);
        setLayoutParams(params);
        setWeightSum(getGameSet().getPlayers().size() + 1);
        initializeViews();
    }

    public PlayersRow(final Context context, final PersistableBusinessObject nextDealer) {
        this(context, null, nextDealer);
    }

    public PlayerList getPlayers() {
        return getGameSet().getPlayers();
    }

    protected void initializeViews() {
        // "Players" label
        TextView lblPlayers = new TextView(getContext());
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
        addView(lblPlayers);

        // player names and pics
        for (Player player : getGameSet().getPlayers()) {

            OnClickListener playerClickListener = new PlayerClickListener(player);

            // contact picture
            if (player.getPictureUri() != null && player.getPictureUri()
                                                        .toString()
                                                        .contains(
                                                                "content://com.android.contacts/contacts")) {
                Bitmap playerImage = null;
                try {
                    playerImage = UIHelper.getContactPicture(getContext(),
                                                             Uri.parse(player.getPictureUri())
                                                                .getLastPathSegment());
                } catch (Exception e) {
                    playerImage = null;
                }

                if (playerImage != null) {
                    ImageView imgPlayer = new ImageView(getContext());
                    imgPlayer.setImageBitmap(playerImage);
                    imgPlayer.setLayoutParams(UIConstants.PLAYERS_LAYOUT_PARAMS);
                    if (nextDealer == player) {
                        imgPlayer.setBackgroundResource(R.drawable.border_next_player);
                    }

                    imgPlayer.setOnClickListener(playerClickListener);
                    addView(imgPlayer);
                } else {

                    // TODO Improve duplicate code
                    TextView txtPlayer = new TextView(getContext());
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
                    if (nextDealer == player) {
                        txtPlayer.setBackgroundResource(R.drawable.border_next_player);
                    }

                    txtPlayer.setOnClickListener(playerClickListener);
                    addView(txtPlayer);
                }
            }

            // no picture, only name
            else {
                TextView txtPlayer = new TextView(getContext());
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
                if (nextDealer == player) {
                    txtPlayer.setBackgroundResource(R.drawable.border_next_player);
                }

                txtPlayer.setOnClickListener(playerClickListener);
                addView(txtPlayer);
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
            // TODO create key for player string
            Intent intent = new Intent(getContext(), PlayerStatisticsActivity.class);
            intent.putExtra("player", player.getName());
            ((Activity) getContext()).startActivityForResult(intent, RequestCodes.DISPLAY_PLAYER);
        }
    }
}
