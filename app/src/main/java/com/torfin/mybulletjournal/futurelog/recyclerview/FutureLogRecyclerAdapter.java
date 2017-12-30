package com.torfin.mybulletjournal.futurelog.recyclerview;

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
import com.torfin.mybulletjournal.utils.DateUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by torftorf1 on 12/29/17.
 */

public class FutureLogRecyclerAdapter extends RecyclerView.Adapter<LogViewHolder> {

    private List<Object> logObjects;

    public FutureLogRecyclerAdapter(List<Object> objects) {
        this.logObjects = objects;
    }

    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.future_log_task, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LogViewHolder holder, int position) {
        holder.bind(logObjects.get(position) instanceof Task, logObjects.get(position));
    }

    @Override
    public int getItemCount() {
        return this.logObjects.size();
    }
}


class LogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.future_log_task_icon)
    ImageView icon;

    @BindView(R.id.future_log_task_name)
    TextView taskHeader;

    @BindView(R.id.future_log_task_date)
    TextView taskDateHeader;

    @BindView(R.id.future_log_task_divider)
    View divider;

    @BindView(R.id.date_header_view)
    TextView dateHeader;

    private Task task;

    public LogViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(this);
    }

    public void bind(boolean isTask, Object object) {
        if (isTask) {

            task = (Task) object;
            icon.setImageResource(getTaskImage(task));
            taskHeader.setText(task.taskName);
            taskDateHeader.setText(DateUtils.formatDate(task.taskDate));
            icon.setContentDescription(task.taskType + " icon");
        } else if (object instanceof String) {

            icon.setVisibility(View.GONE);
            taskHeader.setVisibility(View.GONE);
            taskDateHeader.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);

            dateHeader.setText((String)object);
            dateHeader.setVisibility(View.VISIBLE);
        } else {

            icon.setVisibility(View.GONE);
            taskHeader.setVisibility(View.GONE);
            taskDateHeader.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
            dateHeader.setVisibility(View.GONE);
        }
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

    @Override
    public void onClick(View v) {
        if (task != null) {
            TaskDetailsActivity.start(v.getContext(), task.uid);
        }
    }
}