package com.temalu.findfilm.di.modules

import android.content.Context
import com.androtema.local.data.MainRepository
import com.androtema.local.data.PreferenceProvider
import com.androtema.remote.data.tmdb.TmdbApi
import com.temalu.findfilm.domain.Interactor
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

@Module
//Передаем контекст для SharedPreferences через конструктор
class DomainModule(val context: Context) {
    //Нам нужно контекст как-то провайдить, поэтому создаем такой метод
    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    //Создаем экземпляр SharedPreferences
    fun providePreferences(context: Context) = PreferenceProvider(context)

    @Singleton
    @Provides
    fun provideInteractor(
        repository: MainRepository,
        tmdbApi: TmdbApi,
        preferenceProvider: PreferenceProvider
    ) = Interactor(repo = repository, retrofitService = tmdbApi, preferences = preferenceProvider)
}