package org.nla.tarotdroid.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import android.support.v4.app.Fragment;

import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.BelgianBaseGame;
import org.nla.tarotdroid.biz.BelgianTarot3Game;
import org.nla.tarotdroid.biz.BelgianTarot4Game;
import org.nla.tarotdroid.biz.BelgianTarot5Game;
import org.nla.tarotdroid.biz.Bet;
import org.nla.tarotdroid.biz.Chelem;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.King;
import org.nla.tarotdroid.biz.PassedGame;
import org.nla.tarotdroid.biz.PenaltyGame;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.biz.StandardTarot5Game;
import org.nla.tarotdroid.biz.Team;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.ui.constants.ActivityParams;
import org.nla.tarotdroid.ui.controls.Selector;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;

public class GameReadFragment extends Fragment
{
	protected TextView txtGlobalPenaltyPoints;
	protected TextView txtAttackPoints;
	protected TextView txtDefensePoints;
	private RelativeLayout panelDead;
	private Selector<Player> selectorDead;
	private RelativeLayout panelDealer;
	private Selector<Player> selectorDealer;
	private Selector<Bet> selectorBet;
	private Selector<Player> selectorLeader;
	private RelativeLayout panelCalled;
	private Selector<Player> selectorCalled;
	private RelativeLayout panelKing;
	private Selector<King> selectorKing;
	private Selector<Integer> selectorOudlers;
	private SeekBar barAttackPoints;
	private SeekBar barDefensePoints;
	private Selector<Team> selectorHandful;
	private Selector<Team> selectorDoubleHandful;
	private Selector<Team> selectorTripleHandful;
	private Selector<Player> selectorMisery;
	private Selector<Team> selectorKidAtTheEnd;
	private Selector<Chelem> selectorSlam;
	private LinearLayout panelDeadAndDealerSection;
	private LinearLayout panelAnnoucementSection;
	private LinearLayout panelAnnouncements;
	private RelativeLayout panelHandful;
	private RelativeLayout panelDoubleHandful;
	private RelativeLayout panelTripleHandful;
	private RelativeLayout panelMisery;
	private RelativeLayout panelKidAtTheEnd;
	private RelativeLayout panelSlam;
	private Selector<Player> selectorFirst;
	private Selector<Player> selectorSecond;
	private Selector<Player> selectorThird;
	private RelativeLayout panelFourth;
	private Selector<Player> selectorFourth;
	private RelativeLayout panelFifth;
	private Selector<Player> selectorFifth;
	private BaseGame game;
	private FrameLayout frameLayout;

	public static GameReadFragment newInstance(int gameIndex, GameSet gameSet) {
		checkArgument(gameSet != null, "gameSet is null");
		GameReadFragment fragment = new GameReadFragment();

		Bundle args = new Bundle();
		args.putInt(ActivityParams.PARAM_GAME_INDEX, gameIndex);
		fragment.setArguments(args);

		return fragment;
	}

	/**
	 * Returns the game set on which activity has to work.
	 * @return
	 */
	private GameSet getGameSet() {
		return TabGameSetActivity.getInstance().gameSet;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// check params
		Bundle args = this.getArguments();
//		if (args.containsKey(ActivityParams.PARAM_GAMESET_ID)) {
//			this.gameSet = AppContext.getApplication().getDalService().getGameSetById(args.getLong(ActivityParams.PARAM_GAMESET_ID));
//		}
//		else if (args.containsKey(ActivityParams.PARAM_GAMESET_SERIALIZED)) {
//			//this.gameSet = UIHelper.deserializeGameSet(args.getString(ActivityParams.PARAM_GAMESET_SERIALIZED));
//			this.gameSet = (GameSet)args.getSerializable(ActivityParams.PARAM_GAMESET_SERIALIZED);
//		}
//		else {
//			throw new IllegalArgumentException("Game set id or serialized game set must be provided");
//		}
		
		checkArgument(args.containsKey(ActivityParams.PARAM_GAME_INDEX), "Game index must be provided");
		this.game = this.getGameSet().getGameOfIndex(args.getInt(ActivityParams.PARAM_GAME_INDEX));		
		
		// set layout
		if (this.game instanceof StandardBaseGame) {
			this.frameLayout = (FrameLayout)inflater.inflate(R.layout.standard_game_read, container, false);
		}
		else if (this.game instanceof BelgianBaseGame) {
			this.frameLayout = (FrameLayout)inflater.inflate(R.layout.belgian_game_read, container, false);
		}
		else if (this.game instanceof PenaltyGame) {
			this.frameLayout = (FrameLayout)inflater.inflate(R.layout.penalty_game_read, container, false);
		}
		else if (this.game instanceof PassedGame) {
			this.frameLayout = (FrameLayout)inflater.inflate(R.layout.passed_game_read, container, false);
		}

		
		// display game
		this.displayDeadAndDealer();
		if (this.game instanceof StandardBaseGame) {
			this.intializeStandardViews();
			this.displayStandardGame();
		}
		else if (this.game instanceof BelgianBaseGame) {
			this.intializeBelgianViews();
			this.displayBelgianGame();
		}
		else if (this.game instanceof PenaltyGame) {
			this.intializePenaltyViews();
			this.displayPenaltyGame();			
		}
		else if (this.game instanceof PassedGame) {
			// I guess nothing to do....			
		}
		
		this.smoothlyHideText();
		return this.frameLayout;
	}
	
