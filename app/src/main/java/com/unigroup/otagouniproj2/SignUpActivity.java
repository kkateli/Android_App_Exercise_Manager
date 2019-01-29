package com.unigroup.otagouniproj2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.unigroup.otagouniproj2.utils.Utils;
import domain.AvailablePreferences;
import domain.MyAppUser;
import domain.SetPreferences;

/**
 * Created by Harry Ellerm on 10/03/18.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextPassword, editTextName;
    private FirebaseAuth mAuth;
    ProgressBar pb;
    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = Utils.getDatabase().getReference("users");
        initPanel();
    }

    private void initPanel() {
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.signup);
        TextView tv = this.findViewById(R.id.textLogin);
        tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.nameText);
        pb = findViewById(R.id.progressbar);
        pb.setVisibility(View.INVISIBLE);
        findViewById(R.id.signUpButton).setOnClickListener(this);
        findViewById(R.id.textLogin).setOnClickListener(this);
    }

    /*
        Registers a new user. Making use of FireBase version 10.0.1 to avoid
        conflicts which cause the registration process to fail. Generally, the
        Google Play services version should be higher than the FireBase version.
     */
    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        if (validateInput(email, password, name)) {
            pb.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        //called when completed
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pb.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registered Successfully!",
                                        Toast.LENGTH_SHORT).show();
                                final String uid = mAuth.getCurrentUser().getUid();
                                addUserInfo(uid);
                                Intent intent = new Intent(getApplicationContext(), LoadingScreenActivity.class);
                                startActivity(intent);
                            } else {
                                //User is already registered
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(getApplicationContext(), "Account already registered!",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    //Registration failed
                                    Toast.makeText(getApplicationContext(), "Whoops! Something went wrong.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        //remove call to the super class
        //super.onBackPressed();
    }

    /* Adds a set of user information to the real time firebase database */
    private void addUserInfo(String uid) {
        String email = editTextEmail.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        MyAppUser user = new MyAppUser();
        user.setName(name);
        user.setEmail(email);
        setUserUpWithDefaultFields(user);
        db.child(uid).setValue(user);
    }

    private void setUserUpWithDefaultFields(MyAppUser user) {
        AvailablePreferences uP = new AvailablePreferences();
        uP.addExercisePref("Swimming");
        uP.addExercisePref("Running");
        uP.addExercisePref("Group Sports");
        uP.addExercisePref("Extreme Ironing");
        uP.addLearningPref("Learn a new language");
        uP.addLearningPref("Cook a new recipe");
        uP.addLearningPref("Watch a documentary");
        uP.addLearningPref("Study for 310");
        uP.addRelaxtionPref("Watch TV");
        uP.addRelaxtionPref("Read a book");
        uP.addRelaxtionPref("Juggle");
        uP.addRelaxtionPref("Play Runescape");
        user.setAvailablePreferences(uP);
        SetPreferences sP = new SetPreferences();
        sP.setExercisePreference("Swimming");
        sP.setLearningPreference("Learn a new language");
        sP.setRelaxationPreference("Watch TV");
        user.setSetPreferences(sP);
    }


    /*
    Validates a new users information, checks that the email is provided,
    the email is a valid email address, the password is provided, and that
    the password is greater than 6 characters long.
    @return boolean result, true if all fields are valid
    @param email, the users email
    @param password, the users password
    */
    private boolean validateInput(String email, String password, String name) {
        boolean result = true;
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            result = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email is not valid");
            editTextEmail.requestFocus();
            result = false;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            result = false;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Password must be greater than 6 characters");
            editTextPassword.requestFocus();
            result = false;
        }
        if (name.isEmpty()) {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            result = false;
        }
        return result;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpButton:
                registerUser();
                break;
            case R.id.textLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
