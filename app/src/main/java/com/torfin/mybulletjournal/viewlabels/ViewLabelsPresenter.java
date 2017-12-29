package com.torfin.mybulletjournal.viewlabels;

import android.content.Context;

import com.torfin.mybulletjournal.contentprovider.TaskLabelProvider;

import java.util.List;

/**
 * Created by torftorf1 on 12/26/17.
 */

public class ViewLabelsPresenter implements ViewLabelsContract.Presenter, TaskLabelProvider.LabelsCallback {

    private TaskLabelProvider labelProvider;

    private ViewLabelsContract.View view;

    public static ViewLabelsPresenter newInstance(Context c) {
        return new ViewLabelsPresenter(c);
    }

    private ViewLabelsPresenter(Context c) {
        labelProvider = TaskLabelProvider.getInstance(this);
    }

    @Override
    public void subscribe(ViewLabelsContract.View v) {
        this.view = v;
    }

    @Override
    public void unsubscribe(ViewLabelsContract.View v) {
        if (v.equals(this.view)) {
            this.view = null;
        }
    }

    @Override
    public void setupViews() {
        this.view.showLoading();
        this.view.setRecyclerView(labelProvider.getLabels());
        this.view.hideLoading();
    }

    @Override
    public void getUpdatedLabelList() {
        labelProvider.getLabels();
        this.view.updateRecyclerView();
        this.view.stopRefresh();
    }

    @Override
    public void labelsReturned() {

    }
}
