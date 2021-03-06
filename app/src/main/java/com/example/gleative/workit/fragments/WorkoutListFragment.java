package com.example.gleative.workit.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gleative.workit.R;
import com.example.gleative.workit.adapter.WorkoutRecycleAdapterListener;
import com.example.gleative.workit.adapter.WorkoutsRecyclerAdapter;
import com.example.gleative.workit.model.CustomExercise;
import com.example.gleative.workit.model.Workout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by glenn on 17.10.2017.
 */

public class WorkoutListFragment extends Fragment implements WorkoutRecycleAdapterListener{

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dbReferenceWorkouts;
    private DatabaseReference dbReferenceCustomExercises;
    private DatabaseReference dbReference3;

    private RecyclerView recyclerView;
    private OnWorkoutFragmentInteractionListener listener;
    private WorkoutsRecyclerAdapter adapter;
    private List<Workout> workoutsList;
    private List<CustomExercise> customExerciseList;

    private Button dialogYesBtn, dialogNoBtn;
    private TextView dialogTitleView, dialogMessageView, dialogItemNameView;
    private ProgressBar loadingSpinner;

    private int layoutType;


    public WorkoutListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_list, container, false);

        // If intent contain any extras, it means the user wants to start a workout. Sets the correct layout
        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null){
            layoutType = 1;
        }
        else{
            layoutType = 0;
        }

        loadingSpinner = view.findViewById(R.id.progress_bar_workouts);

        workoutsList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReferenceWorkouts = firebaseDatabase.getReference().child("workouts");
        dbReferenceCustomExercises = firebaseDatabase.getReference().child("customExercises");

        setUpRecyclerView(view);

        getData();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            listener = (WorkoutListFragment.OnWorkoutFragmentInteractionListener) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + "must implement OnFragmentInteractionListener");
        }
    }

    // Retrieves the workouts data from the database and adds the data to the recycler view
    private void getData(){
        loadingSpinner.setVisibility(View.VISIBLE);

        dbReferenceWorkouts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                workoutsList.clear(); // Clear list before getting the data, if data already exist it will be duplicates
                for(DataSnapshot workoutSnapshot : dataSnapshot.getChildren()){
                    try {
                        Workout workout = new Workout();
                        workout.setWorkoutID(workoutSnapshot.getKey().toString());
                        workout.setWorkoutName(workoutSnapshot.child("workoutName").getValue().toString());
                        workout.setWorkoutDescription(workoutSnapshot.child("workoutDescription").getValue().toString());
                        workoutsList.add(workout);

                        getCustomExercises(workoutsList.size() - 1); // The current workout is the one that is last on the list.
                    } catch(IndexOutOfBoundsException i){
                        i.printStackTrace();
                        /*
                         Not something the user has to know of.
                         This probably happends because of onDataChange being async method.
                         But it deletes exercises and workout correctly
                         */
//                        Toast.makeText(getActivity(), "Index out of bounds (Get workouts)", Toast.LENGTH_SHORT).show();
                    } catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Failed to retrieve workouts", Toast.LENGTH_SHORT).show();
                        loadingSpinner.setVisibility(View.GONE); // Hides the loading spinner because it failed loading the data
                    }

                }

                // Tells the adapter to update so it has the newest data
                adapter.updateAdapter(workoutsList);
                loadingSpinner.setVisibility(View.GONE); // Hides the loading spinner because the data is loaded
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


    }

    // Gets the custom exercises that is in the workout
    private void getCustomExercises(final int workoutListID){

        customExerciseList = new ArrayList<>();

        dbReferenceCustomExercises.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot customExerciseSnapshot : dataSnapshot.getChildren()){

                    try{
                        Workout workout = workoutsList.get(workoutListID); // Gets reference to the workout object
                        CustomExercise customExercise = customExerciseSnapshot.getValue(CustomExercise.class);

                        if(customExercise.getWorkoutID().equals(workout.getWorkoutID())){
                            workout.getCustomExercises().add(customExercise);
                        }

                    } catch (IndexOutOfBoundsException i) {
                        i.printStackTrace();
                        /*
                         Not something the user has to know of.
                         This probably happends because of onDataChange being async method.
                         But it deletes exercises and workout correctly
                         */
//                        Toast.makeText(getActivity(), "Index out of bounds" (Get custom exercises), Toast.LENGTH_SHORT).show();
                    } catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Failed to retrieve custom exercises.", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    // Finds the recycler view and sets it up with the type of linear layout
    private void setUpRecyclerView(View view){

        // Finds the recycler_view from the layout file "fragment_exercise_list"
        recyclerView = view.findViewById(R.id.workout_recycler_view);
        adapter = new WorkoutsRecyclerAdapter(getContext(), workoutsList, layoutType, this);
        recyclerView.setAdapter(adapter);

        // Sets up the list in a new layout
        LinearLayoutManager linearLayoutManagerVertical = new LinearLayoutManager(getContext());
        linearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManagerVertical);


    }

    // Shows a dialog for the user when delete button on a workout is clicked, if yes, it deletes the workout, no is cancel
    private void showDialog(final Workout workout){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_message_layout);

        // Finds the elements in the dialog layout, and sets text
        dialogYesBtn = dialog.findViewById(R.id.dialog_button_yes);
        dialogNoBtn = dialog.findViewById(R.id.dialog_button_no);
        dialogTitleView = dialog.findViewById(R.id.dialog_title);
        dialogMessageView = dialog.findViewById(R.id.dialog_message);
        dialogItemNameView = dialog.findViewById(R.id.dialog_item_name);
        dialogTitleView.setText("Delete Workout");
        dialogMessageView.setText("Are you sure you want to delete ");
        dialogItemNameView.setText(workout.getWorkoutName() + "?");

        dialog.show();

        // Deletes workout and exits the dialog
        dialogYesBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                deleteWorkout(workout);
                dialog.cancel();
            }
        });

        // Exits the dialog
        dialogNoBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    // Deletes the workout from the database and list. Also updates the adapter so the user can see the changes
    private void deleteWorkout(Workout workout){
        try{
            deleteExercisesInWorkout(workout);
            dbReferenceWorkouts.child(workout.getWorkoutID()).removeValue();

            workoutsList.remove(workout);
            Snackbar.make(getActivity().findViewById(R.id.coordinator_layout_my_workouts), "Workout successfully deleted", Snackbar.LENGTH_SHORT).show();

        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to delete workout.", Toast.LENGTH_SHORT).show();
        }

        // Updates the recycler view so it displays for the user it is gone
        adapter.updateAdapter(workoutsList);
    }

    private void deleteExercisesInWorkout(Workout workout){
        try{
            for(CustomExercise customExercise : workout.getCustomExercises()){
                dbReferenceCustomExercises.child(customExercise.getCustomExerciseID()).removeValue();
            }
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            /*
             Not something the user has to know of.
             This probably happends because of onDataChange being async method.
             But it deletes exercises and workout correctly
             */
//            Toast.makeText(getActivity(), "Index out of bounds (Delete custom exercise)", Toast.LENGTH_SHORT).show();
        } catch (Exception i){
            i.printStackTrace();
            Toast.makeText(getActivity(), "Failed to delete exercises", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void workoutSelected(Workout workout) {}

    // If user clicks on the delete img that listener will only get triggered, or else it will check workout info.
    @Override
    public void workoutDeleteImageSelected(View v, Workout workout) {
        if(v.getId() == R.id.img_delete_workout){
            showDialog(workout);
        }
        else{
            listener.onWorkoutSelected(workout);
        }
    }

    public interface OnWorkoutFragmentInteractionListener{
        void onWorkoutSelected(Workout workout);
    }

}
