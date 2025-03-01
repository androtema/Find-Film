package com.temalu.findfilm.di.modules

import com.temalu.findfilm.data.MainRepository
import com.temalu.findfilm.data.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideRepository() = MainRepository()
}