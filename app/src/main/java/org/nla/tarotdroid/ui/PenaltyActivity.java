package org.nla.tarotdroid.ui;

import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.PenaltyGame;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.ui.controls.Selector;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.common.collect.Lists.newArrayList;

public class PenaltyActivity extends BaseGameActivity {

    @BindView(R.id.txtGlobalPenaltyPoints) protected TextView txtGlobalPenaltyPoints;
    @BindView(R.id.btnPlusGlobalPenaltyPoints) protected Button btnPlusGlobalPenaltyPoints;
    @BindView(R.id.btnMinusGlobalPenaltyPoints) protected Button btnMinusGlobalPenaltyPoints;
    @BindView(R.id.txtPlayerPenaltyPoints) protected TextView txtPlayerPenaltyPoints;
    @BindView(R.id.barPlayerPenaltyPoints) protected SeekBar barPlayerPenaltyPoints;

    private int playerPenaltyPoints;
    private int playerMultiplicationRate;

    private SeekBar.OnSeekBarChangeListener playerPenaltyPointsChangeListener = new SeekBar.OnSeekBarChangeListener() {

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
            playerPenaltyPoints = progress;
            updatePenaltyPointsViews();
        }
    };

    @OnClick(R.id.btnPlusGlobalPenaltyPoints)
    public void onClickOnBtnPlusGlobalPenaltyPoints() {
        playerPenaltyPoints += 1;
        updatePenaltyPointsViews();
    }

    @OnClick(R.id.btnMinusGlobalPenaltyPoints)
    public void onClickOnBtnMinusGlobalPenaltyPoints() {
        playerPenaltyPoints -= 1;
        updatePenaltyPointsViews();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.penalty_game_creation;
    }

    @Override
    protected void setTitle() {
        if (isInEditMode) {
            setTitle(R.string.lblUpdateGameTitle);
        } else {
            setTitle(R.string.lblNewPenaltyGameTitle);
        }
    }

    //TODO implement
    @Override
    protected void auditEvent() {

    }

    @Override
    protected String getHelpTitle() {
        return getString(R.string.titleHelpNewPenaltyGame);
    }

    @Override
    protected String getHelpContent() {
        return getString(R.string.msgHelpNewPenaltyGame);
    }

    @Override
    public boolean isFormValid() {
        if (!this.selectorDealer.isSelected()) {
            return false;
        }

        return this.playerPenaltyPoints != 0;

    }

    private void intializePenaltyViews() {
        this.barPlayerPenaltyPoints.setOnSeekBarChangeListener(this.playerPenaltyPointsChangeListener);

        // set bar properties
        this.barPlayerPenaltyPoints.setMax(100);
        this.playerPenaltyPoints = 0;

        // synchronizes buttons/edit text with score
        this.updatePenaltyPointsViews();
    }

    private void initializeDeadAndDealerPanelForPenaltyCase() {
        // if no dead player, case is easy...
        if (!isDisplayDeadPlayerPanel()) {
            panelDead.setVisibility(View.GONE);
            setDealerPlayer();
            return;
        }

        // if not, it becomes more complicated...
        else {
            // load dead player selector with all players
            selectorDead.setObjects(newArrayList(this.getGameSet().getPlayers().getPlayers()));
            selectorDealer.setObjects(newArrayList(this.getGameSet().getPlayers().getPlayers()));
            selectorDead.setObjectSelectedListener(new Selector.OnObjectSelectedListener<Player>() {

                @Override
                public void onItemSelected(final Player selected) {
                    // create new ingame player list
                    inGamePlayers = newArrayList(getGameSet().getPlayers().getPlayers());
                    inGamePlayers.remove(selectorDead.getSelected());

                    // display panels
                    panelMainParameters.startAnimation(new ScaleAnimToShow(1.0f,
                                                                           1.0f,
                                                                           1.0f,
                                                                           0.0f,
                                                                           500,
                                                                           panelMainParameters,
                                                                           true));
                    txtTitleMainParameters.setOnClickListener(null);
                }

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
                    txtTitleMainParameters.setOnClickListener(onNoDeadPlayerSelectedClickListener);
                }
            });

            // if no dead player was previously selected, hide dealer and all other panels
            if (!this.trySetDeadPlayer()) {
                panelDeadAndDealer.setVisibility(View.VISIBLE);
                panelMainParameters.setVisibility(View.GONE);
                txtTitleMainParameters.setOnClickListener(onNoDeadPlayerSelectedClickListener);
            }
            setDealerPlayer();
        }
    }

    private int computeGlobalPenaltyPoints() {
        return playerPenaltyPoints * playerMultiplicationRate;
    }

    private void updatePenaltyPointsViews() {
        // set zoom rate
        playerMultiplicationRate = getGameSet().getPlayers().size() - 1;

        // set player point values
        txtPlayerPenaltyPoints.setText(new Integer(playerPenaltyPoints).toString());
        barPlayerPenaltyPoints.setProgress(playerPenaltyPoints);

        // set global point values
        txtGlobalPenaltyPoints.setText(new Integer(computeGlobalPenaltyPoints()).toString());

        // disable/enable -/+ buttons depending on the player penalty points
        if (playerPenaltyPoints == 100) {
            btnPlusGlobalPenaltyPoints.setEnabled(false);
            btnMinusGlobalPenaltyPoints.setEnabled(true);
        } else if (playerPenaltyPoints == 0) {
            btnMinusGlobalPenaltyPoints.setEnabled(false);
            btnPlusGlobalPenaltyPoints.setEnabled(true);
        } else {
            btnMinusGlobalPenaltyPoints.setEnabled(true);
            btnPlusGlobalPenaltyPoints.setEnabled(true);
        }
    }

    @Override
    public BaseGame createGame() {
        PenaltyGame game = new PenaltyGame();
        game.setPlayers(getGameSet().getPlayers());
        game.setDeadPlayer(selectorDead.getSelected());
        game.setDealer(selectorDealer.getSelected());
        game.setPenaltedPlayer(selectorDealer.getSelected());
        game.setPenaltyPoints(playerPenaltyPoints * playerMultiplicationRate);
        return game;
    }

    @Override
    public void updateGame(BaseGame game) {
        PenaltyGame penaltyGame = (PenaltyGame) game;
        penaltyGame.setPlayers(new PlayerList(this.inGamePlayers));
        penaltyGame.setDeadPlayer(this.selectorDead.getSelected());
        penaltyGame.setDealer(this.selectorDealer.getSelected());
        penaltyGame.setPenaltedPlayer(this.selectorDealer.getSelected());
        penaltyGame.setPenaltyPoints(this.playerPenaltyPoints * this.playerMultiplicationRate);
    }

    @Override
    public void initializeSpecificViews() {
        intializePenaltyViews();
        initializeDeadAndDealerPanelForPenaltyCase();
    }

    @Override
    public void displayGame() {
        PenaltyGame penaltyGame = (PenaltyGame) game;
        playerPenaltyPoints = penaltyGame.getPenaltyPoints() / playerMultiplicationRate;
        updatePenaltyPointsViews();
    }
}
