package com.temalu.findfilm.data

import android.content.ContentValues
import android.database.Cursor
import com.temalu.findfilm.data.db.DatabaseHelper
import com.temalu.findfilm.domain.Film

class MainRepository(databaseHelper: DatabaseHelper) : Repository {
    //Инициализируем объект для взаимодействия с БД
    private val sqlDb = databaseHelper.readableDatabase
    //Создаем курсор для обработки запросов из БД
    private lateinit var cursor: Cursor

    fun putToDb(film: Film) {
        //Создаем объект, который будет хранить пары ключ-значение, для того
        //чтобы класть нужные данные в нужные столбцы
        val cv = ContentValues()
        cv.apply {
            put(DatabaseHelper.COLUMN_TITLE, film.title)
            put(DatabaseHelper.COLUMN_POSTER, film.poster)
            put(DatabaseHelper.COLUMN_DESCRIPTION, film.description)
            put(DatabaseHelper.COLUMN_RATING, film.rating)
        }
        //Кладем фильм в БД
        sqlDb.execSQL("INSERT OR IGNORE INTO ${DatabaseHelper.TABLE_NAME} (title, poster, overview, vote_average) VALUES (?, ?, ?, ?)",
            arrayOf(film.title, film.poster, film.description, film.rating))
    }

    fun getAllFromDB(): List<Film> {
        //Создаем курсор на основании запроса "Получить все из таблицы"
        cursor = sqlDb.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME}", null)
        val result = mutableListOf<Film>()
        //Проверяем, есть ли хоть одна строка в ответе на запрос
        if (cursor.moveToFirst()) {
            //Итерируемся по таблице, пока есть записи, и создаем на основании объект Film
            do {
                val title = cursor.getString(1)
                val poster = cursor.getString(2)
                val description = cursor.getString(3)
                val rating = cursor.getDouble(4)

                result.add(Film(title, poster, description, rating))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return result
    }
}