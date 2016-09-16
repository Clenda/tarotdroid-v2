package org.nla.tarotdroid.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.nla.tarotdroid.AppContext;
import org.nla.tarotdroid.R;
import org.nla.tarotdroid.TarotDroidApp;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.PersistableBusinessObject;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.dal.DalException;
import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.UIHelper;
import org.nla.tarotdroid.ui.constants.ActivityParams;
import org.nla.tarotdroid.ui.constants.ResultCodes;
import org.nla.tarotdroid.ui.controls.Selector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.google.common.base.Preconditions.checkArgument;

public abstract class BaseGameActivity extends BaseActivity {

    @Nullable @BindView(R.id.panelMainParameters) protected LinearLayout panelMainParameters;
    @Nullable @BindView(R.id.txtTitleMainParameters) protected TextView txtTitleMainParameters;
    @Nullable @BindView(R.id.panelDead) protected RelativeLayout panelDead;
    @Nullable @BindView(R.id.panelDeadAndDealer) protected LinearLayout panelDeadAndDealer;
    @Nullable @BindView(R.id.panelDealer) protected RelativeLayout panelDealer;
    @Nullable @BindView(R.id.panelDeadAndDealerSection)
    protected LinearLayout panelDeadAndDealerSection;
    protected Selector<Player> selectorDead;
    protected Selector<Player> selectorDealer;

    protected OnClickListener onNoDeadPlayerSelectedClickListener = new OnClickListener() {
        @Override
        public void onClick(final View layoutView) {
            Toast.makeText(
                    layoutView.getContext(),
                    BaseGameActivity.this.getString(R.string.msgSelectDeadPlayer),
                    Toast.LENGTH_LONG
            ).show();
        }
    };

    //    protected GameType gameType;
    protected List<Player> allPlayers;
    protected List<Player> inGamePlayers;
    protected BaseGame game;
    protected boolean isInEditMode;

    protected GameSet getGameSet() {
        return TabGameSetActivity.getInstance().gameSet;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
//        identifyGameType();
        super.onCreate(savedInstanceState);
        try {
            List<Player> players = this.getGameSet().getPlayers().getPlayers();
            allPlayers = new ArrayList<>(players);
            inGamePlayers = new ArrayList<>(players);
            isInEditMode = false;

            // get dead and dealer widgets
            selectorDead = (Selector<Player>) findViewById(R.id.galleryDead);
            selectorDead.setObjects(allPlayers);
            selectorDealer = (Selector<Player>) findViewById(R.id.galleryDealer);
            selectorDealer.setObjects(allPlayers);

            // initialize views
            initializeCommonViews();
            initializeSpecificViews();

            // if the activity is in edit mode, get the game and display its values
            if (getIntent().hasExtra(ActivityParams.PARAM_GAME_INDEX)) {
                game = getGameSet().getGameOfIndex(getIntent().getExtras()
                                                              .getInt(ActivityParams.PARAM_GAME_INDEX));
                isInEditMode = true;

                // display game
                displayDeadAndDealer();
                displayGame();
            }
        } catch (Exception e) {
            AuditHelper.auditError(AuditHelper.ErrorTypes.gameCreationActivityError, e, this);
        }
    }

    private void initializeCommonViews() {
        TextView txtTitleDeadAndDealer = (TextView) findViewById(R.id.txtTitleDeadAndDealer);
        if (isDisplayDeadPlayerPanel()) {
            txtTitleDeadAndDealer.setText(R.string.lblDeadAndDealerSection);
        } else {
            txtTitleDeadAndDealer.setText(R.string.lblDealerSection);
        }
    }

