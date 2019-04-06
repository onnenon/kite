package com.team100.kite_master;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.team100.kite_master.devtests.UserTestsFragment;
import com.team100.kite_master.forum.ForumTopicListFragment;
import com.team100.kite_master.help.HelpFragment;
import com.team100.kite_master.login.LoginFragment;
import com.team100.kite_master.login.SaveSharedPreference;
import com.team100.kite_master.messages.MessagesFragment;
import com.team100.kite_master.profile.ProfileFragment;
import com.team100.kite_master.search.SearchFragment;
import com.team100.kite_master.settings.SettingsFragment;
import com.team100.kite_master.userdata.User;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //global variables
    public int cur_screen;
    public User currentUser;
    private String LOCAL_IP_ADDRESS;

    //global layout elements
    DrawerLayout drawer;
    public Toolbar toolbar;


    //on create method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //set drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //set navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //instantiate user with blank fields
        currentUser = new User("", "", "", 0, false, false);

        //login - if they were previously logged in auto login, else require log in
        if (SaveSharedPreference.getUserName(MainActivity.this).length() == 0) {
            displayLoginScreen();
        } else {
            logIn();
        }
    }


    //back button goes to forum unless it is on forum, then it closes app
    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (cur_screen == R.id.login_screen) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        } else if (cur_screen != R.id.nav_forum) {
            displaySelectedScreen(R.id.nav_forum);
        } else if (count > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }


    private void displayLoginScreen() {
        cur_screen = R.id.login_screen;
        Fragment fragment = new LoginFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

    }

    public void setLocalIP(String ip){
        LOCAL_IP_ADDRESS = ip;
    }

    private void logIn() {
        currentUser.setUsername(SaveSharedPreference.getUserName(MainActivity.this));
        LOCAL_IP_ADDRESS = SaveSharedPreference.getHostIp(MainActivity.this);
        displaySelectedScreen(R.id.nav_forum);
    }



    public void setNavDrawerData(String username, String displayname){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navDisplayname = headerView.findViewById(R.id.nav_display_name);
        navDisplayname.setText(displayname);
        TextView navUsername = headerView.findViewById(R.id.nav_user_name);
        String atUsername = "@" + username;
        navUsername.setText(atUsername);
    }

    public void setCurScreen(int screenID){
        cur_screen = screenID;
    }


    private void displaySelectedScreen(int itemId) {
        cur_screen = itemId;
        //creating fragment object
        Fragment fragment = null;
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_forum:
                fragment = new ForumTopicListFragment();
                break;
            case R.id.nav_search:
                fragment = new SearchFragment();
                break;
            case R.id.nav_messages:
                fragment = new MessagesFragment();
                break;
            case R.id.nav_favorites:
                fragment = new ProfileFragment();
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            case R.id.nav_help:
                fragment = new HelpFragment();
                break;
            case R.id.nav_user_tests:
                fragment = new UserTestsFragment();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("serverIP", LOCAL_IP_ADDRESS);
            bundle.putStringArray("userData", currentUser.toArray());
            fragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


}
