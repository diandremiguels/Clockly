package com.example.clockly.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.clockly.database.TaskContract;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, com.example.clockly.database.TaskContract.DB_NAME, null, com.example.clockly.database.TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTaskTable = "CREATE TABLE " + com.example.clockly.database.TaskContract.TaskEntry.TASK_TABLE + " ( " +
                com.example.clockly.database.TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                com.example.clockly.database.TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL);";
        String createReqTable = "CREATE TABLE " + com.example.clockly.database.TaskContract.TaskEntry.REQ_TABLE + " ( " +
                com.example.clockly.database.TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                com.example.clockly.database.TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL);";
        db.execSQL(createTaskTable);
        db.execSQL(createReqTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + com.example.clockly.database.TaskContract.TaskEntry.TASK_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.REQ_TABLE);
        onCreate(db);
    }
}
