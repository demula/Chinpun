package name.demula.chinpun.test;


import android.content.Context;
import android.test.InstrumentationTestCase;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import name.demula.chinpun.fragments.servicies.ChinpunManagerServiceFragment;
import name.demula.chinpun.model.Session;
import name.demula.chinpun.model.User;

import static name.demula.chinpun.fragments.servicies.ChinpunManagerServiceFragment.getMonthHoursfromHTML;
import static name.demula.chinpun.fragments.servicies.ChinpunManagerServiceFragment.getSessionFromJSON;
import static name.demula.chinpun.fragments.servicies.ChinpunManagerServiceFragment.getUserFirstNamefromHTML;
import static name.demula.chinpun.fragments.servicies.ChinpunManagerServiceFragment.getUserFromJSON;
import static name.demula.chinpun.fragments.servicies.ChinpunManagerServiceFragment.getUserIDfromHTML;
import static name.demula.chinpun.fragments.servicies.ChinpunManagerServiceFragment.getUserLastNamefromHTML;
import static name.demula.chinpun.fragments.servicies.ChinpunManagerServiceFragment.getWeekHoursfromHTML;

/**
 * Created by Jesus on 29/07/13.
 */
public class ChinpunManagerServiceFragmentTest extends InstrumentationTestCase {
    @Mock
    Context context;
    ChinpunManagerServiceFragment chinpunManagerServiceFragment;

    @Override
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    public void testGetUserIDfromHTML() throws Exception {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        getInstrumentation().getContext().getResources().openRawResource(R.raw.welcome),
                        "UTF-8"));
        String html;
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            html = sb.toString();
        } finally {
            br.close();
        }
        assertNotNull(html);
        assertEquals(91, getUserIDfromHTML(html));
    }

    public void testGetUserFirstNamefromHTML() throws Exception {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        //getInstrumentation().getContext().getAssets().open("raw/welcome.html"),
                        getInstrumentation().getContext().getResources().openRawResource(R.raw.welcome),
                        "UTF-8"));
        String html;
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            html = sb.toString();
        } finally {
            br.close();
        }
        assertNotNull(html);
        //FIXME it doesn't assert propertly when using accented characters
        assertEquals("Jesus", getUserFirstNamefromHTML(html));
    }

    public void testGetUserLastNamefromHTML() throws Exception {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        //getInstrumentation().getContext().getAssets().open("raw/welcome.html"),
                        getInstrumentation().getContext().getResources().openRawResource(R.raw.welcome),
                        "UTF-8"));
        String html;
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            html = sb.toString();
        } finally {
            br.close();
        }
        assertNotNull(html);
        assertEquals("de Mula", getUserLastNamefromHTML(html));
    }

    public void testGetWeekHoursfromHTML() throws Exception {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        //getInstrumentation().getContext().getAssets().open("raw/welcome.html"),
                        getInstrumentation().getContext().getResources().openRawResource(R.raw.welcome),
                        "UTF-8"));
        String html;
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            html = sb.toString();
        } finally {
            br.close();
        }
        assertNotNull(html);

        //Document dom = Jsoup.parse(html);
        //assertNotNull(dom);
        //assertEquals(new String("Te faltan 20 horas"), getWeekHoursfromHTML(dom));
        assertEquals(-20 * 60, getWeekHoursfromHTML(html));
    }

    public void testGetMonthHoursfromHTML() throws Exception {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        //getInstrumentation().getContext().getAssets().open("raw/welcome.html"),
                        getInstrumentation().getContext().getResources().openRawResource(R.raw.welcome),
                        "UTF-8"));
        String html;
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            html = sb.toString();
        } finally {
            br.close();
        }
        assertNotNull(html);

        //Document dom = Jsoup.parse(html);
        //assertNotNull(dom);
        //assertEquals(new String("Te has pasado 35 horas 15 minutos"), getMonthHoursfromHTML(dom));
        assertEquals(35 * 60 + 15, getMonthHoursfromHTML(html));
    }

    public void testGetUserFromJSON() throws Exception {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        getInstrumentation().getContext().getResources().openRawResource(R.raw.user),
                        "UTF-8"));
        String jsonText;
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            jsonText = sb.toString();
        } finally {
            br.close();
        }
        assertNotNull(jsonText);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        JSONObject json = new JSONObject(jsonText);
        User user = getUserFromJSON(json);

        assertEquals(91, user.getIdPersona());
        assertEquals("Jesus", user.getNombre());
        assertEquals("de Mula", user.getApellido1());
        assertEquals("Cano", user.getApellido2());
        assertEquals("Jesus de Mula Cano", user.getNombreCompleto());
        assertEquals("jdemula", user.getIdLdap());
        assertEquals("A", user.getEstado());
        assertEquals(df.parse("1986-03-17"), user.getFechaNacimiento().getTime());
        assertEquals(df.parse("2012-10-03"), user.getFechaAlta().getTime());
    }

    public void testGetSessionFromJSON() throws Exception {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        getInstrumentation().getContext().getResources().openRawResource(R.raw.fichaje),
                        "UTF-8"));
        String jsonText;
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            jsonText = sb.toString();
        } finally {
            br.close();
        }
        assertNotNull(jsonText);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        JSONObject json = new JSONObject(jsonText);
        Session session = getSessionFromJSON(json);

        assertEquals(63565, session.getIdFichaje());

        assertEquals(91, session.getPersona().getIdPersona());
        assertEquals("Jesus", session.getPersona().getNombre());
        assertEquals("de Mula", session.getPersona().getApellido1());
        assertEquals("Cano", session.getPersona().getApellido2());
        assertEquals("Jesus de Mula Cano", session.getPersona().getNombreCompleto());
        assertEquals("jdemula", session.getPersona().getIdLdap());
        assertEquals("A", session.getPersona().getEstado());
        assertEquals(df.parse("1986-03-17"), session.getPersona().getFechaNacimiento().getTime());
        assertEquals(df.parse("2012-10-03"), session.getPersona().getFechaAlta().getTime());

        assertEquals(new Date(1376308041847l), session.getFechaInicio().getTime());

        df = new SimpleDateFormat("HH'h'mm''");
        assertEquals(df.parse("0h00'"), session.getHoras().getTime());
        assertEquals(false, session.isHorasExtras());
    }
}
