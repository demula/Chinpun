package name.demula.chinpun.fragments.servicies;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import name.demula.chinpun.R;
import name.demula.chinpun.fragments.SettingsFragment;
import name.demula.chinpun.model.Session;
import name.demula.chinpun.model.User;

/**
 * Created by Jesus on 27/07/13.
 */
@Deprecated
public class ChinpunManagerServiceFragment extends Fragment {
  public static final String CHINPUN_MANAGER_TAG = "CHINPUN_MANAGER_TAG";
  private SharedPreferences sharedPref;
  private String username;
  private String password;
  private long userID;
  private User user;
  private Session currentSession;

  public ChinpunManagerServiceFragment() {
    // Empty constructor required for fragment subclasses
  }

  //--------------------------------------------------- STATIC UTILITIES
  public static long getUserIDfromHTML(String html) {
    Pattern p = Pattern.compile("idUsuario : \'([0-9]+)\'");
    Matcher m = p.matcher(html);
    if (m.find() && m.groupCount() > 0) {
      return Long.parseLong(m.group(1));
    }
    return -1;
  }

  public static String getUserFirstNamefromHTML(String html) {
    Pattern p = Pattern.compile("<span id=\"nombreHeader\">([\\p{L}|\\s]+)</span>");
    Matcher m = p.matcher(html);
    if (m.find() && m.groupCount() > 0) {
      return m.group(1);
    }
    return "";
  }

  public static String getUserLastNamefromHTML(String html) {
    Pattern p = Pattern.compile("<span id=\"apellidoHeader\">([\\p{L}|\\s]+)</span>");
    Matcher m = p.matcher(html);
    if (m.find() && m.groupCount() > 0) {
      return m.group(1);
    }
    return "";
  }

  public static long getWeekHoursfromHTML(String html) {
    //Document dom
    //Element hoursElem = dom.select("div.widget-info-semanal-body-row > div > ul > li").first();
    Pattern p = Pattern.compile("" +
        "<div class=\"row-fluid widget-info-semanal-body-row toHide\">[\\s]*" +
        "<div class=\"span12 widget-element widget-body\">[\\s]*" +
        "<ul class=\"unstyled\">[\\s]*" +
        "<li>Te (has pasado|faltan) ([0-9]{2}) hora[s]{0,1}[\\s]{0,1}([0-9]{2}){0,1}( minuto[s]{0,1}){0,1}[\\s]{0,1}</li>");
    //Matcher m = p.matcher(hoursElem.text());
    Matcher m = p.matcher(html);
    boolean positive = true;
    long hours = 0;
    long minutes = 0;
    if (m.find() && m.groupCount() >= 3) {
      positive = !m.group(1).equals("faltan");
      hours = Long.parseLong(m.group(2) != null ? m.group(2) : "0");
      minutes = Long.parseLong(m.group(3) != null ? m.group(3) : "0");
    }
    long totalMinutes = hours * 60 + minutes;
    return positive ? totalMinutes : -totalMinutes;
  }

  public static long getMonthHoursfromHTML(String html) {
    //Document dom
    //Element hoursElem = dom.select("div.widget-balance-mensual-body-row > div > ul > li").first();
    Pattern p = Pattern.compile("" +
        "<div class=\"row-fluid widget-balance-mensual-body-row toHide\">[\\s]*" +
        "<div class=\"span12 widget-element widget-body\">[\\s]*" +
        "<ul class=\"unstyled\">[\\s]*" +
        "<li>Te (has pasado|faltan) ([0-9]{2}) hora[s]{0,1}[\\s]{0,1}([0-9]{2}){0,1}( minuto[s]{0,1}){0,1}[\\s]{0,1}</li>");
    //Matcher m = p.matcher(hoursElem.text());
    Matcher m = p.matcher(html);
    boolean positive = true;
    long hours = 0;
    long minutes = 0;
    if (m.find() && m.groupCount() >= 3) {
      positive = !m.group(1).equals("faltan");
      hours = Long.parseLong(m.group(2) != null ? m.group(2) : "0");
      minutes = Long.parseLong(m.group(3) != null ? m.group(3) : "0");
    }
    long totalMinutes = hours * 60 + minutes;
    return positive ? totalMinutes : -totalMinutes;
  }

