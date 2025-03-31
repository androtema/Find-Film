package com.temalu.findfilm.domain

import com.temalu.findfilm.data.MainRepository
import com.temalu.findfilm.data.PreferenceProvider
import com.temalu.findfilm.data.entity.Film
import com.temalu.findfilm.data.tmdb.API_KEY
import com.temalu.findfilm.data.tmdb.TmdbApi
import com.temalu.findfilm.presentation.utils.Converter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Interactor(
    private val repo: MainRepository,
    private val retrofitService: TmdbApi,
    private val preferences: PreferenceProvider
) {
    fun getFilmsFromApi(page: Int): Flow<ApiResult> = flow {
        try {
            // Получаем данные синхронно с помощью корутин
            val response = retrofitService.getFilms(
                getDefaultCategoryFromPreferences(),
                API_KEY.TMDB_FILMS_API,
                "ru-RU",
                page
            )

            val list = Converter.convertApiListToDtoList(response.tmdbFilms)

            // Сохраняем в БД (если repo поддерживает корутины)
            repo.putToDb(list)

            // Эмитируем успешный результат
            emit(ApiResult.Success(list))
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
    data class Success(val films: List<Film>) : ApiResult()
    data class Error(val throwable: Throwable) : ApiResult()
}