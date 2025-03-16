package com.temalu.findfilm.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.temalu.findfilm.App
import com.temalu.findfilm.data.db.DeleteDatabaseWorker
import com.temalu.findfilm.data.entity.Film
import com.temalu.findfilm.domain.Interactor
import com.temalu.findfilm.presentation.utils.SingleLiveEvent
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean> = _showProgressBar

    private val _showToastEvent = SingleLiveEvent<String>()
    val showToastEvent: LiveData<String> = _showToastEvent

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
        viewModelScope.launch {
            _showProgressBar.value = true
            withContext(Dispatchers.IO) {
                interactor.getFilmsFromApi(page, object : ApiCallback {
                    override fun onSuccess() {
                        _showProgressBar.value = false
                    }

                    override fun onFailure() {
                        val context = getApplication<Application>().applicationContext
                        WorkManager.getInstance(context).enqueue(workRequest)

                        _showProgressBar.postValue(false)
                        _showToastEvent.postValue("Загрузка не удалась")
                    }
                })
            }
        }
    }

    interface ApiCallback {
        fun onSuccess()
        fun onFailure()
    }

    companion object {
        private val TIME_DELETE_BD = 30L
    }
}