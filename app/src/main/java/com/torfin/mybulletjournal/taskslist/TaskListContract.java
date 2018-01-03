package com.torfin.mybulletjournal.taskslist;

import com.torfin.mybulletjournal.dataobjects.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by torftorf1 on 12/25/17.
 */

public class TaskListContract {

    public interface View {

        void showLoading();

        void hideLoading();

        void showNoTasks();

        void showTasksList(HashMap<String, Task> tasks);

        void updateTasks();

        void setDate(String date);

        void refreshAdapter(boolean setRefreshing);

        void updateList();

        void setAdapter(HashMap<String, Task> tasks);
    }

    public interface Presenter {

        void getTasks();

        void getTasksWithDate();

        void getTasksWithDate(long date);

        void subscribe(View v);

        void unsubscribe(View v);

        void setDate();

        void onDateButtonPressed(int counter);

        void updateTasksList();

        ArrayList<Task> getListOfTasks(HashMap<String, Task> tasks);

        String getDate(long date);

        String getTime(long date);

        long getCurrentlyDisplayedDate();

        boolean showAllTasks();

        void onGetTasksComplete(HashMap<String, Task> tasks);

        void onGetTasksByDateComplete(List<Task> list);
    }
}
