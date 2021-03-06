package com.example.gleative.workit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.gleative.workit.adapter.ExercisesRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Is a exercise, but has custom reps, sets or time
 *
 * Created by gleative on 09.10.2017.
 */

public class CustomExercise implements Parcelable{

    private String customExerciseID;
    private String workoutID; // Which workout the customexercise is in
    private int exerciseID; // Which exercise is customized
    private Exercise exercise;
    private int sets;
    private int reps;

    public CustomExercise(){
        super();
    }

    public CustomExercise(String _customExerciseID, String _workoutID, int _exerciseID, Exercise _exercise, int _sets, int _reps){
        this.customExerciseID = _customExerciseID;
        this.workoutID = _workoutID;
        this.exerciseID = _exerciseID;
        this.exercise = _exercise;
        this.sets = _sets;
        this.reps = _reps;
    }

    public CustomExercise(Parcel parcel){
        this.customExerciseID = parcel.readString();
        this.workoutID = parcel.readString();
        this.exerciseID = parcel.readInt();
        this.exercise = parcel.readParcelable(Exercise.class.getClassLoader());
        this.sets = parcel.readInt();
        this.reps = parcel.readInt();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.customExerciseID);
        dest.writeString(this.workoutID);
        dest.writeInt(this.exerciseID);
        dest.writeParcelable(this.exercise, flags);
        dest.writeInt(this.sets);
        dest.writeInt(this.reps);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CustomExercise> CREATOR = new Creator<CustomExercise>() {
        @Override
        public CustomExercise createFromParcel(Parcel in) {
            return new CustomExercise(in);
        }

        @Override
        public CustomExercise[] newArray(int size) {
            return new CustomExercise[size];
        }
    };

    public String getCustomExerciseID(){
        return this.customExerciseID;
    }

    public void setCustomExerciseID(String customExerciseID){
        this.customExerciseID = customExerciseID;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public String getWorkoutID(){
        return workoutID;
    }

    public void setWorkoutID(String workoutID){
        this.workoutID = workoutID;
    }

    public int getExerciseID(){
        return exerciseID;
    }

    public void setExerciseID(int exerciseID){
        this.exerciseID = exerciseID;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }
}
