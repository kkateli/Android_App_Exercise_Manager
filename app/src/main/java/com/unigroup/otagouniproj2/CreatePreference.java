package com.unigroup.otagouniproj2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.unigroup.otagouniproj2.utils.Utils;
import java.util.HashMap;
import java.util.Map;

public class CreatePreference extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference dbRef;
    private String uid;
    private Spinner categories;
    private final String[] options = {"Exercise", "Learning", "Relaxation"};
    EditText editTextPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        this.uid = currentUser.getUid();
        dbRef = Utils.getDatabase().getReference("users");
        initPanel();

    }

    private void initPanel() {
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_create_preference);
        editTextPreference = findViewById(R.id.editPrefText);
        categories = findViewById(R.id.categoryDropDown);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreatePreference.this,
                R.layout.my_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);
        findViewById(R.id.createPreferenceButton).setOnClickListener(this);
        findViewById(R.id.cancelCreatePrefText).setOnClickListener(this);
        TextView tv = this.findViewById(R.id.cancelCreatePrefText);
        tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void createPreferenceForUser() {
        String selected = categories.getSelectedItem().toString().trim();
        String enteredPref = editTextPreference.getText().toString().trim();
        if (selected.equals("Exercise")) {
            updatePreference("exercisePref", enteredPref);
        } else if (selected.equals("Learning")) {
            updatePreference("learningPref", enteredPref);
        } else if (selected.equals("Relaxation")) {
            updatePreference("relaxationPref", enteredPref);
        }
    }

    private void updatePreference(final String path, final String enteredPref) {
        dbRef.child(uid).child("availablePreferences").child(path).addListenerForSingleValueEvent
                (new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> postValues = new HashMap<>();
                        int activityCount = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            postValues.put(snapshot.getKey(), snapshot.getValue());
                            activityCount++;
                        }
                        if (!postValues.containsValue(enteredPref)) {
                            postValues.put((Integer.toString(activityCount)), enteredPref);
                            dbRef.child(uid).child("availablePreferences").child(path).updateChildren(postValues,
                                    new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            Toast.makeText(getApplicationContext(), "Activity created!",
                                                    Toast.LENGTH_SHORT).show();
                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // Do something after 2s = 2000ms
                                                    Intent intent = new Intent(getApplicationContext(), PreferencesActivity.class);
                                                    startActivity(intent);
                                                }
                                            }, 2000);
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Activity already registered!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    /*
        Called when the save button is pressed.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createPreferenceButton:
                createPreferenceForUser();
                break;
            case R.id.cancelCreatePrefText:
                Intent intent = new Intent(getApplicationContext(), PreferencesActivity.class);
                startActivity(intent);
                break;
        }
    }

}
