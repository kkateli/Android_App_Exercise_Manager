package com.unigroup.otagouniproj2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.unigroup.otagouniproj2.data.IPopup;
import com.unigroup.otagouniproj2.decoration.CdvDecorationDefault;
import com.unigroup.otagouniproj2.utils.Utils;
import com.unigroup.otagouniproj2.view.CalendarDayView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.unigroup.otagouniproj2.model.Popup;

import org.joda.time.LocalTime;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import domain.TimetableActivity;
import domain.UserTimetable;

import com.unigroup.otagouniproj2.view.PopupView;

public class DayViewCalendar extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {

    private CalendarDayView dayView;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private String uid;
    private ArrayList<IPopup> popups = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_activity_button, menu);

        MenuItem itemMenuCreate = menu.findItem(R.id.activity_button);
        itemMenuCreate.setOnMenuItemClickListener(this);

        MenuItem itemMenuSave = menu.findItem(R.id.save_button);
        itemMenuSave.setOnMenuItemClickListener(this);

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_day_view_calendar);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.menubar);
        setSupportActionBar(toolbar);
        dayView = findViewById(R.id.calendar);
        dayView.setLimitTime(0, 23);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        this.uid = currentUser.getUid();
        dbRef = Utils.getDatabase().getReference("users");

        retrieveTimeTableIfExists();

        ((CdvDecorationDefault) (dayView.getDecoration())).setOnPopupClickListener(
                new PopupView.OnEventPopupClickListener() {
                    @Override
                    public void onPopupClick(PopupView view, IPopup data) {
                        Log.e("TAG", "onPopupClick:" + data.getTitle());
                        // View Item Click
                        Intent intent = new Intent(DayViewCalendar.this, PreferencesActivity.class);
                        intent.putExtra("data", data);
                        startActivity(intent);
                    }

                    @Override
                    public void onQuoteClick(PopupView view, IPopup data) {
                    }

                    @Override
                    public void onLoadData(PopupView view, ImageView end,
                                           IPopup data) {
                    }
                });

    }

    private void retrieveTimeTableIfExists() {
        final List<IPopup> popupList = new ArrayList<>();

        dbRef.child(uid).child("TimeTable").child("activitylist").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {

                    for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {

                        LocalTime startTime = null;
                        LocalTime endTime = null;
                        String name = "";
                        boolean setStartTime = false;
                        boolean setEndTime = false;
                        boolean setName = false;

                        //Get rid of brackets and commas inserted by FB
                        String str = areaSnapshot.getValue().toString();
                        str = str.replaceAll("[\\[\\]{}]", "");
                        String array1[] = str.split(",");

                        for (int i = 0; i < array1.length; i++) {
                            if (i == 0) {
                                String[] split = array1[i].split("=");
                                endTime = new LocalTime(split[1]);
                                System.out.println(endTime);
                                setEndTime = true;

                            } else if (i == 1) {
                                String[] split = array1[i].split("=");
                                name = split[1];
                                System.out.println(name);
                                setName = true;

                            } else {
                                String[] split = array1[i].split("=");
                                startTime = new LocalTime(split[1]);
                                System.out.println(startTime);
                                setStartTime = true;

                            }

                            if (setStartTime && setEndTime && setName) {
                                Popup popup = new Popup(i, name, "", startTime.toString(), endTime.toString(), "");
                                popupList.add(popup);
                            }
                        }
                    }
                    dayView.setPopups(popupList);
                }
                if (popupList != null && !popupList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Restored Timetable!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void getBlockedTimes(final List<String> preferences) {
        final List<org.joda.time.LocalTime> timeList = new ArrayList<>();
        dbRef.child(uid).child("blockedTimes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    if (!timeList.contains(new LocalTime(areaSnapshot.getKey()))) {
                        timeList.add(new LocalTime(areaSnapshot.getKey()));
                    }
                }
                displayDailyEvents(preferences, timeList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void displayDailyEvents(final List<String> preferences, final List<LocalTime> blockedTimes) {
        LocalTime randomStart;
        List<LocalTime> availableTimes = findAvailableTimes(blockedTimes);
        //TODO fix this to allow them to only generate one or two instead, also let them customize length
        if(availableTimes.size() < 6) {
            AlertDialog.Builder build = new AlertDialog.Builder(this);
            build.setMessage("Oops. You have too few hours available to generate" +
                    "3 two hour activities! Please unblock some and try again!");
            build.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            return;
        }
        int i = 0;
        for (String pref : preferences) {
            randomStart = getRandomItem(availableTimes);
            Popup popup = new Popup(i, pref, "",
                    randomStart.toString(), randomStart.plusHours(2).toString(),
                    "");
            popups.add(popup);
            i++;
        }
        dayView.setPopups(popups);
    }


    private LocalTime getRandomItem(List<LocalTime> list) {
        Random random = new Random();
        int listSize = list.size();
        int randomIndex = random.nextInt(listSize);
        LocalTime t = list.get(randomIndex);
        list.remove(t.minusHours(1)); //So they don't overlap
        list.remove(t.plusHours(1));
        list.remove(t.plusHours(2));
        list.remove(t);
        return t;
    }


    private List<LocalTime> findAvailableTimes(List<LocalTime> blocked) {
        List<LocalTime> available = new ArrayList<>();
        LocalTime possibleTime;

        for(int i = 0; i < 22; i++) {
           possibleTime = new LocalTime().
                    withHourOfDay(i).withMinuteOfHour(0).
                    withSecondOfMinute(0).withMillisOfSecond(0);

            if(!blocked.contains(possibleTime)) {
                available.add(possibleTime);
            }
        }
        return available;
    }

    private void generateRandomActivities() {
        popups.clear();
        final ArrayList<String> items = new ArrayList();
        dbRef.child(uid).child("setPreferences").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String ex = areaSnapshot.getValue(String.class);
                    items.add(ex);
                }
                getBlockedTimes(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void saveActivities() {
        if (this.popups != null && this.popups.size() > 1) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setupLoadingScreen();
                }
            });
        } else {
            Toast.makeText(this, "No events to save!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTimeTableToFirebase(final ArrayList<IPopup> popupList, final RelativeLayout layout) {
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm");
        UserTimetable timetable = new UserTimetable();

        List<TimetableActivity> activityList = new ArrayList<>();
        for (IPopup popup : popupList) {
            TimetableActivity activity = new TimetableActivity();
            activity.setActivityName(popup.getTitle());
            activity.setActivityStart(timeF.format(popup.getStartTime().getTime()));
            activity.setActivityEnd(timeF.format(popup.getEndTime().getTime()));
            activityList.add(activity);
        }
        timetable.setActivitylist(activityList);

        final Toast toast = Toast.makeText(this, "Timetable Saved!", Toast.LENGTH_SHORT);

        dbRef.child(uid).child("TimeTable").setValue(timetable).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Reset Panel
                setContentView(R.layout.activity_day_view_calendar);
                android.support.v7.widget.Toolbar toolbar = findViewById(R.id.menubar);
                setSupportActionBar(toolbar);
                dayView = findViewById(R.id.calendar);
                dayView.setLimitTime(0, 23);
                dayView.setPopups(popupList);
                dayView.refresh();
                toast.show();
            }
        });
    }

    private void setupLoadingScreen() {
        final RelativeLayout layout = new RelativeLayout(this);
        ProgressBar progressBar = new ProgressBar(DayViewCalendar.this, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        layout.setBackground(ContextCompat.getDrawable(this, R.drawable.background));

        TextView tv = new TextView(DayViewCalendar.this);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(25.0f);
        tv.setText("Saving your Timetable!");

        layout.addView(tv);
        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) tv.getLayoutParams();

        int mHeight = this.getResources().getDisplayMetrics().heightPixels;

        p.topMargin = mHeight / 2 + 100; // in PX
        p.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tv.setLayoutParams(p);

        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar, params);
        setContentView(layout);

        saveTimeTableToFirebase(this.popups, layout);

    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.activity_button:
                generateRandomActivities();
                break;
            case R.id.save_button:
                saveActivities();
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, NavDrawActivity.class);
        startActivity(intent);
        finish();
    }


}

