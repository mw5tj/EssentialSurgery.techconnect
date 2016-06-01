package org.centum.techconnect;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.centum.techconnect.fragments.ReportsFragment;
import org.centum.techconnect.fragments.SelfHelpFragment;
import org.centum.techconnect.resources.ResourceHandler;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Entry activity.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int FRAGMENT_SELF_HELP = 0;
    private static final int FRAGMENT_LOGS = 1;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    private Fragment[] fragments = new Fragment[]{new SelfHelpFragment(), new ReportsFragment()};
    private String[] fragmentTitles;
    private int fragment = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ResourceHandler.get(this);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fragmentTitles = getResources().getStringArray(R.array.fragment_titles);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            loadResources(FRAGMENT_SELF_HELP);
        } else {
            loadResources(savedInstanceState.getInt("frag", FRAGMENT_SELF_HELP));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("frag", fragment);
    }

    /**
     * Loads the resources in the background, while the splash is showing.
     * All missing resources are downloaded only, so subsequent runs should
     * just use the cached stuff.
     * <p/>
     * Splash shown for a min of 2000ms
     *
     * @param fragToOpen
     */
    private void loadResources(final int fragToOpen) {
        new AsyncTask<Void, Void, Void>() {

            Dialog dialog;

            @Override
            protected void onPreExecute() {
                dialog = new Dialog(MainActivity.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
                dialog.setContentView(R.layout.loading_layout);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                dialog.dismiss();
                setFragment(fragToOpen);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                long time = System.currentTimeMillis();
                ResourceHandler.get().loadResources();
                if ((System.currentTimeMillis() - time) < 2000) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.execute();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        int fragment = -1;
        if (id == R.id.nav_self_help) {
            fragment = FRAGMENT_SELF_HELP;
        } else if (id == R.id.nav_reports) {
            fragment = FRAGMENT_LOGS;
        } else if (id == R.id.nav_refresh) {
            ResourceHandler.get().clear();
            new AlertDialog.Builder(this).setTitle("Sync")
                    .setMessage("Resources will be synced on next app start")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        setFragment(fragment);
        return true;
    }

    private void setFragment(int frag) {
        if (this.fragment != frag || this.fragment == -1) {
            this.fragment = frag;
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, fragments[frag])
                    .commit();
            setTitle(fragmentTitles[frag]);
            navigationView.getMenu().getItem(frag).setChecked(true);
        }
    }
}
