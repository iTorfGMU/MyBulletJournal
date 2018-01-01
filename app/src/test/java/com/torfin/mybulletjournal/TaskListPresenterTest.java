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

import java.util.Calendar;
import java.util.HashMap;
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
    public void test_getTasks_loading() {

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

        when(provider.getTasks()).thenReturn(tasks);



        presenter.getTasks();
        verify(view).showLoading();
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

        when(provider.getTasks()).thenReturn(tasks);

        presenter.getTasks();
        verify(view).setAdapter(tasks);
        verify(view).hideLoading();
        verify(view).showTasksList(tasks);
    }

    @Test
    public void test_getTasks_DoneLoading_NoTasks() {

        HashMap<String, Task> tasks = new HashMap<>();

        presenter.subscribe(view);

        when(provider.getTasks()).thenReturn(tasks);

        presenter.getTasks();
        verify(view).setAdapter(tasks);
        verify(view).hideLoading();
        verify(view).showNoTasks();
    }

}
