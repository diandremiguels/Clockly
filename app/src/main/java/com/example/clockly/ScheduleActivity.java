package com.example.clockly;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

// import com.example.clockly.Adapter.ToDoAdapter;
import com.example.clockly.database.DbHelper;
import com.example.clockly.database.TaskContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    private DbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private Algorithm algorithm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        mHelper = new DbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.schedule_toolbar);
        setSupportActionBar(myChildToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        algorithm = new Algorithm();
        updateUI();
    }

    private void updateUI() {
         List<String> tasks = getAllTasks(TaskContract.TaskEntry.TASK_TABLE);
         List<String> requirements = getAllTasks(TaskContract.TaskEntry.REQ_TABLE);
         algorithm.addAllRequirements(requirements);
         algorithm.addAllTasks(tasks);
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

    private List<String> getAllTasks(String table){
        List<String> tasks = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + table;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()){
            do {
                String task = c.getString(c.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE));
                tasks.add(task);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return tasks;
    }
}