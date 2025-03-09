package com.temalu.findfilm.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.temalu.findfilm.data.dao.FilmDao
import com.temalu.findfilm.data.entity.Film

@Database(
    entities = [Film::class],
    version = 1, //поменять на nextVersion и добавить autoMigrations (для простых изменений)
    exportSchema = true/*, autoMigrations = [AutoMigration(from = 1, to = 2)]*/
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}