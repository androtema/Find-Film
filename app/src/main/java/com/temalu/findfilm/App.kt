package com.temalu.findfilm

import android.app.Application
import com.temalu.findfilm.data.MainRepository
import com.temalu.findfilm.domain.Interactor

class App : Application() {
    lateinit var repoMain: MainRepository
    lateinit var interactor: Interactor

    override fun onCreate() {
        super.onCreate()
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        //Инициализируем репозиторий
        repoMain = MainRepository()
        //Инициализируем интерактор
        interactor = Interactor(repoMain)
    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }
}