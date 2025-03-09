package com.temalu.findfilm

import android.app.Application
import com.temalu.findfilm.di.AppComponent
import com.temalu.findfilm.di.DaggerAppComponent

import com.temalu.findfilm.di.modules.DatabaseModule
import com.temalu.findfilm.di.modules.DomainModule
import com.temalu.findfilm.di.modules.RemoteModule

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