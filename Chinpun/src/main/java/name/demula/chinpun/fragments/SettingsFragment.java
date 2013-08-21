package name.demula.chinpun.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import name.demula.chinpun.R;

/**
 * Created by Jesus on 20/07/13.
 */
public class SettingsFragment extends PreferenceFragment {

    public static final String KEY_PREF_USERNAME = "pref_username";
    public static final String KEY_PREF_PASSWORD = "pref_password";
    public static final String KEY_PREF_USER_FIRSTNAME = "pref_user_firstname";
    public static final String KEY_PREF_USER_LASTNAME = "pref_user_lastname";
    public static final String KEY_PREF_USER_ID = "pref_user_id";
    public static final String KEY_PREF_WEEK_HOURS = "pref_week_hours";
    public static final String KEY_PREF_MONTH_HOURS = "pref_month_hours";
    public static final String KEY_PREF_CURRENT_SESSION_START_TIME = "pref_current_session_start_time";
    public static final String KEY_PREF_CURRENT_SESSION_ID = "pref_current_session_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
