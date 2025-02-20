package com.temalu.findfilm.di

import com.temalu.findfilm.di.modules.DatabaseModule
import com.temalu.findfilm.di.modules.DomainModule
import com.temalu.findfilm.di.modules.InteractorModule
import com.temalu.findfilm.di.modules.RemoteModule
import com.temalu.findfilm.viewmodel.HomeFragmentViewModel
import dagger.Component
import jakarta.inject.Singleton

@Singleton
@Component(
    //Внедряем все модули, нужные для этого компонента
    modules = [
        RemoteModule::class,
        DatabaseModule::class,
        DomainModule::class,
        InteractorModule::class
    ]
)
interface AppComponent {
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
}