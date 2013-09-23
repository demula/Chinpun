package name.demula.chinpun.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import name.demula.chinpun.R;
import name.demula.chinpun.fragments.SettingsFragment;
import name.demula.chinpun.model.Session;
import name.demula.chinpun.model.User;
import name.demula.chinpun.utils.ParseServerUtil;

/**
 * Created by Jesus on 27/08/13.
 */
public class SessionManagerService extends IntentService {
  public static final String SESSION_MANAGER_SERVICE = "SESSION_MANAGER_SERVICE";
  //Actions
  public static final String UPDATE_USER = "name.demula.chinpun.UPDATE_USER";
  public static final String USER_UPDATED = "name.demula.chinpun.USER_UPDATED";
  public static final String START_SESSION = "name.demula.chinpun.START_SESSION";
  public static final String SESSION_STARTED = "name.demula.chinpun.SESSION_STARTED";
  public static final String UPDATE_SESSION = "name.demula.chinpun.UPDATE_SESSION";
  public static final String SESSION_UPDATED = "name.demula.chinpun.SESSION_UPDATED";
  //attributes
  private Handler handler = new Handler();

  public SessionManagerService() {
    super("SessionManagerService");
  }

  public SessionManagerService(String name) {
    super(name);

  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Context context = getApplicationContext();
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    String action = intent.getAction();

    if (action.equals(UPDATE_USER)) {
      updateUserDetails(preferences);
    } else if (action.equals(START_SESSION)) {
      startSession(preferences);
    } else if (action.equals(UPDATE_SESSION)) {
      updateSession(preferences);
    }
  }

  // ------------------------------------------------------------------- ACTIONS
  private void updateUserDetails(SharedPreferences preferences) {
    try {
      InputStream stream = null;
      String html;
      String username = preferences.getString(SettingsFragment.KEY_PREF_USERNAME, "");
      String password = preferences.getString(SettingsFragment.KEY_PREF_PASSWORD, "");
      try {
        stream = downloadUrl("http://oficina.crcit.es/chinpun/welcome", "GET", null, username, password);
        html = readIt(stream);
        updateSharedPreferences(html, preferences);
      } finally {
        if (stream != null) {
          stream.close();
        }
        Intent intent = new Intent();
        intent.setAction(USER_UPDATED);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
      }

    } catch (IOException e) {
      final String message = e.getMessage();
      Log.e(SESSION_MANAGER_SERVICE, message, e);
      handler.post(new Runnable() {
        public void run() {
          Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
      });
    }
  }

  private void startSession(SharedPreferences preferences) {
    try {
      InputStream stream = null;
      String json = "";
      List<AbstractMap.SimpleEntry> params = new ArrayList<AbstractMap.SimpleEntry>();
      params.add(new AbstractMap.SimpleEntry("nuevoEstado", "true"));
      String username = preferences.getString(SettingsFragment.KEY_PREF_USERNAME, "");
      String password = preferences.getString(SettingsFragment.KEY_PREF_PASSWORD, "");
      try {
        stream = downloadUrl("http://oficina.crcit.es/chinpun/fichajes/abrirSesionFichaje", "POST", params, username, password);
        json = readIt(stream);
        updateSessionInSharedPreferences(json, preferences);
      } finally {
        if (stream != null) {
          stream.close();
        }
        Intent intent = new Intent();
        intent.setAction(SESSION_STARTED);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        //TODO; change the way we broadcast these messages
        sendBroadcast(intent); // for the appwidget we cannot use local broadcast
      }
    } catch (IOException e) {
      final String message = e.getMessage();
      Log.e(SESSION_MANAGER_SERVICE, message, e);
      handler.post(new Runnable() {
        public void run() {
          Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
      });
    }
  }

  private void updateSession(SharedPreferences preferences) {
    try {
      long userID = preferences.getLong(SettingsFragment.KEY_PREF_USER_ID, 0);
      if (userID == 0) {
        Log.e(SESSION_MANAGER_SERVICE, "User ID " + userID + " in memory not valid");
        return;
      }
      InputStream stream = null;
      String json = "";
      List<AbstractMap.SimpleEntry> params = new ArrayList<AbstractMap.SimpleEntry>();
      params.add(new AbstractMap.SimpleEntry("idUsuario", userID));
      String username = preferences.getString(SettingsFragment.KEY_PREF_USERNAME, "");
      String password = preferences.getString(SettingsFragment.KEY_PREF_PASSWORD, "");
      try {
        stream = downloadUrl("http://oficina.crcit.es/chinpun/fichajes/cargarFichajeAbierto", "POST", params, username, password);
        json = readIt(stream);
        updateSessionInSharedPreferences(json, preferences);
      } finally {
        if (stream != null) {
          stream.close();
        }
        Intent intent = new Intent();
        intent.setAction(SESSION_UPDATED);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        //TODO; change the way we broadcast these messages
        sendBroadcast(intent); // for the appwidget we cannot use local broadcast
      }
    } catch (IOException e) {
      final String message = e.getMessage();
      Log.e(SESSION_MANAGER_SERVICE, message, e);
      handler.post(new Runnable() {
        public void run() {
          Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
      });
    }
  }

  // ---------------------------------------------------------------- UTILITIES

  /**
   * Reads an InputStream and converts it to a String.
   *
   * @param stream InputStream containing HTML from targeted site.
   * @return String concatenated according to len parameter.
   * @throws java.io.IOException
   * @throws java.io.UnsupportedEncodingException
   */
  private String readIt(InputStream stream) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
    StringBuilder builder = new StringBuilder(stream.available());
    String aux;
    while ((aux = reader.readLine()) != null) {
      builder.append(aux);
    }
    return builder.toString();
  }

  private String getQuery(List<AbstractMap.SimpleEntry> params) throws UnsupportedEncodingException {
    StringBuilder result = new StringBuilder();
    boolean first = true;

    for (AbstractMap.SimpleEntry pair : params) {
      if (first) {
        first = false;
      } else {
        result.append("&");
      }

      result.append(URLEncoder.encode(pair.getKey().toString(), "UTF-8"));
      result.append("=");
      result.append(URLEncoder.encode(pair.getValue().toString(), "UTF-8"));
    }

    return result.toString();
  }

  /**
   * Given a string representation of a URL, sets up a connection and gets
   * an input stream.
   *
   * @param urlString A string representation of a URL.
   * @return An InputStream retrieved from a successful HttpURLConnection.
   * @throws IOException
   */
  private InputStream downloadUrl(String urlString, String method, List<AbstractMap.SimpleEntry> params, final String username, final String password) throws IOException {

    Authenticator.setDefault(new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password.toCharArray());
      }
    });

