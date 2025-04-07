package com.temalu.findfilm.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.temalu.findfilm.App
import com.temalu.findfilm.data.db.DeleteDatabaseWorker
import com.temalu.findfilm.data.entity.Film
import com.temalu.findfilm.domain.ApiResult
import com.temalu.findfilm.domain.Interactor
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val _showProgressBar = MutableStateFlow<Boolean>(false)
    val showProgressBar: StateFlow<Boolean> = _showProgressBar

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent

    val filmsListFlow: Flow<List<Film>>

    //таймер удаления БД
    val workRequest = OneTimeWorkRequestBuilder<DeleteDatabaseWorker>()
        .setInitialDelay(TIME_DELETE_BD, TimeUnit.SECONDS)
        .build()

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        filmsListFlow = interactor.getFilmsFromDB()
    }

    fun loadPage(page: Int) {
        viewModelScope.launch {
            _showProgressBar.value = true

            interactor.getFilmsFromApi(page)
                .catch { error ->
                    handleError(error)
                }
                .collect { result ->
                    _showProgressBar.value = false
                    when (result) {
                        is ApiResult.Success -> Unit // Данные уже в filmsListFlow
                        is ApiResult.Error -> handleError(result.throwable)
                        is ApiResult.Loading -> Unit // Уже обрабатывается _showProgressBar
                    }
                }
        }
    }

    private suspend fun handleError(error: Throwable) {
        WorkManager.getInstance(getApplication()).enqueue(workRequest)
        _toastEvent.emit("Ошибка загрузки: ${error.message ?: "Неизвестная ошибка"}")
    }

    companion object {
        private val TIME_DELETE_BD = 30L
    }
}