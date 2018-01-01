package com.torfin.mybulletjournal;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.torfin.mybulletjournal.contentprovider.TasksProvider;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.dataobjects.TaskTypeIds;
import com.torfin.mybulletjournal.taskslist.TaskListContract;
import com.torfin.mybulletjournal.taskslist.TaskListPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Ila on 1/1/18.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseAuth.class, FirebaseDatabase.class})
public class TaskListPresenterTest {

    TaskListPresenter presenter;

    @Mock
    TasksProvider provider;

    @Mock
    TaskListContract.View view;

    @Mock
    Context context;

    @Before
    public void setup() {

        initMocks(this);

        TasksProvider.setMockInstance(provider);
        presenter = TaskListPresenter.newInstance(context);
    }

    @Test
    public void test_subscribe() {
        presenter.subscribe(view);
        assertNotNull(presenter.getView());
    }

    @Test
    public void test_unsubscribe() {
        presenter.subscribe(view);
        assertNotNull(presenter.getView());

        presenter.unsubscribe(view);
        assertNull(presenter.getView());
    }

    @Test
    public void test_getTasks_DoneLoading() {

        HashMap<String, Task> tasks = new HashMap<>();

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

        tasks.put(test.uid, test);

        presenter.subscribe(view);

        presenter.onGetTasksComplete(tasks);

        verify(view).setAdapter(tasks);
        verify(view).hideLoading();
        verify(view).showTasksList(tasks);
    }

    @Test
    public void test_getTasks_DoneLoading_NoTasks() {

        HashMap<String, Task> tasks = new HashMap<>();

        presenter.subscribe(view);

        presenter.onGetTasksComplete(tasks);

        verify(view).setAdapter(tasks);
        verify(view).hideLoading();
        verify(view).showNoTasks();
    }

    @Test
    public void test_getTasksByDate_DoneLoading() {

        List<Task> tasks = new ArrayList<>(2);

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        Task testOne = new Task(
                uuidString,
                "test1",
                "NOTE",
                Calendar.getInstance().getTimeInMillis(),
                TaskTypeIds.TASK_TYPE_NOTE,
                "label"
        );

        uuid = UUID.randomUUID();
        uuidString = uuid.toString();

        Task testTwo = new Task(
                uuidString,
                "test2",
                "NOTE",
                Calendar.getInstance().getTimeInMillis(),
                TaskTypeIds.TASK_TYPE_NOTE,
                "label"
        );

        tasks.add(testOne);
        tasks.add(testTwo);

        HashMap<String, Task> tasksMap = new HashMap<>(2);

        tasksMap.put(testOne.uid, testOne);
        tasksMap.put(testTwo.uid, testTwo);

        presenter.subscribe(view);

        when(provider.convertListToMap(tasks)).thenReturn(tasksMap);

        presenter.onGetTasksByDateComplete(tasks);

        verify(view).setAdapter(tasksMap);
        verify(view).hideLoading();
        verify(view).showTasksList(tasksMap);
    }

    @Test
    public void test_getTasksByDate_DoneLoading_NoTasks() {

        HashMap<String, Task> taskMap = new HashMap<>(2);
        List<Task> taskList = new ArrayList<>(2);

        presenter.subscribe(view);

        when(provider.convertListToMap(taskList)).thenReturn(taskMap);

        presenter.onGetTasksByDateComplete(taskList);

        verify(view).setAdapter(taskMap);
        verify(view).hideLoading();
        verify(view).showNoTasks();
    }

}