    @Override
    protected void inject() {
        TarotDroidApp.get(this).getComponent().inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem miSaveGame = menu.add(R.string.lblValidateGame);
        miSaveGame.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        miSaveGame.setIcon(R.drawable.ic_compose);
        miSaveGame.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            /* (non-Javadoc)
             * @see com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener#onMenuItemClick(com.actionbarsherlock.view.MenuItem)
             */
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (!isFormValid()) {
                    Toast.makeText(
                            BaseGameActivity.this,
                            getString(R.string.msgValidationKo),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    if (isInEditMode) {
                        updateGame(game);
                        new PersistGameTask(BaseGameActivity.this).execute(game);
                    } else {
                        new PersistGameTask(BaseGameActivity.this).execute(createGame());
                    }
                }
                return true;
            }
        });

        MenuItem miInfo = menu.add(R.string.lblHelpItem);
        miInfo.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        miInfo.setIcon(R.drawable.gd_action_bar_info);
        miInfo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            /* (non-Javadoc)
             * @see com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener#onMenuItemClick(com.actionbarsherlock.view.MenuItem)
             */
            @Override
            public boolean onMenuItemClick(MenuItem item) {

//                // identify content to display
//                String content = null;
//                switch (GameCreationActivity.this.getGameSet().getGameStyleType()) {
//                    case Tarot3:
//                        switch (gameType) {
//                            case Belgian:
//                                content = String.format(
//                                        GameCreationActivity.this.getText(R.string.msgHelpNewBelgian3Game)
//                                                                 .toString(),
//                                        GameCreationActivity.this.getGameSet()
//                                                                 .getGameSetParameters()
//                                                                 .getBelgianBaseStepPoints()
//                                );
//                                break;
//
//                            case Standard:
//                                content = GameCreationActivity.this.getText(R.string.msgHelpNewStd3Game)
//                                                                   .toString();
//                            default:
//                                break;
//                        }
//                        break;
//
//                    case Tarot5:
//                        switch (gameType) {
//                            case Belgian:
//                                content = String.format(
//                                        GameCreationActivity.this.getText(R.string.msgHelpNewBelgian5Game)
//                                                                 .toString(),
//                                        GameCreationActivity.this.getGameSet()
//                                                                 .getGameSetParameters()
//                                                                 .getBelgianBaseStepPoints(),
//                                        GameCreationActivity.this.getGameSet()
//                                                                 .getGameSetParameters()
//                                                                 .getBelgianBaseStepPoints() * 2
//                                );
//                                break;
//
//                            case Standard:
//                                content = GameCreationActivity.this.getText(R.string.msgHelpNewStd5Game)
//                                                                   .toString();
//                            default:
//                                break;
//                        }
//                        break;
//
//                    case Tarot4:
//                    default:
//                        switch (gameType) {
//                            case Belgian:
//                                content = String.format(
//                                        GameCreationActivity.this.getText(R.string.msgHelpNewBelgian4Game)
//                                                                 .toString(),
//                                        GameCreationActivity.this.getGameSet()
//                                                                 .getGameSetParameters()
//                                                                 .getBelgianBaseStepPoints(),
//                                        GameCreationActivity.this.getGameSet()
//                                                                 .getGameSetParameters()
//                                                                 .getBelgianBaseStepPoints() * 2
//                                );
//                                break;
//
//                            case Standard:
//                                content = GameCreationActivity.this.getText(R.string.msgHelpNewStd4Game)
//                                                                   .toString();
//                            default:
//                                break;
//                        }
//                        break;
//                }
//
//                // identify title to display
//                String title = null;
//                switch (GameCreationActivity.this.getGameSet().getGameStyleType()) {
//                    case Tarot3:
//                        switch (gameType) {
//                            case Belgian:
//                                title = GameCreationActivity.this.getString(R.string.titleHelpNewBelgian3Game);
//                                break;
//
//                            case Standard:
//                                title = GameCreationActivity.this.getString(R.string.titleHelpNewStd3Game);
//                            default:
//                                break;
//                        }
//                        break;
//
//                    case Tarot5:
//                        switch (gameType) {
//                            case Belgian:
//                                title = GameCreationActivity.this.getString(R.string.titleHelpNewBelgian5Game);
//                                break;
//
//                            case Standard:
//                                title = GameCreationActivity.this.getString(R.string.titleHelpNewStd5Game);
//                            default:
//                                break;
//                        }
//                        break;
//
//                    case Tarot4:
//                    default:
//                        switch (gameType) {
//                            case Belgian:
//                                title = GameCreationActivity.this.getString(R.string.titleHelpNewBelgian4Game);
//                                break;
//
//                            case Standard:
//                                title = GameCreationActivity.this.getString(R.string.titleHelpNewStd4Game);
//                            default:
//                                break;
//                        }
//                        break;
//                }
//
//                if (title == null) {
//                    switch (gameType) {
//                        case Pass:
//                            title = GameCreationActivity.this.getString(R.string.titleHelpNewPassedGame);
//                            content = GameCreationActivity.this.getString(R.string.msgHelpNewPassedGame);
//                            break;
//                        case Penalty:
//                            title = GameCreationActivity.this.getString(R.string.titleHelpNewPenaltyGame);
//                            content = GameCreationActivity.this.getString(R.string.msgHelpNewPenaltyGame);
//                            break;
//                    }
//                }

                String title = getHelpTitle();
                String content = getHelpContent();

                // display help only if messages are set
                if (title != null && content != null) {
                    UIHelper.showSimpleRichTextDialog(
                            BaseGameActivity.this,
                            content,
                            title
                    );
                }

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    protected abstract String getHelpTitle();

    protected abstract String getHelpContent();

    protected void setDealerPlayer() {
        if (this.getGameSet().getGameCount() > 0) {
            PersistableBusinessObject formerDealerPlayer = this.getGameSet()
                                                               .getLastGame()
                                                               .getDealer();
            if (formerDealerPlayer != null) {
                this.selectorDealer.setSelected(this.getGameSet()
                                                    .getPlayers()
                                                    .getNextPlayer(formerDealerPlayer));
            }
        } else {
            this.selectorDealer.setSelected(this.getGameSet().getPlayers().get(1));
            this.panelDeadAndDealer.setVisibility(View.VISIBLE);
        }
    }

    protected boolean trySetDeadPlayer() {
        if (this.getGameSet().getGameCount() > 0) {
            PersistableBusinessObject formerDeadPlayer = this.getGameSet()
                                                             .getLastGame()
                                                             .getDeadPlayer();
            if (formerDeadPlayer != null) {
                this.selectorDead.setSelected(this.getGameSet()
                                                  .getPlayers()
                                                  .getNextPlayer(formerDeadPlayer));
                return true;
            }
        }
        return false;
    }

//    private void identifyGameType() {
//        if (getIntent().getExtras() != null && !getIntent().getExtras()
//                                                           .containsKey(ActivityParams.PARAM_TYPE_OF_GAME)) {
//            throw new IllegalArgumentException("type of game must be provided");
//        }
//        gameType = GameType.valueOf(getIntent().getExtras()
//                                               .getString(ActivityParams.PARAM_TYPE_OF_GAME));
//    }

    protected boolean isDisplayDeadPlayerPanel() {
        if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot5 && this.getGameSet()
                                                                                .getPlayers()
                                                                                .size() == 5) {
            return false;
        }
        if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot4 && this.getGameSet()
                                                                                .getPlayers()
                                                                                .size() == 4) {
            return false;
        }
        return !(this.getGameSet().getGameStyleType() == GameStyleType.Tarot3 && this.getGameSet()
                                                                                     .getPlayers()
                                                                                     .size() == 3);
    }

    public abstract boolean isFormValid();

    public abstract BaseGame createGame();

    public abstract void updateGame(final BaseGame game);

    public abstract void initializeSpecificViews();

    public abstract void displayGame();

    private void displayDeadAndDealer() {

        // initialize dealer player widgets
        if (game.getDealer() != null) {
            panelDealer.setVisibility(View.VISIBLE);
            selectorDealer = (Selector<Player>) findViewById(R.id.galleryDealer);
            selectorDealer.setObjects(getGameSet().getPlayers().getPlayers());
            selectorDealer.setSelected(game.getDealer());
        } else {
            panelDealer.setVisibility(View.GONE);
        }

        // initialize dead player widgets
        if (game.getDeadPlayer() != null) {
            panelDead.setVisibility(View.VISIBLE);
            selectorDead = (Selector<Player>) findViewById(R.id.galleryDead);
            selectorDead.setObjects(getGameSet().getPlayers().getPlayers());
            selectorDead.setSelected(game.getDeadPlayer());
        } else {
            panelDead.setVisibility(View.GONE);
        }

        // if neither dealer nor dead players are set, don't display the section
        if (game.getDealer() == null && game.getDeadPlayer() == null) {
            panelDeadAndDealerSection.setVisibility(View.GONE);
        }
    }

