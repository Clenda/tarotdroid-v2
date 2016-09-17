package org.nla.tarotdroid.gameset;

import android.view.View;
import android.widget.RelativeLayout;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.BelgianTarot3Game;
import org.nla.tarotdroid.biz.BelgianTarot4Game;
import org.nla.tarotdroid.biz.BelgianTarot5Game;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.gameset.controls.Selector;

import java.util.ArrayList;

import butterknife.BindView;

public class BelgianGameActivity extends BaseGameActivity {

    @BindView(R.id.panelFourth) protected RelativeLayout panelFourth;
    @BindView(R.id.panelFifth) protected RelativeLayout panelFifth;
    @BindView(R.id.galleryFirst) protected Selector<Player> selectorFirst;
    @BindView(R.id.gallerySecond) protected Selector<Player> selectorSecond;
    @BindView(R.id.galleryThird) protected Selector<Player> selectorThird;
    @BindView(R.id.galleryFourth) protected Selector<Player> selectorFourth;
    @BindView(R.id.galleryFifth) protected Selector<Player> selectorFifth;

    @Override
    protected int getLayoutResId() {
        return R.layout.belgian_game_creation;
    }

    @Override
    protected void setTitle() {
        if (isInEditMode) {
            setTitle(R.string.lblUpdateGameTitle);
        } else {
            setTitle(R.string.lblNewBelgianGameTitle);
        }
    }

    // TODO Implement
    @Override
    protected void auditEvent() {

    }

    @Override
    protected String getHelpContent() {
        switch (getGameSet().getGameStyleType()) {
            case Tarot3:

                return String.format(
                        getText(R.string.msgHelpNewBelgian3Game)
                                .toString(),
                        getGameSet()
                                .getGameSetParameters()
                                .getBelgianBaseStepPoints());

            case Tarot5:
                return String.format(
                        getText(R.string.msgHelpNewBelgian5Game)
                                .toString(),
                        getGameSet()
                                .getGameSetParameters()
                                .getBelgianBaseStepPoints(),
                        getGameSet()
                                .getGameSetParameters()
                                .getBelgianBaseStepPoints() * 2
                );
            case Tarot4:
            default:
                return String.format(
                        getText(R.string.msgHelpNewBelgian4Game).toString(),
                        getGameSet().getGameSetParameters().getBelgianBaseStepPoints(),
                        getGameSet().getGameSetParameters().getBelgianBaseStepPoints() * 2
                );
        }
    }

    @Override
    protected String getHelpTitle() {
        switch (getGameSet().getGameStyleType()) {
            case Tarot3:
                return getString(R.string.titleHelpNewBelgian3Game);
            case Tarot5:
                return getString(R.string.titleHelpNewBelgian5Game);
            case Tarot4:
            default:
                return getString(R.string.titleHelpNewBelgian4Game);
        }
    }

    @Override
    public boolean isFormValid() {
        switch (getGameSet().getGameStyleType()) {
            case Tarot3:
                return isBelgian3GameValid();
            case Tarot4:
                return isBelgian4GameValid();
            case Tarot5:
                return isBelgian5GameValid();
            default:
                throw new RuntimeException("Incorrect game style type");
        }
    }

    private boolean isBelgian5GameValid() {
        boolean isValid = isBelgian4GameValid();

        // fifth
        isValid = isValid && selectorFifth.isSelected();

        // 5 != 1
        isValid = isValid && selectorFifth.getSelected() != selectorFirst.getSelected();

        // 5 != 2
        isValid = isValid && selectorFifth.getSelected() != selectorSecond.getSelected();

        // 5 != 3
        isValid = isValid && selectorFifth.getSelected() != selectorThird.getSelected();

        // 5 != 4
        isValid = isValid && selectorFifth.getSelected() != selectorFourth.getSelected();

        return isValid;
    }

    private boolean isBelgian4GameValid() {
        boolean isValid = isBelgian3GameValid();

        // fourth
        isValid = isValid && selectorFourth.isSelected();

        // 4 != 1
        isValid = isValid && selectorFourth.getSelected() != selectorFirst.getSelected();

        // 4 != 2
        isValid = isValid && selectorFourth.getSelected() != selectorSecond.getSelected();

        // 4 != 3
        isValid = isValid && selectorFourth.getSelected() != selectorThird.getSelected();

        return isValid;
    }

    private boolean isBelgian3GameValid() {
        boolean isValid = true;

        // first
        isValid = isValid && selectorFirst.isSelected();

        // second
        isValid = isValid && selectorSecond.isSelected();

        // third
        isValid = isValid && selectorThird.isSelected();

        // 1 != 2
        isValid = isValid && selectorFirst.getSelected() != selectorSecond.getSelected();

        // 1 != 3
        isValid = isValid && selectorFirst.getSelected() != selectorThird.getSelected();

        // 2 != 3
        isValid = isValid && selectorSecond.getSelected() != selectorThird.getSelected();

        return isValid;
    }

    @Override
    public void displayGame() {
        if (game instanceof BelgianTarot3Game) {
            BelgianTarot3Game belgianGame = (BelgianTarot3Game) game;
            selectorFirst.setSelected(belgianGame.getFirst());
            selectorSecond.setSelected(belgianGame.getSecond());
            selectorThird.setSelected(belgianGame.getThird());
        } else if (game instanceof BelgianTarot4Game) {
            BelgianTarot4Game belgianGame = (BelgianTarot4Game) game;
            selectorFirst.setSelected(belgianGame.getFirst());
            selectorSecond.setSelected(belgianGame.getSecond());
            selectorThird.setSelected(belgianGame.getThird());
            selectorFourth.setSelected(belgianGame.getFourth());
        } else if (game instanceof BelgianTarot5Game) {
            BelgianTarot5Game belgianGame = (BelgianTarot5Game) game;
            selectorFirst.setSelected(belgianGame.getFirst());
            selectorSecond.setSelected(belgianGame.getSecond());
            selectorThird.setSelected(belgianGame.getThird());
            selectorFourth.setSelected(belgianGame.getFourth());
            selectorFifth.setSelected(belgianGame.getFifth());
        }
    }

