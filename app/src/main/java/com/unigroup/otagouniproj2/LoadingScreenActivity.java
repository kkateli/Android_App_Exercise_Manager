package com.unigroup.otagouniproj2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.unigroup.otagouniproj2.utils.Global;
import com.unigroup.otagouniproj2.utils.Utils;

import java.sql.Time;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LoadingScreenActivity extends AppCompatActivity {

    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        String currentUserUID = mAuth.getCurrentUser().getUid();
        dbRef = Utils.getDatabase().getReference().child("users").child(currentUserUID).
                child("availablePreferences");
        dbRef.keepSynced(true);
        //Tell application database has been cached, no need to do this again now.
        ((Global) this.getApplication()).setCachedFirebase(true);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_loading_screen);
        final Thread welcomeThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(5000);
                } catch (Exception e) {

                } finally {
                        Intent i = new Intent(LoadingScreenActivity.this,
                                PreferencesActivity.class);
                        startActivity(i);
                        finish();
                }
            }
        };

        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                welcomeThread.start();
            }
        }, 1, TimeUnit.SECONDS);
    }
}
