package com.temalu.findfilm.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.temalu.findfilm.App
import com.temalu.findfilm.domain.Film
import com.temalu.findfilm.domain.Interactor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class HomeFragmentViewModel : ViewModel(), KoinComponent {
    val filmsListLiveData:  MutableLiveData<List<Film>> = MutableLiveData()
    private val interactor: Interactor by inject()

    fun loadPage(page : Int){
        interactor.getFilmsFromApi(page, object : ApiCallback {
            override fun onSuccess(films: List<Film>) {
                filmsListLiveData.postValue(films)
            }

            override fun onFailure() {
            }
        })
    }

    interface ApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }
}