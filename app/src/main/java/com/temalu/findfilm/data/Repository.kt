package com.temalu.findfilm.data

import com.temalu.findfilm.data.entity.Film
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun putToDb(films: List<Film>): Flow<Unit>

    fun getAllFromDB(): Flow<List<Film>>

}