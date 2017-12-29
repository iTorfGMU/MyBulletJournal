package com.torfin.mybulletjournal.contentprovider;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.torfin.mybulletjournal.dataobjects.Task;

/**
 * Created by torftorf1 on 12/26/17.
 */

@Database(entities = {Task.class}, version = 2)
public abstract class TasksDatabase extends RoomDatabase {

    private static TasksDatabase instance;

    public abstract TasksDao tasksDao();

    public static TasksDatabase getDatabaseInstance(Context context) {
        if (instance == null){
            synchronized (TasksDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            TasksDatabase.class, "task-database")
                            .fallbackToDestructiveMigration().build();
                }
            }
        }

        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

}
