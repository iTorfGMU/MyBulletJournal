package com.torfin.mybulletjournal.contentprovider;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.utils.AnalyticUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by torftorf1 on 12/25/17.
 */

public class TasksProvider {

    public static final String TAG = TasksProvider.class.getSimpleName();

    private HashMap<String, Task> tasks;

    private static TasksProvider instance;

    private static DatabaseReference database;

    private static DatabaseReference tasksDatabase;

    private static FirebaseUser user;

    private static TasksDatabase localDatabase;

    private static ArrayList<TaskAdded> callbacks;

    public static TasksProvider getInstance(Context context, TaskAdded callback) {
        if (instance == null) {
            synchronized (TasksProvider.class) {
                if (instance == null) {
                    instance = new TasksProvider(context, callback);
                }
            }
        }

        return instance;
    }

    private TasksProvider(Context context, TaskAdded callback) {
        if (callbacks == null) {
            callbacks = new ArrayList<>();
        }

        callbacks.add(callback);
        database = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        tasksDatabase = FirebaseDatabase.getInstance().getReference().child("tasks").child(user.getUid());
        localDatabase = TasksDatabase.getDatabaseInstance(context);
    }

    public HashMap<String, Task> getTasks() {

        if (this.tasks == null) {
            this.tasks = new HashMap<>();
            getTasksFromDatabase();
        }

        if (this.tasks.size() == 0) {
            this.tasks = convertListToMap(localDatabase.tasksDao().loadAllTasks());
        }

        return this.tasks;
    }

    private void getTasksFromDatabase() {

        tasksDatabase.orderByChild("taskDate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Task addedTask = dataSnapshot.getValue(Task.class);
                Log.d(TAG + " CHILD ADDED", addedTask.taskName);
                tasks.put(addedTask.uid, addedTask);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Task changedTask = dataSnapshot.getValue(Task.class);
                Log.d(TAG + " CHILD CHANGED", changedTask.taskName);
                tasks.put(changedTask.uid, changedTask);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Task removedTask = dataSnapshot.getValue(Task.class);
                Log.d(TAG + " CHILD REMOVED", removedTask.taskName);

                if (tasks.containsKey(removedTask.uid)) {
                    tasks.remove(removedTask);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Task movedTask = dataSnapshot.getValue(Task.class);
                Log.d(TAG + " CHILD MOVED", movedTask.taskName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG + " CANCELLED", databaseError.getDetails());
            }
        });
    }

    public List<Task> getTasksWithDate(long dateOne, long dateTwo) {
        return localDatabase.tasksDao().findTaskswithDate(dateOne, dateTwo);
    }

    public List<Task> getFutureTasks(long startDate) {
        return localDatabase.tasksDao().getFutureTasks(startDate);
    }

    public void updateTasksDatabase(Task task) {
        localDatabase.tasksDao().updateTask(task);
        updateDatabase(task);
    }

    public HashMap<String, Task> convertListToMap(List<Task> taskList) {
        HashMap<String, Task> taskMap = new HashMap<>(taskList.size());

        for (Task task : taskList) {
            taskMap.put(task.uid, task);
        }

        return taskMap;
    }

    public ArrayList<Task> convertMapToList(HashMap<String, Task> taskMap) {
        ArrayList<Task> taskList = new ArrayList<>(taskMap.size());

        for (Map.Entry<String, Task> taskEntry : taskMap.entrySet()) {
            taskList.add(taskEntry.getValue());
        }

        return taskList;
    }

    private void updateDatabase(Task task) {
        AnalyticUtils.sendAnalytics_TaskUpdated(task);

        String key = database.child("tasks").child(user.getUid()).push().getKey();
        Map<String, Object> taskValues = task.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/tasks/" + user.getUid() + "/" + key, taskValues);

        database.updateChildren(childUpdates);
    }

    public void addTask(Task task) {
        AnalyticUtils.sendAnalytics_TaskAdded(task);

        tasks.put(task.uid, task);
        localDatabase.tasksDao().addTask(task);
        updateDatabase(task);
    }

    public void deleteTask(Task task) {
        if (tasks.containsKey(task.uid)) {
            tasks.remove(task);
        }

        AnalyticUtils.sendAnalytics_TaskDeleted(task);

        localDatabase.tasksDao().deleteTask(task);
        String key = database.child("tasks").push().getKey();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/tasks/" + user.getUid() + "/" + key, null);

        database.updateChildren(childUpdates);
    }

    private void notifyCallbacks() {
        for (TaskAdded callback : callbacks) {
            if (callback != null) {
                callback.taskAdded();
            }
        }
    }

    public interface TaskAdded {
        void taskAdded();
    }

}
