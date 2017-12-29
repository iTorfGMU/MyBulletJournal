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
        } else if (type.equals(context.getString(R.string.type_task))) {
            return TASK_TYPE_TASK;
        } else if (type.equals(context.getString(R.string.deadline_task_type))) {
            return TASK_TYPE_DEADLINE;
        } else if (type.equals(context.getString(R.string.urgent_task_type))) {
            return TASK_TYPE_URGENT;
        } else if (type.equals(context.getString(R.string.note_task_type))) {
            return TASK_TYPE_NOTE;
        } else if (type.equals(context.getString(R.string.completed_type_task))) {
            return TASK_TYPE_TASK_COMPLETED;
        } else if (type.equals(context.getString(R.string.cancelled_type_task))) {
            return TASK_TYPE_TASK_CANCELLED;
        } else if (type.equals(context.getString(R.string.scheduled_type_task))) {
            return TASK_TYPE_TASK_SCHEDULED;
        } else if (type.equals(context.getString(R.string.rescheduled_type_task))) {
            return TASK_TYPE_TASK_RESCHEDULED;
        }

        return -1;
    }

    public static boolean isATask(int id) {
        return id == TASK_TYPE_TASK
                || id == TASK_TYPE_TASK_COMPLETED
                || id == TASK_TYPE_TASK_CANCELLED
                || id == TASK_TYPE_TASK_RESCHEDULED
                || id == TASK_TYPE_TASK_SCHEDULED;
    }

    public static String getTaskType(int id, Context context) {
        switch (id) {
            case TASK_TYPE_EVENT:
                return context.getString(R.string.event_task_type);
            case TASK_TYPE_DEADLINE:
                return context.getString(R.string.deadline_task_type);
            case TASK_TYPE_NOTE:
                return context.getString(R.string.note_task_type);
            case TASK_TYPE_URGENT:
                return context.getString(R.string.urgent_task_type);
            case TASK_TYPE_TASK:
                return context.getString(R.string.type_task);
            case TASK_TYPE_TASK_COMPLETED:
                return context.getString(R.string.completed_type_task);
            case TASK_TYPE_TASK_CANCELLED:
                return context.getString(R.string.cancelled_type_task);
            case TASK_TYPE_TASK_SCHEDULED:
                return context.getString(R.string.scheduled_type_task);
            case TASK_TYPE_TASK_RESCHEDULED:
                return context.getString(R.string.rescheduled_type_task);
            default:
                return null;
        }
    }

}
