package com.temalu.findfilm.domain

import com.temalu.findfilm.data.MainRepository

class Interactor(val repoMain: MainRepository) {
    fun getFilmsDB(): MutableList<Film> = repoMain.filmsDataBase
}