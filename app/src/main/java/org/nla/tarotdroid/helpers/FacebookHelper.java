package org.nla.tarotdroid.helpers;

public final class FacebookHelper {

//	private static final Random RANDOM = new Random();
//
//	/**
//	 * Default constructor.
//	 */
//	private FacebookHelper() {
//	}
//
//	/**
//	 * Returns the url of the gameset for facebook.
//	 * @param gameSet
//	 * @return
//	 */
//	public static String buildGameSetUrl(final GameSet gameSet) {
//		return UrlHelper.buildLink(gameSet, AppContext.getApplication().getFacebookCloudUrl());
//	}
//
//	/**
//	 * Returns the url of the picture.
//	 * @param gameSet
//	 * @return
//	 */
//	public static String buildGameSetPictureUrl(final GameSet gameSet) {
//		return UrlHelper.buildPictureLink(gameSet);
//	}
//
//	/**
//	 *
//	 * @param gameSet
//	 * @return
//	 */
//	public static String buildCaption(final GameSet gameSet) {
//		return AppContext.getApplication().getResources().getString(R.string.lblFacebookPostCaption, Joiner
//				.on(", " ).skipNulls().join(gameSet.getPlayers().getPlayerNames()), gameSet.getGameCount());
//	}
//
//	/**
//	 * @param gameSet
//	 * @return
//	 */
//	public static String buildName(final GameSet gameSet) {
//		return AppContext.getApplication().getResources().getString(R.string.lblFacebookPostName);
//	}
//
//	/**
//	 * Build description to be posted on facebook timeline.
//	 * @param gameSet
//	 * @return
//	 */
//	public static String buildDescription(final GameSet gameSet) {
//		StringBuffer toReturn = new StringBuffer();
//
//		Player winner = null;
//		for (Player player : gameSet.getPlayers().getPlayers()) {
//			if (gameSet.isWinner(player)) {
//				winner = player;
//			}
//		}
//
//		if (winner != null) {
//			toReturn.append(
//				AppContext.getApplication().getResources().getString(
//					R.string.lblFacebookPostHasWon,
//					winner.getName(),
//					gameSet.getGameSetScores().getIndividualResultsAtGameOfIndex(gameSet.getGameCount(), winner))
//				);
//		}
//
//		return toReturn.toString();
//	}
//
//	/**
//	 * Shows a notification indicating the publish is in progress.
//	 * @param activity
//	 * @return
//	 */
//	public static int showNotificationStartProgress(Activity activity) {
//		checkArgument(activity != null, "activity is null");
//
//		int notificationId = RANDOM.nextInt();
//		try {
//			NotificationCompat.Builder mBuilder =
//			        new NotificationCompat.Builder(activity)
//			        .setSmallIcon(R.drawable.icon_notification_small)
//			        .setContentTitle(AppContext.getApplication().getResources().getString(R.string.titleFacebookNotificationInProgress))
//			        .setContentText(AppContext.getApplication().getResources().getString(R.string.msgFacebookNotificationInProgress))
//			        .setProgress(0, 0, true);
//
//			NotificationManager mNotificationManager = (NotificationManager)activity.getSystemService(
//					Context.NOTIFICATION_SERVICE);
//			mNotificationManager.notify(notificationId, mBuilder.build());
//		}
//		// Problem with older versions of android or devices, need to provide a PendingIntent that leads to an activity
//		catch (IllegalArgumentException iae) {
//			Intent intent = new Intent(activity, NotificationActivity.class);
//			PendingIntent contentIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//			NotificationCompat.Builder mBuilder =
//			        new NotificationCompat.Builder(activity)
//			        .setSmallIcon(R.drawable.icon_notification_small)
//			        .setContentTitle(AppContext.getApplication().getResources().getString(R.string.titleFacebookNotificationInProgress))
//			        .setContentText(AppContext.getApplication().getResources().getString(R.string.msgFacebookNotificationInProgress))
//			        .setProgress(0, 0, true)
//			        .setContentIntent(contentIntent);
//
//			NotificationManager mNotificationManager = (NotificationManager)activity.getSystemService(
//					Context.NOTIFICATION_SERVICE);
//			mNotificationManager.notify(notificationId, mBuilder.build());
//		}
//
//		return notificationId;
//	}
//
//	/**
//	 * Shows a notification indicating the publish is finished.
//	 * @param activity
//	 * @param url
//	 * @param notificationId
//	 */
//	public static void showNotificationStopProgressSuccess(Activity activity, String url, int notificationId) {
//		checkArgument(activity != null, "activity is null");
//		checkArgument(activity != null, "url is null");
//
//		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//		NotificationCompat.Builder mBuilder =
//		        new NotificationCompat.Builder(activity)
//		        .setSmallIcon(R.drawable.icon_notification_small)
//		        .setContentTitle(AppContext.getApplication().getResources().getString(R.string.titleFacebookNotificationDone))
//		        .setContentText(AppContext.getApplication().getResources().getString(R.string.msgFacebookNotificationDone))
//		        .setAutoCancel(true)
//		        .setProgress(0, 0, false);
//
//		mBuilder.setContentIntent(PendingIntent.getActivity(activity, 0, intent, 0));
//
//		NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(
//				Context.NOTIFICATION_SERVICE);
//		mNotificationManager.notify(notificationId, mBuilder.build());
//	}
//
//	/**
//	 * Shows a notification indicating the publish has failed.
//	 * @param activity
//	 * @param notificationId
//	 */
//	public static void showNotificationStopProgressFailure(Activity activity, int notificationId) {
//		checkArgument(activity != null, "activity is null");
//
//		Intent intent = new Intent(activity, NotificationActivity.class);
//		PendingIntent contentIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//		NotificationCompat.Builder mBuilder =
//		        new NotificationCompat.Builder(activity)
//		        .setSmallIcon(R.drawable.icon_notification_small)
//		        .setContentTitle(AppContext.getApplication().getResources().getString(R.string.titleFacebookNotificationError))
//		        .setContentText(AppContext.getApplication().getResources().getString(R.string.msgFacebookNotificationError))
//		        .setAutoCancel(true)
//		        .setProgress(0, 0, false)
//		        .setContentIntent(contentIntent);
//
//		NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(
//				Context.NOTIFICATION_SERVICE);
//		mNotificationManager.notify(notificationId, mBuilder.build());
//
//		AppContext.getApplication().getNotificationIds().clear();
//	}
}