package org.nla.tarotdroid.gameset.controls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.constants.ActivityParams;
import org.nla.tarotdroid.constants.RequestCodes;
import org.nla.tarotdroid.gameset.DisplayAndRemoveGameDialogActivity;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseGameRow extends BaseRow implements View.OnLongClickListener {

    private static final int SELECTED_COLOR = Color.LTGRAY;
    protected BaseGame game;

    private Map<Integer, Integer> cellColors = new HashMap<>();
    private boolean isSelected;

    protected BaseGameRow(
            final Context context,
            final AttributeSet attrs,
            final float weight
    ) {
        super(context, attrs, weight);
        isSelected = false;
    }

    protected int getGameIndex() {
        return game.getIndex();
    }

    @Override
    public final boolean onLongClick(final View v) {
        Intent intent = new Intent(getContext(), DisplayAndRemoveGameDialogActivity.class);
        intent.putExtra(ActivityParams.PARAM_GAME_INDEX, game.getIndex());
        ((Activity) getContext()).startActivityForResult(intent,
                                                         RequestCodes.DISPLAY_OR_MODIFY_OR_REMOVE_GAME);
        return true;
    }

    // VERY UGLY CODE: prone to problems in future maintenance...
    // TODO: Refactor...
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isSelected = true;
                new WaitToHighlightRowTask().execute();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                unHighlightRow();
                isSelected = false;
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    private void highlightRow() {
        if (Build.VERSION.SDK_INT >= 11) {
            setBackgroundColor(SELECTED_COLOR);
            for (int i = 0; i < getChildCount(); ++i) {
                View view = getChildAt(i);
                if (view instanceof TextView) {
                    TextView txtView = (TextView) view;
                    int txtColor = ((ColorDrawable) txtView.getBackground())
                            .getColor();
                    cellColors.put(i, txtColor);
                    txtView.setBackgroundColor(SELECTED_COLOR);
                } else if (view instanceof LinearLayout) {
                    LinearLayout layoutView = (LinearLayout) view;
                    layoutView.setBackgroundColor(SELECTED_COLOR);
                    // text
                    View txtView = layoutView.getChildAt(0);
                    int txtColor = ((ColorDrawable) txtView.getBackground())
                            .getColor();
                    cellColors.put(i, txtColor);
                    txtView.setBackgroundColor(SELECTED_COLOR);
                    // image
                    View imgView = layoutView.getChildAt(1);
                    imgView.setBackgroundColor(SELECTED_COLOR);
                }
            }
        }
    }

    private void unHighlightRow() {
        if (Build.VERSION.SDK_INT >= 11) {
            if (cellColors.size() == 0) {
                return;
            }
            setBackgroundColor(Color.TRANSPARENT);
            for (int i = 0; i < getChildCount(); ++i) {
                View view = getChildAt(i);
                if (view instanceof TextView) {
                    TextView txtView = (TextView) view;
                    txtView.setBackgroundColor(cellColors.get(i));
                } else if (view instanceof LinearLayout) {
                    LinearLayout layoutView = (LinearLayout) view;
                    layoutView.setBackgroundColor(cellColors.get(i));
                    // text
                    View txtView = layoutView.getChildAt(0);
                    txtView.setBackgroundColor(cellColors.get(i));
                    // image
                    View imgView = layoutView.getChildAt(1);
                    imgView.setBackgroundColor(cellColors.get(i));
                }
            }
            cellColors.clear();
        }
    }

    private class WaitToHighlightRowTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (isSelected) {
                highlightRow();
            }
        }
    }
}