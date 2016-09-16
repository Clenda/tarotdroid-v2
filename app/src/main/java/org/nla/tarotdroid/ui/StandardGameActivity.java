package org.nla.tarotdroid.ui;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.Bet;
import org.nla.tarotdroid.biz.Chelem;
import org.nla.tarotdroid.biz.King;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.biz.StandardTarot3Game;
import org.nla.tarotdroid.biz.StandardTarot4Game;
import org.nla.tarotdroid.biz.StandardTarot5Game;
import org.nla.tarotdroid.biz.Team;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.ui.controls.Selector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

public class StandardGameActivity extends BaseGameActivity {
    @BindView(R.id.btnPlusAttackPoints) protected Button btnPlusAttackPoints;
    @BindView(R.id.btnMinusAttackPoints) protected Button btnMinusAttackPoints;
    @BindView(R.id.btnPlusDefensePoints) protected Button btnPlusDefensePoints;
    @BindView(R.id.btnMinusDefensePoints) protected Button btnMinusDefensePoints;
    @BindView(R.id.barAttackPoints) protected SeekBar barAttackPoints;
    @BindView(R.id.barDefensePoints) protected SeekBar barDefensePoints;
    @BindView(R.id.txtAttackPoints) protected TextView txtAttackPoints;
    @BindView(R.id.txtDefensePoints) protected TextView txtDefensePoints;
    @BindView(R.id.panelCalled) protected RelativeLayout panelCalled;
    @BindView(R.id.panelKing) protected RelativeLayout panelKing;
    @BindView(R.id.txtTitleAnnouncements) protected TextView txtTitleAnnouncements;
    @BindView(R.id.panelAnnouncements) protected LinearLayout panelAnnouncements;
    @BindView(R.id.panelMisery) protected RelativeLayout panelMisery;
    @BindView(R.id.galleryBet) protected Selector<Bet> selectorBet;
    @BindView(R.id.galleryLeader) protected Selector<Player> selectorLeader;
    @BindView(R.id.galleryCalled) protected Selector<Player> selectorCalled;
    @BindView(R.id.galleryKing) protected Selector<King> selectorKing;
    @BindView(R.id.galleryOudlers) protected Selector<Integer> selectorOudlers;
    @BindView(R.id.galleryHandful) protected Selector<Team> selectorHandful;
    @BindView(R.id.galleryDoubleHandful) protected Selector<Team> selectorDoubleHandful;
    @BindView(R.id.galleryTribleHandful) protected Selector<Team> selectorTripleHandful;
    @BindView(R.id.galleryMisery) protected Selector<Player> selectorMisery;
    @BindView(R.id.galleryKidAtTheEnd) protected Selector<Team> selectorKidAtTheEnd;
    @BindView(R.id.gallerySlam) protected Selector<Chelem> selectorSlam;

