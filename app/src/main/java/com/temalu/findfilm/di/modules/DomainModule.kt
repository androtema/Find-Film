package com.temalu.findfilm.di.modules

import android.content.Context
import com.temalu.findfilm.data.MainRepository
import com.temalu.findfilm.data.PreferenceProvider
import com.temalu.findfilm.data.Repository
import com.temalu.findfilm.data.TmdbApi
import com.temalu.findfilm.domain.Interactor
import dagger.Binds
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

/*@Module
abstract class DomainModule {
    @Singleton
    @Binds
    abstract fun provideInteractor(repository: MainRepository) : Repository
}*/

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

}