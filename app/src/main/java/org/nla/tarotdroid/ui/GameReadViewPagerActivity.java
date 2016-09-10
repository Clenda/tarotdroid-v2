package org.nla.tarotdroid.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.app.AppContext;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.UIHelper;
import org.nla.tarotdroid.ui.constants.ActivityParams;
import org.nla.tarotdroid.ui.tasks.RemoveGameTask;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;


public class GameReadViewPagerActivity extends AppCompatActivity {

	/**
	 * Game view pager.
	 */
	private ViewPager mPager;

	/**
	 * Fragment pager adapter.
	 */
	private PagerAdapter mPagerAdapter;
	
	/**
	 * Index of current game displayed.
	 */
	private int currentGameIndex;
	
	/**
	 * Pager indicator.
	 */
	private PageIndicator mIndicator;
	
//	/**
//	 * Current game set.
//	 */
//	private GameSet gameSet;

	/**
	 * "Yes / No" game removal dialog box listener.
	 */
	private DialogInterface.OnClickListener removeGameDialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(final DialogInterface dialog, final int which) {
	        switch (which) {
    	        case DialogInterface.BUTTON_POSITIVE:
    	        	new RemoveGameTask(GameReadViewPagerActivity.this, GameReadViewPagerActivity.this, GameReadViewPagerActivity.this.getGameSet()).execute();
    	            break;
    	        case DialogInterface.BUTTON_NEGATIVE:
    	            break;
                default:
                    break;
	        }
	    }
	};
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			super.setContentView(R.layout.simple_titles);
			
//			// check params
//			Bundle args = this.getIntent().getExtras();
//			if (args.containsKey(ActivityParams.PARAM_GAMESET_ID)) {
//				this.gameSet = AppContext.getApplication().getDalService().getGameSetById(args.getLong(ActivityParams.PARAM_GAMESET_ID));
//			}
//			else if (args.containsKey(ActivityParams.PARAM_GAMESET_SERIALIZED)) {
//				//this.gameSet = UIHelper.deserializeGameSet(args.getString(ActivityParams.PARAM_GAMESET_SERIALIZED));
//				this.gameSet = (GameSet)args.getSerializable(ActivityParams.PARAM_GAMESET_SERIALIZED);
//			}
//			else {
//				throw new IllegalArgumentException("Game set id or serialized game set must be provided");
//			}
//			checkArgument(this.getIntent().getExtras().containsKey(ActivityParams.PARAM_GAME_INDEX), "Game index must be provided");
			this.currentGameIndex = this.getIntent().getExtras().getInt(ActivityParams.PARAM_GAME_INDEX);
			
			// set keep screen on 
			UIHelper.setKeepScreenOn(this, AppContext.getApplication().getAppParams().isKeepScreenOn());

			// initialize the pager
			this.initialisePaging();
			
			ActionBar mActionBar = getSupportActionBar();
			mActionBar.setHomeButtonEnabled(true);
			mActionBar.setDisplayShowHomeEnabled(true);
			this.setTitle(R.string.lblViewGameActivityTitle);
		}
		catch (Exception e) {
			AuditHelper.auditError(AuditHelper.ErrorTypes.gameReadViewPagerActivityError, e, this);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.support.v7.app.AppCompatActivity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (this.currentGameIndex == this.getGameSet().getGameCount()) {
	        MenuItem miTrash = menu.add(this.getString(R.string.lblDeleteGameItem));
	        miTrash.setIcon(R.drawable.gd_action_bar_trashcan);
	        miTrash.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			miTrash.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					AlertDialog.Builder builder = new AlertDialog.Builder(GameReadViewPagerActivity.this);
					String dialogTitle = String.format(
	    					GameReadViewPagerActivity.this.getString(R.string.titleRemoveGameYesNo),
		    				GameReadViewPagerActivity.this.currentGameIndex
		    			);
	    			String dialogMessage = GameReadViewPagerActivity.this.getString(R.string.msgRemoveGameYesNo);
	    			builder.setTitle(dialogTitle);
	    			builder.setMessage(dialogMessage);
	    			builder.setPositiveButton(GameReadViewPagerActivity.this.getString(R.string.btnOk), GameReadViewPagerActivity.this.removeGameDialogClickListener);
	    			builder.setNegativeButton(GameReadViewPagerActivity.this.getString(R.string.btnCancel), GameReadViewPagerActivity.this.removeGameDialogClickListener);
	    			builder.show();
	    			builder.setIcon(android.R.drawable.ic_dialog_alert);
	                return true;
	            }
	        });			
		}
		return true;
	}
	
	private GameSet getGameSet() {
		return TabGameSetActivity.getInstance().gameSet;
	}

	private void initialisePaging() {
		List<Fragment> fragments = newArrayList();
		for(BaseGame game : this.getGameSet().getGames()) {
			fragments.add(GameReadFragment.newInstance(game.getIndex(), this.getGameSet()));
		}
		this.mPagerAdapter = new GameReadPagerAdapter(super.getSupportFragmentManager(), fragments, this.getGameSet());

		this.mPager = (ViewPager) super.findViewById(R.id.pager);
		this.mPager.setAdapter(this.mPagerAdapter);
		this.mPager.setCurrentItem(this.currentGameIndex-1);
		
		TitlePageIndicator indicator = (TitlePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
        mIndicator = indicator;
        
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            
        	@Override
            public void onPageSelected(int position) {
            	GameReadViewPagerActivity.this.currentGameIndex = position + 1;
            	GameReadViewPagerActivity.this.invalidateOptionsMenu();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
	}
}