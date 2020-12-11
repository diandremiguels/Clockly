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

public class dialogRequiredTime extends AppCompatDialogFragment {
    private EditText editTextTask;
    private EditText editTextStart;
    private EditText editTextEnd;
    private DbHelper mHelper; // don't know how to initialize properly yet :/

    // Creates pop-up menu asking for information about required time
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        mHelper = new DbHelper(getContext());
        final EditText taskEditText = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add New Required Time");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogrequiredtime, null);
        editTextTask = (EditText) view.findViewById(R.id.requiredTaskName);
        editTextStart = (EditText) view.findViewById(R.id.taskStartTime);
        editTextEnd = (EditText) view.findViewById(R.id.taskEndTime);
        ContentValues values = new ContentValues();
        builder.setView(view);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @NonNull
            @Override // adds required time information into database
            public void onClick(DialogInterface dialogInterface, int i) {
                String taskName = String.valueOf(editTextTask.getText());
                String startTime = String.valueOf(editTextStart.getText());
                String endTime = String.valueOf(editTextEnd.getText());
                String databaseEntry = startTime + " " + endTime + " " + taskName;
                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, databaseEntry);
                db.insertWithOnConflict(TaskContract.TaskEntry.REQ_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                db.close();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            // does nothing when cancel button is clicked
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return builder.create();
    }

}