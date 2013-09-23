package name.demula.chinpun.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import name.demula.chinpun.R;
import name.demula.chinpun.services.SessionManagerService;

/**
 * Created by Jesus on 20/07/13.
 */
public class SessionManagerFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

  public static final String CHINPUN_SESSION_TAG = "CHINPUN_SESSION_TAG";
  private SharedPreferences sharedPref;
  private String userFirstName;
  private String userLastName;
  private String weekHoursString;
  private String monthHoursString;
  private TextView userFirstNameTextView;
  private TextView userLastNameTextView;
  private TextView weekHoursStringTextView;
  private TextView monthHoursStringTextView;
  private TextView currentSessionTimeStringTextView;
  private TextView currentSessionDateStringTextView;
  private boolean userDetailsReqPending;
  private boolean sessionUpdateReqPending;
  private boolean sessionStartReqPending;
  private Button button;
  private Menu fMenu;
  private ImageView refreshAnimView;
  private BroadcastReceiver sessionReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (action.equals(SessionManagerService.SESSION_UPDATED)) {
        Log.d(CHINPUN_SESSION_TAG, "Recieved SESSION_UPDATED action.");
        sessionUpdateReqPending = false;
        clearUpdateAnimation();
      } else if (action.equals(SessionManagerService.USER_UPDATED)) {
        Log.d(CHINPUN_SESSION_TAG, "Recieved USER_UPDATED action.");
        userDetailsReqPending = false;
        clearUpdateAnimation();
      } else if (action.equals(SessionManagerService.SESSION_STARTED)) {
        Log.d(CHINPUN_SESSION_TAG, "Recieved SESSION_STARTED action.");
        button.setText(R.string.end_session);
      }
    }
  };

  public SessionManagerFragment() {
    // Empty constructor required for fragment subclasses
  }

  private String minutesFormatted(long totalMinutes) {
    long hours = totalMinutes / 60;
    long minutes = (totalMinutes < 0 ? -totalMinutes : totalMinutes) % 60;
    return (hours == 0 ? "00" : hours) + ":" + (minutes == 0 ? "00" : (minutes < 10 ? "0" + minutes : minutes));
  }

  private void updateSessionInfoViews() {
    long millis = sharedPref.getLong(SettingsFragment.KEY_PREF_CURRENT_SESSION_START_TIME, 0);
    if (millis != 0) {
      Date currentSession = new Date(millis);
      DateFormat dfTime = new SimpleDateFormat("HH'h'mm''");
      DateFormat dfDate = new SimpleDateFormat("EEE, MMM d, yyyy");
      currentSessionTimeStringTextView.setText(dfTime.format(currentSession));
      currentSessionDateStringTextView.setText(dfDate.format(currentSession));
      button.setText(R.string.end_session);
      button.setEnabled(false);
    } else {
      currentSessionTimeStringTextView.setText(R.string.session_time_default);
      currentSessionDateStringTextView.setText("");
      button.setText(R.string.start_session);
      button.setEnabled(true);
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    refreshAnimView = (ImageView) inflater.inflate(R.layout.refresh_action_view, null);

    View view = inflater.inflate(R.layout.fragment_session_manager, container, false);
    userFirstNameTextView = (TextView) view.findViewById(R.id.session_manager_user_firstname);
    userLastNameTextView = (TextView) view.findViewById(R.id.session_manager_user_lastname);
    weekHoursStringTextView = (TextView) view.findViewById(R.id.session_manager_hours_week);
    monthHoursStringTextView = (TextView) view.findViewById(R.id.session_manager_hours_month);
    currentSessionTimeStringTextView = (TextView) view.findViewById(R.id.session_manager_current_session_time);
    currentSessionDateStringTextView = (TextView) view.findViewById(R.id.session_manager_current_session_date);

    button = (Button) view.findViewById(R.id.session_manager_button);
    //iniciar boton
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (button.getText().equals(getString(R.string.start_session))) {
          //new DownloadTask().execute("http://oficina.crcit.es/chinpun/welcome");
          button.setEnabled(false);
          button.setText(R.string.starting_session);
          sessionStartReqPending = true;
          Intent intent = new Intent(getActivity(), SessionManagerService.class);
          intent.setAction(SessionManagerService.START_SESSION);
          getActivity().startService(intent);
        } else {
          //button.setText(R.string.start_session);
          //onSessionActionListener.onSessionEnd();
        }
      }
    });

    userFirstName = sharedPref.getString(SettingsFragment.KEY_PREF_USER_FIRSTNAME, "");
    userFirstNameTextView.setText(userFirstName);

    userLastName = sharedPref.getString(SettingsFragment.KEY_PREF_USER_LASTNAME, "");
    userLastNameTextView.setText(userLastName);

    long weekHoursInMinutes = sharedPref.getLong(SettingsFragment.KEY_PREF_WEEK_HOURS, 0);
    weekHoursString = "" + minutesFormatted(weekHoursInMinutes) + " " + getString(R.string.hours_short);
    weekHoursStringTextView.setText(weekHoursString);

    long monthHoursInMinutes = sharedPref.getLong(SettingsFragment.KEY_PREF_MONTH_HOURS, 0);
    monthHoursString = "" + minutesFormatted(monthHoursInMinutes) + " " + getString(R.string.hours_short);
    monthHoursStringTextView.setText(monthHoursString);

    updateSessionInfoViews();

    return view;
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    Log.d(CHINPUN_SESSION_TAG, "Updating preferences with key: " + key);
    if (key.equals(SettingsFragment.KEY_PREF_USER_FIRSTNAME)) {
      userFirstName = sharedPreferences.getString(SettingsFragment.KEY_PREF_USER_FIRSTNAME, "");
      userFirstNameTextView.setText(userFirstName);
      userFirstNameTextView.invalidate();
    } else if (key.equals(SettingsFragment.KEY_PREF_USER_LASTNAME)) {
      userLastName = sharedPreferences.getString(SettingsFragment.KEY_PREF_USER_LASTNAME, "");
      userLastNameTextView.setText(userLastName);
      userLastNameTextView.invalidate();
    } else if (key.equals(SettingsFragment.KEY_PREF_WEEK_HOURS)) {
      long weekHoursInMinutes = sharedPreferences.getLong(SettingsFragment.KEY_PREF_WEEK_HOURS, 0);
      weekHoursString = "" + minutesFormatted(weekHoursInMinutes) + " " + getString(R.string.hours_short);
      weekHoursStringTextView.setText(weekHoursString);
      weekHoursStringTextView.invalidate();
    } else if (key.equals(SettingsFragment.KEY_PREF_MONTH_HOURS)) {
      long monthHoursInMinutes = sharedPreferences.getLong(SettingsFragment.KEY_PREF_MONTH_HOURS, 0);
      monthHoursString = "" + minutesFormatted(monthHoursInMinutes) + " " + getString(R.string.hours_short);
      monthHoursStringTextView.setText(monthHoursString);
      monthHoursStringTextView.invalidate();
    } else if (key.equals(SettingsFragment.KEY_PREF_CURRENT_SESSION_START_TIME)) {
      updateSessionInfoViews();
      currentSessionTimeStringTextView.invalidate();
      currentSessionDateStringTextView.invalidate();
      button.invalidate();
    }
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    //insertamos valores por defecto en caso de que aun no esten
    if (sharedPref == null) {
      PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
      sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
    }
    sharedPref.registerOnSharedPreferenceChangeListener(this);

    IntentFilter filter = new IntentFilter(SessionManagerService.SESSION_UPDATED);
    filter.addAction(SessionManagerService.SESSION_STARTED);
    filter.addAction(SessionManagerService.USER_UPDATED);
    filter.addCategory(Intent.CATEGORY_DEFAULT);
    LocalBroadcastManager.getInstance(getActivity()).registerReceiver(sessionReceiver, filter);

    userDetailsReqPending = false;
    sessionUpdateReqPending = false;
    sessionStartReqPending = false;
  }

  @Override
  public void onDetach() {
    LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(sessionReceiver);
    super.onDetach();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.main, menu);
    this.fMenu = menu;
    startUpdateAnimation();
    clearUpdateAnimation();
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action buttons
    switch (item.getItemId()) {
      case R.id.action_update:
        sessionUpdateReqPending = true;
        Intent intent = new Intent(getActivity(), SessionManagerService.class);
        intent.setAction(SessionManagerService.UPDATE_SESSION);
        getActivity().startService(intent);
        userDetailsReqPending = true;
        intent.setAction(SessionManagerService.UPDATE_USER);

        startUpdateAnimation();
        getActivity().startService(intent);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void startUpdateAnimation() {
    if (userDetailsReqPending || sessionUpdateReqPending) {
      MenuItem refreshItem = fMenu.findItem(R.id.action_update);
      refreshItem.setEnabled(false);
      Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.clockwise_refresh);
      rotation.setRepeatCount(Animation.INFINITE);
      refreshAnimView.startAnimation(rotation);
//        item.setActionView(iv);
      MenuItemCompat.setActionView(refreshItem, refreshAnimView);
    }
  }

  private void clearUpdateAnimation() {
    if (!userDetailsReqPending && !sessionUpdateReqPending) {
      MenuItem refreshItem = fMenu.findItem(R.id.action_update);
      if (refreshAnimView != null) {
        refreshItem.setEnabled(true);
        refreshAnimView.clearAnimation();
//        refreshItem.setActionView(null);
        MenuItemCompat.setActionView(refreshItem, null);
      }
    }
  }

  /* Called whenever we call invalidateOptionsMenu() */
  @Override
  public void onPrepareOptionsMenu(Menu menu) {
    // If the nav drawer is open, hide action items related to the content view
    DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
    boolean drawerOpen = drawerLayout.isDrawerOpen(getActivity().findViewById(R.id.left_drawer));
    MenuItem refreshItem = menu.findItem(R.id.action_update);
    if (refreshItem != null) {
      if (drawerOpen) {
        View actionBarView = MenuItemCompat.getActionView(refreshItem);
        if (actionBarView != null) {
          actionBarView.clearAnimation();
//        refreshItem.setActionView(null);
          MenuItemCompat.setActionView(refreshItem, null);
        }
        menu.findItem(R.id.action_update).setVisible(false);
      } else {
        menu.findItem(R.id.action_update).setVisible(true);
        startUpdateAnimation();
        clearUpdateAnimation();
      }
    }
  }

}
