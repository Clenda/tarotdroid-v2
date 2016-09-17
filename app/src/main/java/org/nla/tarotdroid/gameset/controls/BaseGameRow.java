/*
	This file is part of the Android application TarotDroid.
 	
	TarotDroid is free software: you can redistribute it and/or modify
 	it under the terms of the GNU General Public License as published by
 	the Free Software Foundation, either version 3 of the License, or
 	(at your option) any later version.
 	
 	TarotDroid is distributed in the hope that it will be useful,
 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 	GNU General Public License for more details.
 	
 	You should have received a copy of the GNU General Public License
 	along with TarotDroid. If not, see <http://www.gnu.org/licenses/>.
*/
package org.nla.tarotdroid.gameset.controls;

import android.app.ProgressDialog;
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
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.constants.ActivityParams;
import org.nla.tarotdroid.constants.RequestCodes;
import org.nla.tarotdroid.core.AppParams;
import org.nla.tarotdroid.gameset.DisplayAndRemoveGameDialogActivity;
import org.nla.tarotdroid.gameset.TabGameSetActivity;

import java.util.Map;

import javax.inject.Inject;

import static com.google.common.collect.Maps.newHashMap;

public abstract class BaseGameRow extends BaseRow implements View.OnLongClickListener {

	private static final int SELECTED_COLOR = Color.LTGRAY;
	protected BaseGame game;
    @Inject AppParams appParams;
	private Map<Integer, Integer> cellColors = newHashMap();
    private boolean isSelected;

	protected BaseGameRow(final Context context, final ProgressDialog dialog, final AttributeSet attrs, final float weight, final GameSet gameSet) {
		super(context, dialog, attrs, weight);
		this.setOrientation(HORIZONTAL);
		this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, weight));
		this.setWeightSum(this.getGameSet().getPlayers().size() + 1);
		this.isSelected = false;
	}
	
	public int getGameIndex() {
		return this.game.getIndex();
	}
	
	@Override
	public final boolean onLongClick(final View v) {
		Intent intent = new Intent(TabGameSetActivity.getInstance(), DisplayAndRemoveGameDialogActivity.class);
		intent.putExtra(ActivityParams.PARAM_GAME_INDEX, BaseGameRow.this.game.getIndex());
		TabGameSetActivity.getInstance().startActivityForResult(intent, RequestCodes.DISPLAY_OR_MODIFY_OR_REMOVE_GAME);
		return true;
	}
	
	 // VERY UGLY CODE: prone to problems in future maintenance...
	 // TODO: Refactor...
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN :
				this.isSelected = true;
				new WaitToHighlightRowTask().execute();
				break;
			case MotionEvent.ACTION_UP :
			case MotionEvent.ACTION_CANCEL :
				this.unHighlightRow();
				this.isSelected = false;
				break;
			default:
				break;
		}

		return super.onTouchEvent(event);
	}
	
	private void highlightRow() {
		if (Build.VERSION.SDK_INT >= 11) {
			this.setBackgroundColor(SELECTED_COLOR);
			for (int i = 0; i < this.getChildCount(); ++i) {
				View view = this.getChildAt(i);
				if (view instanceof TextView) {
					TextView txtView = (TextView) view;
					int txtColor = ((ColorDrawable) txtView.getBackground())
							.getColor();
					this.cellColors.put(i, txtColor);
					txtView.setBackgroundColor(SELECTED_COLOR);
				}
				else if (view instanceof LinearLayout) {
					LinearLayout layoutView = (LinearLayout) view;
					layoutView.setBackgroundColor(SELECTED_COLOR);
					// text
					View txtView = layoutView.getChildAt(0);
					int txtColor = ((ColorDrawable) txtView.getBackground())
							.getColor();
					this.cellColors.put(i, txtColor);
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
			if (this.cellColors.size() == 0) {
				return;
			}
			this.setBackgroundColor(Color.TRANSPARENT);
			for (int i = 0; i < this.getChildCount(); ++i) {
				View view = this.getChildAt(i);
				if (view instanceof TextView) {
					TextView txtView = (TextView) view;
					txtView.setBackgroundColor(this.cellColors.get(i));
				}
				else if (view instanceof LinearLayout) {
					LinearLayout layoutView = (LinearLayout) view;
					layoutView.setBackgroundColor(this.cellColors.get(i));
					// text
					View txtView = layoutView.getChildAt(0);
					txtView.setBackgroundColor(this.cellColors.get(i));
					// image
					View imgView = layoutView.getChildAt(1);
					imgView.setBackgroundColor(this.cellColors.get(i));
				}
			}
			this.cellColors.clear();
		}
	}
	
	private class WaitToHighlightRowTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(40);
			}
			catch (InterruptedException e) {
			}
			return null;
		}
	
		@Override
		protected void onPostExecute(Void result) {
			if (BaseGameRow.this.isSelected) {
				BaseGameRow.this.highlightRow();
			}
		}
	}
}