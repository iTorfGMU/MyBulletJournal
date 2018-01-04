package com.torfin.mybulletjournal;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.torfin.mybulletjournal.contentprovider.TaskLabelProvider;
import com.torfin.mybulletjournal.contentprovider.TasksProvider;
import com.torfin.mybulletjournal.newtask.AddNewTaskContract;
import com.torfin.mybulletjournal.newtask.AddNewTaskPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Ila on 1/1/18.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseAuth.class, FirebaseDatabase.class})
public class AddNewTaskUnitTest {

    AddNewTaskPresenter presenter;

    @Mock
    AddNewTaskContract.View view;

    @Mock
    TasksProvider provider;

    @Mock
    TaskLabelProvider labelProvider;

    @Mock
    Context context;

    private String name = "name";

    private String type = "type";

    private String label = "label";


    @Before
    public void setup() {
        initMocks(this);

        TasksProvider.setMockInstance(provider);
        TaskLabelProvider.setMockInstance(labelProvider);
        presenter = AddNewTaskPresenter.newInstance(context);

        presenter.subscribe(view);
    }

    @Test
    public void test_subscribe() {
        assertNotNull(presenter.getView());
    }

    @Test
    public void test_unsubscribe() {
        assertNotNull(presenter.getView());

        presenter.unsubscribe(view);
        assertNull(presenter.getView());
    }

    @Test
    public void test_getLabels() {
        List<String> labels = new ArrayList<>();
        labels.add("labelOne");
        labels.add("labelTwo");
        labels.add("labelThree");

        when(labelProvider.getLabels()).thenReturn(labels);
        assertNotNull(presenter.getLabels());
    }

    @Test
    public void test_getLabelsArrayAdapter() {
        List<String> labels = new ArrayList<>();
        labels.add("labelOne");
        labels.add("labelTwo");
        labels.add("labelThree");

        when(labelProvider.getLabels()).thenReturn(labels);
        assertNotNull(presenter.getLabelArrayAdapter());
    }

    @Test
    public void test_VerifyForm_Valid() {

        assertTrue(presenter.verifyForm(name, type, label));
    }

    @Test
    public void test_VerifyForm_InvalidName() {
        assertFalse(presenter.verifyForm("", type, label));
        assertFalse(presenter.verifyForm(null, type, label));
    }

    @Test
    public void test_VerifyForm_InvalidType() {
        assertFalse(presenter.verifyForm(name, "Choose a task type", label));
        assertFalse(presenter.verifyForm(name, "", label));
        assertFalse(presenter.verifyForm(name, null, label));
    }

    @Test
    public void test_VerifyForm_InvalidLabel() {
        assertFalse(presenter.verifyForm(name, type, "Choose a task label"));
        assertFalse(presenter.verifyForm(name, type, ""));
        assertFalse(presenter.verifyForm(name, type, null));
    }

    @Test
    public void test_HandleDatePicker() {
        presenter.handleDatePicker();

        verify(view).hideTimePicker();
        verify(view).showDatePicker();
    }

    @Test
    public void test_DismessDatePicker() {
        presenter.dismissDatePicker();
        verify(view).hideDatePicker();
    }

    @Test
    public void test_HandleTimePicker() {
        presenter.handleTimePicker();

        verify(view).hideDatePicker();
        verify(view).showTimePicker();
    }

    @Test
    public void test_DismessTimePicker() {
        presenter.dismissTimePicker();
        verify(view).hideTimePicker();
    }

    @Test
    public void test_OnCreateTaskComplete() {
        presenter.onCreateTaskComplete();

        verify(view).hideLoading();
        verify(view).showMessage(R.string.snackbar_task_added);
        verify(view).dismiss();
    }

    @Test
    public void test_TaskAdded() {
        presenter.taskAdded();
        verify(view).hideLoading();
    }

    @Test
    public void test_LabelsReturned() {
        presenter.labelsReturned();
        verify(view).refreshLabelSpinner();
    }
}
