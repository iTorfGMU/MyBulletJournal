package com.torfin.mybulletjournal.viewlabels.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.dataobjects.Label;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by torftorf1 on 12/28/17.
 */

public class LabelsRecyclerViewAdapter extends RecyclerView.Adapter<LabelsViewHolder> {

    private List<String> labels;

    public LabelsRecyclerViewAdapter(Context context, List<String> list) {
        this.labels = list;
    }

    @Override
    public LabelsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.label_list_object, parent, false);
        return new LabelsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LabelsViewHolder holder, int position) {
        holder.bind(labels.get(position), (position == getItemCount()));
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }
}

class LabelsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.label_textView)
    TextView labelTextView;

    @BindView(R.id.label_divider)
    View divider;

    public LabelsViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    void bind(String label, boolean isLast) {
        labelTextView.setText(label);
        labelTextView.setContentDescription(label.toLowerCase() + " label");

        if (isLast) {
            divider.setVisibility(View.GONE);
        }
    }
}
