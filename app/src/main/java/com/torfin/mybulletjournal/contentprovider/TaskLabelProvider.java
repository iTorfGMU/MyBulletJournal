package com.torfin.mybulletjournal.contentprovider;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by torftorf1 on 12/26/17.
 */

public class TaskLabelProvider implements ChildEventListener {

    public static final String TAG = TaskLabelProvider.class.getSimpleName();

    private static List<String> labels;

    private static TaskLabelProvider instance;

    private static DatabaseReference database;

    private static long numOfLabels;

    private List<LabelsCallback> callbacks;

    public static TaskLabelProvider getInstance(LabelsCallback callback) {
        if (instance == null) {
            synchronized (TaskLabelProvider.class) {
                if (instance == null) {
                    instance = new TaskLabelProvider(callback);
                }
            }
        }

        return instance;
    }

    private TaskLabelProvider(LabelsCallback callback) {
        database = FirebaseDatabase.getInstance().getReference().child("labels");
        database.orderByKey().addChildEventListener(this);
        callbacks = new ArrayList<>();
        callbacks.add(callback);
    }

    public List<String> getLabels() {
        if (labels == null) {
            labels = new ArrayList<>();
        }

        return labels;
    }

    public interface LabelsCallback {
        void labelsReturned();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        if (dataSnapshot.getValue() instanceof String) {
            String label = dataSnapshot.getValue(String.class);
            labels.add(label);

            if (labels.size() == numOfLabels) {
                notifyCallbacks();
            }
        } else if (dataSnapshot.getValue() instanceof Long) {
            numOfLabels = dataSnapshot.getValue(Long.class);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void notifyCallbacks() {
        for (LabelsCallback callback : callbacks) {
            callback.labelsReturned();
        }
    }
}
