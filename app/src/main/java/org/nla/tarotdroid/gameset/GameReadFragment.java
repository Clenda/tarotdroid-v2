package org.nla.tarotdroid.gameset;

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

import org.nla.tarotdroid.R;
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
import org.nla.tarotdroid.constants.ActivityParams;
import org.nla.tarotdroid.gameset.controls.Selector;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;

public class GameReadFragment extends BaseGameSetFragment {
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

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle args = getArguments();
        this.game = getGameSet().getGameOfIndex(args.getInt(ActivityParams.PARAM_GAME_INDEX));

        // set layout
        if (game instanceof StandardBaseGame) {
            frameLayout = (FrameLayout) inflater.inflate(R.layout.standard_game_read,
                                                         container,
                                                         false);
        } else if (game instanceof BelgianBaseGame) {
            frameLayout = (FrameLayout) inflater.inflate(R.layout.belgian_game_read,
                                                         container,
                                                         false);
        } else if (game instanceof PenaltyGame) {
            frameLayout = (FrameLayout) inflater.inflate(R.layout.penalty_game_read,
                                                         container,
                                                         false);
        } else if (game instanceof PassedGame) {
            frameLayout = (FrameLayout) inflater.inflate(R.layout.passed_game_read,
                                                         container,
                                                         false);
        }


        // display game
        displayDeadAndDealer();
        if (game instanceof StandardBaseGame) {
            intializeStandardViews();
            displayStandardGame();
        } else if (game instanceof BelgianBaseGame) {
            intializeBelgianViews();
            displayBelgianGame();
        } else if (game instanceof PenaltyGame) {
            intializePenaltyViews();
            displayPenaltyGame();
        } else if (game instanceof PassedGame) {
            // I guess nothing to do....
        }

