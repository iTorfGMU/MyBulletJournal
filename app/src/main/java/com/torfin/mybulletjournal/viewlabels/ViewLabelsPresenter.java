package com.torfin.mybulletjournal.viewlabels;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.torfin.mybulletjournal.contentprovider.TaskLabelProvider;

/**
 * Created by torftorf1 on 12/26/17.
 */

public class ViewLabelsPresenter implements ViewLabelsContract.Presenter, TaskLabelProvider.LabelsCallback {

    private TaskLabelProvider labelProvider;

    private ViewLabelsContract.View view;

    private Resubscribe callback;

    public static ViewLabelsPresenter newInstance(Context c) {
        return newInstance(c, null);
    }

    public static ViewLabelsPresenter newInstance(Context c, Resubscribe callback) {
        return new ViewLabelsPresenter(c, callback);
    }

    private ViewLabelsPresenter(Context c, Resubscribe callback) {
        labelProvider = TaskLabelProvider.getInstance(this);
        this.callback = callback;
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
        checkView();

        this.view.showLoading();
        this.view.setRecyclerView(labelProvider.getLabels());
        this.view.hideLoading();
    }

    @Override
    public void getUpdatedLabelList() {
        checkView();

        labelProvider.getLabels();
        this.view.updateRecyclerView();
        this.view.stopRefresh();
    }

    @Override
    public void labelsReturned() {
        getUpdatedLabelList();
    }

    private void checkView() {
        if (this.view == null && callback != null) {
            callback.resubscribeView();
        }
    }

    public interface Resubscribe {
        void resubscribeView();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public ViewLabelsContract.View getView() {
        return this.view;
    }
}
