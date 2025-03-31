package com.temalu.findfilm.data

import com.temalu.findfilm.data.entity.Film
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun putToDb(films: List<Film>)

    fun getAllFromDB(): Flow<List<Film>>

}