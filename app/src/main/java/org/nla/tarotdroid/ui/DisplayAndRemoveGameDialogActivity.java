package org.nla.tarotdroid.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.BelgianBaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.PassedGame;
import org.nla.tarotdroid.biz.PenaltyGame;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.ui.constants.ActivityParams;
import org.nla.tarotdroid.ui.tasks.RemoveGameTask;

public class DisplayAndRemoveGameDialogActivity extends BaseActivity
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
                            getGameSet()
                    ).execute(game);
    	            break;
    	        case DialogInterface.BUTTON_NEGATIVE:
                    finish();
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
            Bundle args = getIntent().getExtras();
            if (!args.containsKey(ActivityParams.PARAM_GAME_INDEX)) {
                throw new IllegalArgumentException("Game index must be provided");
            }
            game = getGameSet().getGameOfIndex(args.getInt(ActivityParams.PARAM_GAME_INDEX));

            final Item[] items = {
                    new Item(getString(R.string.lblViewGame),
                             android.R.drawable.ic_menu_view,
                             Item.ItemTypes.View),
                    new Item(getString(R.string.lblEditGame),
                             android.R.drawable.ic_menu_edit,
                             Item.ItemTypes.Edit),
                    new Item(getString(R.string.lblDeleteGame),
                             R.drawable.gd_action_bar_trashcan,
                             Item.ItemTypes.Delete),

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
			builder.setTitle(getResources().getString(R.string.lblGameOptionsDialogTitle));
			
			builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			    
				public void onClick(DialogInterface dialog, int itemIndex) {
					
					Item item = items[itemIndex];
					if (item.itemType == Item.ItemTypes.View) {
                        viewGame();
                    }
					else if (item.itemType == Item.ItemTypes.Edit) {
                        modifyGame();
                    }
					else if (item.itemType == Item.ItemTypes.Delete) {
                        deleteGame();
                    }
			    }
			});
			AlertDialog alert = builder.create();
			alert.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
                    finish();
                }
			});
			alert.show();
		}
		catch (Exception e) {
			auditHelper.auditError(ErrorTypes.displayAndRemoveGameDialogActivityCreationError,
								   e,
								   this);
		}
	}

    @Override
    protected void inject() {
        TarotDroidApp.get(this).getComponent().inject(this);
    }

    // TODO Implement
    @Override
    protected void auditEvent() {

    }

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected boolean shouldSetContentView() {
        return false;
    }

    private void viewGame() {
        Intent intent = new Intent(TabGameSetActivity.getInstance(), GameReadViewPagerActivity.class);
        intent.putExtra(ActivityParams.PARAM_GAME_INDEX, game.getIndex());
        TabGameSetActivity.getInstance().startActivity(intent);
        finish();
    }
	
	private void modifyGame() {
		Class<? extends BaseGameActivity> typeOfGame = null;

        if (game instanceof StandardBaseGame) {
            typeOfGame = StandardGameActivity.class;
		} else if (game instanceof BelgianBaseGame) {
            typeOfGame = BelgianGameActivity.class;
		} else if (game instanceof PenaltyGame) {
            typeOfGame = PenaltyActivity.class;
		} else if (game instanceof PassedGame) {
            typeOfGame = PassActivity.class;
		}

		Intent intent = new Intent(TabGameSetActivity.getInstance(), typeOfGame);
        intent.putExtra(ActivityParams.PARAM_GAME_INDEX, game.getIndex());
        TabGameSetActivity.getInstance().startActivity(intent);
        finish();
    }
	
	private void deleteGame() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String dialogTitle = String.format(
                getString(R.string.titleRemoveGameYesNo),
                game.getIndex()
        );
        String dialogMessage = game.getIndex() == game.getHighestGameIndex()
                ? getString(R.string.msgRemoveGameYesNo)
                : getString(R.string.msgRemoveGameAndSubsequentGamesYesNo);
        builder.setTitle(dialogTitle);
		builder.setMessage(dialogMessage);
        builder.setPositiveButton(getString(R.string.btnOk), removeGameDialogClickListener);
        builder.setNegativeButton(getString(R.string.btnCancel), removeGameDialogClickListener);
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