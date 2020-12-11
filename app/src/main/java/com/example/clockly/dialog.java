package com.example.clockly;

import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.clockly.database.DbHelper;
import com.example.clockly.database.TaskContract;

public class dialog extends AppCompatDialogFragment {
    private EditText editTextTask;
    private EditText editTextDuration;
    private DbHelper mHelper;

    // Creates pop-up menu asking for information about tasks
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        mHelper = new DbHelper(getContext());
        final EditText taskEditText = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add a new task");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);
        editTextTask = view.findViewById(R.id.taskname);
        editTextDuration = view.findViewById(R.id.Duration);
        final EditText editTask = new EditText(getActivity());
        builder.setView(view);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @NonNull
            @Override // adds task information to database once the user clicks add
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = String.valueOf(editTextTask.getText());
                String duration = String.valueOf(editTextDuration.getText());
                String databaseEntry = duration + " " + name;
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, databaseEntry);
                db.insertWithOnConflict(TaskContract.TaskEntry.TASK_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                db.close();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            // does nothing when cancel is pressed
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        return builder.create();
    }

}
