package com.androtema.local.data

import com.androtema.local.data.entity.Film
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface Repository {

    fun putToDb(films: List<Film>): Completable

    fun getAllFromDB(): Observable<List<Film>>

}