	/**
	 * Displays game index in game set and then smoothly removes the text.
	 */
	private void smoothlyHideText() {
	    final TextView onTop = (TextView) this.frameLayout.findViewById(R.id.txtOnTop);
	    onTop.setText(this.game.getIndex() + "/" + this.game.getHighestGameIndex());
	    
		Animation animation1 = new AlphaAnimation(1.0f, 0.0f);
	    animation1.setDuration(1500);
	    
	  //animation1 AnimationListener
	    animation1.setAnimationListener(new AnimationListener(){

	        @Override
	        public void onAnimationEnd(Animation arg0) {
	            // start animation2 when animation1 ends (continue)
	        	onTop.setVisibility(View.GONE);
	        }

	        @Override
	        public void onAnimationRepeat(Animation arg0) {
	        }

	        @Override
	        public void onAnimationStart(Animation arg0) {
	        }
	    });

	    onTop.startAnimation(animation1);
	}
		
	/**
	 * Displays the dead and dealer panels.
	 */
	@SuppressWarnings("unchecked")
	private void displayDeadAndDealer() {
		
		// initialize dealer player widgets
		this.panelDealer = (RelativeLayout) this.frameLayout.findViewById(R.id.panelDealer);
		if (this.game.getDealer() != null) {
			this.panelDealer.setVisibility(View.VISIBLE);
			this.selectorDealer = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryDealer);
			this.selectorDealer.setObjects(this.getGameSet().getPlayers().getPlayers());
			this.selectorDealer.setSelected(this.game.getDealer());
			this.selectorDealer.setReadOnly(true);
		}
		else {
			this.panelDealer.setVisibility(View.GONE);
		}
		
