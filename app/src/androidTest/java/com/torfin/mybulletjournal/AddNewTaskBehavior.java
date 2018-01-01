package com.torfin.mybulletjournal;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseUser;
import com.torfin.mybulletjournal.contentprovider.TaskLabelProvider;
import com.torfin.mybulletjournal.contentprovider.TasksProvider;
import com.torfin.mybulletjournal.newtask.AddNewTaskActivity;
import com.torfin.mybulletjournal.newtask.AddNewTaskPresenter;
import com.torfin.mybulletjournal.utils.ProfileUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by torftorf1 on 1/1/18.
 */

@RunWith(AndroidJUnit4.class)
public class AddNewTaskBehavior {

    AddNewTaskPresenter presenter;

    TasksProvider provider;

    TaskLabelProvider labelProvider;

    private static final String TASK_NAME = "Testing!";

    private static final String TASK_LABEL = "health";

    @Rule
    public ActivityTestRule<AddNewTaskActivity> mActivityRule = new ActivityTestRule<>(AddNewTaskActivity.class);

    @Before
    public void setUp() throws Exception {

        mActivityRule.getActivity();

        provider = mock(TasksProvider.class);
        TasksProvider.setMockInstance(provider);

        labelProvider = mock(TaskLabelProvider.class);
        TaskLabelProvider.setMockInstance(labelProvider);

        FirebaseUser user = mock(FirebaseUser.class);
        ProfileUtils.setUser(user);

        presenter = AddNewTaskPresenter.newInstance(mActivityRule.getActivity());
        presenter.subscribe(mActivityRule.getActivity());

        List<String> labels = new ArrayList<>(2);
        labels.add(TASK_LABEL);
        labels.add("testing");
        when(presenter.getLabels()).thenReturn(labels);
    }

    @Test
    public void testEditing_TaskName() {
        onView(withId(R.id.taskname_edittext_view)).perform(typeText(TASK_NAME), closeSoftKeyboard());
        onView(allOf(withId(R.id.taskname_edittext_view), withText(TASK_NAME))).check(matches(isDisplayed()));
    }

    @Test
    public void testEditing_SelectingLabel() {
        onView(withId(R.id.tasklabel_spinner_view)).perform(click());
    }

    @Test
    public void testEditing_SelectingType() {
        onView(withId(R.id.tasktype_spinner_view)).perform(click());
    }

    @Test
    public void testEditing_SelectingDate() {
        onView(withId(R.id.taskdate_textview)).perform(click());
        onView(withId(R.id.date_picker_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testEditing_SelectingTime() {
        onView(withId(R.id.tasktime_textview)).perform(click());
        onView(withId(R.id.time_picker_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testEditing_AddTask() {

        onView(withId(R.id.taskname_edittext_view)).perform(typeText(TASK_NAME), closeSoftKeyboard());

        onView(withId(R.id.tasklabel_spinner_view)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());

        onView(withId(R.id.tasktype_spinner_view)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());

        onView(withId(R.id.add_new_task_button)).perform(click());
    }

}
