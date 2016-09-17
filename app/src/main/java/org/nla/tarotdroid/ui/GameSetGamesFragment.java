package org.nla.tarotdroid.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.nla.tarotdroid.AppParams;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.PersistableBusinessObject;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.ui.controls.PlayersRow;
import org.nla.tarotdroid.ui.controls.RowFactory;
import org.nla.tarotdroid.ui.controls.ScoresRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameSetGamesFragment extends Fragment {

    @BindView(R.id.playersLayout) protected LinearLayout playersLayout;
    @BindView(R.id.gamesLayout) protected LinearLayout gamesLayout;
    @Inject protected AppParams appParams;
    @Inject protected AuditHelper auditHelper;
    private View viewTabGameSet;
    private ScoresRow playerScoresRow;
    private PlayersRow playersRow;

    public static GameSetGamesFragment newInstance() {
        GameSetGamesFragment fragment = new GameSetGamesFragment();
		return fragment;
	}
	
	private GameSet getGameSet() {
		return TabGameSetActivity.getInstance().gameSet;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            TarotDroidApp.get(getContext()).getComponent().inject(this);
            viewTabGameSet = inflater.inflate(R.layout.tablegameset, null);
//            playersLayout = (LinearLayout)viewTabGameSet.findViewById(R.id.playersLayout);
//            gamesLayout = (LinearLayout) viewTabGameSet.findViewById(R.id.gamesLayout);
            playerScoresRow = new ScoresRow(getActivity(), getGameSet());
        }
        catch (Exception e) {
            auditHelper.auditError(ErrorTypes.tabGameSetActivityError, e, getActivity());
        }

        return viewTabGameSet;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
		super.onResume();
        refreshPlayersRows();
        refreshGameRows();
    }
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
    	// HACK found on http://code.google.com/p/android/issues/detail?id=19917 to prevent error "Unable to pause activity" (described on web site) 
    	outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
    	super.onSaveInstanceState(outState);
    }

    protected void refreshGameRows() {
        gamesLayout.removeAllViews();

        // display things only if there's at least one game
        if (getGameSet().getGameCount() > 0) {

    		// build an ordered list of views to display 
            List<View> rowsToDisplay = new ArrayList<View>(getGameSet().getGameCount() + 1);
            for (BaseGame game : getGameSet()) {
                rowsToDisplay.add(RowFactory.buildGameRow(TabGameSetActivity.getInstance(),
                                                          TabGameSetActivity.getInstance().progressDialog,
                                                          game,
                                                          1,
                                                          getGameSet()));
            }

    		// display rows in reverse order ?
            if (appParams.isDisplayGamesInReverseOrder()) {
                Collections.reverse(rowsToDisplay);
    		}
    		
    		// display each view in the layout
    		for (View rowToDisplay : rowsToDisplay) {
                gamesLayout.addView(rowToDisplay);
            }
    	}
    	
    	// always update title and score, in case of deletion for instance
        updatePlayerScores();
        setTitle();
    }

    protected void refreshPlayersRows() {
        playerScoresRow = new ScoresRow(getActivity(), getGameSet());
        playersRow = buildPlayerRow();
        playersLayout.removeAllViews();
        playersLayout.addView(playersRow);
        playersLayout.addView(playerScoresRow);
    }

    protected PlayersRow buildPlayerRow() {
        int gameCount = getGameSet().getGameCount();

        // display next dealer only if params say so and there's more than one game
    	PersistableBusinessObject nextDealer = null;
        if (appParams.isDisplayNextDealer() && gameCount > 0) {
            PersistableBusinessObject formerDealer = getGameSet().getLastGame().getDealer();
            if (formerDealer != null) {
                nextDealer = getGameSet().getPlayers().getNextPlayer(formerDealer);
            }
		}

        return new PlayersRow(getActivity(), nextDealer);
    }

    protected void updatePlayerScores() {
    	// updates score only if a game has already been played 
        if (getGameSet().getGameSetScores().getResultsAtLastGame() != null) {
            playerScoresRow.updatePlayerScore(getGameSet().getGameSetScores()
                                                          .getResultsAtLastGame());
        }
    	// if no game is present in the set, reset all scores to zero
    	else {
            playerScoresRow.resetAllPlayerScores();
        }
    }
    
    private void setTitle() {
    	String title;
    	// no game in GameSet
        if (getGameSet().getGameCount() == 0) {
            title = getString(R.string.lblTabGameSetActivityNoGameTitle);
        }
    	// 1 game in GameSet
        else if (getGameSet().getGameCount() == 1) {
            title = getString(R.string.lblTabGameSetActivityOneGameTitle);
        }
    	// several games in GameSet
    	else {
            title = String.format(getString(R.string.lblTabGameSetActivitySeveralGamesTitle),
                                  getGameSet().getGameCount());
        }
    	getActivity().setTitle(title);
    }
}
