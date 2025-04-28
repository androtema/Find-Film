package com.androtema.local.data.db

import android.content.Context
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