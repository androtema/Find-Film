package com.temalu.findfilm.data

import androidx.lifecycle.LiveData
import com.temalu.findfilm.data.dao.FilmDao
import com.temalu.findfilm.data.entity.Film
import java.util.concurrent.Executors

class MainRepository(private val filmDao: FilmDao) : Repository {

    override fun putToDb(films: List<Film>) {
        //Запросы в БД должны быть в отдельном потоке
        Executors.newSingleThreadExecutor().execute {
            filmDao.insertAll(films)
        }
    }

    override fun getAllFromDB(): LiveData<List<Film>> {
        return filmDao.getCachedFilms()
    }
}