package com.torfin.mybulletjournal.futurelog;

import com.torfin.mybulletjournal.dataobjects.Task;

import java.util.List;

/**
 * Created by torftorf1 on 12/25/17.
 */

public class FutureLogContract {

    public interface View {

        void showLoading();

        void hideLoading();

        void setRecyclerView(List<Object> list);

    }

    public interface Presenter {

        void subscribe(View v);

        void unsubscribe(View v);

        void configureRecyclerView();

        void onGetFutureTasksComplete(List<Task> list);

    }

}
