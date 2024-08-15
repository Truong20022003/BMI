package com.example.bmi.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Dbhelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "Bmi"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "User"
        private const val COL_Gender = "Gender"
        private const val COL_Age = "Age"
        private const val COL_Date = "Date"
        private const val COL_Height = "Height"
        private const val COL_Weight = "Weight"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COL_Date TEXT NOT NULL PRIMARY KEY,
                $COL_Gender TEXT NOT NULL,
                $COL_Age TEXT NOT NULL,
                $COL_Height TEXT NOT NULL,
                $COL_Weight TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }

    fun saveUserData(date: String, gender: String, age: String, height: String, weight: String) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_Date, date)
            put(COL_Gender, gender)
            put(COL_Age, age)
            put(COL_Height, height)
            put(COL_Weight, weight)
        }

        val cursor = db.query(TABLE_NAME, null, "$COL_Date=?", arrayOf(date), null, null, null)
        if (cursor.moveToFirst()) {
            db.update(TABLE_NAME, contentValues, "$COL_Date=?", arrayOf(date))
        } else {
            db.insert(TABLE_NAME, null, contentValues)
        }
        cursor.close()
        db.close()
    }


}
