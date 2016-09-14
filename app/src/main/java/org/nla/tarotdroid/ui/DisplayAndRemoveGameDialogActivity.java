package org.nla.tarotdroid.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.nla.tarotdroid.AppContext;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.BelgianBaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.PassedGame;
import org.nla.tarotdroid.biz.PenaltyGame;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.helpers.UIHelper;
import org.nla.tarotdroid.ui.constants.ActivityParams;
import org.nla.tarotdroid.ui.tasks.RemoveGameTask;

import static com.google.common.base.Preconditions.checkArgument;

public class DisplayAndRemoveGameDialogActivity extends AppCompatActivity
{
	private BaseGame game;
	
	private DialogInterface.OnClickListener removeGameDialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(final DialogInterface dialog, final int which) {
	        switch (which) {
    	        case DialogInterface.BUTTON_POSITIVE:
    	        	new RemoveGameTask(
    	        			TabGameSetActivity.getInstance(), 
    	        			DisplayAndRemoveGameDialogActivity.this,
    	        			DisplayAndRemoveGameDialogActivity.this.getGameSet()
    	        	).execute(game);
    	            break;
    	        case DialogInterface.BUTTON_NEGATIVE:
    	        	DisplayAndRemoveGameDialogActivity.this.finish();
    	            break;
                default:
                    break;
	        }
	    }
	};
	
	private GameSet getGameSet() {
		return TabGameSetActivity.getInstance().gameSet;
	}
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			Bundle args = this.getIntent().getExtras();
			checkArgument(args.containsKey(ActivityParams.PARAM_GAME_INDEX), "Game index must be provided");
			this.game = this.getGameSet().getGameOfIndex(args.getInt(ActivityParams.PARAM_GAME_INDEX));
 			
			UIHelper.setKeepScreenOn(this, AppContext.getApplication().getAppParams().isKeepScreenOn());
 			
			final Item[] items = {
					new Item(this.getString(R.string.lblViewGame), android.R.drawable.ic_menu_view, Item.ItemTypes.View),
					new Item(this.getString(R.string.lblEditGame), android.R.drawable.ic_menu_edit, Item.ItemTypes.Edit),
					new Item(this.getString(R.string.lblDeleteGame), R.drawable.gd_action_bar_trashcan, Item.ItemTypes.Delete),
					
			};
			
			ListAdapter adapter = new ArrayAdapter<Item>(this, android.R.layout.select_dialog_item, android.R.id.text1, items){
				        public View getView(int position, View convertView, ViewGroup parent) {
				            //User super class to create the View
				            View v = super.getView(position, convertView, parent);
				            TextView tv = (TextView)v.findViewById(android.R.id.text1);

				            //Put the image on the TextView
				            tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

				            //Add margin between image and text (support various screen densities)
				            int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
				            tv.setCompoundDrawablePadding(dp5);

				            return v;
				        }
				    };
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(AppContext.getApplication().getResources().getString(R.string.lblGameOptionsDialogTitle));
			
			builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			    
				public void onClick(DialogInterface dialog, int itemIndex) {
					
					Item item = items[itemIndex];
					if (item.itemType == Item.ItemTypes.View) {
						DisplayAndRemoveGameDialogActivity.this.viewGame();
					}
					else if (item.itemType == Item.ItemTypes.Edit) {
						DisplayAndRemoveGameDialogActivity.this.modifyGame();
					}
					else if (item.itemType == Item.ItemTypes.Delete) {
						DisplayAndRemoveGameDialogActivity.this.deleteGame();
					}
			    }
			});
			AlertDialog alert = builder.create();
			alert.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					DisplayAndRemoveGameDialogActivity.this.finish();
				}
			});
			alert.show();
		}
		catch (Exception e) {
			AuditHelper.auditError(ErrorTypes.displayAndRemoveGameDialogActivityCreationError, e, this);
		}
	}
	
	private void viewGame() {
		Intent intent = new Intent(TabGameSetActivity.getInstance(), GameReadViewPagerActivity.class);
		intent.putExtra(ActivityParams.PARAM_GAME_INDEX, this.game.getIndex());
		TabGameSetActivity.getInstance().startActivity(intent);
		this.finish();
	}
	
	private void modifyGame() {
		String typeOfGame = null;
		
		if (this.game instanceof StandardBaseGame) {
			typeOfGame = GameCreationActivity.GameType.Standard.toString();
		}
		else if (this.game instanceof BelgianBaseGame) {
			typeOfGame = GameCreationActivity.GameType.Belgian.toString();
		}
		else if (this.game instanceof PenaltyGame) {
			typeOfGame = GameCreationActivity.GameType.Penalty.toString();
		}
		else if (this.game instanceof PassedGame) {
			typeOfGame = GameCreationActivity.GameType.Pass.toString();
		}
		
		Intent intent = new Intent(TabGameSetActivity.getInstance(), GameCreationActivity.class);
		intent.putExtra(ActivityParams.PARAM_GAME_INDEX, this.game.getIndex());
		//intent.putExtra(ActivityParams.PARAM_GAMESET_ID, this.gameSet.getId());
		intent.putExtra(ActivityParams.PARAM_TYPE_OF_GAME, typeOfGame);
		TabGameSetActivity.getInstance().startActivity(intent);
		this.finish();
	}
	
	private void deleteGame() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String dialogTitle = String.format(
			this.getString(R.string.titleRemoveGameYesNo),
			this.game.getIndex()
		);
		String dialogMessage = this.game.getIndex() == this.game.getHighestGameIndex() ? this.getString(R.string.msgRemoveGameYesNo) : this.getString(R.string.msgRemoveGameAndSubsequentGamesYesNo);
		builder.setTitle(dialogTitle);
		builder.setMessage(dialogMessage);
		builder.setPositiveButton(this.getString(R.string.btnOk), this.removeGameDialogClickListener);
		builder.setNegativeButton(this.getString(R.string.btnCancel), this.removeGameDialogClickListener);
		builder.show();
		builder.setIcon(android.R.drawable.ic_dialog_alert);
	}
	
	private static class Item {

		public final String text;
		public final int icon;
		public final ItemTypes itemType;
	    public Item(String text, Integer icon, ItemTypes itemType) {
	        this.text = text;
	        this.icon = icon;
	        this.itemType = itemType;
	    }

	    @Override
	    public String toString() {
	        return text;
	    }

		protected enum ItemTypes {
			View,
			Edit,
			Delete
		}
	}
}