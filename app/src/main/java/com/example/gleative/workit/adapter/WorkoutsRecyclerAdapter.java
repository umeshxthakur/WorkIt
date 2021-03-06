package com.example.gleative.workit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gleative.workit.R;
import com.example.gleative.workit.model.Workout;

import java.util.List;

/**
 * Created by glenn on 17.10.2017.
 */

public class WorkoutsRecyclerAdapter extends RecyclerView.Adapter<WorkoutViewHolder>{

    private List<Workout> workoutData;
    private LayoutInflater inflater;
    private OnWorkoutSelectedListener workoutSelectedListener;
    private WorkoutRecycleAdapterListener workoutRecycleAdapterListener;

    // 0 is with delete icon and 1 is without the delete icon, we dont want to user be able to delete a workout when they are going to start it
    private int layoutType;

    public WorkoutsRecyclerAdapter(Context context, List<Workout> data, int layoutType,
                                   WorkoutRecycleAdapterListener _workoutRecycleAdapterListener){
        this.workoutData = data;
        this.inflater = LayoutInflater.from(context);
        this.layoutType = layoutType;

        this.workoutRecycleAdapterListener = _workoutRecycleAdapterListener;

        workoutSelectedListener = new OnWorkoutSelectedListener() {
            @Override
            public void workoutSelected(int position) {
                Workout workout = workoutData.get(position);
                workoutRecycleAdapterListener.workoutSelected(workout);
            }

            @Override
            public void workoutDeleteImageSelected(View v, int position) {
                Workout workout = workoutData.get(position);
                workoutRecycleAdapterListener.workoutDeleteImageSelected(v, workout);
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public WorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_workout, parent, false); // Inflater list_exercise layout, so it gets its design
        WorkoutViewHolder holder = new WorkoutViewHolder(view);

        view.setOnClickListener(holder);
        holder.setListenersToImages(); // Adds listeners to delete icon

        return holder;
    }

    @Override
    public void onBindViewHolder(WorkoutViewHolder holder, int position) {
        Workout currObj = workoutData.get(position);

        holder.bind(currObj, workoutSelectedListener, layoutType);
    }

    @Override
    public int getItemCount() {
        return workoutData.size();
    }

    public Workout getItem(int position){
        return workoutData.get(position);
    }

    // Updates the adapter with new data
    public void updateAdapter(List<Workout> newList){
        this.workoutData = newList;
        notifyDataSetChanged();
    }


}
