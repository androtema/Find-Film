package com.temalu.findfilm.data

import android.util.Log
import com.temalu.findfilm.data.dao.FilmDao
import com.temalu.findfilm.data.entity.Film
import com.temalu.findfilm.data.tmdb.API_KEY
import com.temalu.findfilm.data.tmdb.TmdbFilm
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.subscribeOn
import kotlin.collections.map

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