    @Override
    public void initializeSpecificViews() {
        intializeBelgianViews();
        initializeDeadAndDealerPanelForBelgianCase();
    }

    private void intializeBelgianViews() {
        selectorFirst.setObjects(inGamePlayers);
        selectorSecond.setObjects(inGamePlayers);
        selectorThird.setObjects(inGamePlayers);
        selectorFourth.setObjects(inGamePlayers);
        selectorFifth.setObjects(inGamePlayers);

        if (getGameSet().getGameStyleType() == GameStyleType.Tarot4) {
            panelFourth.setVisibility(View.VISIBLE);
        }
        if (getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
            panelFourth.setVisibility(View.VISIBLE);
            panelFifth.setVisibility(View.VISIBLE);
        }
    }

    private void initializeDeadAndDealerPanelForBelgianCase() {
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

                    // create new ingame player list
                    inGamePlayers = new ArrayList<>(getGameSet().getPlayers().getPlayers());
                    inGamePlayers.remove(selectorDead.getSelected());

                    // load player selector with this new player list
                    selectorFirst.setObjects(inGamePlayers);
                    selectorSecond.setObjects(inGamePlayers);
                    selectorThird.setObjects(inGamePlayers);
                    selectorFourth.setObjects(inGamePlayers);
                    selectorFifth.setObjects(inGamePlayers);

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
            if (!trySetDeadPlayer()) {
                panelDeadAndDealer.setVisibility(View.VISIBLE);
                panelMainParameters.setVisibility(View.GONE);
                txtTitleMainParameters.setOnClickListener(onNoDeadPlayerSelectedClickListener);
            }
            setDealerPlayer();
        }
    }

    @Override
    public BaseGame createGame() {
        switch (getGameSet().getGameStyleType()) {
            case Tarot3:
                return createBelgian3Game();
            case Tarot4:
                return createBelgian4Game();
            case Tarot5:
                return createBelgian5Game();
            default:
                throw new RuntimeException("Incorrect game style type");
        }
    }

    private BelgianTarot5Game createBelgian5Game() {
        BelgianTarot5Game game = new BelgianTarot5Game();
        setCommonBelgianProperties(game);

        // first
        game.setFirst(selectorFirst.getSelected());

        // second
        game.setSecond(selectorSecond.getSelected());

        // third
        game.setThird(selectorThird.getSelected());

        // fourth
        game.setFourth(selectorFourth.getSelected());

        // fifth
        game.setFifth(selectorFifth.getSelected());

        return game;
    }

    private BelgianTarot4Game createBelgian4Game() {
        BelgianTarot4Game game = new BelgianTarot4Game();
        setCommonBelgianProperties(game);

        // first
        game.setFirst(selectorFirst.getSelected());

        // second
        game.setSecond(selectorSecond.getSelected());

        // third
        game.setThird(selectorThird.getSelected());

        // fourth
        game.setFourth(selectorFourth.getSelected());

        return game;
    }

    private BelgianTarot3Game createBelgian3Game() {
        BelgianTarot3Game game = new BelgianTarot3Game();
        setCommonBelgianProperties(game);

        // first
        game.setFirst(selectorFirst.getSelected());

        // second
        game.setSecond(selectorSecond.getSelected());

        // third
        game.setThird(selectorThird.getSelected());
        return game;
    }

    @Override
    public void updateGame(final BaseGame game) {
        if (game instanceof BelgianTarot3Game) {
            updateBelgian3Game((BelgianTarot3Game) game);
        } else if (game instanceof BelgianTarot4Game) {
            updateBelgian4Game((BelgianTarot4Game) game);
        } else if (game instanceof BelgianTarot5Game) {
            updateBelgian5Game((BelgianTarot5Game) game);
        }
    }

    private void updateBelgian5Game(final BelgianTarot5Game game) {
        setCommonBelgianProperties(game);

        // first
        game.setFirst(selectorFirst.getSelected());

        // second
        game.setSecond(selectorSecond.getSelected());

        // third
        game.setThird(selectorThird.getSelected());

        // fourth
        game.setFourth(selectorFourth.getSelected());

        // fifth
        game.setFifth(selectorFifth.getSelected());
    }

    private void updateBelgian4Game(final BelgianTarot4Game game) {
        setCommonBelgianProperties(game);

        // first
        game.setFirst(selectorFirst.getSelected());

        // second
        game.setSecond(selectorSecond.getSelected());

        // third
        game.setThird(selectorThird.getSelected());

        // fourth
        game.setFourth(selectorFourth.getSelected());
    }

    private void updateBelgian3Game(final BelgianTarot3Game game) {
        setCommonBelgianProperties(game);

        // first
        game.setFirst(selectorFirst.getSelected());

        // second
        game.setSecond(selectorSecond.getSelected());

        // third
        game.setThird(selectorThird.getSelected());
    }

    private void setCommonBelgianProperties(final BaseGame game) {
        if (game == null) {
            throw new IllegalArgumentException("game is null");
        }

        // game players
        game.setPlayers(new PlayerList(inGamePlayers));

        // dead player
        if (selectorDead.isSelected()) {
            game.setDeadPlayer(selectorDead.getSelected());
        }

        // dealer player
        game.setDealer(selectorDealer.getSelected());
    }
}
