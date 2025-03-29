package com.temalu.findfilm.data.db

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class DeleteDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val dbFile = applicationContext.getDatabasePath("film_db")
        if (dbFile.exists()) {
            dbFile.delete()
        }
        return Result.success()
    }
}