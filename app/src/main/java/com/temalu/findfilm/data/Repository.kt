package com.temalu.findfilm.data

import androidx.lifecycle.LiveData
import com.temalu.findfilm.data.entity.Film

interface Repository {

    fun putToDb(films: List<Film>)

    fun getAllFromDB(): LiveData<List<Film>>

}