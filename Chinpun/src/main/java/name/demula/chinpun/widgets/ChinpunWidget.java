package name.demula.chinpun.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import name.demula.chinpun.R;
import name.demula.chinpun.activities.MainActivity;
import name.demula.chinpun.fragments.SettingsFragment;
import name.demula.chinpun.services.SessionManagerService;

/**
 * Created by Jesus on 21/08/13.
 */
public class ChinpunWidget extends AppWidgetProvider {
    public static final String CHINPUN_WIDGET = "CHINPUN_WIDGET";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateSessionInfo(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(SessionManagerService.INFO_UPDATED)) {
            Log.d(CHINPUN_WIDGET, "Recieved INFO_UPDATED action.");
            updateSessionInfo(context);
        }
    }

    private void updateSessionInfo(Context context) {
        ComponentName thisWidget = new ComponentName(context, ChinpunWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        updateSessionInfo(context, appWidgetManager, appWidgetIds);
    }

    private void updateSessionInfo(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        long sessionStartMillis = sharedPreferences.getLong(SettingsFragment.KEY_PREF_CURRENT_SESSION_START_TIME, 0);
        Date currentSession = new Date(sessionStartMillis);
        DateFormat dfTime = new SimpleDateFormat("HH'h'mm''");
        String sessionStartText = dfTime.format(currentSession);

        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.chinpun_widget);

            if (sessionStartMillis == 0) {
                views.setTextViewText(R.id.session_info, context.getString(R.string.session_time_default));

                Intent startSessionIntent = new Intent();
                startSessionIntent.setAction(SessionManagerService.START_SESSION);
                PendingIntent pendingIntent = PendingIntent.getService(context, 0, startSessionIntent, 0);

                views.setOnClickPendingIntent(R.id.session_info, pendingIntent);
            } else {
                views.setTextViewText(R.id.session_info, sessionStartText);

                Intent openAppIntent = new Intent(context, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, 0);

                views.setOnClickPendingIntent(R.id.session_info, pendingIntent);
            }

            Intent updateSessionIntent = new Intent();
            updateSessionIntent.setAction(SessionManagerService.UPDATE_SESSION);

            PendingIntent pendingIntent = PendingIntent.getService(context, 0, updateSessionIntent, 0);
            views.setOnClickPendingIntent(R.id.refresh_button_widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


}
