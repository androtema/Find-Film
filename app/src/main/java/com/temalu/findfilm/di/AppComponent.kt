package com.temalu.findfilm.di

import com.androtema.local.di.DatabaseModule
import com.temalu.findfilm.di.modules.DomainModule
import com.androtema.remote.di.RemoteModule
import com.temalu.findfilm.presentation.viewmodel.HomeFragmentViewModel
import com.temalu.findfilm.presentation.viewmodel.SettingsFragmentViewModel
import dagger.Component
import jakarta.inject.Singleton

@Singleton
@Component(
    modules = [
        RemoteModule::class,
        DatabaseModule::class,
        DomainModule::class,
    ]
)
interface AppComponent {
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
    fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
}