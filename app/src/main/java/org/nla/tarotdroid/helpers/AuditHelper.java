
package org.nla.tarotdroid.helpers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.google.common.base.Throwables;

import org.nla.tarotdroid.BuildConfig;

import java.util.HashMap;
import java.util.Map;

public class AuditHelper {
    static {
        if (!BuildConfig.IS_IN_DEV_MODE) {
            FlurryAgent.setContinueSessionMillis(180000);
        }
    }

    public void auditSession(final Context context) {
        FlurryAgent.onStartSession(context, BuildConfig.FLURRY_APP_ID);
    }

    public void stopSession(final Context context) {
        FlurryAgent.onEndSession(context);
    }

    public void auditEvent(final EventTypes eventType) {
        auditEvent(eventType, null);
    }

    public void auditEvent(
            final EventTypes eventType,
            final Map<ParameterTypes, Object> parameters
    ) {
        try {
            Map<String, String> toSend = new HashMap<>();
            if (parameters != null) {
                for (Map.Entry<ParameterTypes, Object> entry : parameters.entrySet()) {
                    try {
                        toSend.put(new String(entry.getKey().toString()),
                                   entry.getValue().toString());
                    } catch (Exception e) {
                    }
                }
            }
            FlurryAgent.logEvent(eventType.toString(), toSend, true);
        } catch (Exception ex) {
            Log.v(BuildConfig.APP_LOG_TAG, "AuditHelper.auditEvent()", ex);
        }
    }

    public void auditError(
            final ErrorTypes errorType,
            final Throwable exception,
            final Activity activity
    ) {
        if (activity != null) {
            UIHelper.showSimpleRichTextDialog(activity, exception.toString(), "Unexpected error");
        }

        String exceptionAsString = exception == null
                ? null
                : Throwables.getStackTraceAsString(exception);
        auditError(errorType, exceptionAsString);
    }

//    public static void auditErrorAsString(
//            final ErrorTypes errorType,
//            final String exceptionAsString,
//            final Activity activity
//    ) {
//        if (activity != null) {
//            UIHelper.showSimpleRichTextDialog(activity, exceptionAsString, "Unexpected error");
//        }
//        auditError(errorType, exceptionAsString);
//    }

    public void auditError(final ErrorTypes errorType, final Throwable exception) {
        String exceptionAsString = exception == null
                ? null
                : Throwables.getStackTraceAsString(exception);
        auditError(errorType, exceptionAsString);
    }

    public void auditError(final ErrorTypes errorType, final String exceptionAsString) {
        try {
            ErrorTypes errorTypeToAudit = errorType;
            if (errorTypeToAudit == null) {
                errorTypeToAudit = ErrorTypes.unexpectedError;
            }

            if (exceptionAsString != null) {
                FlurryAgent.onError(errorTypeToAudit.toString(),
                                    android.os.Build.MODEL + "|" + android.os.Build.VERSION.SDK_INT,
                                    exceptionAsString);
            } else {
                FlurryAgent.onError(errorTypeToAudit.toString(),
                                    android.os.Build.MODEL + "|" + android.os.Build.VERSION.SDK_INT,
                                    "NULL_EXCEPTION");
            }
        } catch (Exception ex) {
            Log.v(BuildConfig.APP_LOG_TAG, "AuditHelper.auditError()", ex);
        }
    }


    public enum UserEventTypes {
        clickedNewGameSet,
        clickedGameSetHistory
    }

    public enum EventTypes {

        // main features
        displayMainDashboardPage,
        displayNewGameSetDashboardPage,
        displayGameSetHistoryPage,
        displayPlayerListPage,
        displayCommunityPage,
        displayMainPreferencePage,

        // bluetooth
        actionBluetoothDiscoverDevices,
        actionBluetoothSetDiscoverable,
        actionBluetoothReceiveGameSet,
        actionBluetoothSendGameSet,

        // game set
        displayGameSetCreationPage,
        displayTabGameSetPreferencePage,
        displayTabGameSetPageWithNewGameSetAction,
        displayTabGameSetPageWithExistingGameSetAction,

        // game creation
        displayGameCreationV2Page,
        displayGameCreationV1Page,
        displayGameReadV1Page,
        displayGameReadV2Page,

        // player statistics
        displayPlayerStatisticsPage,

        // game set statistics
        displayGameSetStatisticsPage,
        displayGameSetStatisticsScoresEvolutionChart,
        displayGameSetStatisticsBetsDistribution,
        displayGameSetStatisticsLeadingPlayerRepartition,
        displayGameSetStatisticsCalledPlayerRepartition,
        displayGameSetStatisticsKingsRepartition,
        displayGameSetStatisticsResultsDistribution,
        displayCharts,

        // problem in TabGameSetActivity.onCreateOptionsMenu()
        tabGameSetActivity_onCreateOptionsMenu_GameSetParametersIsNull,
        tabGameSetActivity_auditEvent_GameSetIsNull
    }

    public enum ParameterTypes {
        gameStyleType,
        gameType,
        playerCount,
        version
    }

    public enum ErrorTypes {
        activityCreationError,
        unexpectedError,
        dalInitializationError,
        globalUncaughtError,
        bluetoothReceiveError,
        bluetoothSendError,
        notificationError,
        excelFileStorage,
        displayAndRemoveGameDialogActivityCreationError,
        gameCreationActivityError,
        gameSetHistoryActivityError,
        bluetoothBeforeSendError,
        gameSetCreationActivityError,
        gameSetStatisticsActivityError,
        mainDashBoardActivityError,
        mainPreferencesActivityError,
        newGameSetDashboardActivityError,
        playerListActivityError,
        playerStatisticsActivityError,
        tabGameSetActivityError,
        tabGameSetPreferencesActivityError,
        gameReadViewPagerActivityError,
        tabGameSetActivityOnStartError,
        tabGameSetActivityOnResumeError,
        facebookNewMeRequestFailed,
        facebookNewMeRequestFailedWithUserNull,
        upSyncGameSetTaskError,
        postGameSetLinkOnFacebookWallTaskError,
        postGameSetOnFacebookAppError,
        persistGameTaskError,
        playerSelectorActivityError,
        updateGameSetError,
        exportDatabaseError,
        importDatabaseError,
        exportExcelError,
        facebookHasNoPublishPermission
    }
}