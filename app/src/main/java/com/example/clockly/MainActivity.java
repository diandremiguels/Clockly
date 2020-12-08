package com.example.clockly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.clockly.Adapter.ToDoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView taskRecyclerView;
    private ToDoAdapter tasksAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle uToggle;
    private List<ToDoModel> taskList;
    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //This is the menu bar
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        uToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close );
        drawerLayout.addDrawerListener(uToggle);
        uToggle.syncState();
        getSupportActionBar().setTitle("Daily Tasks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //This is for the tasks view
        taskList = new ArrayList<>();
        taskRecyclerView = findViewById(R.id.tasksRecyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(this);
        taskRecyclerView.setAdapter(tasksAdapter);

        ToDoModel task = new ToDoModel();
        task.setTask("This is a test task");
        task.setStatus(0);
        task.setId(1);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        tasksAdapter.setTasks(taskList);
        button = (FloatingActionButton) findViewById(R.id.button);
        button.setOnClickListener (new View.OnClickListener() {
            @NonNull
            @Override
            public void onClick(View view) {
                openDialog();
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(uToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openDialog () {
        dialog dialog = new dialog();
        dialog.show(getSupportFragmentManager(),"example dialog");
    }

}