package com.temalu.findfilm.presentation.utils

import com.temalu.findfilm.data.entity.Film
import com.temalu.findfilm.data.tmdb.TmdbFilm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object Converter {

    fun convertApiListToDtoListFlow(list: List<TmdbFilm>?): Flow<List<Film>> = flow {
        val convertedList = list?.map { convertFilm(it) } ?: emptyList()
        emit(convertedList)
    }.flowOn(Dispatchers.Default)

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