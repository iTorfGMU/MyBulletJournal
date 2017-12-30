package com.torfin.mybulletjournal.monthlylog;

/**
 * Created by torftorf1 on 12/25/17.
 */

public class MonthlyLogContract {

    public interface View {
        void showTaskListActivity(long selectedDate);
    }

    public interface Presenter {

        void subscribe(View v);

        void unsubscribe(View v);

    }
}
