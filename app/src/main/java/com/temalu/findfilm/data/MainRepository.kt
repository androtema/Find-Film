package com.temalu.findfilm.data

import android.content.ContentValues
import android.database.Cursor
import com.temalu.findfilm.data.dao.FilmDao
import com.temalu.findfilm.data.entity.Film
import java.util.concurrent.Executors

class MainRepository(private val filmDao: FilmDao) : Repository {

    fun putToDb(films: List<Film>) {
        //Запросы в БД должны быть в отдельном потоке
        Executors.newSingleThreadExecutor().execute {
            filmDao.insertAll(films)
        }
    }

    fun getAllFromDB(): List<Film> {
        return filmDao.getCachedFilms()
    }
}