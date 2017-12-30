package com.torfin.mybulletjournal.utils;

import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.dataobjects.TaskTypeIds;

import java.util.List;

/**
 * Created by torftorf1 on 12/29/17.
 */

public class WidgetTaskUtils {

    private static List<Task> tasks;

    public static void setTasks(List<Task> list) {
        tasks = list;
    }

    public static List<Task> getTasks() {
        return tasks;
    }


    public static int getTaskImage(Task task) {
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
}
