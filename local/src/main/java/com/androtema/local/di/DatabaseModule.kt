package com.androtema.local.di

import android.content.Context
import androidx.room.Room
import com.androtema.local.data.MainRepository
import com.androtema.local.data.dao.FilmDao
import com.androtema.local.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideFilmDao(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "film_db"
        ).build().filmDao()


    @Provides
    @Singleton
    fun provideRepository(filmDao: FilmDao) = MainRepository(filmDao)
}