    CookieManager cookieManager = new CookieManager();
    CookieHandler.setDefault(cookieManager);

    URL url = new URL(urlString);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setReadTimeout(10000 /* milliseconds */);
    conn.setConnectTimeout(15000 /* milliseconds */);
    conn.setRequestMethod(method);
    conn.setUseCaches(false);
    conn.setDoInput(true);
    if (method.equals("POST")) {
      conn.setDoOutput(true);
      OutputStream os = conn.getOutputStream();
      BufferedWriter writer = new BufferedWriter(
          new OutputStreamWriter(os, "UTF-8"));
      writer.write(getQuery(params));
      writer.close();
      os.close();
    }
    // Start the query
    conn.connect();
    int responseCode = conn.getResponseCode();
    if (responseCode != HttpURLConnection.HTTP_OK) {
      String message = getResources().getString(R.string.connection_error_with_code);
      switch (responseCode) {
        case HttpURLConnection.HTTP_INTERNAL_ERROR:
          throw new IOException(message + getResources().getString(R.string.connection_error_500_internal));
        case HttpURLConnection.HTTP_FORBIDDEN:
          throw new IOException(message + getResources().getString(R.string.connection_error_403_forbidden));
        case HttpURLConnection.HTTP_UNAUTHORIZED:
          throw new IOException(message + getResources().getString(R.string.connection_error_401_unauthorized));
        default:
          throw new IOException(message + getResources().getString(R.string.connection_error_unhandled));
      }
    }
    return conn.getInputStream();
  }

  protected void updateSharedPreferences(String html, SharedPreferences sharedPref) {
    if (html == null) {
      Log.d(SESSION_MANAGER_SERVICE, "Got null html from server when updating preferences.");
      return;
    }
    Log.d(SESSION_MANAGER_SERVICE, html);
    String userFirstName = ParseServerUtil.getUserFirstNamefromHTML(html);
    String userLastName = ParseServerUtil.getUserLastNamefromHTML(html);
    long userID = ParseServerUtil.getUserIDfromHTML(html);
    //Document doc = Jsoup.parse(html);
    long weekHours = ParseServerUtil.getWeekHoursfromHTML(html);
    long monthHours = ParseServerUtil.getMonthHoursfromHTML(html);
    sharedPref.edit()
        .putString(SettingsFragment.KEY_PREF_USER_FIRSTNAME, userFirstName)
        .putString(SettingsFragment.KEY_PREF_USER_LASTNAME, userLastName)
        .putLong(SettingsFragment.KEY_PREF_USER_ID, userID)
        .putLong(SettingsFragment.KEY_PREF_WEEK_HOURS, weekHours)
        .putLong(SettingsFragment.KEY_PREF_MONTH_HOURS, monthHours)
        .commit();
  }

  protected void updateSessionInSharedPreferences(String json, SharedPreferences sharedPref) {
    if (json == null) {
      Log.d(SESSION_MANAGER_SERVICE, "Got null json from server when updating session.");
      return;
    }
    Log.d(SESSION_MANAGER_SERVICE, json);
    Session currentSession;
    try {
      currentSession = ParseServerUtil.getSessionFromJSON(new JSONObject(json));
    } catch (JSONException e) {
      Log.e(SESSION_MANAGER_SERVICE, "Error when parsing json of the session. Probably an HTML auth error page.", e);
      currentSession = null;
    }

    if (currentSession != null) {
      User user = currentSession.getPersona();
      sharedPref.edit()
          .putLong(SettingsFragment.KEY_PREF_CURRENT_SESSION_ID, currentSession.getIdFichaje())
          .putLong(SettingsFragment.KEY_PREF_CURRENT_SESSION_START_TIME,
              currentSession.getFechaInicio().getTimeInMillis())
          .commit();
    } else {
      sharedPref.edit()
          .putLong(SettingsFragment.KEY_PREF_CURRENT_SESSION_ID, 0)
          .putLong(SettingsFragment.KEY_PREF_CURRENT_SESSION_START_TIME, 0)
          .commit();
    }
  }
}
