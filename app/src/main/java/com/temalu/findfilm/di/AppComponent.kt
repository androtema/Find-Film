package com.temalu.findfilm.di

import com.temalu.findfilm.di.modules.DatabaseModule
import com.temalu.findfilm.di.modules.DomainModule
import com.temalu.findfilm.di.modules.RemoteModule
import com.temalu.findfilm.viewmodel.HomeFragmentViewModel
import com.temalu.findfilm.viewmodel.SettingsFragmentViewModel
import dagger.Component
import jakarta.inject.Singleton

@Singleton
@Component(
    //Внедряем все модули, нужные для этого компонента
    modules = [
        RemoteModule::class,
        DatabaseModule::class,
        DomainModule::class,
    ]
)
interface AppComponent {
    //метод для того, чтобы появилась возможность внедрять зависимости в HomeFragmentViewModel
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
    //метод для того, чтобы появилась возможность внедрять зависимости в SettingsFragmentViewModel
    fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
}