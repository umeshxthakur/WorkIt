package com.example.gleative.workit;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.gleative.workit.fragments.NavigationDrawerFragment;

public class MyWorkoutActivity extends AppCompatActivity {

    Toolbar toolbar;
    NavigationDrawerFragment navigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpDrawer();
    }

    private void setUpDrawer() {
        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationDrawerFragment.setUpDrawer(drawerLayout, toolbar, R.id.nav_exercises);
    }

    @Override
    protected void onStart(){
        navigationDrawerFragment.updateCheckedItem(R.id.nav_myWorkouts);

        super.onStart();
    }

    // FAB button in activity_workout.xml, Starts CreateWorkoutActivity
    public void createWorkout(View view) {
        Intent intent = new Intent(this, CreateWorkoutActivity.class);
        startActivity(intent);
    }
}
