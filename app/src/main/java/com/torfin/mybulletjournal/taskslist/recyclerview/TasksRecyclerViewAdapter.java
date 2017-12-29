package com.torfin.mybulletjournal.taskslist.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.dataobjects.TaskTypeIds;
import com.torfin.mybulletjournal.taskdetails.TaskDetailsActivity;
import com.torfin.mybulletjournal.taskslist.TaskListPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by torftorf1 on 12/25/17.
 */

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.TasksViewHolder> {

    public static final String TAG = TasksRecyclerViewAdapter.class.getSimpleName();

    private List<Task> tasks;

    private TaskListPresenter presenter;

    public TasksRecyclerViewAdapter(List<Task> tasksList, TaskListPresenter p) {
        this.tasks = tasksList;
        this.presenter = p;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_object, parent, false);
        return new TasksViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.task_icon)
        ImageView taskIcon_ImageView;

        @BindView(R.id.task_name)
        TextView taskName_TextView;

        @BindView(R.id.task_label)
        TextView taskLabel_TextView;

        @BindView(R.id.task_date)
        TextView taskDate_TextView;

        @BindView(R.id.task_time)
        TextView taskTime_TextView;

        private Context context;

        private Task task;

        TasksViewHolder(View itemView, Context c) {
            super(itemView);

            itemView.setOnClickListener(this);
            this.context = c;

            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            task = tasks.get(position);

            int taskType = getTaskImage(task);
            if (taskType != -1) {
                this.taskIcon_ImageView.setImageResource(taskType);
                this.taskIcon_ImageView.setVisibility(View.VISIBLE);
            } else {
                this.taskIcon_ImageView.setVisibility(View.GONE);
            }

            this.taskName_TextView.setText(task.taskName);
            this.taskLabel_TextView.setText(task.taskLabel);

            String dayOfYear = presenter.getDate(task.taskDate);
            if (dayOfYear != null && dayOfYear.length() > 0) {
                taskDate_TextView.setText(dayOfYear);
                taskDate_TextView.setVisibility(View.VISIBLE);
            }

            String timeOfDay = presenter.getTime(task.taskDate);
            if (timeOfDay != null && timeOfDay.length() > 0) {
                taskTime_TextView.setText(timeOfDay);
                taskTime_TextView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            TaskDetailsActivity.start(context, task.uid);
        }

        private int getTaskImage(Task task) {
            switch (task.taskTypeId) {
                case TaskTypeIds.TASK_TYPE_EVENT:
                    return R.drawable.ic_event;
                case TaskTypeIds.TASK_TYPE_TASK:
                    return R.drawable.ic_task;
                case TaskTypeIds.TASK_TYPE_TASK_CANCELLED:
                    return R.drawable.ic_task_cancelled;
                case TaskTypeIds.TASK_TYPE_TASK_COMPLETED:
                    return R.drawable.ic_task_completed;
                case TaskTypeIds.TASK_TYPE_TASK_RESCHEDULED:
                    return R.drawable.ic_task_rescheduled;
                case TaskTypeIds.TASK_TYPE_TASK_SCHEDULED:
                    return R.drawable.ic_task_scheduled;
                case TaskTypeIds.TASK_TYPE_DEADLINE:
                    return R.drawable.ic_task_deadline;
                case TaskTypeIds.TASK_TYPE_URGENT:
                    return R.drawable.ic_task_urgent;
                case TaskTypeIds.TASK_TYPE_NOTE:
                    return R.drawable.ic_note;
                default:
                    break;
            }

            return -1;
        }
    }
}