  public static User getUserFromJSON(JSONObject json) {
    User user = new User();
    try {
      for (Field field : User.class.getDeclaredFields()) {
        if (!json.isNull(field.getName())) {
          String setMethodName = "set" +
              Character.toUpperCase(field.getName().charAt(0)) +
              field.getName().substring(1, field.getName().length());
          if (field.getType().equals(String.class)) {

            Method setMethod = User.class.getDeclaredMethod(setMethodName, String.class);
            setMethod.invoke(user, json.getString(field.getName()));
            //field.set(user, json.getString(field.getName()));
          } else if (field.getType().equals(long.class)) {
            Method setMethod = User.class.getDeclaredMethod(setMethodName, long.class);
            setMethod.invoke(user, json.getLong(field.getName()));
          } else if (field.getType().equals(Calendar.class)) {
            Method setMethod = User.class.getDeclaredMethod(setMethodName, Calendar.class);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(df.parse(json.getString(field.getName())));
            setMethod.invoke(user, cal);
          }
        }
      }
    } catch (JSONException e) {
      Log.e(CHINPUN_MANAGER_TAG, e.getMessage(), e);
    } catch (IllegalAccessException e) {
      Log.e(CHINPUN_MANAGER_TAG, e.getMessage(), e);
    } catch (NoSuchMethodException e) {
      Log.e(CHINPUN_MANAGER_TAG, e.getMessage(), e);
    } catch (InvocationTargetException e) {
      Log.e(CHINPUN_MANAGER_TAG, e.getMessage(), e);
    } catch (ParseException e) {
      Log.e(CHINPUN_MANAGER_TAG, e.getMessage(), e);
    }
    return user;
  }

  public static Session getSessionFromJSON(JSONObject json) {
    Session session = new Session();
    try {
      for (Field field : Session.class.getDeclaredFields()) {
        if (!json.isNull(field.getName())) {
          String setMethodName = "set" +
              Character.toUpperCase(field.getName().charAt(0)) +
              field.getName().substring(1, field.getName().length());
          if (field.getType().equals(String.class)) {
            Method setMethod = Session.class.getDeclaredMethod(setMethodName, String.class);
            setMethod.invoke(session, json.getString(field.getName()));
            //field.set(user, json.getString(field.getName()));
          } else if (field.getType().equals(long.class)) {
            Method setMethod = Session.class.getDeclaredMethod(setMethodName, long.class);
            setMethod.invoke(session, json.getLong(field.getName()));
          } else if (field.getType().equals(User.class)) {
            Method setMethod = Session.class.getDeclaredMethod(setMethodName, User.class);
            User user = getUserFromJSON(json.getJSONObject(field.getName()));
            setMethod.invoke(session, user);
          } else if (field.getType().equals(Calendar.class)) {
            Method setMethod = Session.class.getDeclaredMethod(setMethodName, Calendar.class);
            //fix for format of attribute horas "0h00'"
            String calendar = json.getString(field.getName());
            Calendar cal = Calendar.getInstance();
            try {
              cal.setTime(new Date(Long.parseLong(calendar)));
              setMethod.invoke(session, cal);
            } catch (NumberFormatException e) {
              DateFormat df = new SimpleDateFormat("HH'h'mm''");
              cal.setTime(df.parse(calendar));
              setMethod.invoke(session, cal);
            }

          }
        }
      }
    } catch (JSONException e) {
      Log.e(CHINPUN_MANAGER_TAG, e.getMessage(), e);
    } catch (IllegalAccessException e) {
      Log.e(CHINPUN_MANAGER_TAG, e.getMessage(), e);
    } catch (NoSuchMethodException e) {
      Log.e(CHINPUN_MANAGER_TAG, e.getMessage(), e);
    } catch (InvocationTargetException e) {
      Log.e(CHINPUN_MANAGER_TAG, e.getMessage(), e);
    } catch (ParseException e) {
      Log.e(CHINPUN_MANAGER_TAG, e.getMessage(), e);
    }
    return session;
  }

