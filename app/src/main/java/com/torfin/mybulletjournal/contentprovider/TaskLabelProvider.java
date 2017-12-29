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

    private static FirebaseUser user;

    public static TaskLabelProvider getInstance() {
        if (instance == null) {
            synchronized (TaskLabelProvider.class) {
                if (instance == null) {
                    instance = new TaskLabelProvider();
                }
            }
        }

        return instance;
    }

    private TaskLabelProvider() {
        database = FirebaseDatabase.getInstance().getReference().child("labels");
        database.orderByKey().addChildEventListener(this);
    }

    public List<String> getLabels() {
        if (labels == null) {
            labels = new ArrayList<>();
            labels.add("Choose a task label");
        }

        return labels;
    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String label = dataSnapshot.getValue(String.class);
        Log.d(TAG + " LABEL KEY", dataSnapshot.getKey());
        Log.d(TAG + " LABEL VALUE", label);

        labels.add(label);
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
}
