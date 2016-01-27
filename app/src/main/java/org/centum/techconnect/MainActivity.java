package org.centum.techconnect;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.centum.techconnect.fragments.SelfHelpFragment;
import org.centum.techconnect.model.DeviceManager;
import org.json.JSONException;

import java.io.IOException;

import butterknife.Bind;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int FRAGMENT_SELF_HELP = 0;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    private Fragment[] fragments = new Fragment[]{new SelfHelpFragment()};
    private String[] fragmentTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceManager.get(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentTitles = getResources().getStringArray(R.array.fragment_titles);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        setFragment(FRAGMENT_SELF_HELP);

        loadDevices();
    }

    private void loadDevices() {
        new AsyncTask<Void, Void, Void>() {

            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setIndeterminate(true);
                dialog.setTitle("Loading resources");
                dialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    DeviceManager.get().loadDevices();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                dialog.dismiss();
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        setFragment(fragment);
        return true;
    }

    private void setFragment(int frag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, fragments[frag])
                .addToBackStack(null)
                .commit();
        setTitle(fragmentTitles[frag]);
        navigationView.getMenu().getItem(FRAGMENT_SELF_HELP).setChecked(true);
    }
}
