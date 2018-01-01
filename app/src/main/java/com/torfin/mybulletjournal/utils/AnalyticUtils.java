package com.torfin.mybulletjournal.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.torfin.mybulletjournal.dataobjects.Task;

/**
 * Created by torftorf1 on 12/29/17.
 */

public class AnalyticUtils {

    private static FirebaseAnalytics firebaseAnalytics;

    public static void init(Context context) {
        if (firebaseAnalytics == null && context != null && context.getApplicationContext() != null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        }
    }

    public static void sendAnalytics_TaskAdded(Task task) {

        if (firebaseAnalytics != null) {
            Bundle bundle = new Bundle();

            bundle.putString(FirebaseAnalytics.Param.SOURCE, "TASK ADDED");
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, task.uid);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, task.taskName);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "tasks");
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }

    public static void sendAnalytics_TaskDeleted(Task task) {
        if (firebaseAnalytics != null) {
            Bundle bundle = new Bundle();

            bundle.putString(FirebaseAnalytics.Param.SOURCE, "TASK DELETED");
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, task.uid);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, task.taskName);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "tasks");
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }

    public static void sendAnalytics_TaskUpdated(Task task) {
        if (firebaseAnalytics != null) {
            Bundle bundle = new Bundle();

            bundle.putString(FirebaseAnalytics.Param.SOURCE, "TASK UPDATE");
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, task.uid);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, task.taskName);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "tasks");
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }
}
