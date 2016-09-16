package org.nla.tarotdroid.ui;

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
import org.nla.tarotdroid.ui.controls.Selector;

import butterknife.BindView;

import static com.google.common.collect.Lists.newArrayList;

public class BelgianGameActivity extends BaseGameActivity {

    @BindView(R.id.panelFourth) protected RelativeLayout panelFourth;
    @BindView(R.id.panelFifth) protected RelativeLayout panelFifth;
    private Selector<Player> selectorFirst;
    private Selector<Player> selectorSecond;
    private Selector<Player> selectorThird;
    private Selector<Player> selectorFourth;
    private Selector<Player> selectorFifth;

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

    @SuppressWarnings("unchecked")
    private void intializeBelgianViews() {
        this.selectorFirst = (Selector<Player>) findViewById(R.id.galleryFirst);
        this.selectorSecond = (Selector<Player>) findViewById(R.id.gallerySecond);
        this.selectorThird = (Selector<Player>) findViewById(R.id.galleryThird);
        this.selectorFourth = (Selector<Player>) findViewById(R.id.galleryFourth);
        this.selectorFifth = (Selector<Player>) findViewById(R.id.galleryFifth);

        this.selectorFirst.setObjects(this.inGamePlayers);
        this.selectorSecond.setObjects(this.inGamePlayers);
        this.selectorThird.setObjects(this.inGamePlayers);
        this.selectorFourth.setObjects(this.inGamePlayers);
        this.selectorFifth.setObjects(this.inGamePlayers);

        if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot4) {
            this.panelFourth.setVisibility(View.VISIBLE);
        }
        if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
            this.panelFourth.setVisibility(View.VISIBLE);
            this.panelFifth.setVisibility(View.VISIBLE);
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
            this.selectorDead.setObjects(newArrayList(this.getGameSet().getPlayers().getPlayers()));
            this.selectorDealer.setObjects(newArrayList(this.getGameSet()
                                                            .getPlayers()
                                                            .getPlayers()));

