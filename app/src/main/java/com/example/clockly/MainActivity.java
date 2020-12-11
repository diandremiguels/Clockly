package com.example.clockly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

//import com.example.clockly.Adapter.ToDoAdapter;
import com.example.clockly.database.DbHelper;
import com.example.clockly.database.TaskContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private DbHelper mHelper;
    private ArrayAdapter<String> taskAdapter;
    private ArrayAdapter<String> reqAdapter;
    private ListView mTaskListView;
    private ListView mReqListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTaskListView = (ListView) findViewById(R.id.list_tasks);
        mReqListView = (ListView) findViewById(R.id.list_reqs);
        mHelper = new DbHelper(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(myToolbar);
        updateUI();
    }


    public void openDialog () {
        dialog dialog = new dialog();
        dialog.show(getSupportFragmentManager(),"example dialog");
    }
    public void openRequiredTimeDialog() {
        dialogRequiredTime dialog = new dialogRequiredTime();
        dialog.show(getSupportFragmentManager(), "required time dialog");
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.tasks:
                openDialog();
                return true;
            case R.id.required_times:
                openRequiredTimeDialog();
                return true;
            case R.id.refresh:
               updateUI();
                return true;
        }
        return true;
    }

    public void openSchedule(View view){
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
    }

    public void updateUI(){
        // Handle task list
        List<String> tasks = getAllTasks(TaskContract.TaskEntry.TASK_TABLE);
        List<String> tasksAsOutput = new ArrayList<>();
        for (String task : tasks){
            tasksAsOutput.add(makeTaskOutput(task));
        }
        if (taskAdapter == null) {
            taskAdapter = new ArrayAdapter<>(this,
                    R.layout.main_activity_task_layout, // what view to use for the items
                    R.id.task_title, // where to put the String of data
                    tasksAsOutput); // where to get all the data
            mTaskListView.setAdapter(taskAdapter); // set it as the adapter of the ListView instance
        } else {
            // if the adapter's already made, clear it, re-populate it, and notify the view that the data has changed
            taskAdapter.clear();
            taskAdapter.addAll(tasksAsOutput);
            taskAdapter.notifyDataSetChanged();
        }

        // Handle req list
        List<String> reqs = getAllTasks(TaskContract.TaskEntry.REQ_TABLE);
        List<String> reqsAsOutput = new ArrayList<>();
        for (String req : reqs){
            reqsAsOutput.add(makeReqOutput(req));
        }
        if (reqAdapter == null) {
            reqAdapter = new ArrayAdapter<>(this,
                    R.layout.main_activity_req_layout, // what view to use for the items
                    R.id.task_title, // where to put the String of data
                    reqsAsOutput); // where to get all the data
            mReqListView.setAdapter(reqAdapter); // set it as the adapter of the ListView instance
        } else {
            // if the adapter's already made, clear it, re-populate it, and notify the view that the data has changed
            reqAdapter.clear();
            reqAdapter.addAll(reqsAsOutput);
            reqAdapter.notifyDataSetChanged();
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

    public void deleteTask(View view){
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = translateTaskOutput(String.valueOf(taskTextView.getText()));

        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TASK_TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

    public void deleteRequirement(View view){
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = translateReqOutput(String.valueOf(taskTextView.getText()));
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.REQ_TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

    private String makeTaskOutput(String task){
        String[] words = task.split(" ");
        String output = "";
        if (words.length >= 2){
            String duration = words[0];
            String name = "";
            for (int i = 1; i < words.length; i++){
                name += words[i] + " ";
            }
            output = duration + " minutes of " + name.trim();
        }
        return output;
    }

    private String translateTaskOutput(String taskOutput){
        String[] words = taskOutput.split(" ");
        String output = "";
        if (words.length >= 4){
            String duration = words[0];
            String name = "";
            for (int i = 3; i < words.length; i++){
                name += words[i] + " ";
            }
            output = duration + " " + name.trim();
        }
        return output;
    }

    private String makeReqOutput(String req){
        String[] words = req.split(" ");
        String output = "";
        if (words.length >= 3){
            String startTime = words[0];
            String endTime = words[1];
            String name = "";
            for (int i = 2; i < words.length; i++){
                name += words[i] + " ";
            }
            output = startTime + " - " + endTime + " --> " + name.trim();
        }
        return output;
    }

    private String translateReqOutput(String reqOutput){
        String[] words = reqOutput.split(" ");
        String output = "";
        if (words.length >= 5){
            String startTime = words[0];
            String endTime = words[2];
            String name = "";
            for (int i = 4; i < words.length; i++){
                name += words[i] + " ";
            }
            output = startTime + " " + endTime + " " + name.trim();
        }
        return output;
    }
}