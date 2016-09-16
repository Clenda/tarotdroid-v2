package org.nla.tarotdroid.ui;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.nla.tarotdroid.AppContext;
import org.nla.tarotdroid.BuildConfig;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.dal.DalException;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.UIHelper;
import org.nla.tarotdroid.ui.constants.ActivityParams;
import org.nla.tarotdroid.ui.constants.PreferenceConstants;
import org.nla.tarotdroid.ui.controls.PlayerSelectorRow;
import org.nla.tarotdroid.ui.tasks.StartNewGameSetTask;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PlayerSelectorActivity extends BaseActivity {
  
	private static final String PLAYER_ROW_COUNT = "player_row_count";
	private static final String PLAYER_NAME = "player_name";
	private static final String OPTIONAL_PLAYER_NAME = "optional_player_name";

    @BindView(R.id.layoutCompulsoryPlayers) protected LinearLayout layoutCompulsoryPlayers;
    @BindView(R.id.optionalPlayerSelectorRow) protected PlayerSelectorRow optionalPlayerSelectorRow;
    @Inject GameSetParameters gameSetParameters;
    private ProgressDialog progressDialog;
    private GameStyleType gameStyleType;
    private int rowCount;
    private List<PlayerSelectorRow> playerSelectorRows;
    private GameSet gameSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	try {
			// create game set stub
            gameSet = new GameSet();
//            gameSet.setGameSetParameters(AppContext.getApplication().initializeGameSetParameters());
            gameSet.setGameSetParameters(gameSetParameters);

            identifyGameSetType();

            // display warning message if more than 5 games are already stored and and app is not limited
            boolean gameSetNotToBeStored = !BuildConfig.IS_FULL && AppContext.getApplication()
                                                                             .getDalService()
                                                                             .getGameSetCount() >= 5;
            if (gameSetNotToBeStored) {
				
				UIHelper.showSimpleRichTextDialog(
                        this,
                        getText(R.string.msgGameSetNotStored).toString(),
                        getString(R.string.titleGameSetNotStored)
                );
			}

            rowCount = 0;
            playerSelectorRows = new ArrayList<>();
            progressDialog = new ProgressDialog(this);
            layoutCompulsoryPlayers = (LinearLayout) findViewById(R.id.layoutCompulsoryPlayers);
            optionalPlayerSelectorRow = (PlayerSelectorRow) findViewById(R.id.optionalPlayerSelectorRow);
            optionalPlayerSelectorRow.setPlayerIndex(10);
            initializeViews();
        }
        catch (Exception e) {
        	AuditHelper.auditError(AuditHelper.ErrorTypes.playerSelectorActivityError, e, this);
		}
    }

    @Override
    protected void inject() {
        TarotDroidApp.get(this).getComponent().inject(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
        outState.putInt(PLAYER_ROW_COUNT, rowCount);
        for (int i = 0; i < rowCount; ++i) {
            PlayerSelectorRow playerSelectorRow = playerSelectorRows.get(i);
			outState.putString(i + PLAYER_NAME, playerSelectorRow.getPlayerName());
    	}

        if (optionalPlayerSelectorRow.getPlayerName() != null && !optionalPlayerSelectorRow.getPlayerName()
                                                                                           .equals("")) {
            outState.putString(OPTIONAL_PLAYER_NAME, optionalPlayerSelectorRow.getPlayerName());
        }
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
        	int formerRowCount = savedInstanceState.getInt(PLAYER_ROW_COUNT);
        	for (int i = 0; i < formerRowCount; ++i) {
                PlayerSelectorRow playerSelectorRow = playerSelectorRows.get(i);
                playerSelectorRow.setPlayerName(savedInstanceState.getString(i + PLAYER_NAME));
    		}
        }
        
    	if (savedInstanceState.containsKey(OPTIONAL_PLAYER_NAME)) {
            optionalPlayerSelectorRow.setPlayerName(savedInstanceState.getString(
                    OPTIONAL_PLAYER_NAME));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem miStartGameSet = menu.add(R.string.lblStartItem);
		miStartGameSet.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		miStartGameSet.setIcon(R.drawable.ic_compose);
		miStartGameSet.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
    			// form isnt valid
    			if (!isFormValid()) {
    				Toast.makeText(
							PlayerSelectorActivity.this,
							AppContext.getApplication().getResources().getString(R.string.msgValidationKo),
							Toast.LENGTH_SHORT
    						).show();
    			}
    			// form is valid
    			else {
    				// set game properties
    				setPlayersAndGameStyleType();

    				// create game set and navigate towards tab view
    				new StartNewGameSetTask(
    						PlayerSelectorActivity.this, 
    						PlayerSelectorActivity.this.progressDialog,
    						PlayerSelectorActivity.this.gameSet
    				).execute();
    			}
				return true;
			}
		});
		
		return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void auditEvent() {
        AuditHelper.auditEvent(AuditHelper.EventTypes.displayGameSetCreationPage);
	}

    @Override
    protected int getLayoutResId() {
        return R.layout.player_selector;
    }

    private void identifyGameSetType() {
        if (getIntent().getExtras() != null && !getIntent().getExtras()
                                                           .containsKey(ActivityParams.PARAM_TYPE_OF_GAMESET)) {
            throw new IllegalArgumentException("type of gameset must be provided");
		}

        gameStyleType = GameStyleType.valueOf(getIntent().getExtras()
                                                         .getString(ActivityParams.PARAM_TYPE_OF_GAMESET));
    }
	
	private void addCompulsoryPlayerRow() {
        PlayerSelectorRow playerSelectorRow = new PlayerSelectorRow(this, rowCount);
        layoutCompulsoryPlayers.addView(playerSelectorRow);
        playerSelectorRows.add(playerSelectorRow);
        rowCount += 1;
    }
	
	private void initializeViews() {
        switch (gameStyleType) {
            case Tarot3:
                addCompulsoryPlayerRow();
                addCompulsoryPlayerRow();
                addCompulsoryPlayerRow();
                break;
			case Tarot4:
                addCompulsoryPlayerRow();
                addCompulsoryPlayerRow();
                addCompulsoryPlayerRow();
                addCompulsoryPlayerRow();
                break;
			case Tarot5:
                addCompulsoryPlayerRow();
                addCompulsoryPlayerRow();
                addCompulsoryPlayerRow();
                addCompulsoryPlayerRow();
                addCompulsoryPlayerRow();
                break;
    	}
	}
	
    protected void setPlayersAndGameStyleType() {
    	
    	// sets the players
        switch (gameStyleType) {
            case Tarot3:
                setPlayers3();
                break;
			case Tarot4:
                setPlayers4();
                break;
			case Tarot5:
                setPlayers5();
                break;
    	}

		// sets the game style type
        gameSet.setGameStyleType(gameStyleType);
    }
    
    private Player getPlayerByName(String playerName) {
    	Player toReturn = null;
    	try {
			toReturn = AppContext.getApplication().getDalService().getPlayerByName(playerName);
		}
    	catch (DalException e) {
    		toReturn = null;
		}
    	if (toReturn == null) {
    		toReturn = new Player(playerName);
    	}
    	
    	return toReturn;
    }
    
    private void setPlayers3() {
    	// get the player names
        String player1Name = playerSelectorRows.get(0).getPlayerName();
        String player2Name = playerSelectorRows.get(1).getPlayerName();
        String player3Name = playerSelectorRows.get(2).getPlayerName();
        String player4Name = optionalPlayerSelectorRow.getPlayerName();

		// create the player list
		PlayerList playerList = new PlayerList();

		// add first 3 players
        Player player1 = getPlayerByName(player1Name);
        Player player2 = getPlayerByName(player2Name);
        Player player3 = getPlayerByName(player3Name);
        playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		
		// add potential 4th player
		if (player4Name != null && player4Name.length() != 0) {
            Player player4 = getPlayerByName(player4Name);
            playerList.add(player4);
		}
		
		// store player names in preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PreferenceConstants.PrefPlayer1Name, player1Name);
		editor.putString(PreferenceConstants.PrefPlayer2Name, player2Name);
		editor.putString(PreferenceConstants.PrefPlayer3Name, player3Name);
		editor.commit();
		
		// set players in game set
        gameSet.setPlayers(playerList);
    }

    private void setPlayers4() {
    	// get the player names
        String player1Name = playerSelectorRows.get(0).getPlayerName();
        String player2Name = playerSelectorRows.get(1).getPlayerName();
        String player3Name = playerSelectorRows.get(2).getPlayerName();
        String player4Name = playerSelectorRows.get(3).getPlayerName();
        String player5Name = optionalPlayerSelectorRow.getPlayerName();

        // create the player list
		PlayerList playerList = new PlayerList();

		// add first 4 players
        Player player1 = getPlayerByName(player1Name);
        Player player2 = getPlayerByName(player2Name);
        Player player3 = getPlayerByName(player3Name);
        Player player4 = getPlayerByName(player4Name);

		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		playerList.add(player4);
		
		// add potential 5th player
		if (player5Name != null && player5Name.length() != 0) {
            Player player5 = getPlayerByName(player5Name);
            playerList.add(player5);
		}
		
		// store player names in preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PreferenceConstants.PrefPlayer1Name, player1Name);
		editor.putString(PreferenceConstants.PrefPlayer2Name, player2Name);
		editor.putString(PreferenceConstants.PrefPlayer3Name, player3Name);
		editor.putString(PreferenceConstants.PrefPlayer4Name, player4Name);
		editor.commit();
		
		// set players in game set
        gameSet.setPlayers(playerList);
    }
    
    private void setPlayers5() {
    	// get the player names
        String player1Name = playerSelectorRows.get(0).getPlayerName();
        String player2Name = playerSelectorRows.get(1).getPlayerName();
        String player3Name = playerSelectorRows.get(2).getPlayerName();
        String player4Name = playerSelectorRows.get(3).getPlayerName();
        String player5Name = playerSelectorRows.get(4).getPlayerName();
        String player6Name = optionalPlayerSelectorRow.getPlayerName();

        // create the player list
		PlayerList playerList = new PlayerList();

		// add first 5 players
        Player player1 = getPlayerByName(player1Name);
        Player player2 = getPlayerByName(player2Name);
        Player player3 = getPlayerByName(player3Name);
        Player player4 = getPlayerByName(player4Name);
        Player player5 = getPlayerByName(player5Name);
        playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		playerList.add(player4);
		playerList.add(player5);
		
		// add potential 6th player
		if (player6Name != null && player6Name.length() != 0) {
            Player player6 = getPlayerByName(player6Name);
            playerList.add(player6);
		}
		
		// store player names in preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PreferenceConstants.PrefPlayer1Name, player1Name);
		editor.putString(PreferenceConstants.PrefPlayer2Name, player2Name);
		editor.putString(PreferenceConstants.PrefPlayer3Name, player3Name);
		editor.putString(PreferenceConstants.PrefPlayer4Name, player4Name);
		editor.putString(PreferenceConstants.PrefPlayer5Name, player4Name);
		editor.commit();
		
		// set players in game set
        gameSet.setPlayers(playerList);
    }
    
    private boolean isFormValid() {
        switch (gameStyleType) {
            case Tarot3:
                return isForm3Valid();
            case Tarot4:
                return isForm4Valid();
            case Tarot5:
                return isForm5Valid();
            default:
    			return true;
    	}
    }
    
    protected boolean isForm3Valid() {
        String player1Name = playerSelectorRows.get(0).getPlayerName();
        String player2Name = playerSelectorRows.get(1).getPlayerName();
        String player3Name = playerSelectorRows.get(2).getPlayerName();

        return
    		!player1Name.trim().equals("")
    		&& !player2Name.trim().equals("")
    		&& !player3Name.trim().equals("")
    		&& !player1Name.equalsIgnoreCase(player2Name)
    		&& !player1Name.equalsIgnoreCase(player3Name)
    		&& !player2Name.equalsIgnoreCase(player3Name);
    }

    protected boolean isForm4Valid() {
        String player1Name = playerSelectorRows.get(0).getPlayerName();
        String player2Name = playerSelectorRows.get(1).getPlayerName();
        String player3Name = playerSelectorRows.get(2).getPlayerName();
        String player4Name = playerSelectorRows.get(3).getPlayerName();

        return
    		!player1Name.trim().equals("")
    		&& !player2Name.trim().equals("")
    		&& !player3Name.trim().equals("")
    		&& !player4Name.trim().equals("")
    		&& !player1Name.equalsIgnoreCase(player2Name)
    		&& !player1Name.equalsIgnoreCase(player3Name)
    		&& !player1Name.equalsIgnoreCase(player4Name)
    		&& !player2Name.equalsIgnoreCase(player3Name)
    		&& !player2Name.equalsIgnoreCase(player4Name)
    		&& !player3Name.equalsIgnoreCase(player4Name);
    }

    protected boolean isForm5Valid() {
        String player1Name = playerSelectorRows.get(0).getPlayerName();
        String player2Name = playerSelectorRows.get(1).getPlayerName();
        String player3Name = playerSelectorRows.get(2).getPlayerName();
        String player4Name = playerSelectorRows.get(3).getPlayerName();
        String player5Name = playerSelectorRows.get(4).getPlayerName();

    	return 
        		!player1Name.trim().equals("")
        		&& !player2Name.trim().equals("")
        		&& !player3Name.trim().equals("")
        		&& !player4Name.trim().equals("")
        		&& !player5Name.trim().equals("")
        		&& !player1Name.equalsIgnoreCase(player2Name)
        		&& !player1Name.equalsIgnoreCase(player3Name)
        		&& !player1Name.equalsIgnoreCase(player4Name)
        		&& !player1Name.equalsIgnoreCase(player5Name)
        		&& !player2Name.equalsIgnoreCase(player3Name)
        		&& !player2Name.equalsIgnoreCase(player4Name)
        		&& !player2Name.equalsIgnoreCase(player5Name)
        		&& !player3Name.equalsIgnoreCase(player4Name)
    			&& !player3Name.equalsIgnoreCase(player5Name)
    			&& !player4Name.equalsIgnoreCase(player5Name);
    }
}