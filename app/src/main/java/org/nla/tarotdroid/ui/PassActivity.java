package org.nla.tarotdroid.ui;

import android.view.View;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.PassedGame;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.ui.controls.Selector;

import java.util.ArrayList;

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
        return selectorDealer.isSelected();
    }

    @Override
    public BaseGame createGame() {
        PassedGame game = new PassedGame();
        game.setPlayers(new PlayerList(inGamePlayers));
        game.setDeadPlayer(selectorDead.getSelected());
        game.setDealer(selectorDealer.getSelected());
        return game;
    }

    @Override
    public void updateGame(BaseGame game) {
        game.setPlayers(new PlayerList(inGamePlayers));
        game.setDeadPlayer(selectorDead.getSelected());
        game.setDealer(selectorDealer.getSelected());
    }

    @Override
    public void displayGame() {
    }

    @Override
    public void initializeSpecificViews() {
        initializeDeadAndDealerPanelForPassCase();
    }

    private void initializeDeadAndDealerPanelForPassCase() {
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
            selectorDealer.setObjects(new ArrayList<>(getGameSet()
                                                              .getPlayers()
                                                              .getPlayers()));
            selectorDead.setObjectSelectedListener(new Selector.OnObjectSelectedListener<Player>() {

                @Override
                public void onItemSelected(final Player selected) {
                    inGamePlayers = new ArrayList<>(getGameSet().getPlayers().getPlayers());
                    inGamePlayers.remove(selectorDead.getSelected());
                }

                @Override
                public void onNothingSelected() {
                }
            });

            // if no dead player was previously selected, hide dealer and all other panels
            if (!trySetDeadPlayer()) {
                panelDeadAndDealer.setVisibility(View.VISIBLE);
            }
            setDealerPlayer();
        }
    }

    // TODO Implement
    @Override
    protected void auditEvent() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.passed_game_creation;
    }
}
