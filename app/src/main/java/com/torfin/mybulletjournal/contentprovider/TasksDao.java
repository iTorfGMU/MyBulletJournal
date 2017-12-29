package com.torfin.mybulletjournal.contentprovider;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.torfin.mybulletjournal.dataobjects.Task;

import java.util.List;

/**
 * Created by torftorf1 on 12/26/17.
 */

@Dao
public interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTask(Task ... task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("SELECT * FROM tasks")
    List<Task> loadAllTasks();

    @Query("SELECT * FROM tasks WHERE task_date BETWEEN :dateOne AND :dateTwo")
    public List<Task> findTaskswithDate(long dateOne, long dateTwo);

}
