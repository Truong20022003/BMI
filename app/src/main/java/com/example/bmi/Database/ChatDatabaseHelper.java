package com.example.bmi.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bmi.Model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    // Database name and version
    private static final String DATABASE_NAME = "chat.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    public static final String TABLE_MESSAGES = "messages";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_IS_BOT = "is_bot";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // SQL query to create table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_MESSAGES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MESSAGE + " TEXT, " +
                    COLUMN_IS_BOT + " INTEGER, " +
                    COLUMN_TIMESTAMP + " TEXT" +
                    ");";

    // Constructor
    public ChatDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    // Upgrade table when database version is updated
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(db);
    }

    // Method to retrieve all messages from the database
    public List<ChatMessage> getAllMessages() {
        List<ChatMessage> messages = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MESSAGES, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String message = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE));
                boolean isBot = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_BOT)) == 1;
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP));
                messages.add(new ChatMessage(message, isBot, timestamp));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return messages;
    }

    // Method to clear all messages from the database
    public void clearAllMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGES, null, null);
    }
}
