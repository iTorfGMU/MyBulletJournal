package com.torfin.mybulletjournal.newtask;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.ArrayAdapter;

import java.util.Date;
import java.util.List;

public class AddNewTaskContract {

    public interface Presenter<V extends View> {
        void checkToken();

        List<String> getLabels();

        ArrayAdapter<String> getLabelArrayAdapter();

        boolean verifyForm(String name, String type, String label);

        void createTask(String name, String type, int typeId, String label);

        void subscribe(View v);

        void unsubscribe(View v);

        void handleDatePicker();

        void handleTimePicker();

        void dismissDatePicker();

        void dismissTimePicker();

        void onCreateTaskComplete();

        void setDate();
    }

    public interface View {

        void showLoading();

        void hideLoading();

        void openActivityOnTokenExpire();

        void showMessage(String message);

        void showMessage(@StringRes int resId);

        boolean isNetworkConnected();

        void signout();

        void refreshLabelSpinner();

        void showDatePicker();

        void hideDatePicker();

        void showTimePicker();

        void hideTimePicker();

        Date getSelectedDate();

        void onDatePicked(String date);

        void onTimePicked(String date);

        void dismiss();
    }

}