        smoothlyHideText();
        return frameLayout;
    }

    private void smoothlyHideText() {
        final TextView onTop = (TextView) frameLayout.findViewById(R.id.txtOnTop);
        onTop.setText(game.getIndex() + "/" + game.getHighestGameIndex());

        Animation animation1 = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(1500);

        //animation1 AnimationListener
        animation1.setAnimationListener(new AnimationListener() {

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

    private void displayDeadAndDealer() {

        // initialize dealer player widgets
        panelDealer = (RelativeLayout) frameLayout.findViewById(R.id.panelDealer);
        if (game.getDealer() != null) {
            panelDealer.setVisibility(View.VISIBLE);
            selectorDealer = (Selector<Player>) frameLayout.findViewById(R.id.galleryDealer);
            selectorDealer.setObjects(getGameSet().getPlayers().getPlayers());
            selectorDealer.setSelected(game.getDealer());
            selectorDealer.setReadOnly(true);
        } else {
            panelDealer.setVisibility(View.GONE);
        }

        // initialize dead player widgets
        panelDead = (RelativeLayout) frameLayout.findViewById(R.id.panelDead);
        if (game.getDeadPlayer() != null) {
            panelDead.setVisibility(View.VISIBLE);
            selectorDead = (Selector<Player>) frameLayout.findViewById(R.id.galleryDead);
            selectorDead.setObjects(getGameSet().getPlayers().getPlayers());
            selectorDead.setSelected(game.getDeadPlayer());
            selectorDead.setReadOnly(true);
        } else {
            panelDead.setVisibility(View.GONE);
        }

        // if neither dealer nor dead players are set, don't display the section
        if (game.getDealer() == null && game.getDeadPlayer() == null) {
            panelDeadAndDealerSection = (LinearLayout) frameLayout.findViewById(R.id.panelDeadAndDealerSection);
            panelDeadAndDealerSection.setVisibility(View.GONE);
        }
    }

    private void displayStandardGame() {
        StandardBaseGame stdGame = (StandardBaseGame) game;

        // common properties
        selectorBet.setSelected(stdGame.getBet());
        selectorBet.setReadOnly(true);
        selectorLeader.setSelected(stdGame.getLeadingPlayer());
        selectorLeader.setReadOnly(true);
        selectorOudlers.setSelected(stdGame.getNumberOfOudlers());
        selectorOudlers.setReadOnly(true);
        barAttackPoints.setProgress((int) stdGame.getPoints());
        txtAttackPoints.setText(new Integer((int) stdGame.getPoints()).toString());
        barDefensePoints.setProgress(91 - (int) stdGame.getPoints());
        txtDefensePoints.setText(new Integer(91 - (int) stdGame.getPoints()).toString());

        // announcements
        if (isDisplayAnnouncementPanel()) {
            displayAnnouncementSubPanels();
            panelAnnouncements.setVisibility(View.VISIBLE);
            selectorHandful.setSelected(stdGame.getTeamWithPoignee());
            selectorHandful.setReadOnly(true);
            selectorDoubleHandful.setSelected(stdGame.getTeamWithDoublePoignee());
            selectorDoubleHandful.setReadOnly(true);
            selectorTripleHandful.setSelected(stdGame.getTeamWithTriplePoignee());
            selectorTripleHandful.setReadOnly(true);
            selectorKidAtTheEnd.setSelected(stdGame.getTeamWithKidAtTheEnd());
            selectorKidAtTheEnd.setReadOnly(true);
            selectorSlam.setSelected(stdGame.getChelem());
            selectorSlam.setReadOnly(true);
            selectorMisery.setSelected(stdGame.getPlayerWithMisery());
            selectorMisery.setReadOnly(true);
        } else {
            panelAnnoucementSection.setVisibility(View.GONE);
            panelAnnouncements.setVisibility(View.GONE);
        }

        // 5 player specifics
        if (getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
            StandardTarot5Game std5Game = (StandardTarot5Game) game;
            panelCalled.setVisibility(View.VISIBLE);
            panelKing.setVisibility(View.VISIBLE);

            selectorCalled.setSelected(std5Game.getCalledPlayer());
            selectorCalled.setReadOnly(true);
            selectorKing.setSelected(std5Game.getCalledKing());
            selectorKing.setReadOnly(true);

        }
    }

    private void intializeStandardViews() {

        // Main Parameters widget recuperation
        selectorBet = (Selector<Bet>) frameLayout.findViewById(R.id.galleryBet);
        selectorLeader = (Selector<Player>) frameLayout.findViewById(R.id.galleryLeader);
        panelCalled = (RelativeLayout) frameLayout.findViewById(R.id.panelCalled);
        selectorCalled = (Selector<Player>) frameLayout.findViewById(R.id.galleryCalled);
        panelKing = (RelativeLayout) frameLayout.findViewById(R.id.panelKing);
        selectorKing = (Selector<King>) frameLayout.findViewById(R.id.galleryKing);
        selectorOudlers = (Selector<Integer>) frameLayout.findViewById(R.id.galleryOudlers);
        barAttackPoints = (SeekBar) frameLayout.findViewById(R.id.barAttackPoints);
        txtAttackPoints = (TextView) frameLayout.findViewById(R.id.txtAttackPoints);
        barDefensePoints = (SeekBar) frameLayout.findViewById(R.id.barDefensePoints);
        txtDefensePoints = (TextView) frameLayout.findViewById(R.id.txtDefensePoints);

        // Annoucements widgets recuperation
        panelAnnouncements = (LinearLayout) frameLayout.findViewById(R.id.panelAnnouncements);
        panelAnnoucementSection = (LinearLayout) frameLayout.findViewById(R.id.panelAnnoucementSection);
        panelHandful = (RelativeLayout) frameLayout.findViewById(R.id.panelHandful);
        selectorHandful = (Selector<Team>) frameLayout.findViewById(R.id.galleryHandful);
        panelDoubleHandful = (RelativeLayout) frameLayout.findViewById(R.id.panelDoubleHandful);
        selectorDoubleHandful = (Selector<Team>) frameLayout.findViewById(R.id.galleryDoubleHandful);
        panelTripleHandful = (RelativeLayout) frameLayout.findViewById(R.id.panelTripleHandful);
        selectorTripleHandful = (Selector<Team>) frameLayout.findViewById(R.id.galleryTribleHandful);
        panelMisery = (RelativeLayout) frameLayout.findViewById(R.id.panelMisery);
        selectorMisery = (Selector<Player>) frameLayout.findViewById(R.id.galleryMisery);
        panelKidAtTheEnd = (RelativeLayout) frameLayout.findViewById(R.id.panelKidAtTheEnd);
        selectorKidAtTheEnd = (Selector<Team>) frameLayout.findViewById(R.id.galleryKidAtTheEnd);
        panelSlam = (RelativeLayout) frameLayout.findViewById(R.id.panelSlam);
        selectorSlam = (Selector<Chelem>) frameLayout.findViewById(R.id.gallerySlam);

        // widget content loading
        selectorBet.setObjects(newArrayList(Bet.PETITE,
                                            Bet.PRISE,
                                            Bet.GARDE,
                                            Bet.GARDESANS,
                                            Bet.GARDECONTRE));
        selectorLeader.setObjects(game.getPlayers().getPlayers());
        selectorOudlers.setObjects(newArrayList(0, 1, 2, 3));
        barAttackPoints.setProgress(0);
        barAttackPoints.setEnabled(false);
        barDefensePoints.setProgress(91);
        barDefensePoints.setEnabled(false);
        selectorHandful.setObjects(newArrayList(Team.LEADING_TEAM,
                                                Team.DEFENSE_TEAM,
                                                Team.BOTH_TEAMS));
        selectorDoubleHandful.setObjects(newArrayList(Team.LEADING_TEAM, Team.DEFENSE_TEAM));
        selectorTripleHandful.setObjects(newArrayList(Team.LEADING_TEAM, Team.DEFENSE_TEAM));
        selectorKidAtTheEnd.setObjects(newArrayList(Team.LEADING_TEAM, Team.DEFENSE_TEAM));
        selectorSlam.setObjects(newArrayList(Chelem.CHELEM_ANOUNCED_AND_SUCCEEDED,
                                             Chelem.CHELEM_ANOUNCED_AND_FAILED,
                                             Chelem.CHELEM_NOT_ANOUNCED_BUT_SUCCEEDED));
        panelMisery.setVisibility(View.VISIBLE);
        selectorMisery.setObjects(game.getPlayers().getPlayers());

        // tarot 5 specific widget content loading
        if (getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
            panelCalled.setVisibility(View.VISIBLE);
            selectorCalled.setObjects(game.getPlayers().getPlayers());
            panelKing.setVisibility(View.VISIBLE);
            selectorKing.setObjects(newArrayList(King.CLUB,
                                                 King.DIAMOND,
                                                 King.HEART,
                                                 King.SPADE));
        } else {
            panelCalled.setVisibility(View.GONE);
            panelKing.setVisibility(View.GONE);
        }
    }

    private boolean isDisplayAnnouncementPanel() {
        StandardBaseGame stdGame = (StandardBaseGame) game;
        boolean toReturn =
                stdGame.getTeamWithPoignee() != null ||
                        stdGame.getTeamWithDoublePoignee() != null ||
                        stdGame.getTeamWithTriplePoignee() != null ||
                        stdGame.getTeamWithKidAtTheEnd() != null ||
                        stdGame.getChelem() != null ||
                        stdGame.getPlayerWithMisery() != null;

        return toReturn;
    }

    private void displayAnnouncementSubPanels() {
        StandardBaseGame stdGame = (StandardBaseGame) game;

        if (stdGame.getTeamWithPoignee() == null) {
            panelHandful.setVisibility(View.GONE);
        }
        if (stdGame.getTeamWithDoublePoignee() == null) {
            panelDoubleHandful.setVisibility(View.GONE);
        }
        if (stdGame.getTeamWithTriplePoignee() == null) {
            panelTripleHandful.setVisibility(View.GONE);
        }
        if (stdGame.getTeamWithKidAtTheEnd() == null) {
            panelKidAtTheEnd.setVisibility(View.GONE);
        }
        if (stdGame.getChelem() == null) {
            panelSlam.setVisibility(View.GONE);
        }
        if (stdGame.getPlayerWithMisery() == null) {
            panelMisery.setVisibility(View.GONE);
        }
    }

    private void displayBelgianGame() {
        if (game instanceof BelgianTarot3Game) {
            BelgianTarot3Game belgianGame = (BelgianTarot3Game) game;
            selectorFirst.setSelected(belgianGame.getFirst());
            selectorFirst.setReadOnly(true);
            selectorSecond.setSelected(belgianGame.getSecond());
            selectorSecond.setReadOnly(true);
            selectorThird.setSelected(belgianGame.getThird());
            selectorThird.setReadOnly(true);
        } else if (game instanceof BelgianTarot4Game) {
            BelgianTarot4Game belgianGame = (BelgianTarot4Game) game;
            selectorFirst.setSelected(belgianGame.getFirst());
            selectorFirst.setReadOnly(true);
            selectorSecond.setSelected(belgianGame.getSecond());
            selectorSecond.setReadOnly(true);
            selectorThird.setSelected(belgianGame.getThird());
            selectorThird.setReadOnly(true);
            selectorFourth.setSelected(belgianGame.getFourth());
            selectorFourth.setReadOnly(true);
        } else if (game instanceof BelgianTarot5Game) {
            BelgianTarot5Game belgianGame = (BelgianTarot5Game) game;
            selectorFirst.setSelected(belgianGame.getFirst());
            selectorFirst.setReadOnly(true);
            selectorSecond.setSelected(belgianGame.getSecond());
            selectorSecond.setReadOnly(true);
            selectorThird.setSelected(belgianGame.getThird());
            selectorThird.setReadOnly(true);
            selectorFourth.setSelected(belgianGame.getFourth());
            selectorFourth.setReadOnly(true);
            selectorFifth.setSelected(belgianGame.getFifth());
            selectorFifth.setReadOnly(true);
        }
    }

    @SuppressWarnings("unchecked")
    private void intializeBelgianViews() {

        // Main Parameters widget recuperation
        selectorFirst = (Selector<Player>) frameLayout.findViewById(R.id.galleryFirst);
        selectorSecond = (Selector<Player>) frameLayout.findViewById(R.id.gallerySecond);
        selectorThird = (Selector<Player>) frameLayout.findViewById(R.id.galleryThird);
        panelFourth = (RelativeLayout) frameLayout.findViewById(R.id.panelFourth);
        selectorFourth = (Selector<Player>) frameLayout.findViewById(R.id.galleryFourth);
        panelFifth = (RelativeLayout) frameLayout.findViewById(R.id.panelFifth);
        selectorFifth = (Selector<Player>) frameLayout.findViewById(R.id.galleryFifth);

        // widget content loading
        selectorFirst.setObjects(game.getPlayers().getPlayers());
        selectorSecond.setObjects(game.getPlayers().getPlayers());
        selectorThird.setObjects(game.getPlayers().getPlayers());
        panelFourth.setVisibility(View.GONE);
        panelFifth.setVisibility(View.GONE);

        if (game instanceof BelgianTarot4Game) {
            panelFourth.setVisibility(View.VISIBLE);
            selectorFourth.setObjects(game.getPlayers().getPlayers());
        } else if (game instanceof BelgianTarot5Game) {
            panelFourth.setVisibility(View.VISIBLE);
            selectorFourth.setObjects(game.getPlayers().getPlayers());
            panelFifth.setVisibility(View.VISIBLE);
            selectorFifth.setObjects(game.getPlayers().getPlayers());
        }
    }

    private void displayPenaltyGame() {
        PenaltyGame penaltyGame = (PenaltyGame) game;
        txtGlobalPenaltyPoints.setText(new Integer(penaltyGame.getPenaltyPoints()).toString());
    }

    private void intializePenaltyViews() {
        txtGlobalPenaltyPoints = (TextView) frameLayout.findViewById(R.id.txtGlobalPenaltyPoints);
    }
}