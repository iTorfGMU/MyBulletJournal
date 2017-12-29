package com.torfin.mybulletjournal.viewlabels;

import java.util.List;

/**
 * Created by torftorf1 on 12/26/17.
 */

public class ViewLabelsContract {

    public interface View {
        void showLoading();

        void hideLoading();

        void setRecyclerView(List<String> labels);

        void updateRecyclerView();

        void stopRefresh();
    }

    public interface Presenter {
        void subscribe(View v);

        void unsubscribe(View v);

        void setupViews();

        void getUpdatedLabelList();
    }

}
