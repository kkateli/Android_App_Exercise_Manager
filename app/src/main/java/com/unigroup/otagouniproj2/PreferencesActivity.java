package com.unigroup.otagouniproj2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.unigroup.otagouniproj2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PreferencesActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private String uid;
    private Spinner exercise;
    private Boolean exSpinnerTouched = false;
    private Spinner learning;
    private Boolean leaSpinnerTouched = false;
    private Spinner leisure;
    private Boolean leiSpinnerTouched = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        this.uid = currentUser.getUid();
        dbRef = Utils.getDatabase().getReference("users");
        initPanel();
    }

    private void initPanel() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_preferences);
        findViewById(R.id.doneButton).setOnClickListener(this);
        findViewById(R.id.customText).setOnClickListener(this);
        TextView tv = this.findViewById(R.id.customText);
        tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //Set up first drop down combo box
        exercise = findViewById(R.id.exerciseDropDown);
        populateSpinner(exercise, "exercisePref");
        exercise.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                exSpinnerTouched = true;
                return false;
            }
        });
        //Set up second drop down combo box
        learning = findViewById(R.id.learningDropDown);
        populateSpinner(learning, "learningPref");
        learning.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                leaSpinnerTouched = true;
                return false;
            }
        });
        //Set up third drop down combo box
        leisure = findViewById(R.id.leisureDropDown);
        populateSpinner(leisure, "relaxationPref");
        leisure.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                leiSpinnerTouched = true;
                return false;
            }
        });
    }

    private void populateSpinner(final Spinner spinner, String child) {
        dbRef.child(uid).child("availablePreferences").child(child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> items = new ArrayList<>();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String ex = areaSnapshot.getValue(String.class);
                    items.add(ex);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(PreferencesActivity.this,
                        R.layout.my_spinner_item, items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpExerciseSpinnerOnItemSelected();
        setUpLearningSpinnerOnItemSelected();
        setUpLeisureSpinnerOnItemSelected();
    }

    private void setUpLeisureSpinnerOnItemSelected() {
        leisure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (leiSpinnerTouched) {
                    String selectedItem = adapterView.getSelectedItem().toString().trim();
                    dbRef.child(uid).child("setPreferences").child("relaxationPreference").setValue(selectedItem).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Preference updated!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                leiSpinnerTouched = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    private void setUpLearningSpinnerOnItemSelected() {
        learning.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (leaSpinnerTouched) {
                    String selectedItem = adapterView.getSelectedItem().toString().trim();
                    dbRef.child(uid).child("setPreferences").child("learningPreference").setValue(selectedItem).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Preference updated!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                leaSpinnerTouched = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setUpExerciseSpinnerOnItemSelected() {
        exercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (exSpinnerTouched) {
                    String selectedItem = adapterView.getSelectedItem().toString().trim();
                    dbRef.child(uid).child("setPreferences").child("exercisePreference").setValue(selectedItem).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Preference updated!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                exSpinnerTouched = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /*
        Called when the save button is pressed.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.doneButton:
                startActivity(new Intent(PreferencesActivity.this,
                        NavDrawActivity.class));
                break;
            case R.id.customText:
                startActivity(new Intent(this, CreatePreference.class));
        }
    }

    //Disable back button for time being
    @Override
    public void onBackPressed() { }

}
