package name.demula.chinpun.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import name.demula.chinpun.R;
import name.demula.chinpun.fragments.SessionManagerFragment;
import name.demula.chinpun.fragments.SettingsFragment;
import name.demula.chinpun.fragments.servicies.ChinpunManagerServiceFragment;

public class MainActivity extends Activity implements SessionManagerFragment.OnSessionActionListener {

    public static final String CHINPUN_MAIN_ACTIVITY_TAG = "CHINPUN_MAIN_ACTIVITY_TAG";

    private static final int SESSION_MENU_POSITION = 0;
    private static final int PREFS_MENU_POSITION = 1;
    private ChinpunManagerServiceFragment chinpunManager;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Menu mMenu;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mDrawerMenuTitles;
    private String mPrefTitle;
    private String mSessionTitle;
    private boolean showRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chinpunManager = new ChinpunManagerServiceFragment();

        getFragmentManager().beginTransaction()
                .add(chinpunManager, ChinpunManagerServiceFragment.CHINPUN_MANAGER_TAG)
                .commit();

        mTitle = mDrawerTitle = getTitle();
        mDrawerMenuTitles = getResources().getStringArray(R.array.menu_items_array);
        mPrefTitle = getResources().getString(R.string.preferences);
        mSessionTitle = getResources().getString(R.string.app_name);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerMenuTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        this.mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_update).setVisible(!drawerOpen && showRefresh);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_update:
//                // create intent to perform web search for this planet
//                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
//                // catch event that there's no activity to handle intent
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
//                }
                onSessionUpdate(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSessionStart(Button button, String executingText, String doneText) {
        //TODO get events from clicks in SessionManagerFragment button
        chinpunManager.startSession(button, executingText, doneText);
    }

    @Override
    public void onSessionUpdate(MenuItem refreshItem) {
        chinpunManager.updateSession(refreshItem);
        chinpunManager.updateDataFromServer();
    }

    @Override
    public void onSessionEnd() {
        //TODO get events from clicks in SessionManagerFragment button
    }

    private void selectItem(int position) {
        switch (position) {
            case SESSION_MENU_POSITION:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new SessionManagerFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                        .addToBackStack(null)
                        .commit();
                setTitle(mSessionTitle);
                //unhide refresh button
                showRefresh = true;
                break;
            case PREFS_MENU_POSITION:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new SettingsFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                        .addToBackStack(null)
                        .commit();
                setTitle(mPrefTitle);
                //hide refresh button
                showRefresh = false;
                break;
        }
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}
