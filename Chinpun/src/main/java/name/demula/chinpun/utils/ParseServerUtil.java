package name.demula.chinpun.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import name.demula.chinpun.model.Session;
import name.demula.chinpun.model.User;

/**
 * Created by Jesus on 28/08/13.
 */
public class ParseServerUtil {
  public static final String PARSE_SERVER_UTIL = "PARSE_SERVER_UTIL";

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
        "<li>Te (has pasado|faltan) ([0-9]+) hora[s]{0,1}[\\s]{0,1}([0-9]{1,2}){0,1}( minuto[s]{0,1}){0,1}[\\s]{0,1}</li>");
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
        "<li>Te (has pasado|faltan) ([0-9]+) hora[s]{0,1}[\\s]{0,1}([0-9]{1,2}){0,1}( minuto[s]{0,1}){0,1}[\\s]{0,1}</li>");
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
      Log.e(PARSE_SERVER_UTIL, e.getMessage(), e);
    } catch (IllegalAccessException e) {
      Log.e(PARSE_SERVER_UTIL, e.getMessage(), e);
    } catch (NoSuchMethodException e) {
      Log.e(PARSE_SERVER_UTIL, e.getMessage(), e);
    } catch (InvocationTargetException e) {
      Log.e(PARSE_SERVER_UTIL, e.getMessage(), e);
    } catch (ParseException e) {
      Log.e(PARSE_SERVER_UTIL, e.getMessage(), e);
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
      Log.e(PARSE_SERVER_UTIL, e.getMessage(), e);
    } catch (IllegalAccessException e) {
      Log.e(PARSE_SERVER_UTIL, e.getMessage(), e);
    } catch (NoSuchMethodException e) {
      Log.e(PARSE_SERVER_UTIL, e.getMessage(), e);
    } catch (InvocationTargetException e) {
      Log.e(PARSE_SERVER_UTIL, e.getMessage(), e);
    } catch (ParseException e) {
      Log.e(PARSE_SERVER_UTIL, e.getMessage(), e);
    }
    return session;
  }
}
