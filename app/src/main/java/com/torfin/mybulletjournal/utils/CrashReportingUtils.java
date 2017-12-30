package com.torfin.mybulletjournal.utils;

import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by torftorf1 on 12/29/17.
 */

public class CrashReportingUtils {

    public static void reportCrash(Exception ex) {
        FirebaseCrash.report(ex);
    }

    public static void logError(String error) {
        FirebaseCrash.log(error);
    }

}
