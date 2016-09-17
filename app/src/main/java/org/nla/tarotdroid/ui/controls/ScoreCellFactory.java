package org.nla.tarotdroid.ui.controls;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.nla.tarotdroid.R;
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.biz.enums.KingType;
import org.nla.tarotdroid.helpers.LocalizationHelper;
import org.nla.tarotdroid.ui.constants.UIConstants;

public final class ScoreCellFactory {

	private ScoreCellFactory() {
	}
    
	protected static View buildStandardGameDescription(
			final Context context,
			final BetType bet,
			final int points,
			final int gameIndex,
			final LocalizationHelper localizationHelper
	) {
		TextView txtBet = new TextView(context);
		txtBet.setGravity(Gravity.LEFT);
		txtBet.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		txtBet.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		txtBet.setBackgroundColor(Color.TRANSPARENT);
		txtBet.setSingleLine();
		txtBet.setTypeface(null, Typeface.BOLD);
		txtBet.setTextColor(Color.WHITE);
		txtBet.setEllipsize(TruncateAt.END);
		
		txtBet.setText(
				String.format(
						context.getResources().getString(R.string.lblStandardGameSynthesis),
						Integer.toString(gameIndex),
						localizationHelper.getShortBetTranslation(bet),
						(points >= 0 ? "+" + points : Integer.toString(points))
				)
		);
		return txtBet;
	}
	
	protected static View buildBelgianGameDescription(final Context context, final int gameIndex) {
		TextView txtBet = new TextView(context);
		txtBet.setGravity(Gravity.LEFT);
		txtBet.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		txtBet.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		txtBet.setBackgroundColor(Color.TRANSPARENT);
		txtBet.setSingleLine();
		txtBet.setTypeface(null, Typeface.BOLD);
		txtBet.setTextColor(Color.WHITE);
		txtBet.setEllipsize(TruncateAt.END);
		txtBet.setText(
				String.format(
						context.getResources().getString(R.string.lblBelgianGameSynthesis),
						Integer.toString(gameIndex),
						context.getResources().getString(R.string.belgeDescription)
				)
		);
		
		return txtBet;
	}
	
	protected static View buildNextGameDescription(final Context context) {
		TextView txtBet = new TextView(context);
		txtBet.setGravity(Gravity.LEFT);
		txtBet.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		txtBet.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		txtBet.setBackgroundColor(Color.TRANSPARENT);
		txtBet.setSingleLine();
		txtBet.setTypeface(null, Typeface.BOLD);
		txtBet.setTextColor(Color.WHITE);
		txtBet.setEllipsize(TruncateAt.END);
		txtBet.setText("Next");
		
		return txtBet;
	}
	
	protected static View buildPenaltyGameDescription(final Context context, final int gameIndex) {
		TextView txtBet = new TextView(context);
		txtBet.setGravity(Gravity.LEFT);
		txtBet.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		txtBet.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		txtBet.setBackgroundColor(Color.TRANSPARENT);
		txtBet.setSingleLine();
		txtBet.setTypeface(null, Typeface.BOLD);
		txtBet.setTextColor(Color.WHITE);
		txtBet.setEllipsize(TruncateAt.END);
//		txtBet.setText(context.getResources().getString(R.string.penaltyDescription));
		txtBet.setText(
				String.format(
						context.getResources().getString(R.string.lblPenaltyGameSynthesis),
						Integer.toString(gameIndex),
						context.getResources().getString(R.string.shortPenaltyDescription)
			)
		);
		
		return txtBet;
	}
	
	protected static View buildPassedGameDescription(final Context context, final int gameIndex) {
		TextView txtBet = new TextView(context);
		txtBet.setGravity(Gravity.LEFT);
		txtBet.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		txtBet.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		txtBet.setBackgroundColor(Color.TRANSPARENT);
		txtBet.setSingleLine();
		txtBet.setTypeface(null, Typeface.BOLD);
		txtBet.setTextColor(Color.WHITE);
		txtBet.setEllipsize(TruncateAt.END);
//		txtBet.setText(context.getResources().getString(R.string.passDescription));
		txtBet.setText(
				String.format(
						context.getResources().getString(R.string.lblPassedGameSynthesis),
						Integer.toString(gameIndex),
						context.getResources().getString(R.string.passDescription)
			)
		);
		
		return txtBet;
	}
	
	protected static View buildLeaderPlayerCell(final Context context, final int points) {
		final int leadingPlayerColor = context.getResources().getColor(R.color.LeadingPlayer);
		
		// text view
		TextView txtIndividualGameScore = ScoreCellFactory.buildScoreCellView(context, leadingPlayerColor, Integer
				.toString(points), UIConstants.TXT_SCORE_LAYOUT_PARAMS);

		// image view
		ImageView imgLeader = new ImageView(context);
		imgLeader.setBackgroundColor(leadingPlayerColor);
		imgLeader.setLayoutParams(UIConstants.CALLED_COLOR_LAYOUT_PARAMS);
		imgLeader.setScaleType(ImageView.ScaleType.FIT_XY);
		imgLeader.setImageResource(R.drawable.icon_leader);

		// horizontal layout
		LinearLayout layoutIndividualGameScore = ScoreCellFactory.buildLayout(context, leadingPlayerColor);
		layoutIndividualGameScore.addView(txtIndividualGameScore);
		layoutIndividualGameScore.addView(imgLeader);
		return layoutIndividualGameScore;
	}
	
