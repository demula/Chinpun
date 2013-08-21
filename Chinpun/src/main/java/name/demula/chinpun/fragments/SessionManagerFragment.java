package name.demula.chinpun.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import name.demula.chinpun.R;

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
    private String currentSessionTimeString;
    private String currentSessionDateString;
    private OnSessionActionListener onSessionActionListener;
    private TextView userFirstNameTextView;
    private TextView userLastNameTextView;
    private TextView weekHoursStringTextView;
    private TextView monthHoursStringTextView;
    private TextView currentSessionTimeStringTextView;
    private TextView currentSessionDateStringTextView;
    private Button button;

    public SessionManagerFragment() {
        // Empty constructor required for fragment subclasses
    }

    private String minutesFormatted(long totalMinutes) {
        long hours = totalMinutes / 60;
        long minutes = (totalMinutes < 0 ? -totalMinutes : totalMinutes) % 60;
        return (hours == 0 ? "00" : hours) + ":" + (minutes == 0 ? "00" : minutes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session_manager, container, false);

        userFirstNameTextView = (TextView) view.findViewById(R.id.session_manager_user_firstname);
        userLastNameTextView = (TextView) view.findViewById(R.id.session_manager_user_lastname);
        weekHoursStringTextView = (TextView) view.findViewById(R.id.session_manager_hours_week);
        monthHoursStringTextView = (TextView) view.findViewById(R.id.session_manager_hours_month);
        currentSessionTimeStringTextView = (TextView) view.findViewById(R.id.session_manager_current_session_time);
        currentSessionDateStringTextView = (TextView) view.findViewById(R.id.session_manager_current_session_date);
        button = (Button) view.findViewById(R.id.session_manager_button);

        //insertamos valores por defecto en caso de que aun no esten
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

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

        long millis = sharedPref.getLong(SettingsFragment.KEY_PREF_CURRENT_SESSION_START_TIME, 0);
        if (millis != 0) {
            Date currentSession = new Date(millis);
            DateFormat dfTime = new SimpleDateFormat("HH'h'mm''");
            DateFormat dfDate = new SimpleDateFormat("EEE, MMM d, yyyy");
            currentSessionTimeString = dfTime.format(currentSession);
            currentSessionTimeStringTextView.setText(currentSessionTimeString);
            currentSessionDateString = dfDate.format(currentSession);
            currentSessionDateStringTextView.setText(currentSessionDateString);
            button.setText(R.string.end_session);
            button.setEnabled(false);
        } else {
            currentSessionTimeStringTextView.setText(R.string.session_time_default);
            currentSessionDateStringTextView.setText("");
            button.setText(R.string.start_session);
            button.setEnabled(true);
        }

        //iniciar boton
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (button.getText().equals(getString(R.string.start_session))) {
                    //new DownloadTask().execute("http://oficina.crcit.es/chinpun/welcome");
                    onSessionActionListener.onSessionStart(button,
                            getString(R.string.starting_session),
                            getString(R.string.end_session));
                } else {
                    button.setText(R.string.start_session);
                    onSessionActionListener.onSessionEnd();
                }
            }
        });

        //iniciar reloj

        //setup dialog formsession close

        sharedPref.registerOnSharedPreferenceChangeListener(this);
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
            long millis = sharedPref.getLong(SettingsFragment.KEY_PREF_CURRENT_SESSION_START_TIME, 0);
            if (millis != 0) {
                Date currentSession = new Date(millis);
                DateFormat dfTime = new SimpleDateFormat("HH'h'mm''");
                DateFormat dfDate = new SimpleDateFormat("EEE, MMM d, yyyy");
                currentSessionTimeString = dfTime.format(currentSession);
                currentSessionTimeStringTextView.setText(currentSessionTimeString);
                currentSessionDateString = dfDate.format(currentSession);
                currentSessionDateStringTextView.setText(currentSessionDateString);
                button.setEnabled(false);
            } else {
                currentSessionTimeStringTextView.setText(R.string.session_time_default);
                currentSessionDateStringTextView.setText("");
                button.setText(R.string.start_session);
                button.setEnabled(true);
            }
            currentSessionTimeStringTextView.invalidate();
            currentSessionDateStringTextView.invalidate();
            button.invalidate();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onSessionActionListener = (OnSessionActionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnSessionActionListener");
        }
        //onSessionActionListener.onSessionUpdate();
    }


    public interface OnSessionActionListener {
        public void onSessionStart(Button button, String executingText, String doneText);

        public void onSessionUpdate(MenuItem refreshItem);

        public void onSessionEnd();
    }


}
