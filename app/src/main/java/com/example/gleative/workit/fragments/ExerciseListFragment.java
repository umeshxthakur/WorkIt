package com.example.gleative.workit.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gleative.workit.R;
import com.example.gleative.workit.adapter.ExercisesRecyclerAdapter;
import com.example.gleative.workit.adapter.OnExerciseSelectedListener;
import com.example.gleative.workit.adapter.RecycleAdapterListener;
import com.example.gleative.workit.model.Exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gleative on 12.10.2017.
 *
 * Gets the exercises from the database, and sets it out in a cardview
 */

public class ExerciseListFragment extends Fragment implements RecycleAdapterListener{

    private RecyclerView recyclerView;
    private OnExerciseFragmentInteractionListener listener;
    ExercisesRecyclerAdapter adapter;
    private List<Exercise> exercisesList;

    // Empty constructor required
    public ExerciseListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);

        setUpRecyclerView(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            listener = (OnExerciseFragmentInteractionListener) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + "must implement OnFragmentInteractionListener");
        }
    }

    // Sets up the recyclerView with the type of linear layout
    private void setUpRecyclerView(View view){
        recyclerView = view.findViewById(R.id.exercise_recycler_view); // Henter listen fra layout fil "fragment_exercise_list"
        adapter = new ExercisesRecyclerAdapter(getContext(), Exercise.getData(), this); // Må ha constructor på adapteren ellers du får error! this, Exercise.getData()
        recyclerView.setAdapter(adapter);

        // Sets up the list in a new layout
        LinearLayoutManager linearLayoutManagerVertical = new LinearLayoutManager(getContext());
        linearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManagerVertical);

    }

    public void fillList(){
        for(int i = 0; i <= adapter.getItemCount(); i++){
            exercisesList.add(adapter.getItem(i));
        }
    }

    public void filterExercises(String newText){
        newText = newText.toLowerCase();
        List<Exercise> newList = new ArrayList<>();


        for(Exercise exercise: exercisesList){
            String exerciseName = exercise.getExerciseName().toLowerCase();
            // If the name is in the query from the user, add it to a new list, that will be displayed for the user
            if(exerciseName.contains(newText)){
                newList.add(exercise);
            }
        }

        adapter.setFilter(newList);
    }



    @Override
    public void exerciseSelected(Exercise exercise) {
        Toast.makeText(getContext(), exercise.getExerciseName() + " Selected", Toast.LENGTH_SHORT).show();

        listener.onExerciseSelected(exercise);
    }

    public interface OnExerciseFragmentInteractionListener{
        void onExerciseSelected(Exercise exercise);
    }
}