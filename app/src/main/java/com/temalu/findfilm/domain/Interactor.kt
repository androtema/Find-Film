package com.temalu.findfilm.domain

import androidx.lifecycle.LiveData
import com.temalu.findfilm.data.MainRepository
import com.temalu.findfilm.data.PreferenceProvider
import com.temalu.findfilm.data.entity.Film
import com.temalu.findfilm.data.tmdb.API_KEY
import com.temalu.findfilm.data.tmdb.TmdbApi
import com.temalu.findfilm.data.tmdb.TmdbResultsDto
import com.temalu.findfilm.presentation.utils.Converter
import com.temalu.findfilm.presentation.viewmodel.HomeFragmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor(
    private val repo: MainRepository,
    private val retrofitService: TmdbApi,
    private val preferences: PreferenceProvider
) {
    fun getFilmsFromApi(page: Int, callback: HomeFragmentViewModel.ApiCallback) {
        //Метод getDefaultCategoryFromPreferences() будет нам получать при каждом запросе нужный нам список фильмов
        retrofitService.getFilms(
            getDefaultCategoryFromPreferences(),
            API_KEY.TMDB_FILMS_API,
            "ru-RU",
            page
        ).enqueue(object : Callback<TmdbResultsDto> {
            override fun onResponse(
                call: Call<TmdbResultsDto>,
                response: Response<TmdbResultsDto>
            ) {
                //При успехе мы вызываем метод, передаем onSuccess и в этот коллбэк список фильмов
                val list = Converter.convertApiListToDtoList(response.body()?.tmdbFilms)
                //Кладем фильмы в бд
                list.forEach {
                    repo.putToDb(list)
                }
                callback.onSuccess()
            }

            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                callback.onFailure()
            }
        })
    }

    fun getFilmsFromDB(): LiveData<List<Film>> = repo.getAllFromDB()


    //Метод для сохранения настроек
    fun saveDefaultCategoryToPreferences(category: String) {
        preferences.saveDefaultCategory(category)
    }

    //Метод для получения настроек
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()
}