package com.temalu.findfilm

import android.app.Application
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.temalu.findfilm.data.API_KEY
import com.temalu.findfilm.data.API_TMDB
import com.temalu.findfilm.data.MainRepository
import com.temalu.findfilm.data.TmdbApi
import com.temalu.findfilm.di.AppComponent
import com.temalu.findfilm.di.DaggerAppComponent
import com.temalu.findfilm.di.modules.DatabaseModule
import com.temalu.findfilm.di.modules.DomainModule
import com.temalu.findfilm.di.modules.RemoteModule
import com.temalu.findfilm.domain.Interactor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class App : Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        //Создаем компонент
        dagger = DaggerAppComponent.builder()
            .remoteModule(RemoteModule())
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}