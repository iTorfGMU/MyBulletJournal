package com.torfin.mybulletjournal.dataobjects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@IgnoreExtraProperties
@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey
    @NonNull
    public String uid;

    @ColumnInfo(name = "task_name")
    public String taskName;

    @ColumnInfo(name = "task_label")
    public String taskLabel;

    @ColumnInfo(name = "task_type")
    public String taskType;

    @ColumnInfo(name = "task_type_id")
    public int taskTypeId;

    @ColumnInfo(name = "task_date")
    public long taskDate;

    public Task() {

    }

    public Task(@NonNull String id, @NonNull String name, @NonNull String taskType, long date, int taskTypeId, @NonNull String label) {
        this.uid = id;
        this.taskName = name;
        this.taskDate = date;
        this.taskType = taskType;
        this.taskTypeId = taskTypeId;
        this.taskLabel = label;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("uid", uid);
        result.put("taskName", taskName);
        result.put("taskLabel", taskLabel);
        result.put("taskType", taskType);
        result.put("taskTypeId", taskTypeId);
        result.put("taskDate", taskDate);

        return result;

    }

}
