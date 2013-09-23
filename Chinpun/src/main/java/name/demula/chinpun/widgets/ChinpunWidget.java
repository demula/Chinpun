package name.demula.chinpun.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

  @Override
  public void onEnabled(Context context) {
    super.onEnabled(context);

    IntentFilter filter = new IntentFilter(SessionManagerService.SESSION_UPDATED);
    filter.addAction(SessionManagerService.SESSION_STARTED);
    filter.addCategory(Intent.CATEGORY_DEFAULT);
//    LocalBroadcastManager.getInstance(context).registerReceiver(this, filter);//TODO: this doen't work
  }

  @Override
  public void onDisabled(Context context) {
//    LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    super.onDisabled(context);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    updateSessionInfo(context, appWidgetManager, appWidgetIds);
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);

    if (intent.getAction().equals(SessionManagerService.SESSION_UPDATED) ||
        intent.getAction().equals(SessionManagerService.SESSION_STARTED)) {
      Log.d(CHINPUN_WIDGET, "Updating session data in appwidget.");
      updateSessionInfo(context);
    }
  }

  private String getSessionText(long millis) {
    Date currentSession = new Date(millis);
    DateFormat dfTime = new SimpleDateFormat("HH'h'mm''");
    return dfTime.format(currentSession);
  }

  private void setupSessionText(Context context, RemoteViews views, long millis) {
    // Set text and hook callbacks depending if the session is started or not
    if (millis == 0) {
      views.setTextViewText(R.id.session_info, context.getString(R.string.session_time_default));

      // Start new session in server
      Intent startSessionIntent = new Intent(context, SessionManagerService.class);
      startSessionIntent.setAction(SessionManagerService.START_SESSION);
      PendingIntent pendingIntent = PendingIntent.getService(context, 0, startSessionIntent, PendingIntent.FLAG_ONE_SHOT);
      views.setOnClickPendingIntent(R.id.session_info, pendingIntent);
    } else {
      views.setTextViewText(R.id.session_info, getSessionText(millis));

      //go to activity
      Intent openAppIntent = new Intent(context, MainActivity.class);
      PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, PendingIntent.FLAG_ONE_SHOT);
      views.setOnClickPendingIntent(R.id.session_info, pendingIntent);
    }
  }

  private void setupRefreshButton(Context context, RemoteViews views) {
    Intent updateSessionIntent = new Intent(context, SessionManagerService.class);
    updateSessionIntent.setAction(SessionManagerService.UPDATE_SESSION);
    PendingIntent pendingIntent = PendingIntent.getService(context, 0, updateSessionIntent, PendingIntent.FLAG_ONE_SHOT);
    views.setOnClickPendingIntent(R.id.refresh_button_widget, pendingIntent);
  }

  private void updateSessionInfo(Context context) {
    ComponentName thisWidget = new ComponentName(context, ChinpunWidget.class);
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
    updateSessionInfo(context, appWidgetManager, appWidgetIds);
  }

  private void updateSessionInfo(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    long sessionStartMillis = 0;
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    if (sharedPreferences != null) {
      sessionStartMillis = sharedPreferences.getLong(SettingsFragment.KEY_PREF_CURRENT_SESSION_START_TIME, 0);
    }
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.chinpun_widget);
    for (int appWidgetId : appWidgetIds) {
      setupSessionText(context, views, sessionStartMillis);
      setupRefreshButton(context, views);
      // update widget
      appWidgetManager.updateAppWidget(appWidgetId, views);
    }
  }
}
