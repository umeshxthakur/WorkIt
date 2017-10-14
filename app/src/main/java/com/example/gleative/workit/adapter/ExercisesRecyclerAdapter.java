package com.example.gleative.workit.adapter;

import com.example.gleative.workit.R;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gleative.workit.model.Exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gleative on 09.10.2017.
 */

public class ExercisesRecyclerAdapter extends RecyclerView.Adapter<ExerciseViewHolder>{

    private List<Exercise> exerciseData;
    private LayoutInflater inflater;
    private OnExerciseSelectedListener exerciseSelectedListener;
    private RecycleAdapterListener recycleAdapterListener;

    private List<Exercise> list; // This is needed for the filter method, that handles when user searches.

    public ExercisesRecyclerAdapter(Context context, List<Exercise> data, RecycleAdapterListener _recycleAdapterListener){
        this.exerciseData = data;
        this.inflater = LayoutInflater.from(context);

        this.recycleAdapterListener = _recycleAdapterListener;

        exerciseSelectedListener = new OnExerciseSelectedListener() {
            @Override
            public void exerciseSelected(int position) {
                Exercise exercise = exerciseData.get(position);
                recycleAdapterListener.exerciseSelected(exercise);
            }
        };
    }

    // Måtte ha disse pga extend RecyclerView

    // Får tak i layoutfilen
    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_exercises, parent, false); // Inflater list_exercise layout, so it gets its design
        ExerciseViewHolder holder = new ExerciseViewHolder(view);

        view.setOnClickListener(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        Exercise currentObj = exerciseData.get(position);
        holder.bind(currentObj, exerciseSelectedListener);
    }

    @Override
    public int getItemCount() {
        return exerciseData.size();
    }

    public Exercise getItem(int position){
        return exerciseData.get(position);
    }


    public void setFilter(List<Exercise> newList){
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged(); // This refreshes the adapter.
    }

}
