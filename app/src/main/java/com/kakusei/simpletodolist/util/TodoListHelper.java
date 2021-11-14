package com.kakusei.simpletodolist.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TodoListHelper extends SQLiteOpenHelper {
    public static final String CREATE_EVENT_TABLE =
            "CREATE TABLE event(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title VARCHAR(255) NOT NULL," +
                    "body TEXT," +
                    "creationTime TEXT NOT NULL," +
                    "deadLine TEXT," +
                    "time TEXT," +
                    "status BOOLEAN NOT NULL)";

    private Context context;

    public TodoListHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