		// initialize dead player widgets
		this.panelDead = (RelativeLayout) this.frameLayout.findViewById(R.id.panelDead);
		if (this.game.getDeadPlayer() != null) {
			this.panelDead.setVisibility(View.VISIBLE);
			this.selectorDead = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryDead);
			this.selectorDead.setObjects(this.getGameSet().getPlayers().getPlayers());
			this.selectorDead.setSelected(this.game.getDeadPlayer());
			this.selectorDead.setReadOnly(true);
		}
		else {
			this.panelDead.setVisibility(View.GONE);
		}
		
		// if neither dealer nor dead players are set, don't display the section
		if (this.game.getDealer() == null && this.game.getDeadPlayer() == null) {
			this.panelDeadAndDealerSection = (LinearLayout) this.frameLayout.findViewById(R.id.panelDeadAndDealerSection);
			this.panelDeadAndDealerSection.setVisibility(View.GONE);
		}
	}
    	
	/**
     * Displays the standard game.
     */
    private void displayStandardGame() {
    	StandardBaseGame stdGame = (StandardBaseGame)this.game;
		
    	// common properties
		this.selectorBet.setSelected(stdGame.getBet());
		this.selectorBet.setReadOnly(true);
		this.selectorLeader.setSelected(stdGame.getLeadingPlayer());
		this.selectorLeader.setReadOnly(true);
		this.selectorOudlers.setSelected(stdGame.getNumberOfOudlers());
		this.selectorOudlers.setReadOnly(true);
		this.barAttackPoints.setProgress((int)stdGame.getPoints());
		this.txtAttackPoints.setText(new Integer((int)stdGame.getPoints()).toString());
		this.barDefensePoints.setProgress(91 - (int)stdGame.getPoints());
		this.txtDefensePoints.setText(new Integer(91 - (int)stdGame.getPoints()).toString());
		
		// announcements
		if (this.isDisplayAnnouncementPanel()) {
			this.displayAnnouncementSubPanels();
			this.panelAnnouncements.setVisibility(View.VISIBLE);
			this.selectorHandful.setSelected(stdGame.getTeamWithPoignee());
			this.selectorHandful.setReadOnly(true);
			this.selectorDoubleHandful.setSelected(stdGame.getTeamWithDoublePoignee());
			this.selectorDoubleHandful.setReadOnly(true);
			this.selectorTripleHandful.setSelected(stdGame.getTeamWithTriplePoignee());
			this.selectorTripleHandful.setReadOnly(true);
			this.selectorKidAtTheEnd.setSelected(stdGame.getTeamWithKidAtTheEnd());
			this.selectorKidAtTheEnd.setReadOnly(true);
			this.selectorSlam.setSelected(stdGame.getChelem());
			this.selectorSlam.setReadOnly(true);
			this.selectorMisery.setSelected(stdGame.getPlayerWithMisery());
			this.selectorMisery.setReadOnly(true);
		}
		else {
			this.panelAnnoucementSection.setVisibility(View.GONE);
			this.panelAnnouncements.setVisibility(View.GONE);
		}

		// 5 player specifics
		if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
			StandardTarot5Game std5Game = (StandardTarot5Game)this.game;
			this.panelCalled.setVisibility(View.VISIBLE);
			this.panelKing.setVisibility(View.VISIBLE);
			
			this.selectorCalled.setSelected(std5Game.getCalledPlayer());
			this.selectorCalled.setReadOnly(true);
			this.selectorKing.setSelected(std5Game.getCalledKing());
			this.selectorKing.setReadOnly(true);
			
		}
    }
    
    /**
     * Initializes the widgets specific to belgian games.
     */
	@SuppressWarnings("unchecked")
	private void intializeStandardViews() {

		// Main Parameters widget recuperation	
		this.selectorBet = (Selector<Bet>) this.frameLayout.findViewById(R.id.galleryBet);
		this.selectorLeader = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryLeader);
		this.panelCalled = (RelativeLayout) this.frameLayout.findViewById(R.id.panelCalled);
		this.selectorCalled = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryCalled);
		this.panelKing = (RelativeLayout) this.frameLayout.findViewById(R.id.panelKing);
		this.selectorKing = (Selector<King>) this.frameLayout.findViewById(R.id.galleryKing);
		this.selectorOudlers = (Selector<Integer>) this.frameLayout.findViewById(R.id.galleryOudlers);
		this.barAttackPoints = (SeekBar) this.frameLayout.findViewById(R.id.barAttackPoints);
		this.txtAttackPoints = (TextView)this.frameLayout.findViewById(R.id.txtAttackPoints);
		this.barDefensePoints = (SeekBar) this.frameLayout.findViewById(R.id.barDefensePoints);
		this.txtDefensePoints = (TextView) this.frameLayout.findViewById(R.id.txtDefensePoints);
		
		// Annoucements widgets recuperation
		this.panelAnnouncements = (LinearLayout) this.frameLayout.findViewById(R.id.panelAnnouncements);
		this.panelAnnoucementSection = (LinearLayout) this.frameLayout.findViewById(R.id.panelAnnoucementSection);
		this.panelHandful = (RelativeLayout) this.frameLayout.findViewById(R.id.panelHandful);
		this.selectorHandful = (Selector<Team>) this.frameLayout.findViewById(R.id.galleryHandful);
		this.panelDoubleHandful = (RelativeLayout) this.frameLayout.findViewById(R.id.panelDoubleHandful);
		this.selectorDoubleHandful = (Selector<Team>) this.frameLayout.findViewById(R.id.galleryDoubleHandful);
		this.panelTripleHandful = (RelativeLayout) this.frameLayout.findViewById(R.id.panelTripleHandful);
		this.selectorTripleHandful = (Selector<Team>) this.frameLayout.findViewById(R.id.galleryTribleHandful);
		this.panelMisery = (RelativeLayout) this.frameLayout.findViewById(R.id.panelMisery);
		this.selectorMisery = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryMisery);
		this.panelKidAtTheEnd = (RelativeLayout) this.frameLayout.findViewById(R.id.panelKidAtTheEnd);
		this.selectorKidAtTheEnd = (Selector<Team>) this.frameLayout.findViewById(R.id.galleryKidAtTheEnd);
		this.panelSlam = (RelativeLayout) this.frameLayout.findViewById(R.id.panelSlam);
		this.selectorSlam = (Selector<Chelem>) this.frameLayout.findViewById(R.id.gallerySlam);
	
		// widget content loading
		this.selectorBet.setObjects(newArrayList(Bet.PETITE, Bet.PRISE, Bet.GARDE, Bet.GARDESANS, Bet.GARDECONTRE));
		this.selectorLeader.setObjects(this.game.getPlayers().getPlayers());
		this.selectorOudlers.setObjects(newArrayList(0, 1, 2, 3));
		this.barAttackPoints.setProgress(0);
		this.barAttackPoints.setEnabled(false);
		this.barDefensePoints.setProgress(91);
		this.barDefensePoints.setEnabled(false);
		this.selectorHandful.setObjects(newArrayList(Team.LEADING_TEAM, Team.DEFENSE_TEAM, Team.BOTH_TEAMS));
		this.selectorDoubleHandful.setObjects(newArrayList(Team.LEADING_TEAM, Team.DEFENSE_TEAM));
		this.selectorTripleHandful.setObjects(newArrayList(Team.LEADING_TEAM, Team.DEFENSE_TEAM));
		this.selectorKidAtTheEnd.setObjects(newArrayList(Team.LEADING_TEAM, Team.DEFENSE_TEAM));
		this.selectorSlam.setObjects(newArrayList(Chelem.CHELEM_ANOUNCED_AND_SUCCEEDED, Chelem.CHELEM_ANOUNCED_AND_FAILED, Chelem.CHELEM_NOT_ANOUNCED_BUT_SUCCEEDED));
		this.panelMisery.setVisibility(View.VISIBLE);
		this.selectorMisery.setObjects(this.game.getPlayers().getPlayers());
		
		// tarot 5 specific widget content loading
		if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
			this.panelCalled.setVisibility(View.VISIBLE);
			this.selectorCalled.setObjects(this.game.getPlayers().getPlayers());
			this.panelKing.setVisibility(View.VISIBLE);
			this.selectorKing.setObjects(newArrayList(King.CLUB, King.DIAMOND, King.HEART, King.SPADE));
		}
		else {
			this.panelCalled.setVisibility(View.GONE);
			this.panelKing.setVisibility(View.GONE);
		}
	}

    /**
     * Indicates whether the announcement panel is to be displayed.
     * @return
     */
    private boolean isDisplayAnnouncementPanel() {	
    	StandardBaseGame stdGame = (StandardBaseGame)this.game;
		boolean toReturn = 
				stdGame.getTeamWithPoignee() != null || 
				stdGame.getTeamWithDoublePoignee() != null || 
				stdGame.getTeamWithTriplePoignee() != null || 
				stdGame.getTeamWithKidAtTheEnd() != null || 
				stdGame.getChelem() != null ||
				stdGame.getPlayerWithMisery() != null;
		
		return toReturn;
    }
    
    /**
     * Displays the annoucement sub panels.
     * @return
     */
    private void displayAnnouncementSubPanels() {
    	StandardBaseGame stdGame = (StandardBaseGame)this.game;
    	
    	if (stdGame.getTeamWithPoignee() == null) {
    		this.panelHandful.setVisibility(View.GONE);
    	}
    	if (stdGame.getTeamWithDoublePoignee() == null) {
    		this.panelDoubleHandful.setVisibility(View.GONE);
    	}
    	if (stdGame.getTeamWithTriplePoignee() == null) {
    		this.panelTripleHandful.setVisibility(View.GONE);
    	}
    	if (stdGame.getTeamWithKidAtTheEnd() == null) {
    		this.panelKidAtTheEnd.setVisibility(View.GONE);
    	}
    	if (stdGame.getChelem() == null) {
    		this.panelSlam.setVisibility(View.GONE);
    	}
    	if (stdGame.getPlayerWithMisery() == null) {
    		this.panelMisery.setVisibility(View.GONE);
    	}    	
    }

	/**
     * Displays the belgian game.
     * @return
     */
    private void displayBelgianGame() {
    	if (this.game instanceof BelgianTarot3Game) {
    		BelgianTarot3Game belgianGame = (BelgianTarot3Game)this.game;
        	this.selectorFirst.setSelected(belgianGame.getFirst());
        	this.selectorFirst.setReadOnly(true);
        	this.selectorSecond.setSelected(belgianGame.getSecond());
        	this.selectorSecond.setReadOnly(true);
        	this.selectorThird.setSelected(belgianGame.getThird());
        	this.selectorThird.setReadOnly(true);
    	}
    	else if (this.game instanceof BelgianTarot4Game) {
    		BelgianTarot4Game belgianGame = (BelgianTarot4Game)this.game;
        	this.selectorFirst.setSelected(belgianGame.getFirst());
        	this.selectorFirst.setReadOnly(true);
        	this.selectorSecond.setSelected(belgianGame.getSecond());
        	this.selectorSecond.setReadOnly(true);
        	this.selectorThird.setSelected(belgianGame.getThird());
        	this.selectorThird.setReadOnly(true);
        	this.selectorFourth.setSelected(belgianGame.getFourth());
        	this.selectorFourth.setReadOnly(true);
    	}
    	else if (this.game instanceof BelgianTarot5Game) {
    		BelgianTarot5Game belgianGame = (BelgianTarot5Game)this.game;
        	this.selectorFirst.setSelected(belgianGame.getFirst());
        	this.selectorFirst.setReadOnly(true);
        	this.selectorSecond.setSelected(belgianGame.getSecond());
        	this.selectorSecond.setReadOnly(true);
        	this.selectorThird.setSelected(belgianGame.getThird());
        	this.selectorThird.setReadOnly(true);
        	this.selectorFourth.setSelected(belgianGame.getFourth());
        	this.selectorFourth.setReadOnly(true);
        	this.selectorFifth.setSelected(belgianGame.getFifth());
        	this.selectorFifth.setReadOnly(true);
    	}
    }
    
    /**
     * Initializes the widgets specific to belgian games.
     */
    @SuppressWarnings("unchecked")
	private void intializeBelgianViews() {
    	
    	// Main Parameters widget recuperation	
    	this.selectorFirst = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryFirst);
    	this.selectorSecond = (Selector<Player>) this.frameLayout.findViewById(R.id.gallerySecond);
    	this.selectorThird = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryThird);
    	this.panelFourth = (RelativeLayout) this.frameLayout.findViewById(R.id.panelFourth);
    	this.selectorFourth = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryFourth);
    	this.panelFifth = (RelativeLayout) this.frameLayout.findViewById(R.id.panelFifth);
    	this.selectorFifth = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryFifth);
    	
    	// widget content loading
    	this.selectorFirst.setObjects(this.game.getPlayers().getPlayers());
    	this.selectorSecond.setObjects(this.game.getPlayers().getPlayers());
    	this.selectorThird.setObjects(this.game.getPlayers().getPlayers());
		this.panelFourth.setVisibility(View.GONE);
		this.panelFifth.setVisibility(View.GONE);
    	
    	if (this.game instanceof BelgianTarot4Game) {
    		this.panelFourth.setVisibility(View.VISIBLE);
        	this.selectorFourth.setObjects(this.game.getPlayers().getPlayers());
    	}
    	else if (this.game instanceof BelgianTarot5Game) {
    		this.panelFourth.setVisibility(View.VISIBLE);
    		this.selectorFourth.setObjects(this.game.getPlayers().getPlayers());
    		this.panelFifth.setVisibility(View.VISIBLE);
        	this.selectorFifth.setObjects(this.game.getPlayers().getPlayers());
    	}
    }
    
	/**
     * Displays the penalty game.
     * @return
     */
    private void displayPenaltyGame() {
		PenaltyGame penaltyGame = (PenaltyGame)this.game;
//		this.selectorPenalted.setSelected(penaltyGame.getPenaltedPlayer());
		this.txtGlobalPenaltyPoints.setText(new Integer(penaltyGame.getPenaltyPoints()).toString());
    }
    
    /**
     * Initializes the widgets specific to penalty games.
     */
	private void intializePenaltyViews() {
    	// widget recuperation
//    	this.selectorPenalted = (Selector<Player>) this.frameLayout.findViewById(R.id.selectorPenalted);
    	this.txtGlobalPenaltyPoints = (TextView) this.frameLayout.findViewById(R.id.txtGlobalPenaltyPoints);

    	// widget content loading
//    	this.selectorPenalted.setObjects(this.game.getPlayers().getPlayers());    	
//    	this.selectorPenalted.setReadOnly(true);
    }
}