	protected static View buildDefensePlayerCell(final Context context, final int points) {
		return ScoreCellFactory.buildScoreCellView(context, context.getResources().getColor(R.color.DefensePlayer), Integer
				.toString(points));
	}
	
	protected static View buildCalledPlayerCell(final Context context, final int points, final KingType calledKing) {
		final int leadingPlayerColor = context.getResources().getColor(R.color.LeadingPlayer);
		
		// text view
		TextView txtIndividualGameScore = ScoreCellFactory.buildScoreCellView(context, leadingPlayerColor, new Integer(points).toString(), UIConstants.TXT_SCORE_LAYOUT_PARAMS);

		// image view
		ImageView imgCalledColor = new ImageView(context);
		imgCalledColor.setBackgroundColor(leadingPlayerColor);
		imgCalledColor.setLayoutParams(UIConstants.CALLED_COLOR_LAYOUT_PARAMS);
		imgCalledColor.setScaleType(ImageView.ScaleType.FIT_XY);
		switch(calledKing) {
		case Clubs:
			imgCalledColor.setImageResource(R.drawable.icon_club);
			break;
		case Diamonds:
			imgCalledColor.setImageResource(R.drawable.icon_diamond_old);
			break;					
		case Hearts:
			imgCalledColor.setImageResource(R.drawable.icon_heart_old);
			break;
		case Spades:
			imgCalledColor.setImageResource(R.drawable.icon_spade);
			break;
		default :
		    break;
	}

		// horizontal layout
		LinearLayout layoutIndividualGameScore = ScoreCellFactory.buildLayout(context, leadingPlayerColor);
		layoutIndividualGameScore.addView(txtIndividualGameScore);
		layoutIndividualGameScore.addView(imgCalledColor);
		return layoutIndividualGameScore;
	}
	
	protected static View buildDeadPlayerCell(final Context context) {
		return ScoreCellFactory.buildScoreCellView(context, Color.GRAY, "#");
	}
	
	private static TextView buildScoreCellView(final Context context, final int backGroundColor, final String points, final LinearLayout.LayoutParams layoutParams) {
		TextView txtIndividualGameScore = new TextView(context);
		txtIndividualGameScore.setGravity(Gravity.CENTER);
		txtIndividualGameScore.setLayoutParams(layoutParams);
		txtIndividualGameScore.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		txtIndividualGameScore.setBackgroundColor(backGroundColor);
		txtIndividualGameScore.setTextColor(Color.BLACK);
		txtIndividualGameScore.setTypeface(null, Typeface.BOLD);
		txtIndividualGameScore.setText(points);
		txtIndividualGameScore.setSingleLine();
		txtIndividualGameScore.setEllipsize(TruncateAt.END);
		return txtIndividualGameScore;
	}
	
	public static TextView buildEmptyCellView(final Context context) {
		TextView txtIndividualGameScore = new TextView(context);
		txtIndividualGameScore.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		txtIndividualGameScore.setBackgroundColor(Color.TRANSPARENT);
		return txtIndividualGameScore;
	}
	
	public static TextView buildNextDealerCellView(final Context context) {
		TextView txtIndividualGameScore = new TextView(context);
		txtIndividualGameScore.setGravity(Gravity.CENTER);
		txtIndividualGameScore.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		txtIndividualGameScore.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		txtIndividualGameScore.setBackgroundColor(Color.TRANSPARENT);
		txtIndividualGameScore.setTextColor(Color.WHITE);
		txtIndividualGameScore.setTypeface(null, Typeface.BOLD);
		txtIndividualGameScore.setText("DEALER");
		txtIndividualGameScore.setSingleLine();
		txtIndividualGameScore.setEllipsize(TruncateAt.END);
		return txtIndividualGameScore;
	}
	
	private static TextView buildScoreCellView(final Context context, final int backGroundColor, final String points) {
		return ScoreCellFactory.buildScoreCellView(context, backGroundColor, points, UIConstants.TABGAMESET_LAYOUT_PARAMS);
	}
	
	private static LinearLayout buildLayout(final Context context, final int backGroundColor) {
		LinearLayout layoutIndividualGameScore = new LinearLayout(context);
		layoutIndividualGameScore.setOrientation(LinearLayout.HORIZONTAL);
		layoutIndividualGameScore.setGravity(Gravity.CENTER);
		layoutIndividualGameScore.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		layoutIndividualGameScore.setBackgroundColor(backGroundColor);
		layoutIndividualGameScore.setWeightSum(1);
		return layoutIndividualGameScore;
	}
}
