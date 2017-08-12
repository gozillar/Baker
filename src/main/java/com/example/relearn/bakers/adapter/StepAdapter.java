package com.example.relearn.bakers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.relearn.bakers.model.Step;
import com.example.relearn.bakers.R;

import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepsViewHolder> {

    private static final String TAG = StepAdapter.class.getSimpleName();
    private List<Step> items = new ArrayList<>();
    private ListItemClickListener listItemClickListener;

    public StepAdapter(List<Step> arrayList, ListItemClickListener listItemClickListener) {
        this.items = arrayList;
        this.listItemClickListener = listItemClickListener;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_card, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Step)
            return 1;
        else
            return -1;
    }

    @Override
    public void onBindViewHolder(final StepsViewHolder holder, int position) {

        final Step steps = items.get(position);
        if (steps != null) {
            holder.stepTextView.setText(steps.getShortDescription());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                listItemClickListener.onListItemClick(clickedPosition);
            }
        });
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    class StepsViewHolder extends RecyclerView.ViewHolder {

        TextView stepTextView;

        public StepsViewHolder(View view) {
            super(view);
            stepTextView = (TextView) view.findViewById(R.id.stepNumber);

        }

    }

}
