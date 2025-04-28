package com.androtema.local.data

import android.util.Log
import com.androtema.local.data.dao.FilmDao
import com.androtema.local.data.entity.Film
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainRepository(private val filmDao: FilmDao) : Repository {

    override fun putToDb(films: List<Film>): Completable {
        return filmDao.insertAll(films)
            .ignoreElement()  // Преобразуем Single в Completable
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                Log.d("Repository", "Inserted ${films.size} films")
            }
    }

    override fun getAllFromDB(): Observable<List<Film>> {
        return filmDao.getCachedFilms()
            .subscribeOn(Schedulers.io())
            .doOnNext { films ->
                Log.d("Repository", "Retrieved ${films.size} films")
            }
    }
}