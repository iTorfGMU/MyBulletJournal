package com.torfin.mybulletjournal;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.torfin.mybulletjournal.contentprovider.TaskLabelProvider;
import com.torfin.mybulletjournal.viewlabels.ViewLabelsContract;
import com.torfin.mybulletjournal.viewlabels.ViewLabelsPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by torftorf1 on 1/1/18.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseAuth.class, FirebaseDatabase.class})
public class ViewLabelsUnitTest {

    @Mock
    Context context;

    @Mock
    TaskLabelProvider provider;

    @Mock
    ViewLabelsContract.View view;

    ViewLabelsPresenter presenter;

    @Before
    public void setup() {
        initMocks(this);

        TaskLabelProvider.setMockInstance(provider);
        presenter = ViewLabelsPresenter.newInstance(context);

        presenter.subscribe(view);
    }

    @Test
    public void test_subscrube() {
        assertNotNull(presenter.getView());
    }

    @Test
    public void test_unsubscrube() {
        presenter.unsubscribe(view);
        assertNull(presenter.getView());
    }

    @Test
    public void test_setupViews() {
        List<String> labels = new ArrayList<>();
        labels.add("label1");
        labels.add("label2");
        labels.add("label3");

        when(provider.getLabels()).thenReturn(labels);

        presenter.setupViews();

        verify(view).showLoading();
        verify(view).setRecyclerView(provider.getLabels());
        verify(view).hideLoading();
    }

    @Test
    public void test_UpdateLabelList() {
        List<String> labels = new ArrayList<>();
        labels.add("label1");
        labels.add("label2");
        labels.add("label3");

        when(provider.getLabels()).thenReturn(labels);

        presenter.getUpdatedLabelList();

        verify(view).updateRecyclerView();
        verify(view).stopRefresh();
    }

    @Test
    public void test_LabelsReturned() {
        List<String> labels = new ArrayList<>();
        labels.add("label1");
        labels.add("label2");
        labels.add("label3");

        when(provider.getLabels()).thenReturn(labels);

        presenter.labelsReturned();

        verify(view).updateRecyclerView();
        verify(view).stopRefresh();
    }

}
