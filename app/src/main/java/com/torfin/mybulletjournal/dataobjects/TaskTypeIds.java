package com.torfin.mybulletjournal.dataobjects;

import android.content.Context;

import com.torfin.mybulletjournal.R;

/**
 * Created by torftorf1 on 12/25/17.
 */

public final class TaskTypeIds {

    public static final int TASK_TYPE_EVENT = 0;

    public static final int TASK_TYPE_TASK = 100;

    public static final int TASK_TYPE_TASK_CANCELLED = 101;

    public static final int TASK_TYPE_TASK_COMPLETED = 102;

    public static final int TASK_TYPE_TASK_RESCHEDULED = 103;

    public static final int TASK_TYPE_TASK_SCHEDULED = 104;

    public static final int TASK_TYPE_DEADLINE = 2;

    public static final int TASK_TYPE_URGENT = 3;

    public static final int TASK_TYPE_NOTE = 4;

    private TaskTypeIds() {

    }

    public static int getTaskTypeId(String type, Context context) {

        if (type.equals(context.getString(R.string.event_task_type))) {
            return TASK_TYPE_EVENT;
        } else if (type.equals(context.getString(R.string.task_task_type))) {
            return TASK_TYPE_TASK;
        } else if (type.equals(context.getString(R.string.deadline_task_type))) {
            return TASK_TYPE_DEADLINE;
        } else if (type.equals(context.getString(R.string.urgent_task_type))) {
            return TASK_TYPE_URGENT;
        } else if (type.equals(context.getString(R.string.note_task_type))) {
            return TASK_TYPE_NOTE;
        }

        return -1;
    }

}
