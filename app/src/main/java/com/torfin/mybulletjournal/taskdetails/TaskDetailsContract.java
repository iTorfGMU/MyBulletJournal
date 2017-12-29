package com.torfin.mybulletjournal.taskdetails;

import android.support.annotation.StringRes;

import com.torfin.mybulletjournal.dataobjects.Task;

/**
 * Created by torftorf1 on 12/25/17.
 */

public class TaskDetailsContract {

    public interface View {
        void showLoading();

        void hideLoading();

        void setupEditView();

        void hideEditView();

        void updateStatus(Task updatedTask);

        void onError(@StringRes int resId);

        void onError(String message);

        void showMessage(String message);

        void showMessage(@StringRes int resId);
    }

    public interface Presenter {

        void subscribe(View v);

        void unsubscribe(View v);

        Task getTask(String id);

        int getTaskTypeResId(int typeId);

        String getTaskDate(long time);

        void onEditSelected(Task task);

        void onUpdateTaskSelected(String selectedStatus);

        void onDeleteTaskSelected();
    }

}
