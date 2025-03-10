package com.temalu.findfilm.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        //Создаем саму таблицу для фильмов
        db?.execSQL(
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_TITLE TEXT UNIQUE," +
                    "$COLUMN_POSTER TEXT," +
                    "$COLUMN_DESCRIPTION TEXT," +
                    "$COLUMN_RATING REAL);"
        )
    }
    //Миграций мы не предполагаем, поэтому метод пустой
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN release_date TEXT DEFAULT ''")
        }
        if (oldVersion < 3) {
            db?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN genre TEXT DEFAULT ''")
        }
    }

    companion object {
        private const val DATABASE_NAME = "films.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "films_table"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_POSTER = "poster"
        const val COLUMN_DESCRIPTION = "overview"
        const val COLUMN_RATING = "vote_average"
    }
}