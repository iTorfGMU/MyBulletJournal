package com.torfin.mybulletjournal;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.torfin.mybulletjournal.contentprovider.TasksProvider;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.dataobjects.TaskTypeIds;
import com.torfin.mybulletjournal.taskslist.TasksListActivity;
import com.torfin.mybulletjournal.utils.DateUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TaskListBehaviorTests {

    TasksProvider provider;

    @Rule
    public ActivityTestRule<TasksListActivity> mActivityRule = new ActivityTestRule<>(TasksListActivity.class);

    @Before
    public void setUp() throws Exception {
        provider = mock(TasksProvider.class);
        TasksProvider.setMockInstance(provider);
    }

    @Test
    public void testDisplayAllTasks() {
        onView(withId(R.id.date_text_view)).check(matches(isDisplayed()));
        onView(withId(R.id.date_text_view)).check(matches(withText("All Tasks")));
    }

    @Test
    public void testDisplayNextDate() {
        onView(withId(R.id.next_date_button)).perform(click());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(DateUtils.addDays(Calendar.getInstance().getTime(), 1));

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, YYYY", Locale.getDefault());

        onView(withId(R.id.date_text_view)).check(matches(withText(DateUtils.formatDate(cal.getTime(), dateFormat))));
    }

    @Test
    public void testDisplayPreviousDate() {
        onView(withId(R.id.previous_date_button)).perform(click());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(DateUtils.addDays(Calendar.getInstance().getTime(), -1));

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, YYYY", Locale.getDefault());

        onView(withId(R.id.date_text_view)).check(matches(withText(DateUtils.formatDate(cal.getTime(), dateFormat))));
    }

    @Test
    public void testAddTask() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.taskname_edittext_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testDisplayTasks() {
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

        provider.setTasks(tasks);
        onView(ViewMatchers.withId(R.id.tasks_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.details_task_name_textview)).check(matches(isDisplayed()));
    }

}
