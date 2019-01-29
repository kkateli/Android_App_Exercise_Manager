package com.unigroup.otagouniproj2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.unigroup.otagouniproj2.utils.Global;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    EditText editTextEmail, editTextPassword;
    ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        // Check if the user is signed in
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // If user is signed in, exit the login screen automatically
            Intent intent = new Intent(getApplicationContext(), NavDrawActivity.class);
            //Will clear all activities at the top of the stack, if we don't do this
            //the user can backtrack to the login page.
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();

        } else {
            initPanel();
        }

    }

    public void initPanel() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        findViewById(R.id.signUpButton).setOnClickListener(this); //sign up
        findViewById(R.id.loginButton).setOnClickListener(this); //log in
        this.editTextEmail = findViewById(R.id.editTextEmail);
        this.editTextPassword = findViewById(R.id.editTextPassword);
        this.pb = findViewById(R.id.progressbar);
    }


    /*
        Logs a user in via base, displays warning message
     */
    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (validateInput(email, password)) {
            pb.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(email, password).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pb.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getApplicationContext(), NavDrawActivity.class);
                                //Will clear all activities at the top of the stack, if we don't do this
                                //the user can backtrack to the login page.
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /*
    Validates a users information, checks that the email is provided,
    the email is a valid email address, the password is provided, and that
    the password is greater than 6 characters long.
    @return boolean result, true if all fields are valid
    @param email, the users email
    @param password, the users password
    */
    private boolean validateInput(String email, String password) {
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
        return result;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpButton: //sign up
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.loginButton: //log in
                userLogin();
                break;
        }
    }
}