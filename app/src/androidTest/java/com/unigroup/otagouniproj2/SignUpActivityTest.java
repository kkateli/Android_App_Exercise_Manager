package com.unigroup.otagouniproj2;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class SignUpActivityTest {
    private SignUpActivity signUpActivity = null;


    @Rule
    public ActivityTestRule<SignUpActivity> signUpActivityTestRule =
            new ActivityTestRule<>(SignUpActivity.class);

    @Before
    public void setUp() throws Exception {
        signUpActivity = signUpActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View welcomeText = signUpActivity.findViewById(R.id.welcomeText);
        View signInText = signUpActivity.findViewById(R.id.signInText);
        View editTextEmail = signUpActivity.findViewById(R.id.editTextEmail);
        View editTextPassword = signUpActivity.findViewById(R.id.editTextPassword);
        View nameText = signUpActivity.findViewById(R.id.nameText);
        View signUpButton = signUpActivity.findViewById(R.id.signUpButton);
        View textLogin = signUpActivity.findViewById(R.id.textLogin);

        assertNotNull(welcomeText);
        assertNotNull(signInText);
        assertNotNull(editTextEmail);
        assertNotNull(editTextPassword);
        assertNotNull(nameText);
        assertNotNull(signUpButton);
        assertNotNull(textLogin);

    }

    @After
    public void tearDown() throws Exception {
        signUpActivity = null;
    }
}