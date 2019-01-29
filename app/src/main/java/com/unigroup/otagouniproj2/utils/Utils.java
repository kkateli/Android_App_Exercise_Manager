package com.unigroup.otagouniproj2.utils;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by harrysmac on 18/05/18.
 */


public class Utils {

        private static FirebaseDatabase mDatabase;

        public static FirebaseDatabase getDatabase() {
            if (mDatabase == null) {
                mDatabase = FirebaseDatabase.getInstance();
                mDatabase.setPersistenceEnabled(true);
            }
            return mDatabase;
        }

    }

