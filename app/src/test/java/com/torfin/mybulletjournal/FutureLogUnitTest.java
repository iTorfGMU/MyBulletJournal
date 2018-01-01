package com.torfin.mybulletjournal;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.torfin.mybulletjournal.contentprovider.TasksProvider;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.dataobjects.TaskTypeIds;
import com.torfin.mybulletjournal.futurelog.FutureLogContract;
import com.torfin.mybulletjournal.futurelog.FutureLogPresenter;
import com.torfin.mybulletjournal.utils.DateUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Ila on 1/1/18.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseAuth.class, FirebaseDatabase.class})
public class FutureLogUnitTest {

    @Mock
    Context context;

    @Mock
    TasksProvider provider;

    @Mock
    FutureLogContract.View view;

    FutureLogPresenter presenter;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

    @Before
    public void setup() {

        initMocks(this);

        TasksProvider.setMockInstance(provider);
        presenter = FutureLogPresenter.newInstance(context);

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
    public void test_GetFutureTasksComplete() {

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

        List<Task> list = new ArrayList<>();
        list.add(test);

        presenter.onGetFutureTasksComplete(list);

        verify(view).hideLoading();
        verify(view).setRecyclerView(presenter.getLog());
    }
}
