package com.unigroup.otagouniproj2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.unigroup.otagouniproj2.utils.Utils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BlockoutTimePeriodActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner toItems;
    private Spinner fromItems;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference dbRef;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blockout_time_period);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        this.uid = currentUser.getUid();
        dbRef = Utils.getDatabase().getReference("users");


        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("0:00");
        spinnerArray.add("1:00");
        spinnerArray.add("2:00");
        spinnerArray.add("3:00");
        spinnerArray.add("4:00");
        spinnerArray.add("5:00");
        spinnerArray.add("6:00");
        spinnerArray.add("7:00");
        spinnerArray.add("8:00");
        spinnerArray.add("9:00");
        spinnerArray.add("10:00");
        spinnerArray.add("11:00");
        spinnerArray.add("12:00");
        spinnerArray.add("13:00");
        spinnerArray.add("14:00");
        spinnerArray.add("15:00");
        spinnerArray.add("16:00");
        spinnerArray.add("17:00");
        spinnerArray.add("18:00");
        spinnerArray.add("19:00");
        spinnerArray.add("20:00");
        spinnerArray.add("21:00");
        spinnerArray.add("22:00");
        spinnerArray.add("23:00");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.my_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toItems = findViewById(R.id.toSpinner);
        toItems.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromItems = findViewById(R.id.fromSpinner);
        fromItems.setAdapter(adapter);

        findViewById(R.id.cancel).setOnClickListener(this);
        TextView tv = this.findViewById(R.id.cancel);
        tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        findViewById(R.id.deleteAll).setOnClickListener(this);
        TextView tv2 = this.findViewById(R.id.deleteAll);
        tv2.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Button bt = findViewById(R.id.block);
        bt.setOnClickListener(this);


    }

    private void blockOutTimes() {

        final String selectedFrom = fromItems.getSelectedItem().toString().trim();
        final String selectedTo = toItems.getSelectedItem().toString().trim();
        final List<String> blockedList = getBlockedTimesList(selectedFrom, selectedTo);
        final AtomicBoolean displayed = new AtomicBoolean(false);
        
        for (int i = 0; i < blockedList.size(); i++) {
            dbRef.child(uid).child("blockedTimes").child(blockedList.get(i)).setValue(true, new DatabaseReference.CompletionListener() {
                public void onComplete(DatabaseError error, DatabaseReference ref) {
                    if(!(displayed.get())) {
                        Toast.makeText(getApplicationContext(), "Time Period Blocked!",
                                Toast.LENGTH_SHORT).show();
                        displayed.set(true);
                    }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 2s = 2000ms
                            startActivity(new Intent(getApplicationContext(), NavDrawActivity.class));
                        }
                    }, 2000);
                }
            });
        }
    }

    /*
        Yes this is ugly but I don't have time to refactor :O
     */
    private List<String> getBlockedTimesList(final String from, final String to) {
        List<String> blockedList = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm");
        DateTime selectedFromTime = dtf.parseDateTime(from);
        DateTime selectedToTime = dtf.parseDateTime(to);
        Period period = new Period(selectedFromTime, selectedToTime);
        int hours = period.getHours();
        int counter = 0;
        while (counter <= hours) {
            DateTime dateTime = selectedFromTime.plusHours(counter);
            String resultingTime = dateTime.hourOfDay().getAsString() + ":00";
            blockedList.add(resultingTime);
            counter++;
        }
        return blockedList;
    }

    public void deleteAllBlockedPeriods() {
        //Adapted from https://stackoverflow.com/questions/5127407/how-to-implement-a-confirmation-yes-no-dialogpreference
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Delete all blocked periods?");

        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                dbRef.child(uid).child("blockedTimes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "All Periods Unblocked!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.block:
                blockOutTimes();
                break;
            case R.id.cancel:
                Intent intent = new Intent(getApplicationContext(), NavDrawActivity.class);
                startActivity(intent);
                break;
            case R.id.deleteAll:
                deleteAllBlockedPeriods();
                break;
        }
    }

    /*
     Solution modified from: https://stackoverflow.com/questions/9888496/android-changing-the-actions-of-the-back-button
   */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, NavDrawActivity.class);
        startActivity(intent);
    }

}
