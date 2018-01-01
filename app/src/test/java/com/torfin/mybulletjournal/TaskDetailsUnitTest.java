package com.torfin.mybulletjournal;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.torfin.mybulletjournal.contentprovider.TasksProvider;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.dataobjects.TaskTypeIds;
import com.torfin.mybulletjournal.taskdetails.TaskDetailsContract;
import com.torfin.mybulletjournal.taskdetails.TaskDetailsPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Ila on 1/1/18.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseAuth.class, FirebaseDatabase.class})
public class TaskDetailsUnitTest {

    TaskDetailsPresenter presenter;

    @Mock
    TaskDetailsContract.View view;

    @Mock
    TasksProvider provider;

    @Mock
    Context context;

    @Before
    public void setup() {
        initMocks(this);

        TasksProvider.setMockInstance(provider);
        presenter = TaskDetailsPresenter.newInstance(context);

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
    public void test_getTask() {

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        Task test = new Task(
                uuidString,
                "test",
                "NOTE",
                Calendar.getInstance().getTimeInMillis(),
                TaskTypeIds.TASK_TYPE_NOTE,
                "label"
        );

        HashMap<String, Task> tasks = new HashMap<>();
        tasks.put(test.uid, test);

        provider.setTasks(tasks);
        when(provider.getTasks()).thenReturn(tasks);

        assertNotNull(presenter.getTask(test.uid));
    }

    @Test
    public void test_getTask_Null() {

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        Task taskNotAdded = new Task(
                uuidString,
                "test",
                "NOTE",
                Calendar.getInstance().getTimeInMillis(),
                TaskTypeIds.TASK_TYPE_NOTE,
                "label"
        );

        HashMap<String, Task> tasks = new HashMap<>();

        provider.setTasks(tasks);

        assertNull(presenter.getTask(taskNotAdded.uid));
        verify(view).onError(anyString());
    }

    @Test
    public void test_getTaskDate() {
        assertNotNull(presenter.getTaskDate(Calendar.getInstance().getTimeInMillis()));
    }

    @Test
    public void test_onEditSelected() {

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        Task task = new Task(
                uuidString,
                "test",
                "TASK",
                Calendar.getInstance().getTimeInMillis(),
                TaskTypeIds.TASK_TYPE_TASK,
                "label"
        );

        presenter.onEditSelected(task);
        verify(view).setupEditView();
    }

    @Test
    public void test_onEditSelected_NotTaskType() {

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        Task task = new Task(
                uuidString,
                "test",
                "TASK",
                Calendar.getInstance().getTimeInMillis(),
                TaskTypeIds.TASK_TYPE_NOTE,
                "label"
        );

        presenter.onEditSelected(task);
        verify(view, never()).setupEditView();
        verify(view).setupDeleteTaskViews();
    }

    @Test
    public void test_onEditSelected_TaskNull() {

        presenter.onEditSelected(null);
        verify(view).onError(anyString());
        verify(view, never()).setupEditView();
        verify(view, never()).setupDeleteTaskViews();
    }

    @Test
    public void test_onUpdateTaskComplete() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        Task task = new Task(
                uuidString,
                "test",
                "NOTE",
                Calendar.getInstance().getTimeInMillis(),
                TaskTypeIds.TASK_TYPE_NOTE,
                "label"
        );

        presenter.setSelectedTask(task);
        presenter.onUpdateTaskComplete();

        verify(view).hideLoading();
        verify(view).updateStatus(task);
        verify(view).hideEditView();
    }

    @Test
    public void test_onUpdateTaskComplete_TaskNull() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        Task task = new Task(
                uuidString,
                "test",
                "NOTE",
                Calendar.getInstance().getTimeInMillis(),
                TaskTypeIds.TASK_TYPE_NOTE,
                "label"
        );

        presenter.onUpdateTaskComplete();

        verify(view).hideLoading();
        verify(view, never()).updateStatus(task);
        verify(view, never()).setupEditView();
        verify(view).onError(anyString());
    }

    @Test
    public void test_onDeleteTaskComplete_TaskNotNull_CanEdit_True() {

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        Task task = new Task(
                uuidString,
                "test",
                "NOTE",
                Calendar.getInstance().getTimeInMillis(),
                TaskTypeIds.TASK_TYPE_NOTE,
                "label"
        );

        presenter.setSelectedTask(task);
        presenter.setCanEdit(true);

        presenter.onDeleteTaskComplete();
        verify(view).hideLoading();
        verify(view).hideEditView();
    }

    @Test
    public void test_onDeleteTaskComplete_TaskNotNull_CanEdit_False() {

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        Task task = new Task(
                uuidString,
                "test",
                "NOTE",
                Calendar.getInstance().getTimeInMillis(),
                TaskTypeIds.TASK_TYPE_NOTE,
                "label"
        );

        presenter.setSelectedTask(task);
        presenter.setCanEdit(false);

        presenter.onDeleteTaskComplete();
        verify(view).hideLoading();
        verify(view, never()).hideEditView();
        verify(view).hideDeleteTask();
    }


    @Test
    public void test_onDeleteTaskComplete_TaskNull() {

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        Task task = new Task(
                uuidString,
                "test",
                "NOTE",
                Calendar.getInstance().getTimeInMillis(),
                TaskTypeIds.TASK_TYPE_NOTE,
                "label"
        );

        presenter.onDeleteTaskComplete();
        verify(view, never()).hideEditView();
        verify(view, never()).hideDeleteTask();
    }
}
