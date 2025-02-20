package com.temalu.findfilm.di.modules

import com.temalu.findfilm.data.MainRepository
import com.temalu.findfilm.data.Repository
import com.temalu.findfilm.data.TmdbApi
import com.temalu.findfilm.domain.Interactor
import dagger.Binds
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

@Module
abstract class DomainModule {
    @Singleton
    @Binds
    abstract fun provideInteractor(repository: MainRepository) : Repository
}