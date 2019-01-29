package com.unigroup.otagouniproj2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.unigroup.otagouniproj2.utils.Global;


public class NavDrawActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Button today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPanel();
    }

    public void initPanel() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_nav_draw);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        this.today = findViewById(R.id.todayButton);
        this.today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent doButton = new Intent(NavDrawActivity.this, DayViewCalendar.class);
                startActivity(doButton);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_draw, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logoutSafely();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        switch (item.getItemId()) {
            case R.id.nav_preferences:
                if (!((Global) getApplication()).getCachedFirebase()) {
                    startActivity(new Intent(NavDrawActivity.this, LoadingScreenActivity.class));
                } else {
                    //Otherwise just head straight to preferences activity
                    startActivity(new Intent(NavDrawActivity.this, PreferencesActivity.class));
                    finish();
                }
                break;
            case R.id.nav_tools:
                startActivity(new Intent(NavDrawActivity.this, BlockoutTimePeriodActivity.class));
                finish();
                break;
            case R.id.nav_support:
                startActivity(new Intent(NavDrawActivity.this, Support.class));
                finish();
                break;
            case R.id.nav_share:
                startActivity(new Intent(NavDrawActivity.this, Share.class));
                finish();
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void logoutSafely() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }
}
