package com.temalu.findfilm.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.temalu.findfilm.App
import com.temalu.findfilm.domain.Interactor
import jakarta.inject.Inject

class SettingsFragmentViewModel : ViewModel() {
    //Инжектим интерактор
    @Inject
    lateinit var interactor: Interactor

    val categoryPropertyLifeData: MutableLiveData<String> = MutableLiveData()

    init {
        App.instance.dagger.inject(this)
        //Получаем категорию при инициализации, чтобы у нас сразу подтягивалась категория
        getCategoryProperty()
    }

    private fun getCategoryProperty() {
        categoryPropertyLifeData.value = interactor.getDefaultCategoryFromPreferences()
    }

    fun putCategoryProperty(category: String) {
        //Сохраняем в настройки
        interactor.saveDefaultCategoryToPreferences(category)
        //И сразу забираем, чтобы сохранить состояние в модели
        getCategoryProperty()
    }
}