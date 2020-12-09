package com.example.clockly;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.clockly.Adapter.ToDoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private Algorithm algorithm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        algorithm = new Algorithm();
        updateUI();
    }

    private void updateUI() {
        algorithm.mapSchedule();
        List<String> taskList = algorithm.scheduleMapToList();

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.schedule_task_layout, // what view to use for the items
                    R.id.task_title, // where to put the String of data
                    taskList); // where to get all the data
            mTaskListView.setAdapter(mAdapter); // set it as the adapter of the ListView instance
        } else {
            // if the adapter's already made, clear it, re-populate it, and notify the view that the data has changed
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }
}