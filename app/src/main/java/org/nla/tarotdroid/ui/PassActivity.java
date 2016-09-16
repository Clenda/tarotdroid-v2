package org.nla.tarotdroid.ui;

import android.view.View;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.PassedGame;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.ui.controls.Selector;

import static com.google.common.collect.Lists.newArrayList;

public class PassActivity extends BaseGameActivity {

    @Override
    protected String getHelpTitle() {
        return getString(R.string.titleHelpNewPassedGame);
    }

    @Override
    protected String getHelpContent() {
        return getString(R.string.msgHelpNewPassedGame);
    }

    @Override
    public boolean isFormValid() {
        return this.selectorDealer.isSelected();
    }

    private void initializeDeadAndDealerPanelForPassCase() {
        // if no dead player, case is easy...
        if (!this.isDisplayDeadPlayerPanel()) {
            this.panelDead.setVisibility(View.GONE);
            this.setDealerPlayer();
            return;
        }

        // if not, it becomes more complicated...
        else {
            // load dead player selector with all players
            this.selectorDead.setObjects(newArrayList(this.getGameSet().getPlayers().getPlayers()));
            this.selectorDealer.setObjects(newArrayList(this.getGameSet()
                                                            .getPlayers()
                                                            .getPlayers()));
            this.selectorDead.setObjectSelectedListener(new Selector.OnObjectSelectedListener<Player>() {

                @Override
                public void onItemSelected(final Player selected) {

                    // create new ingame player list
                    inGamePlayers = newArrayList(getGameSet().getPlayers().getPlayers());
                    inGamePlayers.remove(selectorDead.getSelected());
                }

                @Override
                public void onNothingSelected() {
                }
            });

            // if no dead player was previously selected, hide dealer and all other panels
            if (!this.trySetDeadPlayer()) {
                panelDeadAndDealer.setVisibility(View.VISIBLE);
            }
            this.setDealerPlayer();
        }
    }

    @Override
    public BaseGame createGame() {
        PassedGame game = new PassedGame();
        game.setPlayers(new PlayerList(this.inGamePlayers));
        game.setDeadPlayer(this.selectorDead.getSelected());
        game.setDealer(this.selectorDealer.getSelected());
        return game;
    }

    @Override
    public void updateGame(BaseGame game) {
        game.setPlayers(new PlayerList(this.inGamePlayers));
        game.setDeadPlayer(this.selectorDead.getSelected());
        game.setDealer(this.selectorDealer.getSelected());
    }

    @Override
    public void initializeSpecificViews() {
        initializeDeadAndDealerPanelForPassCase();
    }

    // TODO Implement
    @Override
    protected void auditEvent() {
    }

    @Override
    public void displayGame() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.passed_game_creation;
    }
}
