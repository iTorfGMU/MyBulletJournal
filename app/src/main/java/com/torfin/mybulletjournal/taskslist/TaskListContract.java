package com.torfin.mybulletjournal.taskslist;

import com.torfin.mybulletjournal.dataobjects.Task;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by torftorf1 on 12/25/17.
 */

class TaskListContract {

    interface View {

        void showLoading();

        void hideLoading();

        void showNoTasks();

        void showTasksList(HashMap<String, Task> tasks);

        void updateTasks();

        void setDate(String date);
    }

    interface Presenter {

        void getTasks();

        void getTasksWithDate();

        void subscribe(View v);

        void unsubscribe(View v);

        void setDate();

        void onDateButtonPressed(int counter);

        void updateTasksList();

        ArrayList<Task> getListOfTasks(HashMap<String, Task> tasks);

        String getDate(long date);

        String getTime(long date);
    }
}
