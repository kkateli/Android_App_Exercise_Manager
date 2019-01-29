package com.unigroup.otagouniproj2;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Button;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by harrysmac on 21/04/18.
 */
public class LoginActivityTest {

    public LoginActivity lA = null;

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testLaunch() {
        View view = lA.findViewById(R.id.welcomeText);
        View view1 = lA.findViewById(R.id.signInText);

        View view3 = lA.findViewById(R.id.editTextEmail);
        View view4 = lA.findViewById(R.id.editTextPassword);

        Button button = lA.findViewById(R.id.loginButton);
        Button button1 = lA.findViewById(R.id.signUpButton);


        assertNotNull(view);
        assertNotNull(view1);
        assertNotNull(view3);
        assertNotNull(view4);

        assertNotNull(button);
        assertNotNull(button1);
    }

    @Before
    public void setUp() throws Exception {
        lA = loginActivityTestRule.getActivity();

    }

    @After
    public void tearDown() throws Exception {
        lA = null;
    }

}