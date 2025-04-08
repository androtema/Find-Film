package com.temalu.findfilm.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.temalu.findfilm.App
import com.temalu.findfilm.data.db.DeleteDatabaseWorker
import com.temalu.findfilm.data.entity.Film
import com.temalu.findfilm.domain.ApiResult
import com.temalu.findfilm.domain.Interactor
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import jakarta.inject.Inject
import java.util.concurrent.TimeUnit


class HomeFragmentViewModel(private val application: Application) : AndroidViewModel(application) {
    private val _showProgressBar = BehaviorSubject.createDefault(false)
    val showProgressBar: Observable<Boolean> = _showProgressBar

    private val _toastEvent = PublishSubject.create<String>()
    val toastEvent: Observable<String> = _toastEvent

    val filmsListObservable: Observable<List<Film>>

    private val disposable = CompositeDisposable()

    //таймер удаления БД
    val workRequest = OneTimeWorkRequestBuilder<DeleteDatabaseWorker>()
        .setInitialDelay(TIME_DELETE_BD, TimeUnit.SECONDS)
        .build()

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        filmsListObservable = interactor.getFilmsFromDB()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { films -> Log.d("init","Films from DB: $films") }
    }

    fun loadPage(page: Int) {
        interactor.getFilmsFromApi(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _showProgressBar.onNext(true) } // Показываем ProgressBar перед началом
            .subscribe(
                { result ->
                    _showProgressBar.onNext(false) // Скрываем ProgressBar
                    when (result) {
                        is ApiResult.Success -> Unit // Данные уже в filmsListObservable
                        is ApiResult.Error -> handleError(result.throwable)
                        is ApiResult.Loading -> Unit // Уже обрабатывается через _showProgressBar
                    }
                },
                { error ->
                    _showProgressBar.onNext(false) // Скрываем ProgressBar при ошибке
                    handleError(error)
                }
            ).also { disposable.add(it) }
    }

    private fun handleError(error: Throwable) {
        WorkManager.getInstance(application).enqueue(workRequest)
        _toastEvent.onNext("Ошибка загрузки")
        Log.d("HomeFragmentViewModel", "Ошибка загрузки: ${error.message ?: "Неизвестная ошибка"}")
    }

    companion object {
        private val TIME_DELETE_BD = 30L
    }

    override fun onCleared() {
        disposable.clear() // Очистка подписок
        super.onCleared()
    }
}