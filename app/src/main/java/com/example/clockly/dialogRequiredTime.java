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

public class dialogRequiredTime extends AppCompatDialogFragment {
    private EditText editTextTask;
    private EditText editTextStart;
    private EditText editTextEnd;

    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
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
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return builder.create();
    }

}