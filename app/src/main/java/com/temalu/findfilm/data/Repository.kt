package com.temalu.findfilm.data

import com.temalu.findfilm.data.entity.Film
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun putToDb(films: List<Film>): Completable

    fun getAllFromDB(): Observable<List<Film>>

}