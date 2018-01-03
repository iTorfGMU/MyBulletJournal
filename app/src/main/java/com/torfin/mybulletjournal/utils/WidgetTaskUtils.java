package com.torfin.mybulletjournal.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.dataobjects.TaskTypeIds;

import java.util.List;

/**
 * Created by torftorf1 on 12/29/17.
 */

public class WidgetTaskUtils {

    private static List<Task> tasks;

    public static void setTasks(@NonNull List<Task> list) {
        tasks = list;
    }

    public static List<Task> getTasks() {
        return tasks;
    }


    public static int getTaskImage(@NonNull Task task) {
        switch (task.taskTypeId) {
            case TaskTypeIds.TASK_TYPE_EVENT:
                return R.drawable.ic_event;
            case TaskTypeIds.TASK_TYPE_TASK:
                return R.drawable.ic_task;
            case TaskTypeIds.TASK_TYPE_TASK_CANCELLED:
                return R.drawable.ic_task_cancelled;
            case TaskTypeIds.TASK_TYPE_TASK_COMPLETED:
                return R.drawable.ic_task_completed;
            case TaskTypeIds.TASK_TYPE_TASK_RESCHEDULED:
                return R.drawable.ic_task_rescheduled;
            case TaskTypeIds.TASK_TYPE_TASK_SCHEDULED:
                return R.drawable.ic_task_scheduled;
            case TaskTypeIds.TASK_TYPE_DEADLINE:
                return R.drawable.ic_task_deadline;
            case TaskTypeIds.TASK_TYPE_URGENT:
                return R.drawable.ic_task_urgent;
            case TaskTypeIds.TASK_TYPE_NOTE:
                return R.drawable.ic_note;
            default:
                break;
        }

        return -1;
    }

    public static String getTaskImageContentDescription(@NonNull Task task, @NonNull Context context) {
        switch (task.taskTypeId) {
            case TaskTypeIds.TASK_TYPE_EVENT:
                return context.getString(R.string.event_task_type);
            case TaskTypeIds.TASK_TYPE_TASK:
                return context.getString(R.string.type_task);
            case TaskTypeIds.TASK_TYPE_TASK_CANCELLED:
                return context.getString(R.string.cancelled_type_task);
            case TaskTypeIds.TASK_TYPE_TASK_COMPLETED:
                return context.getString(R.string.completed_type_task);
            case TaskTypeIds.TASK_TYPE_TASK_RESCHEDULED:
                return context.getString(R.string.rescheduled_type_task);
            case TaskTypeIds.TASK_TYPE_TASK_SCHEDULED:
                return context.getString(R.string.scheduled_type_task);
            case TaskTypeIds.TASK_TYPE_DEADLINE:
                return context.getString(R.string.deadline_task_type);
            case TaskTypeIds.TASK_TYPE_URGENT:
                return context.getString(R.string.urgent_task_type);
            case TaskTypeIds.TASK_TYPE_NOTE:
                return context.getString(R.string.note_task_type);
            default:
                return "";
        }
    }
}
