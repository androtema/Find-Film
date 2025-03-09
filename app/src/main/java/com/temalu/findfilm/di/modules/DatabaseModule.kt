package com.temalu.findfilm.di.modules

import android.content.Context
import com.temalu.findfilm.data.MainRepository
import com.temalu.findfilm.data.Repository
import com.temalu.findfilm.data.db.DatabaseHelper
import dagger.Binds
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)

    @Provides
    @Singleton
    fun provideRepository(databaseHelper: DatabaseHelper) = MainRepository(databaseHelper)
}