    private int attackScore;
    private SeekBar.OnSeekBarChangeListener attackPointsChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(final SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(final SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(
                final SeekBar seekBar,
                final int progress,
                final boolean fromUser
        ) {
            attackScore = progress;
            updatePointsViews();
        }
    };
    private SeekBar.OnSeekBarChangeListener defensePointsChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(final SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(final SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(
                final SeekBar seekBar,
                final int progress,
                final boolean fromUser
        ) {
            attackScore = 91 - progress;
            updatePointsViews();
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.standard_game_creation;
    }

    @Override
    protected void setTitle() {
        if (isInEditMode) {
            setTitle(R.string.lblUpdateGameTitle);
        } else {
            setTitle(R.string.lblNewStandardGameTitle);
        }
    }

    // TODO Implement
    @Override
    protected void auditEvent() {
    }

    @Override
    protected String getHelpTitle() {
        switch (getGameSet().getGameStyleType()) {
            case Tarot3:
                return getString(R.string.titleHelpNewStd3Game);

            case Tarot5:
                return getString(R.string.titleHelpNewStd5Game);

            case Tarot4:
            default:
                return getString(R.string.titleHelpNewStd4Game);
        }
    }

    @Override
    protected String getHelpContent() {
        switch (getGameSet().getGameStyleType()) {
            case Tarot3:
                return getText(R.string.msgHelpNewStd3Game).toString();
            case Tarot5:
                return getText(R.string.msgHelpNewStd5Game).toString();
            case Tarot4:
            default:
                return getText(R.string.msgHelpNewStd4Game).toString();
        }
    }

    @Optional
    @OnClick(R.id.btnMinusAttackPoints)
    public void onClickOnBtnMinusAttackPoints() {
        decreaseAttackScore();
    }

    @Optional
    @OnClick(R.id.btnPlusDefensePoints)
    public void onClickOnBtnPlusDefensePoints() {
        decreaseAttackScore();
    }

    @Optional
    @OnClick(R.id.btnPlusAttackPoints)
    public void onClickOnBtnPlusAttackPoints() {
        increaseAttackScore();
    }

    @Optional
    @OnClick(R.id.btnMinusDefensePoints)
    public void onClickOnBtnMinusDefensePoints() {
        increaseAttackScore();
    }

    private void decreaseAttackScore() {
        attackScore -= 1;
        updatePointsViews();
    }

    private void increaseAttackScore() {
        attackScore += 1;
        updatePointsViews();
    }

    private void updatePointsViews() {
        // sets attack and defense default points values
        txtAttackPoints.setText(new Integer(attackScore).toString());
        barAttackPoints.setProgress(attackScore);
        txtDefensePoints.setText(new Integer(91 - attackScore).toString());
        barDefensePoints.setProgress(91 - attackScore);

        // disable/enable -/+ buttons depending on the score
        if (attackScore == 91) {
            btnPlusAttackPoints.setEnabled(false);
            btnMinusDefensePoints.setEnabled(false);
            btnMinusAttackPoints.setEnabled(true);
            btnPlusDefensePoints.setEnabled(true);
        } else if (attackScore == 0) {
            btnMinusAttackPoints.setEnabled(false);
            btnPlusDefensePoints.setEnabled(false);
            btnPlusAttackPoints.setEnabled(true);
            btnMinusDefensePoints.setEnabled(true);
        } else {
            btnMinusAttackPoints.setEnabled(true);
            btnPlusDefensePoints.setEnabled(true);
            btnPlusAttackPoints.setEnabled(true);
            btnMinusDefensePoints.setEnabled(true);
        }
    }

    @Override
    public BaseGame createGame() {
        switch (getGameSet().getGameStyleType()) {
            case Tarot3:
                return createStandard3Game();
            case Tarot4:
                return createStandard4Game();
            case Tarot5:
                return createStandard5Game();
            default:
                throw new RuntimeException("Incorrect game style type");
        }
    }

    private StandardTarot3Game createStandard3Game() {
        // game to return
        StandardTarot3Game toReturn = new StandardTarot3Game();

        // sets the common properties
        setCommonStandardProperties(toReturn);

        return toReturn;
    }

    private StandardTarot4Game createStandard4Game() {
        // game to return
        StandardTarot4Game toReturn = new StandardTarot4Game();

        // sets the common properties
        setCommonStandardProperties(toReturn);

        return toReturn;
    }

    private StandardTarot5Game createStandard5Game() {
        // game to return
        StandardTarot5Game toReturn = new StandardTarot5Game();

        // sets the common properties
        setCommonStandardProperties(toReturn);

        // king
        toReturn.setCalledKing(selectorKing.getSelected());

        // called player
        toReturn.setCalledPlayer(selectorCalled.getSelected());

        return toReturn;
    }

    @Override
    public boolean isFormValid() {
        switch (getGameSet().getGameStyleType()) {
            case Tarot3:
                return isStandard3GameValid();
            case Tarot4:
                return isStandard4GameValid();
            case Tarot5:
                return isStandard5GameValid();
            default:
                throw new RuntimeException("Incorrect game style type");
        }
    }

    private boolean isStandard3GameValid() {
        return areCommonStandardPropertiesValid();
    }

    private boolean isStandard4GameValid() {
        return areCommonStandardPropertiesValid();
    }

    private boolean isStandard5GameValid() {
        boolean isValid = areCommonStandardPropertiesValid();

        // king
        isValid = isValid && selectorKing.isSelected();

        // called playerd
        isValid = isValid && selectorCalled.isSelected();

        return isValid;
    }

    private boolean areCommonStandardPropertiesValid() {
        boolean isValid = true;

        // dealer player
        isValid = isValid && selectorDealer.isSelected();

        // bet
        isValid = isValid && selectorBet.isSelected();

        // oudlers
        isValid = isValid && selectorOudlers.isSelected();

        // leader player
        isValid = isValid && selectorLeader.isSelected();

        return isValid;
    }

    @Override
    public void updateGame(final BaseGame game) {
        if (game instanceof StandardTarot5Game) {
            this.updateStandard5Game((StandardTarot5Game) game);
        } else if (game instanceof StandardBaseGame) {
            this.updateStandardGame((StandardBaseGame) game);
        }
    }

    public void updateStandardGame(final StandardBaseGame game) {
        // sets the common properties
        setCommonStandardProperties(game);
    }

    private void updateStandard5Game(final StandardTarot5Game game) {
        // sets the common properties
        setCommonStandardProperties(game);
        // king
        game.setCalledKing(selectorKing.getSelected());

        // called player
        game.setCalledPlayer(selectorCalled.getSelected());
    }

    @Override
    public void initializeSpecificViews() {
        intializeStandardViews();
        initializeDeadAndDealerPanelForStandardCase();
    }

    private void intializeStandardViews() {
        // loading widget contents
        List<Bet> availableBets = new ArrayList<>();
        if (appParams.isPriseAuthorized()) {
            if (appParams.isPetiteAuthorized()) {
                availableBets.add(Bet.PETITE);
            }
            availableBets.add(Bet.PRISE);
        }
        availableBets.add(Bet.GARDE);
        availableBets.add(Bet.GARDESANS);
        availableBets.add(Bet.GARDECONTRE);

        selectorBet.setObjects(availableBets);
        selectorLeader.setObjects(inGamePlayers);
        selectorCalled.setObjects(inGamePlayers);
        selectorKing.setObjects(Arrays.asList(King.CLUB, King.DIAMOND, King.HEART, King.SPADE));
        selectorOudlers.setObjects(Arrays.asList(0, 1, 2, 3));
        barAttackPoints.setProgress(0);
        barDefensePoints.setProgress(91);
        selectorHandful.setObjects(Arrays.asList(Team.LEADING_TEAM,
                                                 Team.DEFENSE_TEAM,
                                                 Team.BOTH_TEAMS));
        selectorDoubleHandful.setObjects(Arrays.asList(Team.LEADING_TEAM, Team.DEFENSE_TEAM));
        selectorTripleHandful.setObjects(Arrays.asList(Team.LEADING_TEAM, Team.DEFENSE_TEAM));
        selectorKidAtTheEnd.setObjects(Arrays.asList(Team.LEADING_TEAM, Team.DEFENSE_TEAM));
        selectorMisery.setObjects(inGamePlayers);
        selectorSlam.setObjects(Arrays.asList(Chelem.CHELEM_ANOUNCED_AND_SUCCEEDED,
                                              Chelem.CHELEM_ANOUNCED_AND_FAILED,
                                              Chelem.CHELEM_NOT_ANOUNCED_BUT_SUCCEEDED));

        // event handers
        barAttackPoints.setOnSeekBarChangeListener(attackPointsChangeListener);
        barDefensePoints.setOnSeekBarChangeListener(defensePointsChangeListener);

        // set tarot 5 widgets visibilities
        if (getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
            panelCalled.setVisibility(View.VISIBLE);
            panelKing.setVisibility(View.VISIBLE);
            panelMisery.setVisibility(View.VISIBLE);
        }

        // set misery visibility at 3/4 player if parameters say so
        if (appParams.isMiseryAuthorized()) {
            panelMisery.setVisibility(View.VISIBLE);
        }

        // synchronizes buttons/edit text with score
        attackScore = 0;
        updatePointsViews();
    }

    private void initializeDeadAndDealerPanelForStandardCase() {
        // if no dead player, case is easy...
        if (!isDisplayDeadPlayerPanel()) {
            panelDead.setVisibility(View.GONE);
            setDealerPlayer();
            return;
        }

        // if not, it becomes more complicated...
        else {
            // load dead player selector with all players
            selectorDead.setObjects(new ArrayList<>(getGameSet().getPlayers().getPlayers()));
            selectorDealer.setObjects(new ArrayList<>(getGameSet().getPlayers().getPlayers()));

            // load/hide the rest of the views when a dead player is selected
            selectorDead.setObjectSelectedListener(new Selector.OnObjectSelectedListener<Player>() {

                @Override
                public void onItemSelected(final Player selected) {
                    // create new ingame player list
                    inGamePlayers = new ArrayList<>(getGameSet().getPlayers().getPlayers());
                    inGamePlayers.remove(selectorDead.getSelected());

                    // load player selector with this new player list
                    selectorLeader.setObjects(inGamePlayers);
                    selectorCalled.setObjects(inGamePlayers);
                    selectorMisery.setObjects(inGamePlayers);

                    // display panels
                    panelMainParameters.startAnimation(new ScaleAnimToShow(1.0f,
                                                                           1.0f,
                                                                           1.0f,
                                                                           0.0f,
                                                                           500,
                                                                           panelMainParameters,
                                                                           true));
                    panelAnnouncements.startAnimation(new ScaleAnimToShow(1.0f,
                                                                          1.0f,
                                                                          1.0f,
                                                                          0.0f,
                                                                          500,
                                                                          panelAnnouncements,
                                                                          true));
                    txtTitleMainParameters.setOnClickListener(null);
                    txtTitleAnnouncements.setOnClickListener(null);
                }

                /* (non-Javadoc)
                 * @see Selector.OnObjectSelectedListener#onNothingSelected()
                 */
                @Override
                public void onNothingSelected() {
                    // hide panels
                    panelMainParameters.startAnimation(new ScaleAnimToHide(1.0f,
                                                                           1.0f,
                                                                           1.0f,
                                                                           0.0f,
                                                                           500,
                                                                           panelMainParameters,
                                                                           true));
                    panelAnnouncements.startAnimation(new ScaleAnimToHide(1.0f,
                                                                          1.0f,
                                                                          1.0f,
                                                                          0.0f,
                                                                          500,
                                                                          panelAnnouncements,
                                                                          true));
                    txtTitleMainParameters.setOnClickListener(onNoDeadPlayerSelectedClickListener);
                    txtTitleAnnouncements.setOnClickListener(onNoDeadPlayerSelectedClickListener);
                }
            });

            // if no dead player was previously selected, hide dealer and all other panels
            if (!trySetDeadPlayer()) {
                panelDeadAndDealer.setVisibility(View.VISIBLE);
                panelMainParameters.setVisibility(View.GONE);
                txtTitleMainParameters.setOnClickListener(onNoDeadPlayerSelectedClickListener);
            }
            setDealerPlayer();
        }
    }

    @Override
    public void displayGame() {
        StandardBaseGame stdGame = (StandardBaseGame) game;

        // common properties
        selectorBet.setSelected(stdGame.getBet());
        selectorLeader.setSelected(stdGame.getLeadingPlayer());
        selectorOudlers.setSelected(stdGame.getNumberOfOudlers());
        barAttackPoints.setProgress((int) stdGame.getPoints());
        txtAttackPoints.setText(new Integer((int) stdGame.getPoints()).toString());
        barDefensePoints.setProgress(91 - (int) stdGame.getPoints());
        txtDefensePoints.setText(new Integer(91 - (int) stdGame.getPoints()).toString());

        // announcements
        panelAnnouncements.setVisibility(View.VISIBLE);
        selectorHandful.setSelected(stdGame.getTeamWithPoignee());
        selectorDoubleHandful.setSelected(stdGame.getTeamWithDoublePoignee());
        selectorTripleHandful.setSelected(stdGame.getTeamWithTriplePoignee());
        selectorKidAtTheEnd.setSelected(stdGame.getTeamWithKidAtTheEnd());
        selectorSlam.setSelected(stdGame.getChelem());

        // 5 player specifics
        if (getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
            StandardTarot5Game std5Game = (StandardTarot5Game) game;
            panelCalled.setVisibility(View.VISIBLE);
            panelKing.setVisibility(View.VISIBLE);
            panelMisery.setVisibility(View.VISIBLE);
            selectorCalled.setSelected(std5Game.getCalledPlayer());
            selectorKing.setSelected(std5Game.getCalledKing());
            selectorMisery.setSelected(std5Game.getPlayerWithMisery());
        }

        // display misery if game 3/4 player has a misery set
        if (stdGame.getPlayerWithMisery() != null) {
            panelMisery.setVisibility(View.VISIBLE);
            selectorMisery.setSelected(stdGame.getPlayerWithMisery());
        }
    }

    private void setCommonStandardProperties(final StandardBaseGame game) {
        if (game == null) {
            throw new IllegalArgumentException("game is null");
        }

        // game players
        game.setPlayers(new PlayerList(inGamePlayers));

        game.setDeadPlayer(selectorDead.getSelected());

        // dealer player
        game.setDealer(selectorDealer.getSelected());

        // bet
        game.setBet(selectorBet.getSelected());

        // oudlers
        game.setNumberOfOudlers(selectorOudlers.getSelected());

        // leader player
        game.setLeadingPlayer(selectorLeader.getSelected());

        // attack score
        game.setPoints((double) barAttackPoints.getProgress());

        // handful
        game.setTeamWithPoignee(selectorHandful.getSelected());

        // double handful
        game.setTeamWithDoublePoignee(selectorDoubleHandful.getSelected());

        // triple handful
        game.setTeamWithTriplePoignee(selectorTripleHandful.getSelected());

        // misery
        if (selectorMisery.isSelected()) {
            PlayerList playersWithMisery = new PlayerList();
            playersWithMisery.add(selectorMisery.getSelected());
            game.setPlayersWithMisery(playersWithMisery);
        } else {
            game.setPlayersWithMisery(null);
        }

        // kid at the end
        game.setTeamWithKidAtTheEnd(selectorKidAtTheEnd.getSelected());

        // slam
        game.setChelem(selectorSlam.getSelected());
    }
}