  // ------------------------------------------------------ ACTIONS
  public void updateDataFromServer() {
    new GetSessionDataTask().execute();
  }

  public void startSession(Button button, String executingText, String doneText) {
    new StartSessionTask(button, executingText, doneText).execute();
  }

  public void updateSession(MenuItem refreshItem) {
    new UpdateSessionTask(refreshItem).execute();
  }

  public void updateSharedPreferences(String html) {
    if (html == null) {
      Log.d(CHINPUN_MANAGER_TAG, "Got null html from server when updating preferences.");
      return;
    }
    Log.d(CHINPUN_MANAGER_TAG, html);
    String userFirstName = getUserFirstNamefromHTML(html);
    String userLastName = getUserLastNamefromHTML(html);
    long userID = getUserIDfromHTML(html);
    //Document doc = Jsoup.parse(html);
    long weekHours = getWeekHoursfromHTML(html);
    long monthHours = getMonthHoursfromHTML(html);
    sharedPref.edit()
        .putString(SettingsFragment.KEY_PREF_USER_FIRSTNAME, userFirstName)
        .putString(SettingsFragment.KEY_PREF_USER_LASTNAME, userLastName)
        .putLong(SettingsFragment.KEY_PREF_USER_ID, userID)
        .putLong(SettingsFragment.KEY_PREF_WEEK_HOURS, weekHours)
        .putLong(SettingsFragment.KEY_PREF_MONTH_HOURS, monthHours)
        .commit();
  }

  public void updateSessionInSharedPreferences(String json) {
    if (json == null) {
      Log.d(CHINPUN_MANAGER_TAG, "Got null json from server when updating session.");
      return;
    }
    Log.d(CHINPUN_MANAGER_TAG, json);
    try {
      currentSession = getSessionFromJSON(new JSONObject(json));
    } catch (JSONException e) {
      Log.e(CHINPUN_MANAGER_TAG, "Error when parsing json of the session. Probably an HTML auth error page.", e);
      currentSession = null;
    }

    if (currentSession != null) {
      user = currentSession.getPersona();
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

  //--------------------------------------------------------- LIFECYCLE
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    // get type-safe reference to parent Activity
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //create background worker threads and tasks.
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    //initiate worker threads and tasks

    //insertamos valores por defecto en caso de que aun no esten
    if (getActivity() != null) {
      PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
      sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
      username = sharedPref.getString(SettingsFragment.KEY_PREF_USERNAME, "");
      password = sharedPref.getString(SettingsFragment.KEY_PREF_PASSWORD, "");
      userID = sharedPref.getLong(SettingsFragment.KEY_PREF_USER_ID, 0);
    } else {
      Log.e(CHINPUN_MANAGER_TAG, "Error when obtaining activity in ChinpunManagerServiceFragment.onActivityCreated()");
    }
    user = new User();
    currentSession = new Session();
  }

  //--------------------------------------------------- BACKGROUND TASKS

  /**
   * Reads an InputStream and converts it to a String.
   *
   * @param stream InputStream containing HTML from targeted site.
   * @return String concatenated according to len parameter.
   * @throws IOException
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
  private InputStream downloadUrl(String urlString, String method, List<AbstractMap.SimpleEntry> params) throws IOException {

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

  private class GetSessionDataTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
      try {
        InputStream stream = null;
        String str = "";
        try {
          stream = downloadUrl("http://oficina.crcit.es/chinpun/welcome", "GET", null);
          str = readIt(stream);
        } finally {
          if (stream != null) {
            stream.close();
          }
        }
        return str;
      } catch (IOException e) {
        final String message = e.getMessage();
        Log.e(CHINPUN_MANAGER_TAG, message, e);
        if (getActivity() != null) {
          getActivity().runOnUiThread(new Runnable() {
            public void run() {
              Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
          });
        }
        return null;
      }
    }

    /**
     * Uses the logging framework to display the output of the fetch
     * operation in the log fragment.
     */
    @Override
    protected void onPostExecute(String result) {
      updateSharedPreferences(result);
    }
  }

