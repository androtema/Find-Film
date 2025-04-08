package com.temalu.findfilm.presentation.utils

import com.temalu.findfilm.data.entity.Film
import com.temalu.findfilm.data.tmdb.TmdbFilm
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object Converter {

    fun convertApiListToDto(list: List<TmdbFilm>?): Observable<List<Film>> {
        return Observable.fromCallable {
            list?.map { convertFilm(it) } ?: emptyList()
        }.subscribeOn(Schedulers.computation())
    }

    private fun convertFilm(tmdbFilm: TmdbFilm): Film {
        return Film(
            title = tmdbFilm.title,
            poster = tmdbFilm.posterPath ?: DEFAULT_POSTER_URL,
            description = tmdbFilm.overview,
            rating = tmdbFilm.voteAverage,
            isInFavorites = false
        )
    }

    private const val DEFAULT_POSTER_URL = "https://www.kino-teatr.ru/static/images/no_poster.jpg"
}