            this.selectorDead.setObjectSelectedListener(new Selector.OnObjectSelectedListener<Player>() {

                /* (non-Javadoc)
                 * @see Selector.OnObjectSelectedListener#onItemSelected(Player)
                 */
                @Override
                public void onItemSelected(final Player selected) {

                    // create new ingame player list
                    inGamePlayers = newArrayList(getGameSet().getPlayers().getPlayers());
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

    private boolean isBelgian5GameValid() {
        boolean isValid = this.isBelgian4GameValid();

        // fifth
        isValid = isValid && this.selectorFifth.isSelected();

        // 5 != 1
        isValid = isValid && this.selectorFifth.getSelected() != this.selectorFirst.getSelected();

        // 5 != 2
        isValid = isValid && this.selectorFifth.getSelected() != this.selectorSecond.getSelected();

        // 5 != 3
        isValid = isValid && this.selectorFifth.getSelected() != this.selectorThird.getSelected();

        // 5 != 4
        isValid = isValid && this.selectorFifth.getSelected() != this.selectorFourth.getSelected();

        return isValid;
    }

    /**
     * Tells whether the belgian 4 form is valid.
     *
     * @return
     */
    private boolean isBelgian4GameValid() {
        boolean isValid = this.isBelgian3GameValid();

        // fourth
        isValid = isValid && this.selectorFourth.isSelected();

        // 4 != 1
        isValid = isValid && this.selectorFourth.getSelected() != this.selectorFirst.getSelected();

        // 4 != 2
        isValid = isValid && this.selectorFourth.getSelected() != this.selectorSecond.getSelected();

        // 4 != 3
        isValid = isValid && this.selectorFourth.getSelected() != this.selectorThird.getSelected();

        return isValid;
    }

    private boolean isBelgian3GameValid() {
        boolean isValid = true;

        // first
        isValid = isValid && this.selectorFirst.isSelected();

        // second
        isValid = isValid && this.selectorSecond.isSelected();

        // third
        isValid = isValid && this.selectorThird.isSelected();

        // 1 != 2
        isValid = isValid && this.selectorFirst.getSelected() != this.selectorSecond.getSelected();

        // 1 != 3
        isValid = isValid && this.selectorFirst.getSelected() != this.selectorThird.getSelected();

        // 2 != 3
        isValid = isValid && this.selectorSecond.getSelected() != this.selectorThird.getSelected();

        return isValid;
    }

    @Override
    public BaseGame createGame() {
        switch (this.getGameSet().getGameStyleType()) {
            case Tarot3:
                return this.createBelgian3Game();
            case Tarot4:
                return this.createBelgian4Game();
            case Tarot5:
                return this.createBelgian5Game();
            default:
                throw new RuntimeException("Incorrect game style type");
        }
    }

    private BelgianTarot5Game createBelgian5Game() {
        BelgianTarot5Game game = new BelgianTarot5Game();
        this.setCommonBelgianProperties(game);

        // first
        game.setFirst(this.selectorFirst.getSelected());

        // second
        game.setSecond(this.selectorSecond.getSelected());

        // third
        game.setThird(this.selectorThird.getSelected());

        // fourth
        game.setFourth(this.selectorFourth.getSelected());

        // fifth
        game.setFifth(this.selectorFifth.getSelected());

        return game;
    }

    private BelgianTarot4Game createBelgian4Game() {
        BelgianTarot4Game game = new BelgianTarot4Game();
        this.setCommonBelgianProperties(game);

        // first
        game.setFirst(this.selectorFirst.getSelected());

        // second
        game.setSecond(this.selectorSecond.getSelected());

        // third
        game.setThird(this.selectorThird.getSelected());

        // fourth
        game.setFourth(this.selectorFourth.getSelected());

        return game;
    }

    private BelgianTarot3Game createBelgian3Game() {
        BelgianTarot3Game game = new BelgianTarot3Game();
        this.setCommonBelgianProperties(game);

        // first
        game.setFirst(this.selectorFirst.getSelected());

        // second
        game.setSecond(this.selectorSecond.getSelected());

        // third
        game.setThird(this.selectorThird.getSelected());
        return game;
    }

    @Override
    public void updateGame(final BaseGame game) {
        if (game instanceof BelgianTarot3Game) {
            this.updateBelgian3Game((BelgianTarot3Game) game);
        } else if (game instanceof BelgianTarot4Game) {
            this.updateBelgian4Game((BelgianTarot4Game) game);
        } else if (game instanceof BelgianTarot5Game) {
            this.updateBelgian5Game((BelgianTarot5Game) game);
        }
    }

    @Override
    public void initializeSpecificViews() {
        intializeBelgianViews();
        initializeDeadAndDealerPanelForBelgianCase();
    }

    private void updateBelgian5Game(final BelgianTarot5Game game) {
        this.setCommonBelgianProperties(game);

        // first
        game.setFirst(this.selectorFirst.getSelected());

        // second
        game.setSecond(this.selectorSecond.getSelected());

        // third
        game.setThird(this.selectorThird.getSelected());

        // fourth
        game.setFourth(this.selectorFourth.getSelected());

        // fifth
        game.setFifth(this.selectorFifth.getSelected());
    }

    /**
     * Updates and returns a belgian 4 game.
     *
     * @return
     */
    private void updateBelgian4Game(final BelgianTarot4Game game) {
        this.setCommonBelgianProperties(game);

        // first
        game.setFirst(this.selectorFirst.getSelected());

        // second
        game.setSecond(this.selectorSecond.getSelected());

        // third
        game.setThird(this.selectorThird.getSelected());

        // fourth
        game.setFourth(this.selectorFourth.getSelected());
    }

    private void updateBelgian3Game(final BelgianTarot3Game game) {
        this.setCommonBelgianProperties(game);

        // first
        game.setFirst(this.selectorFirst.getSelected());

        // second
        game.setSecond(this.selectorSecond.getSelected());

        // third
        game.setThird(this.selectorThird.getSelected());
    }

    private void setCommonBelgianProperties(final BaseGame game) {
        if (game == null) {
            throw new IllegalArgumentException("game is null");
        }

        // game players
        game.setPlayers(new PlayerList(this.inGamePlayers));

        // dead player
        if (this.selectorDead.isSelected()) {
            game.setDeadPlayer(this.selectorDead.getSelected());
        }

        // dealer player
        game.setDealer(this.selectorDealer.getSelected());
    }
}
