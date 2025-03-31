package com.temalu.findfilm.domain

import com.temalu.findfilm.data.MainRepository
import com.temalu.findfilm.data.PreferenceProvider
import com.temalu.findfilm.data.entity.Film
import com.temalu.findfilm.data.tmdb.API_KEY
import com.temalu.findfilm.data.tmdb.TmdbApi
import com.temalu.findfilm.presentation.utils.Converter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class Interactor(
    private val repo: MainRepository,
    private val retrofitService: TmdbApi,
    private val preferences: PreferenceProvider
) {
    fun getFilmsFromApi(page: Int): Flow<ApiResult> = flow {
        try {
            emit(ApiResult.Loading)

            // Получаем данные синхронно с помощью корутин
            val response = retrofitService.getFilms(
                getDefaultCategoryFromPreferences(),
                API_KEY.TMDB_FILMS_API,
                "ru-RU",
                page
            )

            val films = Converter.convertApiListToDtoListFlow(response.tmdbFilms)
                .first() // Получаем первый (и единственный) элемент Flow

            // Сохраняем в БД
            repo.putToDb(films).collect()

            // Эмитируем успешный результат
            emit(ApiResult.Success(films))

        } catch (t: Throwable) {
            // Эмитируем ошибку
            emit(ApiResult.Error(t))
        }
    }.flowOn(Dispatchers.IO)

    fun getFilmsFromDB(): Flow<List<Film>> = repo.getAllFromDB()

    //Метод для сохранения настроек
    fun saveDefaultCategoryToPreferences(category: String) {
        preferences.saveDefaultCategory(category)
    }

    //Метод для получения настроек
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()
}

sealed class ApiResult {
    object Loading : ApiResult()
    data class Success(val films: List<Film>) : ApiResult()
    data class Error(val throwable: Throwable) : ApiResult()
}