  private class StartSessionTask extends AsyncTask<String, Void, String> {
    private Button button;
    private String executingText;
    private String doneText;

    public StartSessionTask(Button button, String executingText, String doneText) {
      super();
      this.button = button;
      this.executingText = executingText;
      this.doneText = doneText;
    }

    @Override
    protected void onPreExecute() {
      if (button != null && executingText != null) {
        button.setText(executingText);
        button.setEnabled(false);
      }
    }

    @Override
    protected String doInBackground(String... urls) {
      try {
        InputStream stream = null;
        String str = "";
        List<AbstractMap.SimpleEntry> params = new ArrayList<AbstractMap.SimpleEntry>();
        params.add(new AbstractMap.SimpleEntry("nuevoEstado", "true"));
        try {
          stream = downloadUrl("http://oficina.crcit.es/chinpun/fichajes/abrirSesionFichaje", "POST", params);
          str = readIt(stream);
        } finally {
          if (stream != null) {
            stream.close();
          }
        }
        return str;
      } catch (IOException e) {
        final String message = e.getMessage();
        Log.e(CHINPUN_MANAGER_TAG, message, e);
        if (getActivity() != null) {
          getActivity().runOnUiThread(new Runnable() {
            public void run() {
              Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
          });
        }
        return null;
      }
    }

    /**
     * Uses the logging framework to display the output of the fetch
     * operation in the log fragment.
     */
    @Override
    protected void onPostExecute(String result) {
      updateSessionInSharedPreferences(result);
      if (button != null && doneText != null) {
        button.setText(doneText);
        // FIXME uncomment this when close session is implemented
        // button.setEnabled(true);
      }
    }
  }

  private class UpdateSessionTask extends AsyncTask<String, Void, String> {
    private MenuItem refreshItem;

    public UpdateSessionTask(MenuItem refreshItem) {
      super();
      this.refreshItem = refreshItem;
    }

    @Override
    protected void onPreExecute() {
      if (getActivity() != null && refreshItem != null) {
        refreshItem.setEnabled(false);

                /* Attach a rotating ImageView to the refresh item as an ActionView */
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) inflater.inflate(R.layout.refresh_action_view, null);

        Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.clockwise_refresh);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);

        refreshItem.setActionView(iv);
        iv.invalidate();
      }
    }

    @Override
    protected String doInBackground(String... urls) {
      try {
        if (userID == 0) {
          throw new IOException("User ID " + userID + " in memory not valid");
        }
        InputStream stream = null;
        String str = "";
        List<AbstractMap.SimpleEntry> params = new ArrayList<AbstractMap.SimpleEntry>();
        params.add(new AbstractMap.SimpleEntry("idUsuario", userID));
        try {
          stream = downloadUrl("http://oficina.crcit.es/chinpun/fichajes/cargarFichajeAbierto", "POST", params);
          str = readIt(stream);
        } finally {
          if (stream != null) {
            stream.close();
          }
        }
        return str;
      } catch (IOException e) {
        final String message = e.getMessage();
        Log.e(CHINPUN_MANAGER_TAG, message, e);
        if (getActivity() != null) {
          getActivity().runOnUiThread(new Runnable() {
            public void run() {
              Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_LONG).show();
            }
          });
        }

        return null;
      }
    }

    /**
     * Uses the logging framework to display the output of the fetch
     * operation in the log fragment.
     */
    @Override
    protected void onPostExecute(String result) {
      updateSessionInSharedPreferences(result);

      if (refreshItem != null && refreshItem.getActionView() != null) {
        refreshItem.setEnabled(true);
        refreshItem.getActionView().clearAnimation();
        refreshItem.setActionView(null);
      }
    }
  }
}
