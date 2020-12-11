package com.example.clockly.database;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.example.todolist.database";
    public static final int DB_VERSION = 1;

    // Specifies what must happen when the database was created
    public class TaskEntry implements BaseColumns {
        public static final String TASK_TABLE = "tasks";
        public static final String REQ_TABLE = "requirements";

        public static final String COL_TASK_TITLE = "title";
    }
}