package com.temalu.findfilm.presentation.utils

import com.temalu.findfilm.data.tmdb.TmdbFilm
import com.temalu.findfilm.data.entity.Film

object Converter {
    fun convertApiListToDtoList(list: List<TmdbFilm>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(
                Film(
                title = it.title,
                poster = it.posterPath ?: "https://www.kino-teatr.ru/static/images/no_poster.jpg",
                description = it.overview,
                rating = it.voteAverage,
                isInFavorites = false
            )
            )
        }
        return result
    }
}