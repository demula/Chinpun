package name.demula.chinpun.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import name.demula.chinpun.R;
import name.demula.chinpun.fragments.SessionManagerFragment;
import name.demula.chinpun.fragments.SettingsFragment;

public class MainActivity extends ActionBarActivity {

  public static final String CHINPUN_MAIN_ACTIVITY_TAG = "CHINPUN_MAIN_ACTIVITY_TAG";
  private static final int SESSION_MENU_POSITION = 0;
  private static final int PREFS_MENU_POSITION = 1;
  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;
  private ActionBarDrawerToggle mDrawerToggle;
  private CharSequence mDrawerTitle;
  private CharSequence mTitle;
  private String[] mDrawerMenuTitles;
  private String mPrefTitle;
  private String mSessionTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

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
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

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
        getSupportActionBar().setTitle(mTitle);
        supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }

      public void onDrawerOpened(View drawerView) {
        getSupportActionBar().setTitle(mDrawerTitle);
        supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }
    };
    mDrawerLayout.setDrawerListener(mDrawerToggle);

    if (savedInstanceState == null) {
      selectItem(0);
    }
  }

  /* Called whenever we call invalidateOptionsMenu() */
  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // The action bar home/up action should open or close the drawer.
    // ActionBarDrawerToggle will take care of this.
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void selectItem(int position) {
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    Fragment fragment;
    switch (position) {
      case SESSION_MENU_POSITION:
        fragment = getSupportFragmentManager()
            .findFragmentByTag(SessionManagerFragment.CHINPUN_SESSION_TAG);
        if (fragment == null || !fragment.isVisible()) {
          ft.replace(R.id.content_frame,
              new SessionManagerFragment(),
              SessionManagerFragment.CHINPUN_SESSION_TAG);
          ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
          ft.addToBackStack(null);
          setTitle(mSessionTitle);
        }

        break;
      case PREFS_MENU_POSITION:
        fragment = getSupportFragmentManager()
            .findFragmentByTag(SettingsFragment.CHINPUN_SETTINGS_TAG);
        if (fragment == null || !fragment.isVisible()) {
          ft.replace(R.id.content_frame,
              new SettingsFragment(),
              SettingsFragment.CHINPUN_SETTINGS_TAG);
          ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
          ft.addToBackStack(null);
          setTitle(mPrefTitle);
        }
        break;
    }
    ft.commit();
    // update selected item and title, then close the drawer
    mDrawerList.setItemChecked(position, true);
    mDrawerLayout.closeDrawer(mDrawerList);
  }

  @Override
  public void setTitle(CharSequence title) {
    mTitle = title;
    getSupportActionBar().setTitle(mTitle);
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
