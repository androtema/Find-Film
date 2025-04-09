package com.temalu.findfilm.domain

import android.util.Log
import com.temalu.findfilm.data.MainRepository
import com.temalu.findfilm.data.PreferenceProvider
import com.temalu.findfilm.data.entity.Film
import com.temalu.findfilm.data.tmdb.API_KEY
import com.temalu.findfilm.data.tmdb.TmdbApi
import com.temalu.findfilm.presentation.utils.Converter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class Interactor(
    private val repo: MainRepository,
    private val retrofitService: TmdbApi,
    private val preferences: PreferenceProvider
) {
    fun getFilmsFromApi(page: Int): Observable<ApiResult> {
        return Observable.defer {
            // Начальное состояние Loading
            val loadingObservable = Observable.just<ApiResult>(ApiResult.Loading)
                .doOnNext { Log.d("Interactor", "Loading") }

            // Запрос к API и обработка результата
            val apiResultObservable = retrofitService.getFilms(
                getDefaultCategoryFromPreferences(),
                API_KEY.TMDB_FILMS_API,
                "ru-RU",
                page
            )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext { Log.d("Interactor", "Success $it") }
                .flatMap { response ->
                    // Преобразуем список фильмов
                    Converter.convertApiListToDto(response.tmdbFilms)
                        .doOnNext { Log.d("Interactor", "Converted $it") }
                        .flatMap { films ->
                            // Сохраняем в БД и возвращаем Success
                            repo.putToDb(films)
                                .doOnComplete { Log.d("Interactor", "Saved to DB") }
                                .andThen(Observable.just<ApiResult>(ApiResult.Success(films)))
                        }
                }
                .onErrorResumeNext { error ->
                    Observable.just<ApiResult>(ApiResult.Error(error))
                        .doOnNext { Log.d("Interactor", "Error $error") }
                }

            // Объединяем Loading и результат API
            loadingObservable.concatWith(apiResultObservable)
        }
    }

    fun getFilmsFromDB(): Observable<List<Film>> = repo.getAllFromDB()

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