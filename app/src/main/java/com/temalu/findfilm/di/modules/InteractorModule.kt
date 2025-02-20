package com.temalu.findfilm.di.modules

import com.temalu.findfilm.data.MainRepository
import com.temalu.findfilm.data.Repository
import com.temalu.findfilm.data.TmdbApi
import com.temalu.findfilm.domain.Interactor
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

@Module
class InteractorModule {
    @Provides
    @Singleton
    fun provideInteractor(repository: MainRepository, tmdbApi: TmdbApi): Interactor {
        return Interactor(repo = repository, retrofitService = tmdbApi)
    }
}