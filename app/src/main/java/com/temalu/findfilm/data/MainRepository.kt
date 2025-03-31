package com.temalu.findfilm.data

import com.temalu.findfilm.data.dao.FilmDao
import com.temalu.findfilm.data.entity.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MainRepository(private val filmDao: FilmDao) : Repository {

    override fun putToDb(films: List<Film>): Flow<Unit> = flow {
        filmDao.insertAll(films)
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    override fun getAllFromDB(): Flow<List<Film>> {
        return filmDao.getCachedFilms()
    }
}