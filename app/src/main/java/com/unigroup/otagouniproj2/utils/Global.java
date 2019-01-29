package com.unigroup.otagouniproj2.utils;

import android.app.Application;

/**
 * Created by harrysmac on 23/04/18.
 * A global class used to define if the initial Firebase database
 * has been cached. Through the use of this class we can avoid
 * loading unnecessarily, if the database has already been
 * cached.
 */

public class Global extends Application {

    private boolean cachedFirebase;

    public boolean getCachedFirebase() {
        return cachedFirebase;
    }

    public void setCachedFirebase(boolean cachedFirebase) {
        this.cachedFirebase = cachedFirebase;
    }
}
