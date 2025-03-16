package com.temalu.findfilm.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.temalu.findfilm.App
import com.temalu.findfilm.data.db.DeleteDatabaseWorker
import com.temalu.findfilm.data.entity.Film
import com.temalu.findfilm.domain.Interactor
import jakarta.inject.Inject
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean> = _showProgressBar

    private val _showErrorToast = MutableLiveData<Unit>() // Для события ошибки
    val showErrorToast: LiveData<Unit> = _showErrorToast

    val filmsListLiveData: LiveData<List<Film>>
    //таймер удаления БД
    val workRequest = OneTimeWorkRequestBuilder<DeleteDatabaseWorker>()
        .setInitialDelay(TIME_DELETE_BD, TimeUnit.SECONDS)
        .build()

    @Inject
    lateinit var interactor: Interactor


    init {
        App.instance.dagger.inject(this)

        filmsListLiveData = interactor.getFilmsFromDB()
    }

    fun loadPage(page: Int) {
        _showProgressBar.postValue(true)
        interactor.getFilmsFromApi(page, object : ApiCallback {
            override fun onSuccess() {
                _showProgressBar.postValue(false)
            }

            override fun onFailure() {
                Executors.newSingleThreadExecutor().execute {
                    val context = getApplication<Application>().applicationContext
                    WorkManager.getInstance(context).enqueue(workRequest)
                    Log.d("Delete_bd", "HomeFragmentViewModel : loadPage - ПОДАЧА КОМАНДЫ ОБ УДАЛЕНИИ БД")

                    _showProgressBar.postValue(false)
                    _showErrorToast.postValue(Unit)
                }
            }
        })
    }

    interface ApiCallback {
        fun onSuccess()
        fun onFailure()
    }

    companion object{
        private val TIME_DELETE_BD = 30L
    }
}