//    public enum GameType {
//        Standard,
//        Belgian,
//        Penalty,
//        Pass
//    }

    private class PersistGameTask extends AsyncTask<BaseGame, Void, Void> {
        private final ProgressDialog dialog;
        private Context context;
        private boolean backgroundErrorHappened;
        private Exception backgroundException;

        protected PersistGameTask(final Context context) {
            checkArgument(context != null, "context is null");
            this.context = context;
            dialog = new ProgressDialog(context);
            backgroundErrorHappened = false;
        }

        @Override
        protected void onPreExecute() {
            String message = isInEditMode
                    ? context.getResources().getString(R.string.msgGameUpdate)
                    : context.getResources().getString(R.string.msgGameCreation);
            dialog.setMessage(message);
            dialog.show();
        }

        @Override
        protected Void doInBackground(final BaseGame... games) {
            try {
                BaseGame game = games[0];

                // modify the game and the subsequent games
                if (isInEditMode) {
                    List<BaseGame> removedGames = BaseGameActivity.this.getGameSet()
                                                                       .removeGameAndAllSubsequentGames(
                                                                               game);
                    for (int i = removedGames.size(); i > 0; --i) {
                        BaseGame toReInsert = removedGames.get(i - 1);
                        BaseGameActivity.this.getGameSet().addGame(toReInsert);
                    }
                }
                // add the game
                else {
                    BaseGameActivity.this.getGameSet().addGame(game);
                }

                // persist the game only if the gameset was previously persisted
                if (BaseGameActivity.this.getGameSet().isPersisted()) {

                    // update the game
                    if (isInEditMode) {
                        AppContext.getApplication()
                                  .getDalService()
                                  .updateGame(game, BaseGameActivity.this.getGameSet());
                    }
                    // create the game
                    else {
                        AppContext.getApplication()
                                  .getDalService()
                                  .saveGame(game, BaseGameActivity.this.getGameSet());
                    }
                }
            } catch (DalException e) {
                backgroundException = e;
                backgroundErrorHappened = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void unused) {
            // hide busy idicator
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            // display toast if error happened
            if (backgroundErrorHappened) {
                AuditHelper.auditError(AuditHelper.ErrorTypes.persistGameTaskError,
                                       backgroundException,
                                       BaseGameActivity.this);
            } else {
                String message = context.getResources().getString(R.string.msgGameAdded);
                if (isInEditMode) {
                    message = context.getResources().getString(R.string.msgGameUpdated);
                }

                // display toast
                Toast.makeText(
                        context,
                        message,
                        Toast.LENGTH_SHORT
                ).show();
            }

            setResult(ResultCodes.AddGame_Ok);
            finish();
        }
    }

    protected class ScaleAnimToHide extends ScaleAnimation {
        private View mView;
        private LayoutParams mLayoutParams;
        private int mMarginBottomFromY, mMarginBottomToY;
        private boolean mVanishAfter = false;

        public ScaleAnimToHide(
                float fromX,
                float toX,
                float fromY,
                float toY,
                int duration,
                View view,
                boolean vanishAfter
        ) {
            super(fromX, toX, fromY, toY);
            setDuration(duration);
            mView = view;
            mVanishAfter = vanishAfter;
            mLayoutParams = (LayoutParams) view.getLayoutParams();
            int height = mView.getHeight();
            mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin - height;
            mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) - height;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                int newMarginBottom = mMarginBottomFromY + (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime);
                mLayoutParams.setMargins(mLayoutParams.leftMargin,
                                         mLayoutParams.topMargin,
                                         mLayoutParams.rightMargin,
                                         newMarginBottom);
                mView.getParent().requestLayout();
            } else if (mVanishAfter) {
                mView.setVisibility(View.GONE);
            }
        }
    }

    protected class ScaleAnimToShow extends ScaleAnimation {
        private View mView;
        private LayoutParams mLayoutParams;
        private int mMarginBottomFromY, mMarginBottomToY;

        public ScaleAnimToShow(
                float toX,
                float fromX,
                float toY,
                float fromY,
                int duration,
                View view,
                boolean vanishAfter
        ) {
            super(fromX, toX, fromY, toY);
            setDuration(duration);
            mView = view;
            mLayoutParams = (LayoutParams) view.getLayoutParams();
            mView.setVisibility(View.VISIBLE);
            int height = mView.getHeight();
            mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin + height;
            mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) + height;

            mMarginBottomFromY = 0;
            mMarginBottomToY = height;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                int newMarginBottom = (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime) - mMarginBottomToY;
                mLayoutParams.setMargins(mLayoutParams.leftMargin,
                                         mLayoutParams.topMargin,
                                         mLayoutParams.rightMargin,
                                         newMarginBottom);
                mView.getParent().requestLayout();
            }
        }
    }
}